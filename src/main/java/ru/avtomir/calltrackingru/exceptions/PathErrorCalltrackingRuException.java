package ru.avtomir.calltrackingru.exceptions;

import java.nio.file.Path;

public class PathErrorCalltrackingRuException extends BaseCallrackingRuException {
    private final Path path;

    public PathErrorCalltrackingRuException(String message, Path path) {
        this(message, path, null);
    }

    public PathErrorCalltrackingRuException(String message, Path path, Throwable cause) {
        super(message, cause);
        this.path = path;
    }

    public Path getPath() {
        return path;
    }
}
