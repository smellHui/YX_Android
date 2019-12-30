package com.tepia.base.http;

import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.reflect.TypeToken;

/**
 * Created by      android studio
 *
 * @author :       ly
 * Date            :       2019-04-17
 * Time            :       上午9:33
 * Version         :       1.0
 * location        :       武汉研发中心
 * 功能描述         :
 **/
public class NullStringEmptyTypeAdapterFactory<T> implements TypeAdapterFactory {
    @Override
    public <T> TypeAdapter<T> create(Gson gson, TypeToken<T> type) {
        Class<T> rawType = (Class<T>) type.getRawType();
        if (rawType != String.class) {
            return null;
        }
        return (TypeAdapter<T>) new StringNullAdapter();
    }
}
