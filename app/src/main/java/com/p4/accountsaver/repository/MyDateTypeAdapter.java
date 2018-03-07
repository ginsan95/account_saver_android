package com.p4.accountsaver.repository;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;
import java.util.Date;

/**
 * Created by averychoke on 4/3/18.
 */

public class MyDateTypeAdapter extends TypeAdapter<Date> {
    @Override
    public void write(JsonWriter out, Date value) throws IOException {
        if (value == null) {
            out.nullValue();
        } else {
            out.value(value.getTime());
        }
    }

    @Override
    public Date read(JsonReader in) throws IOException {
        if (in != null) {
            if (in.peek() == JsonToken.NUMBER) {
                return new Date(in.nextLong());
            } else {
                in.skipValue();
            }
        }
        return null;
    }
}
