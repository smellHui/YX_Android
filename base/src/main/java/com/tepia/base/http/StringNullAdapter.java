package com.tepia.base.http;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;

/**
 * Created by      android studio
 *
 * @author :       ly
 * Date            :       2019-04-16
 * Time            :       下午10:34
 * Version         :       1.0
 * location        :       武汉研发中心
 * 功能描述         :     自定义一个String适配器
 **/
public class StringNullAdapter extends TypeAdapter<String> {


    @Override
    public void write(JsonWriter jsonWriter, String s) throws IOException {
        //序列化使用的是adapter的write方法
        if (s == null) {
            //jsonWriter.nullValue();//这个方法是错的，而是应该将null转成""
            jsonWriter.value("");
            return;
        }
        jsonWriter.value(s);
    }

    @Override
    public String read(JsonReader jsonReader) throws IOException {
        //反序列化使用的是read方法
        if (jsonReader.peek() == JsonToken.NULL) {
            jsonReader.nextNull();
            return "";
        }
        return jsonReader.nextString();
    }
}
