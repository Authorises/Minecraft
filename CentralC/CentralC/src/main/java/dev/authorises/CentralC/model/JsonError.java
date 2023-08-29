package dev.authorises.CentralC.model;

import com.google.gson.JsonObject;

public class JsonError {

    public static JsonObject generateError(String message){
        JsonObject o = new JsonObject();
        o.addProperty("error", message);
        return o;
    }

}
