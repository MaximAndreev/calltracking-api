package ru.avtomir.calltrackingru.credential;

import java.util.Objects;

public class Credential {
    private final String login;
    private final String password;
    private volatile String token;
    private volatile boolean isValid = true;
    private CredentialsStorage credentialsStorage;

    public Credential(CredentialsStorage credentialsStorage) {
        Objects.requireNonNull(credentialsStorage, "credentialsStorage must not be null");
        this.login = credentialsStorage.getLogin();
        this.password = credentialsStorage.getPassword();
        this.token = credentialsStorage.getToken();
        this.credentialsStorage = credentialsStorage;
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

    public boolean isValid() {
        return isValid;
    }

    public void setValid(boolean isValid) {
        this.isValid = isValid;
    }

    public void updateToken(String token) {
        this.token = token;
        this.isValid = true;
        credentialsStorage.saveToken(token);
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
