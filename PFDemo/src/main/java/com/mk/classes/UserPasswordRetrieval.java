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
public class UserPasswordRetrieval implements Serializable 
{
    private String firstName = "";
    private String lastName = "";
    private String authQuestion = "";
    private String authAnswer = "";
    private String authCode = "";
    private String email = "";
    private String logonName = "";
    private String phone = "";
    private String password = "";

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getAuthQuestion() {
        return authQuestion;
    }

    public void setAuthQuestion(String authQuestion) {
        this.authQuestion = authQuestion;
    }

    public String getAuthAnswer() {
        return authAnswer;
    }

    public void setAuthAnswer(String authAnswer) {
        this.authAnswer = authAnswer;
    }

    public String getAuthCode() {
        return authCode;
    }

    public void setAuthCode(String authCode) {
        this.authCode = authCode;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getLogonName() {
        return logonName;
    }

    public void setLogonName(String logonName) {
        this.logonName = logonName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
    
    public String getQueryToMatchUser() {
        String ret = "";

        if (!this.logonName.trim().equals("")) {
            ret = " where ";
            ret += " LOGON_NAME = '" + this.logonName.trim() + "' ";
            if (!this.authCode.trim().equals("")) {
                ret += " and AUTH_CODE = '" + this.authCode.trim() + "' ";
            }
            if (!this.email.trim().equals("")) {
                ret += " and EMAIL = '" + this.email.trim() + "' ";
            }
        } else if (!this.email.trim().equals("")) {
            ret = " where EMAIL = '" + this.email.trim() + "' ";
            if (!this.authCode.trim().equals("")) {
                ret += " and AUTH_CODE = '" + this.authCode.trim() + "' ";
            }
            if (!this.firstName.trim().equals("") ) {
                ret += " and FIRST_NAME = '" + this.firstName.trim() + "' ";
            }
            if ( !this.lastName.trim().equals("") ) {
                ret += " and LAST_NAME = '" + this.lastName.trim() + "' ";
            }
        } 
        else
        {
            String prefix1 = " where ";
            String prefix2 = " where ";
            if (!this.firstName.trim().equals("") ) {
                ret = " where FIRST_NAME = '" + this.firstName.trim() + "' ";
                prefix1 = " and ";
                prefix2 = " and ";
            }
            if ( !this.lastName.trim().equals("") ) {
                ret += prefix1 + " LAST_NAME = '" + this.lastName.trim() + "' ";
                prefix2 = " and ";
            }
            if (!this.authCode.trim().equals("")) {
                ret += prefix2 + " AUTH_CODE = '" + this.authCode.trim() + "' ";
        }
        }
        return ret;
    }
}
