package ru.avtomir.calltrackingru.gson;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import ru.avtomir.calltrackingru.beans.Project;
import ru.avtomir.calltrackingru.credential.CredentialsStorage;

public final class GsonHolder {
    public static final Gson GSON = new GsonBuilder()
            .registerTypeAdapter(CredentialsStorage.class, new CredentialsStorageSerializer())
            .registerTypeAdapter(Project.class, new ProjectDeserializer())
            .create();

    private GsonHolder() {
    }
}
