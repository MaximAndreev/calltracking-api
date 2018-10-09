package ru.avtomir.calltrackingru.credential;

import java.nio.file.Path;
import java.util.Objects;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * Thread-safe storage for {@link Credential}.
 */
public class CredentialStorage {
    private final Path path;
    private final ReadWriteLock lock = new ReentrantReadWriteLock();

    public CredentialStorage(Path path) {
        this.path = path;
    }

    public ReadWriteLock getLock() {
        return lock;
    }

    public Path getPath() {
        return path;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CredentialStorage that = (CredentialStorage) o;
        return Objects.equals(path, that.path);
    }

    @Override
    public int hashCode() {
        return Objects.hash(path);
    }

    @Override
    public String toString() {
        return "CredentialStorage{" +
                "path=" + path +
                '}';
    }
}
