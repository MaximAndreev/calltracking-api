package ru.avtomir.calltrackingru.credential;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public abstract class CredentialsStorageAbstract implements CredentialsStorage {

    protected final ReadWriteLock lock = new ReentrantReadWriteLock();
    protected String login;
    protected String password;
    protected String token;

    @Override
    public String getLogin() {
        Lock readLock = lock.readLock();
        readLock.lock();
        try {
            return this.login;
        } finally {
            readLock.unlock();
        }
    }

    @Override
    public String getPassword() {
        Lock readLock = this.lock.readLock();
        readLock.lock();
        try {
            return password;
        } finally {
            readLock.unlock();
        }
    }

    @Override
    public String getToken() {
        Lock readLock = this.lock.readLock();
        readLock.lock();
        try {
            return this.token;
        } finally {
            readLock.unlock();
        }
    }

    @Override
    public void setLogin(String login) {
        Lock writeLock = this.lock.writeLock();
        writeLock.lock();
        try {
            this.login = login;
        } finally {
            writeLock.unlock();
        }
    }

    @Override
    public void setPassword(String password) {
        Lock writeLock = this.lock.writeLock();
        writeLock.lock();
        try {
            this.password = password;
        } finally {
            writeLock.unlock();
        }
    }

    @Override
    public void setToken(String token) {
        Lock writeLock = this.lock.writeLock();
        writeLock.lock();
        try {
            this.token = token;
        } finally {
            writeLock.unlock();
        }
    }
}
