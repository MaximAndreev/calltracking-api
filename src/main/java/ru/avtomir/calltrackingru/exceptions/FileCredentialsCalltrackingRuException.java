package ru.avtomir.calltrackingru.exceptions;

import java.nio.file.Path;

// Error in handling of Credentials file
public class FileCredentialsCalltrackingRuException extends CallrackingRuException {
    private final Path path;

    public FileCredentialsCalltrackingRuException(String message, Path path) {
        this(message, path, null);
    }

    public FileCredentialsCalltrackingRuException(String message, Path path, Throwable cause) {
        super(message, cause);
        this.path = path;
    }

    public Path getPath() {
        return path;
    }
}
