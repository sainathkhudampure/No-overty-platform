package com.nopoverty.platform.dto;

public class AuthRequest {
    private String identifier;
    private String usernameOrEmail;
    private String email;
    private String password;

    public AuthRequest() {}

    public String getIdentifier() { 
        if (identifier != null && !identifier.isBlank()) return identifier;
        if (usernameOrEmail != null && !usernameOrEmail.isBlank()) return usernameOrEmail;
        if (email != null && !email.isBlank()) return email;
        return "";
    }
    public void setIdentifier(String identifier) { this.identifier = identifier; }

    public String getUsernameOrEmail() { return usernameOrEmail; }
    public void setUsernameOrEmail(String usernameOrEmail) { this.usernameOrEmail = usernameOrEmail; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
}
