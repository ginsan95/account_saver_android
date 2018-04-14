package com.p4.accountsaver.repository;

import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import com.p4.accountsaver.model.SecurityQuestions;

import org.json.JSONObject;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;

public class SecurityQuestionsAdapter extends TypeAdapter<SecurityQuestions> {
    @Override
    public void write(JsonWriter out, SecurityQuestions value) throws IOException {
        if (value == null) {
            out.nullValue();
        } else {
            out.value(new JSONObject(value.getQuestions()).toString());
        }
    }

    @Override
    public SecurityQuestions read(JsonReader in) throws IOException {
        SecurityQuestions securityQuestions = new SecurityQuestions();
        if (in != null) {
            if (in.peek() != JsonToken.NULL) {
                String jsonString = in.nextString();
                if (jsonString != null) {
                    Map<String, String> map = new Gson().fromJson(
                            jsonString, new TypeToken<LinkedHashMap<String, String>>() {
                            }.getType()
                    );
                    if (map != null) {
                        securityQuestions.setQuestions(map);
                    }
                }
            } else {
                in.skipValue();
            }
        }
        return securityQuestions;
    }
}
