package dev.will.twg;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Dictionary;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.concurrent.CompletableFuture;

public class WebHookHelper {

    public static CompletableFuture<Void> SendWebHook(String URL, String Message) {
        return CompletableFuture.supplyAsync(() -> {
            SendWebHookSynchronous(URL, Message);
            return null;
        });
    }

    public static void SendWebHookSynchronous(String URL, String Message) {

        Dictionary<String, String> contentDict = new Hashtable<String, String>();
        contentDict.put("content", Message);

        DiscordWebHook.LOGGER.debug("Sending web hook.");

        PostJSON(URL, contentDict);

    }

    public static void PostJSON(String URL, Dictionary<String, String> jsonDict) {

        if (URL.isEmpty())
            return;

        HttpClient.Builder httpBuilder = HttpClient.newBuilder();
        HttpRequest.Builder requestBuilder = HttpRequest.newBuilder();

        try (HttpClient httpClient = httpBuilder.build()) {

            String jsonBody = StringDictionaryToJSON(jsonDict);

            requestBuilder.uri(URI.create(URL));
            requestBuilder.header("content-type", "application/json");
            requestBuilder.POST(HttpRequest.BodyPublishers.ofString(jsonBody));

            HttpRequest request = requestBuilder.build();

            HttpResponse<String> response = httpClient.sendAsync(request, HttpResponse.BodyHandlers.ofString()).get();

            DiscordWebHook.LOGGER.debug("Post status code: {}", response.statusCode());

        } catch(Exception e) {
            DiscordWebHook.LOGGER.info("Exception occurred during creation of HttpClient. {}", String.valueOf(e));
        }

    }

    public static String StringDictionaryToJSON(Dictionary<String, String> Dict) {

        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("{");

        Iterator<String> iterator = Dict.keys().asIterator();

        while (iterator.hasNext()) {
            String key = iterator.next();

            stringBuilder.append(String.format("\"%s\": \"%s\"", key, Dict.get(key)));

            if (iterator.hasNext())
                stringBuilder.append(',');

        }

        stringBuilder.append('}');

        return stringBuilder.toString();

    }

}
