package com.mk.classes;

import java.util.*;

import javax.activation.*;

import java.io.*;
import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;


public class EmailAdapter implements Serializable
{
    public static final String EMAIL_TYPE_PLAIN = "text/plain";
    public static final String EMAIL_TYPE_HTML = "text/html";

    private String emailReplyTo = "";
    private String emailTo = "";
    private String emailCC = "";
    private String emailSubject = "";
    private String emailBodyText = "";
    private File[] attachments = new File[0];
    private String emailType = EMAIL_TYPE_PLAIN;
    private String errorMessage = "";

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }
    
    public String getEmailCC()
    {
        return emailCC;
    }


    
    public void setEmailCC( String emailCC )
    {
        this.emailCC = emailCC;
    }


    public String getEmailType()
    {
        return emailType;
    }

    
    public void setEmailType( String emailType )
    {
        this.emailType = emailType;
    }


    public File[] getAttachments()
    {
        return attachments;
    }

    
    public void setAttachments( Vector atVec )
    {
        if ( atVec != null )
        {
            this.attachments = new File[atVec.size()];
            for ( int i = 0; i < atVec.size(); i++)
            {
                this.attachments[i] = (File)atVec.get( i );
            }
        }
    }

    public void setAttachments( File[] attachments )
    {
        this.attachments = attachments;
    }

    public String getEmailReplyTo() {
        return emailReplyTo;
    }

    public void setEmailReplyTo(String emailReplyTo) {
        this.emailReplyTo = emailReplyTo;
    }

    public String getEmailTo()
    {
        return emailTo;
    }
    
    public void setEmailTo( String emailTo )
    {
        this.emailTo = emailTo;
    }
    
    public String getEmailSubject()
    {
        return emailSubject;
    }
    
    public void setEmailSubject( String emailSubject )
    {
        this.emailSubject = emailSubject;
    }
    
    public String getEmailBodyText()
    {
        return emailBodyText;
    }
    
    public void setEmailBodyText( String emailBodyText )
    {
        this.emailBodyText = emailBodyText;
    }
    


    private synchronized boolean sendEmail( String emailType )
    {
        boolean ret = true;
        this.errorMessage = "";
        Properties props = new Properties();
        props.put("mail.smtp.host", ApplicationProperties.MAIL_SMTP_HOST);
        props.put("mail.from", ApplicationProperties.MAIL_FROM);
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.ssl.trust", ApplicationProperties.MAIL_SMTP_HOST);
        props.setProperty("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        props.setProperty("mail.smtp.socketFactory.fallback", "false");
        props.setProperty("mail.smtp.port", ApplicationProperties.MAIL_SMTP_PORT);
        props.setProperty("mail.smtp.socketFactory.port", ApplicationProperties.MAIL_SMTP_SOCKET_FACTORY_PORT);
        SMTPAuthenticator auth = new SMTPAuthenticator();
        Session session = Session.getInstance(props, auth);

        try {
            MimeMessage msg = new MimeMessage(session);
            msg.setFrom();
            if (this.getEmailReplyTo() == null || this.getEmailReplyTo().equals(""))
            {
                this.setEmailReplyTo(ApplicationProperties.MAIL_FROM);
            }
            msg.setReplyTo(new javax.mail.Address[]
            {
                new javax.mail.internet.InternetAddress(this.getEmailReplyTo())
            });
            msg.setRecipients(Message.RecipientType.TO, this.getEmailTo() );
            msg.setRecipients(Message.RecipientType.CC, this.getEmailCC() );
            msg.setSubject( this.getEmailSubject() );
            msg.setSentDate(new Date());

            MimeBodyPart messagePart = new MimeBodyPart(); 
            messagePart.setContent( this.getEmailBodyText(), emailType ); 
            Multipart multipart = new MimeMultipart(); 
            multipart.addBodyPart(messagePart); 
            
            if ( this.getAttachments() != null && this.getAttachments().length != 0 )
            {
                for ( int i = 0; i < this.getAttachments().length; i++ )
                {
                    MimeBodyPart attachmentPart = new MimeBodyPart();
                    FileDataSource fileDataSource = new FileDataSource( this.getAttachments()[i] );
                    attachmentPart.setDataHandler( new DataHandler( fileDataSource ) );
                    attachmentPart.setFileName( this.getAttachments()[i].getName() );
                    multipart.addBodyPart( attachmentPart );
                }
            }
            
            msg.setContent(multipart);
            Transport.send(msg);
        } 
        catch (MessagingException mex) 
        {
            ret = false;
            this.errorMessage = mex.getMessage();
            System.out.println("EmailAdapter.sendEmail() failed with MessagingException: " + mex);
        }
        catch (Exception e)
        {
            ret = false;
            this.errorMessage = e.getMessage();
            System.out.println("EmailAdapter.sendEmail() failed with exception: " + e);
        }
        return ret;
    }

    public boolean sendEmail()
    {
        return sendEmail(EMAIL_TYPE_PLAIN);
    }

    public boolean sendEmailPlain()
    {
        return sendEmail(EMAIL_TYPE_PLAIN);
    }

    public boolean sendEmailHtml()
    {
        return sendEmail(EMAIL_TYPE_HTML);
    }
    
}
