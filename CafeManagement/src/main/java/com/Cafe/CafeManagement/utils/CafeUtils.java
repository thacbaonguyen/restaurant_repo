package com.Cafe.CafeManagement.utils;

import com.google.common.base.Strings;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.File;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class CafeUtils {
    public static String generateUUID(){
        Date date = new Date();
        long now = date.getTime();
        return "Cafe-BIll-" + now;
    }
    public static Map<String, Object> getMapFromJson(String data){
        if(!Strings.isNullOrEmpty(data)){
            return new Gson().fromJson(data, new TypeToken<Map<String, Object>>(){ // Gson để chuyển chuỗi JSON thành Map
            }.getType());
        }
        return new HashMap<>();
    }

    public static Boolean isExistFile(String filePath){
        File file = new File(filePath);
        return (file.exists() && file != null);
    }
}
