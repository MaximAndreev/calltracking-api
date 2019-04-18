package ru.avtomir.calltrackingru.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.avtomir.calltrackingru.credential.Credential;
import ru.avtomir.calltrackingru.credential.CredentialStorage;
import ru.avtomir.calltrackingru.exceptions.FileCredentialsCalltrackingRuException;
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
     * Load of {@link Credential}.
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
            Credential credential;
            try (InputStreamReader isr = new InputStreamReader(Files.newInputStream(path), StandardCharsets.UTF_8)) {
                credential = GSON.fromJson(isr, Credential.class);
                credential.setCredentialStorage(new CredentialStorage(path));
                credential.setValid(true);
            }
            return credential;
        } catch (JsonIOException | JsonSyntaxException e) {
            logger.warn("invalid json in file: {}", path);
            throw new FileCredentialsCalltrackingRuException("Invalid json in file: " + path, path, e);
        } catch (IOException e) {
            logger.warn("can't read file: {}", path);
            throw new FileCredentialsCalltrackingRuException("Can't read file: " + path, path, e);
        }
    }

    /**
     * Thread-safe save of {@link Credential}.
     *
     * @param credential with should be saved to file.
     * @throws FileCredentialsCalltrackingRuException if credentials can't be saved to file.
     */
    public static void save(Credential credential) throws FileCredentialsCalltrackingRuException {
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
            throw new FileCredentialsCalltrackingRuException("Can't write to file cause " + e.getMessage(), path);
        } finally {
            lock.unlock();
        }
    }
}
