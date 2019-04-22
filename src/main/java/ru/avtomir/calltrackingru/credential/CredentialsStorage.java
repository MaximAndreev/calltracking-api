package ru.avtomir.calltrackingru.credential;

/**
 * Thread-safe storage for {@link Credential}.
 */
public interface CredentialsStorage {

    String getLogin();

    String getPassword();

    String getToken();

    void setLogin(String login);

    void setPassword(String password);

    void setToken(String token);

    void saveToken(String token);
}
