package com.epam.campus.gymcrm.utils;

public class ConsoleUtil {
    public static String formatDto(String input) {
        if (input == null || !input.contains("[") || !input.contains("]")) {
            throw new IllegalArgumentException("Formato incorrecto");
        }

        // Extract dto attributes
        String content = input.substring(input.indexOf("[") + 1, input.indexOf("]"));

        // Separate key-value pairs by comma
        StringBuilder formattedOutput = new StringBuilder();
        for (String pair : content.split(", ")) {
            String[] keyValue = pair.split("=");
            if (keyValue.length == 2) {
                // Format attributes
                String formattedKey = formatKey(keyValue[0]);
                formattedOutput.append(formattedKey).append(": ").append(keyValue[1]).append(",\n");
            }
        }

        // Deleting comma and new line for the last attribute
        if (formattedOutput.length() > 0) {
            formattedOutput.setLength(formattedOutput.length() - 2);
        }

        return formattedOutput.toString();
    }

    // Get multiple word attributes to show correctly
    private static String formatKey(String key) {
        StringBuilder formattedKey = new StringBuilder();
        for (char c : key.toCharArray()) {
            if (Character.isUpperCase(c)) {
                formattedKey.append(" ");
            }
            formattedKey.append(c);
        }
        
        return formattedKey.substring(0, 1).toUpperCase() + formattedKey.substring(1);
    }
}
