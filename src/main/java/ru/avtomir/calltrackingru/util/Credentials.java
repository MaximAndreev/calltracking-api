package ru.avtomir.calltrackingru.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.avtomir.calltrackingru.credential.Credential;
import ru.avtomir.calltrackingru.credential.CredentialStorage;
import ru.avtomir.calltrackingru.exceptions.PathErrorCalltrackingRuException;
import ru.avtomir.calltrackingru.jsonserializers.CredentialSerializer;

import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.concurrent.locks.Lock;


public class Credentials {
    private static final Gson GSON = new GsonBuilder()
            .registerTypeAdapter(Credential.class, new CredentialSerializer())
            .create();
    private static Logger logger = LoggerFactory.getLogger(Credentials.class);

    private Credentials() {
    }

    /**
     * Thread-safe load of {@link Credential}
     *
     * @param path - path to file with credentials from user.dir
     */
    public static Credential load(Path path) {
        logger.info("load credentials from file: {}", path);
        if (!Files.exists(path)) {
            logger.warn("file {} doesn't exist", path);
            throw new PathErrorCalltrackingRuException("File " + path.getFileName() + " doesn't exist", path);
        }
        try {
            Credential credential;
            try (InputStreamReader isr = new InputStreamReader(Files.newInputStream(path), StandardCharsets.UTF_8)) {
                credential = GSON.fromJson(isr, Credential.class);
                credential.setCredentialStorage(new CredentialStorage(path));
                credential.setValid(true);
            }
            return credential;
        } catch (JsonIOException | JsonSyntaxException e) {
            logger.warn("invalid json in file: {}", path);
            throw new PathErrorCalltrackingRuException("Invalid json in file: " + path, path, e);
        } catch (IOException e) {
            logger.warn("can't read file: {}", path);
            throw new PathErrorCalltrackingRuException("Can't read file: " + path, path, e);
        }
    }

    /**
     * Thread-safe save of {@link Credential}
     */
    public static void save(Credential credential) {
        CredentialStorage credentialStorage = credential.getCredentialStorage();
        Path path = credentialStorage.getPath();
        Lock lock = credentialStorage.getLock().writeLock();
        String json = GSON.toJson(credential);
        lock.lock();
        logger.info("save credentials to file: {}", path);
        try {
            Files.write(path, json.getBytes());
        } catch (IOException e) {
            logger.warn("can't write to file: {}", path);
            throw new RuntimeException("Can't write to file: " + path.toString(), e);
        } finally {
            lock.unlock();
        }
    }
}
