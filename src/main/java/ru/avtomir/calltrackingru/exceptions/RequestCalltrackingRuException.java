package ru.avtomir.calltrackingru.exceptions;

import org.apache.http.client.methods.HttpPost;

import java.net.URI;

// Error in Calltracking.ru API
public class RequestCalltrackingRuException extends CallrackingRuException {
    private final URI uri;
    private final HttpPost httpPost;

    public RequestCalltrackingRuException(String message, URI uri, Throwable cause) {
        this(message, uri, null, cause);
    }

    public RequestCalltrackingRuException(String message, URI uri, HttpPost httpPost, Throwable cause) {
        super(message, cause);
        this.uri = uri;
        this.httpPost = httpPost;
    }

    public URI getUri() {
        return uri;
    }

    public HttpPost getHttpPost() {
        return httpPost;
    }
}
