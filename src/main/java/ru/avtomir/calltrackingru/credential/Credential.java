package ru.avtomir.calltrackingru.credential;

import java.util.Objects;

public class Credential {
    private final String login;
    private final String password;
    private volatile String token;
    private volatile boolean isValid = true;
    private CredentialStorage credentialStorage;

    private Credential(String login, String password, String token) {
        this.login = login;
        this.password = password;
        this.token = token;
    }

    public String getLogin() {
        return login;
    }

    public String getPassword() {
        return password;
    }

    public String getToken() {
        return token;
    }

    public synchronized void setToken(String token) {
        this.token = token;
    }

    public CredentialStorage getCredentialStorage() {
        return credentialStorage;
    }

    public void setCredentialStorage(CredentialStorage credentialStorage) {
        this.credentialStorage = credentialStorage;
    }

    public boolean isValid() {
        return isValid;
    }

    public void setValid(boolean valid) {
        isValid = valid;
    }

    @Override
    public String toString() {
        return "Login: " + login +
                " Password: " + password +
                " Token: " + token;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Credential that = (Credential) o;
        return Objects.equals(login, that.login) &&
                Objects.equals(password, that.password) &&
                Objects.equals(token, that.token);
    }

    @Override
    public int hashCode() {
        return Objects.hash(login, password, token);
    }
}
