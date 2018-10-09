package ru.avtomir.calltrackingru.jsonserializers;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import ru.avtomir.calltrackingru.credential.Credential;

import java.lang.reflect.Type;

public class CredentialSerializer implements JsonSerializer<Credential> {

    @Override
    public JsonElement serialize(Credential src, Type typeOfSrc, JsonSerializationContext context) {
        JsonObject obj = new JsonObject();
        obj.addProperty("login", src.getLogin());
        obj.addProperty("password", src.getPassword());
        obj.addProperty("token", src.getToken());
        return obj;
    }
}
