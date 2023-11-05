package com.example.duolingopezzotto.InfoStealerManager;

import com.google.gson.JsonObject;

public class JSONParser {
    public static JsonObject convertStringToJSON(String input) {
        JsonObject jsonObject = new JsonObject();

        String[] pairs = input.split("&");
        for (String pair : pairs) {
            String[] parts = pair.split("=");
            if (parts.length == 2) {
                String nome = parts[0];
                String valore = parts[1];
                jsonObject.addProperty(nome, valore);
            }
        }

        return jsonObject;
    }

}
