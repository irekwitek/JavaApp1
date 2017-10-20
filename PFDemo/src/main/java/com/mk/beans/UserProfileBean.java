/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mk.beans;

import com.mk.classes.BusinessProcessControl;
import com.mk.classes.CommonData;
import com.mk.classes.EmailAdapter;
import com.mk.classes.User;
import com.mk.classes.UserEmailConfirmation;
import com.mk.classes.Utility;
import com.mk.dao.DBManager;
import java.io.Serializable;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;

/**
 *
 * @author irek
 */
@ManagedBean(name = "userProfileBean")
@ViewScoped
public class UserProfileBean implements Serializable {

    DBManager dbMgr;
    CommonData commonData;
    BusinessProcessControl bc;
    private UserLogin userLogin;
    // user data
    private User userToUpdate;
    private boolean verificationNeeded = false;
    private boolean verificationinProgress = false;
    private String authorizationCode = "";
    private String password = "";
    private UserEmailConfirmation userEC;

    /**
     * Creates a new instance of RegistrationBean
     */
    public UserProfileBean() {
        userLogin = (UserLogin) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("userLogin");
        // initiate business process control
        dbMgr = userLogin.getDbMgr();
        bc = userLogin.getBc();
        commonData = bc.getCommonData();
        userToUpdate = new User();
        this.bc.copyUser(userLogin.getUser(), userToUpdate);
    }

    public UserEmailConfirmation getUserEC() {
        return userEC;
    }

    public void setUserEC(UserEmailConfirmation userEC) {
        this.userEC = userEC;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getAuthorizationCode() {
        return authorizationCode;
    }

    public void setAuthorizationCode(String authorizationCode) {
        this.authorizationCode = authorizationCode;
    }

    public boolean isVerificationinProgress() {
        return verificationinProgress;
    }

    public void setVerificationinProgress(boolean verificationinProgress) {
        this.verificationinProgress = verificationinProgress;
    }

    public boolean isVerificationNeeded() {
        return verificationNeeded;
    }

    public void setVerificationNeeded(boolean verificationNeeded) {
        this.verificationNeeded = verificationNeeded;
    }

    public User getUserToUpdate() {
        return userToUpdate;
    }

    public void setUserToUpdate(User userToUpdate) {
        this.userToUpdate = userToUpdate;
    }

    public CommonData getCommonData() {
        return commonData;
    }

    public void setCommonData(CommonData commonData) {
        this.commonData = commonData;
    }

    // UI supporting methods
    private boolean isProfileDataCorrect() {
        boolean ret = true;
        if (userToUpdate.getUserFirstName().trim().equals("")
                || userToUpdate.getUserFirstName().trim().equals("")
                || userToUpdate.getUserLastName().trim().equals("")
                || userToUpdate.getUserEmail().trim().equals("")
                || userToUpdate.getAuthCodeTxt().trim().equals("")
                || userToUpdate.getAuthAnswer().trim().equals("")
                || userToUpdate.getAuthQuestion().trim().equals("")) {
            ret = false;
        }
        return ret;
    }

    public void updateUserProfile() {
        if (!isProfileDataCorrect()) {
            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "You must enter all data.", null);
            FacesContext.getCurrentInstance().addMessage(null, message);
        } else if (!Utility.isValidEmailAddress(userToUpdate.getUserEmail())) {
            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Please provide a valid email address.", null);
            FacesContext.getCurrentInstance().addMessage(null, message);
        } else if (checkEmailVerification()) {
            this.verificationNeeded = true;
        } else if (!this.bc.updateUserProfile(userToUpdate)) {
            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error updating your profile.", null);
            FacesContext.getCurrentInstance().addMessage(null, message);
        } else {
            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Your profile has been updated.", null);
            FacesContext.getCurrentInstance().addMessage(null, message);
            this.bc.copyUser(userToUpdate, userLogin.getUser());
        }
    }

    public void updateUserPassword() {
        if (userToUpdate.getLogonPassword().trim().equals("")
                || userToUpdate.getLogonPasswordConfirm().trim().equals("")) {
            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "You must enter all data.", null);
            FacesContext.getCurrentInstance().addMessage(null, message);
        } else if (!userToUpdate.getLogonPassword().trim().equals(userToUpdate.getLogonPasswordConfirm().trim())) {
            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Passwords do not match.", null);
            FacesContext.getCurrentInstance().addMessage(null, message);
        } else if (!this.bc.updateUserPassword(userToUpdate)) {
            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error updating your data.", null);
            FacesContext.getCurrentInstance().addMessage(null, message);
        } else {
            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Your password has been updated successfully.", null);
            FacesContext.getCurrentInstance().addMessage(null, message);
            userLogin.getUser().setLogonPassword(userToUpdate.getLogonPassword());
        }
    }

    public boolean checkEmailVerification() {
        boolean ret = false;
        this.userEC = this.bc.getUserEmailConfirmation(this.userLogin.getUser().getUserID(), userToUpdate.getUserEmail());
        if ( this.userEC.getEmailStatus().equalsIgnoreCase("N")) {
            ret = true;
        }
        return ret;
    }
    
    public void cancelEmailVerification() {
        this.verificationinProgress = false;
        this.verificationNeeded = false;
    }
    
    public String sendEmailConfirmationCode()
    {
        String ret = Utility.generateRandomNumberAsString(6);
        EmailAdapter ea = new EmailAdapter();
        ea.setEmailTo(this.userToUpdate.getUserEmail());
        ea.setEmailSubject("E-mail confirmation Code from Math Kangaroo");
        ea.setEmailBodyText("Your confirmation code is: " 
                + ret + "\n Please provide this code to verify your e-mail address and activate your Math Kangaroo account."
                + "\n\nSincerely,\n\nMATH KANGAROO TEAM\nwww.mathkangaroo.org");
        ea.sendEmail();
        return ret;
    }
    
    public void resendEmail(ActionEvent actionEvent) {
        String code = sendEmailConfirmationCode();
        this.userEC.setAuthCode(code);
        FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Please check your inbox for the e-mail sent to: " + this.userToUpdate.getUserEmail() + " with the 6-digit verification code.", null);
        FacesContext.getCurrentInstance().addMessage(null, message);
    }
    
    public void confirmEmail() 
    {
            String code = sendEmailConfirmationCode();
            this.userEC.setAuthCode(code);
            this.verificationinProgress = true;
    }
    
    public String verifyEmail()
    {
        String ret = "";
        if ( this.userEC.getAuthCode().equals(this.authorizationCode)
                && this.userToUpdate.getLogonPassword().equals(this.password))
        {
            this.userEC.setEmail(this.userToUpdate.getUserEmail());
            this.userEC.setEmailStatus("Y");
            this.bc.setUserEmailConfirmation(userEC);
            if (!this.bc.updateUserProfile(userToUpdate)) 
            {
                FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error updating your data.", null);
                FacesContext.getCurrentInstance().addMessage(null, message);
            }
            else
            {
                FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "New E-mail has been verified and the profile updated.", null);
                FacesContext.getCurrentInstance().addMessage(null, message);
                this.bc.copyUser(userToUpdate, userLogin.getUser());
                this.verificationNeeded = false;
                this.verificationinProgress = false;
            }
        }
        else
        {
            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Verification code or password is incorrect.", null);
            FacesContext.getCurrentInstance().addMessage(null, message);
        }
        return ret;
    }
}
