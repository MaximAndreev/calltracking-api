package ru.avtomir.calltrackingru.jsonserializers;

import com.google.gson.*;
import ru.avtomir.calltrackingru.beans.Project;

import java.lang.reflect.Type;

public class ProjectSerializer implements JsonDeserializer<Project> {

    @Override
    public Project deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        JsonObject jsonObject = json.getAsJsonObject();
        JsonElement projectId = jsonObject.get("project_id");
        JsonElement projectName = jsonObject.get("project_name");
        Project project = new Project();
        if (projectId != null) project.setId(projectId.getAsInt());
        if (projectName != null) project.setName(projectName.getAsString());
        return project;
    }
}
