package ru.avtomir.calltrackingru.gson;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import ru.avtomir.calltrackingru.credential.CredentialsStorage;

import java.lang.reflect.Type;

public class CredentialsStorageSerializer implements JsonSerializer<CredentialsStorage> {

    @Override
    public JsonElement serialize(CredentialsStorage src, Type typeOfSrc, JsonSerializationContext context) {
        JsonObject obj = new JsonObject();
        obj.addProperty("login", src.getLogin());
        obj.addProperty("password", src.getPassword());
        obj.addProperty("token", src.getToken());
        return obj;
    }
}
