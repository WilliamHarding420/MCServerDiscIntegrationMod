package dev.will.twg.commands;

public class CommandUtils {

    // parses value to required type
    public static Object parseArgumentFromString(String value, Object to) {

        if (to == String.class)
            return value;

        if (to == Boolean.class)
            return Boolean.parseBoolean(value);

        return null;

    }

}
