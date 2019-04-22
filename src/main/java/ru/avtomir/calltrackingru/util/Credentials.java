package ru.avtomir.calltrackingru.util;

import com.google.gson.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.avtomir.calltrackingru.credential.Credential;
import ru.avtomir.calltrackingru.credential.CredentialsStorageFile;
import ru.avtomir.calltrackingru.credential.CredentialsStorageMemory;
import ru.avtomir.calltrackingru.exceptions.FileCredentialsCalltrackingRuException;

import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Objects;


public final class Credentials {

    private static Logger logger = LoggerFactory.getLogger(Credentials.class);

    private Credentials() {
    }

    /**
     * Load of {@link Credential} with a file based storage.
     *
     * @param path - path to file.
     * @throws FileCredentialsCalltrackingRuException if file not found or can't be read as specified JSON format.
     */
    public static Credential load(Path path) throws FileCredentialsCalltrackingRuException {
        logger.info("load credentials from file: {}", path);
        if (!Files.exists(path)) {
            logger.warn("file {} doesn't exist", path);
            throw new FileCredentialsCalltrackingRuException("File " + path.getFileName() + " doesn't exist", path);
        }
        try {
            CredentialsStorageFile credentialsStorageFile = new CredentialsStorageFile();
            try (InputStreamReader isr = new InputStreamReader(Files.newInputStream(path), StandardCharsets.UTF_8)) {
                JsonObject jsonObject = new JsonParser().parse(isr).getAsJsonObject();
                credentialsStorageFile.setPath(path);
                credentialsStorageFile.setLogin(jsonElementToString(jsonObject.get("login")));
                credentialsStorageFile.setPassword(jsonElementToString(jsonObject.get("password")));
                credentialsStorageFile.setToken(jsonElementToString(jsonObject.get("token")));
            }
            return new Credential(credentialsStorageFile);
        } catch (JsonIOException | JsonSyntaxException e) {
            logger.warn("invalid json in file: {}", path);
            throw new FileCredentialsCalltrackingRuException("Invalid json in file: " + path, path, e);
        } catch (IOException e) {
            logger.warn("can't read file: {}", path);
            throw new FileCredentialsCalltrackingRuException("Can't read file: " + path, path, e);
        }
    }

    private static String jsonElementToString(JsonElement jsonElement) {
        return jsonElement == null ? null : jsonElement.getAsString();
    }

    /**
     * Load of {@link Credential} with in-memory storage.
     *
     * @param login    - login to API (never {@code null}).
     * @param password - password to API (never {@code null}).
     * @param token    - token to API.
     */
    public static Credential load(String login, String password, String token) {
        logger.info("load credentials with in-memory storage");
        CredentialsStorageMemory credentialsStorage = new CredentialsStorageMemory();
        credentialsStorage.setLogin(Objects.requireNonNull(login, "login must not be null"));
        credentialsStorage.setPassword(Objects.requireNonNull(password, "password must not be null"));
        credentialsStorage.setToken(token);
        return new Credential(credentialsStorage);
    }
}
