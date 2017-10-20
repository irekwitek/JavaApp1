/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mk.classes;

import javax.mail.PasswordAuthentication;

/**
 *
 * @author giw
 */
public class SMTPAuthenticator extends javax.mail.Authenticator {

    public PasswordAuthentication getPasswordAuthentication() {
        return new PasswordAuthentication(ApplicationProperties.MAIL_USER, ApplicationProperties.MAIL_PASSWORD);
    }
}
