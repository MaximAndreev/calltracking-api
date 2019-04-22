package ru.avtomir.calltrackingru.credential;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/*
 * In-memory based implementation.
 */
public class CredentialsStorageMemory extends CredentialsStorageAbstract {
    private static final Logger log = LoggerFactory.getLogger(CredentialsStorageFile.class);

    @Override
    public void saveToken(String token) {
        log.info("save credentials in memory");
        setToken(token);
    }

    @Override
    public String toString() {
        return "CredentialStorageMemory{" +
                "login='" + login + '\'' +
                '}';
    }
}
