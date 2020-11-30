package com.group10.cse5236project;

public class Account {

    private static Account mAccount;
    private String mUsername;
    private String mPassword;

    public static Account getInstance() {
        if (mAccount == null) {
            mAccount = new Account();
        }
        return mAccount;
    }

    private Account() {

    }

    public String getUsername() {
        return mUsername;
    }

    public void setUsername(String mUsername) {
        this.mUsername = mUsername;
    }

    public String getPassword() { return mPassword; }

    public void setPassword(String mPassword) { this.mPassword = mPassword; }

    public void clear() {
        mUsername = null;
        mPassword = null;
    }

}
