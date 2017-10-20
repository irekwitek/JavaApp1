/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mk.classes;

import java.io.Serializable;

/**
 *
 * @author irek
 */
public class UserEmailConfirmation implements Serializable 
{
    private int userId = 0;
    private String email = "";
    private String emailStatus = "";
    private String confirmationDate = "";
    private String authCode = "";

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getEmailStatus() {
        return emailStatus;
    }

    public void setEmailStatus(String emailStatus) {
        this.emailStatus = emailStatus;
    }

    public String getConfirmationDate() {
        return confirmationDate;
    }

    public void setConfirmationDate(String confirmationDate) {
        this.confirmationDate = confirmationDate;
    }

    public String getAuthCode() {
        return authCode;
    }

    public void setAuthCode(String authCode) {
        this.authCode = authCode;
    }

    @Override
    public String toString() {
        return "UserId [" + this.userId 
                + "] Email [" + this.email 
                + "] Status [" + this.emailStatus 
                + "] Confirmation Date [" + this.confirmationDate 
                + "] Auth Code [" + this.authCode + "]"; 
    }
}
