package ru.avtomir.calltrackingru.credential;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.avtomir.calltrackingru.exceptions.FileCredentialsCalltrackingRuException;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.concurrent.locks.Lock;

import static ru.avtomir.calltrackingru.gson.GsonHolder.GSON;

/*
 * File based implementation.
 */
public class CredentialsStorageFile extends CredentialsStorageAbstract {
    private static final Logger log = LoggerFactory.getLogger(CredentialsStorageFile.class);
    private Path path;

    public void setPath(Path path) {
        Lock writeLock = this.lock.writeLock();
        writeLock.lock();
        try {
            this.path = path;
        } finally {
            writeLock.unlock();
        }
    }

    @Override
    public void saveToken(String token) {
        Lock writeLock = this.lock.writeLock();
        writeLock.lock();
        try {
            log.info("save credentials to file: {}", path);
            this.token = token;
            String json = GSON.toJson(this, CredentialsStorage.class);
            Files.write(path, json.getBytes());
        } catch (IOException e) {
            log.warn("can't write to file: {}", path);
            throw new FileCredentialsCalltrackingRuException("Can't write to file cause " + e.getMessage(), path);
        } finally {
            writeLock.unlock();
        }
    }

    @Override
    public String toString() {
        return "CredentialsStorage{" +
                "path=" + path +
                '}';
    }
}
