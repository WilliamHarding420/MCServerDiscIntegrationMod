package dev.will.twg.commands.utils;

import com.google.common.primitives.UnsignedInteger;

import java.util.Dictionary;
import java.util.Hashtable;
import java.util.function.Function;

public class CommandUtils {

    private static Dictionary<Object, Function<String, Object>> conversionMethods = null;

    private static void populateDictionary() {

        conversionMethods = new Hashtable<>();

        conversionMethods.put(String.class, (val) -> val);
        conversionMethods.put(Boolean.class, Boolean::parseBoolean);
        conversionMethods.put(Integer.class, Integer::parseInt);
        conversionMethods.put(UnsignedInteger.class, UnsignedInteger::valueOf);
        conversionMethods.put(Short.class, Short::parseShort);
        conversionMethods.put(Double.class, Double::parseDouble);
        conversionMethods.put(Float.class, Float::parseFloat);

    }

    // parses value to required type
    public static Object parseArgumentFromString(String value, Object to) {

        if (conversionMethods == null)
            populateDictionary();

        Function<String, Object> method;
        if ((method = conversionMethods.get(to)) == null)
            return null;

        return method.apply(value);

    }

}
