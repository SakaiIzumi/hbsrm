package net.bncloud.service.api.platform.user.dto;

public class Password {
    private String password;
    private boolean credentialsExpired;

    public Password() {
    }

    public Password(String password) {
        this.password = password;
    }

    public Password(String password, boolean credentialsExpired) {
        this.password = password;
        this.credentialsExpired = credentialsExpired;
    }

    public static Password of(String password, boolean credentialsExpired) {
        return new Password(password, credentialsExpired);
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isCredentialsExpired() {
        return credentialsExpired;
    }

    public void setCredentialsExpired(boolean credentialsExpired) {
        this.credentialsExpired = credentialsExpired;
    }
}
