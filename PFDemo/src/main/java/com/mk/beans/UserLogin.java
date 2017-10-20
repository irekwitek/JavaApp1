/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mk.beans;

import com.mk.classes.ApplicationProperties;
import com.mk.classes.BusinessProcessControl;
import com.mk.classes.CommonData;
import com.mk.classes.EmailAdapter;
import com.mk.classes.Leader;
import com.mk.classes.Location;
import com.mk.classes.LocationSession;
import com.mk.classes.User;
import com.mk.classes.UserEmailConfirmation;
import com.mk.classes.UserPasswordRetrieval;
import com.mk.classes.Utility;
import com.mk.dao.DBConnectionData;
import com.mk.dao.DBManager;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;

/**
 *
 * @author irek
 */
@ManagedBean(name="userLogin")
@SessionScoped
public class UserLogin implements Serializable {

    DBManager dbMgr;
    CommonData commonData;
    BusinessProcessControl bc;
    private User user = new User();
    private User foundUser;
    private UserEmailConfirmation userEC;
    
    private UserPasswordRetrieval userPR = new UserPasswordRetrieval();
    
    private boolean userLogged = false;
    private boolean userEmailConfirmed = false;
    private boolean userEmailConfirmation = false;
    private boolean existingUserEmailConfirmation = false;
    private String password;
    private String authorizationCode;
    private String existingUserEmail;
    private String existingUserPhone;
    
    private boolean passwordRetrieval = false;
    private boolean readyToCreateUser = false;
    private String redirectPage = "";
    private String centerStudentsReturnPage;
    

    // for managers
    private Leader manager = new Leader();
    private boolean managerLogged = false;
    private boolean managerEmailConfirmed = false;
    private boolean existingManagerEmailConfirmation = false;
    private Location selectedCenter;
    private LocationSession selectedSession;
    private String managerRedirectPage;
    
    // for admin
    private boolean adminLogged = false;
    private String adminRedirectPage;
    
    /**
     * Creates a new instance of UserLogin
     */
    public UserLogin() 
    {
       ExternalContext ctx = FacesContext.getCurrentInstance().getExternalContext();
       String dataSource = ctx.getInitParameter("DataSourceName");
       if ( dataSource == null || dataSource.trim().equals(""))
       {
           dataSource = "jdbc/mysqlDB2";
       }
        
        // set all necessary database connections
        DBConnectionData[] cdArr = new DBConnectionData[DBManager.DB_NAME.length];
        cdArr[DBManager.MYSQL] = new DBConnectionData(DBManager.MYSQL,
                DBManager.DB_NAME[DBManager.MYSQL], dataSource );
        cdArr[DBManager.MSSQL] = new DBConnectionData(DBManager.MSSQL,
                DBManager.DB_NAME[DBManager.MSSQL]);
        cdArr[DBManager.ORACLE] = new DBConnectionData(DBManager.ORACLE,
                DBManager.DB_NAME[DBManager.ORACLE]);
        dbMgr = new DBManager();
        dbMgr.setConnections(cdArr);
        // initiate business process control
        bc = new BusinessProcessControl(dbMgr);
        commonData = bc.getCommonData();
        
    }
    
    // getters and setters
    public DBManager getDbMgr() {
        return dbMgr;
    }

    public void setDbMgr(DBManager dbMgr) {
        this.dbMgr = dbMgr;
    }

    public CommonData getCommonData() {
        return commonData;
    }

    public void setCommonData(CommonData commonData) {
        this.commonData = commonData;
    }

    public BusinessProcessControl getBc() {
        return bc;
    }

    public void setBc(BusinessProcessControl bc) {
        this.bc = bc;
    }

    public boolean isUserLogged() {
        return userLogged;
    }

    public void setUserLogged(boolean userLogged) {
        this.userLogged = userLogged;
    }
    
    
    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public boolean isUserEmailConfirmation() {
        return userEmailConfirmation;
    }

    public void setUserEmailConfirmation(boolean userEmailConfirmation) {
        this.userEmailConfirmation = userEmailConfirmation;
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

    public boolean isUserEmailConfirmed() {
        return userEmailConfirmed;
    }

    public void setUserEmailConfirmed(boolean userEmailConfirmed) {
        this.userEmailConfirmed = userEmailConfirmed;
    }

    public boolean isExistingUserEmailConfirmation() {
        return existingUserEmailConfirmation;
    }

    public void setExistingUserEmailConfirmation(boolean existingUserEmailConfirmation) {
        this.existingUserEmailConfirmation = existingUserEmailConfirmation;
    }

    public UserPasswordRetrieval getUserPR() {
        return userPR;
    }

    public void setUserPR(UserPasswordRetrieval userPR) {
        this.userPR = userPR;
    }

    public boolean isPasswordRetrieval() {
        return passwordRetrieval;
    }

    public void setPasswordRetrieval(boolean passwordRetrieval) {
        this.passwordRetrieval = passwordRetrieval;
    }

    public User getFoundUser() {
        return foundUser;
    }

    public void setFoundUser(User foundUser) {
        this.foundUser = foundUser;
    }

    public String getRedirectPage() {
        return redirectPage;
    }

    public void setRedirectPage(String redirectPage) {
        this.redirectPage = redirectPage;
    }

    public String getCenterStudentsReturnPage() {
        return centerStudentsReturnPage;
    }

    public void setCenterStudentsReturnPage(String centerStudentsReturnPage) {
        this.centerStudentsReturnPage = centerStudentsReturnPage;
    }

    public void isUserLoggedIn() 
    {
/*        if ( ApplicationProperties.getRegistrationRuntimeMode(dbMgr) != 0 )
        {
            ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
            try {
                ec.redirect(ec.getRequestContextPath() + "/faces/MKRMaintenance.xhtml");
            } catch (IOException ex) {
                System.out.println("Exception redirecting: " + ex);
            }
        } */
        if (!this.userLogged ) 
        {
            ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
            try {
                String redirectPage = "/faces/MKRLogin.xhtml";
                ec.redirect(ec.getRequestContextPath() + redirectPage);
            } catch (IOException ex) {
                System.out.println("Exception redirecting: " + ex);
            }
        }
    }

    public void checkRegistrationOpen() 
    {
        int regFlag = ApplicationProperties.getRegistrationRuntimeMode(this.dbMgr);
        if (!this.userLogged || regFlag != 0 ) 
        {
            ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
            try {
                String redirectPage = "/faces/MKRHome.xhtml";
                ec.redirect(ec.getRequestContextPath() + redirectPage);
            } catch (IOException ex) {
                System.out.println("Exception redirecting: " + ex);
            }
        }
    }

    public boolean isRegistrationOpen() 
    {
        boolean ret = false;
        int regFlag = ApplicationProperties.getRegistrationRuntimeMode(this.dbMgr);
        
        if (regFlag == 0 ) 
        {
            ret = true;
        }
        return ret;
    }

    public boolean isStudentCheckinOpen() 
    {
        boolean ret = false;
        int regFlag = ApplicationProperties.getStudentCheckinMode(this.dbMgr);
        
        if (regFlag == 1 ) 
        {
            ret = true;
        }
        return ret;
    }
    
    public void checkUserLogIn() 
    {
/*        if ( ApplicationProperties.getRegistrationRuntimeMode(dbMgr) != 0 )
        {
            ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
            try {
                ec.redirect(ec.getRequestContextPath() + "/faces/MKRMaintenance.xhtml");
            } catch (IOException ex) {
                System.out.println("Exception redirecting: " + ex);
            }
        } */
        if (this.userLogged) 
        {
            ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
            try {
                ec.redirect(ec.getRequestContextPath() + "/faces/MKRHome.xhtml");
            } catch (IOException ex) {
                System.out.println("Exception redirecting: " + ex);
            }
        }
    }

    public void goBackToLoginPage() {
        this.passwordRetrieval = false;
        //this.userPR = new UserPasswordRetrieval();
        ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
        try {
            ec.redirect(ec.getRequestContextPath() + "/faces/MKRLogin.xhtml");
        } catch (IOException ex) {
            System.out.println("Exception redirecting: " + ex);
        }
    }

    
    public boolean isAllDataProvided() {
        readyToCreateUser = true;
        if ( this.user.getUserEmail().equals("")
                || this.user.getUserFirstName().equals("")
                || this.user.getUserLastName().equals("")
                || this.user.getAuthQuestion().equals("")
                || this.user.getAuthAnswer().equals("")
                || this.user.getLogonName().equals("")
                || this.user.getLogonPassword().equals("")
                || this.user.getLogonPasswordConfirm().equals("")
                )
        {
            readyToCreateUser = false;
        }
        return readyToCreateUser;
    }

    public void setReadyToCreateUser(boolean readyToCreateUser) {
        this.readyToCreateUser = readyToCreateUser;
    }

    public void validateUser() 
    {
        boolean flag = true;
        if ( !this.isAllDataProvided() )
        {
            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Please provide all required data.",  null);
            FacesContext.getCurrentInstance().addMessage(null, message);
            flag = false;
        }
        if (!this.isUserLogonNameAvailable())
        {
            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Username is not available.",  null);
            FacesContext.getCurrentInstance().addMessage(null, message);
            flag = false;
        }
        if (!Utility.isValidEmailAddress(this.user.getUserEmail()))
        {
            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "The email address is not valid.",  null);
            FacesContext.getCurrentInstance().addMessage(null, message);
            flag = false;
        }
        if (!this.user.getLogonPassword().equals(this.user.getLogonPasswordConfirm()))
        {
            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Password doesn't match Confirm Password.",  null);
            FacesContext.getCurrentInstance().addMessage(null, message);
            flag = false;
        }
        if ( flag )
        {
            createUser();
        }
    }
 
    public String login() {
        String ret = "";
        if (!user.getLogonName().equals("") && !user.getLogonPassword().equals("")) 
        {
            User userExisting = this.bc.getUserByLogon(user.getLogonName(), user.getLogonPassword());
            if (userExisting != null) 
            {
                this.user = userExisting;
                this.existingUserEmail = userExisting.getUserEmail();
                this.userEC = this.bc.getUserEmailConfirmation(userExisting.getUserID(), userExisting.getUserEmail());
                if (this.userEC.getEmailStatus().equals("N")) 
                {
                    this.existingUserEmailConfirmation = true;
                } else {
                    this.userLogged = true;
                    this.authorizationCode = "";
                    this.setUserRoles();
                    if ( this.redirectPage.equals(""))
                    {
                        ret = "MKRHome";
                    }
                    else
                    {
                        ret = this.redirectPage;
                        this.redirectPage = "";
                    }
                }
            }
            else
            {
                FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Username and password combination incorrect!",  null);
                FacesContext.getCurrentInstance().addMessage(null, message);
            }
        }
        else
        {
            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Please provide both username and password!",  null);
            FacesContext.getCurrentInstance().addMessage(null, message);

        }
        return ret;
    }
 
    public void setUserRoles() 
    {
        if (this.user.getRoleCode() > 0 && this.user.getOtherID() != 0) 
        {
            this.manager = this.bc.getLeaderByID(this.user.getOtherID());
            if (this.manager != null) 
            {
                this.managerLogged = true;
            }
        }
        if (this.user.getAdminRole() > 0) {
            this.adminLogged = true;
        }
    }
    
    public void logOff() {
        this.user = new User();
        this.userLogged = false;
        this.manager = new Leader();
        this.managerLogged = false;
        this.adminLogged = false;
        this.redirectPage = "";
    }

    
    public void loginConfirm() 
    {
        if (user.getUserEmail() != null && !user.getUserEmail().equals("") && Utility.isValidEmailAddress(user.getUserEmail()) ) 
        {
            String code = sendEmailConfirmationCode(user.getUserEmail());
            this.userEC.setAuthCode(code);
            this.setUserEmailConfirmation(true);
        }
        else
        {
            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Please provide valid e-mail address for verification.",  null);
            FacesContext.getCurrentInstance().addMessage(null, message);
        }
    }
    
    public void cancelConfirm() 
    {
            this.setUserEmailConfirmation(false);
            this.setExistingUserEmailConfirmation(false);
            this.user = new User();
    }

    public void createUser()
    {
        if ( this.bc.isUserLogonNameAvailable(this.user.getLogonName()))
        {
            user.setUserID(this.bc.createNewUser(user));
            if ( user.getUserID() != 0 )
            {
                String code = sendEmailConfirmationCode(user.getUserEmail());
                this.userEC = new UserEmailConfirmation();
                this.userEC.setEmail(user.getUserEmail());
                this.userEC.setEmailStatus("N");
                this.userEC.setAuthCode(code);
                this.userEC.setUserId(user.getUserID());
                this.setUserEmailConfirmation(true);
            }
        }
    }

    public boolean isUserLogonNameAvailable()
    {
        boolean ret = true;
        if (!this.bc.isUserLogonNameAvailable(this.user.getLogonName()))
        {
            ret = false;
        }
        return ret;
    }

    public String sendEmailConfirmationCode( String email )
    {
        String ret = Utility.generateRandomNumberAsString(6);
        EmailAdapter ea = new EmailAdapter();
        ea.setEmailTo(email);
        ea.setEmailSubject("E-mail confirmation Code from Math Kangaroo");
        ea.setEmailBodyText("Your confirmation code is: " 
                + ret + "\n Please provide this code to verify your e-mail address and activate your Math Kangaroo account."
                + "\n\nSincerely,\n\nMATH KANGAROO TEAM\nwww.mathkangaroo.org");
        ea.sendEmail();
        return ret;
    }

    public void sendUserCredentials( UserPasswordRetrieval upr )
    {
        EmailAdapter ea = new EmailAdapter();
        ea.setEmailTo(upr.getEmail());
        ea.setEmailSubject("MK user information");
        ea.setEmailBodyText("Your MK website username is: " + upr.getLogonName() 
                + "\n Your password is: " + upr.getPassword()
                + "\n\nSincerely,\n\nMATH KANGAROO TEAM\nwww.mathkangaroo.org");
        ea.sendEmail();
    }

    public void resendEmail() {
        String code = sendEmailConfirmationCode(this.user.getUserEmail());
        this.userEC.setAuthCode(code);
        FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Please check your inbox for the e-mail sent to: " + user.getUserEmail() + " with the 6-digit verification code.", null);
        FacesContext.getCurrentInstance().addMessage(null, message);
    }
    
    public String verifyEmail() {
        String ret = "";
        if (this.userEC.getAuthCode().equals(this.authorizationCode)
                && user.getLogonPassword().equals(this.password)) 
        {
            this.userEC.setEmail(this.user.getUserEmail());
            this.userEC.setEmailStatus("Y");
            this.bc.setUserEmailConfirmation(userEC);
            this.userEmailConfirmed = true;
            this.userEmailConfirmation = false;
            this.existingUserEmailConfirmation = false;
            this.authorizationCode = "";
            this.userLogged = true;
            this.bc.updateUser(user);
            this.setUserRoles();
            if (this.redirectPage.equals("")) {
                ret = "MKRHome";
            } else {
                ret = this.redirectPage;
            }
        } else {
            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_WARN, "Verification code or password is incorrect.", null);
            FacesContext.getCurrentInstance().addMessage(null, message);
        }
        return ret;
    }

    public String resetUserPassword() 
    {
        this.userPR = new UserPasswordRetrieval();
        return "MKRPswReset";
    }

    public void findMatch() {
        boolean errorFlag = false;
        String errorMessage = "";
        ArrayList<User> users = this.bc.findMatchedUser(this.userPR);
        if ( users.isEmpty() )
        {
            errorFlag = true;
            errorMessage = "Cannot match any existing user.";
        }
        else if ( users.size() > 1 )
        {
            errorFlag = true;
            errorMessage = "Too many matches found, please narrow down search by entering more information.";
        }
        else
        {
            this.foundUser = users.get(0);
            if ( this.foundUser.getAuthQuestion() == null || this.foundUser.getAuthAnswer() == null
                || this.foundUser.getAuthQuestion().trim().equals("") 
                || this.foundUser.getAuthAnswer().trim().equals("")) 
            {
                errorFlag = true;
                errorMessage = "User found but has no secret question/answer created. Please contact info@mathkangaroo.org for help recovering your credentials.";
            } 
            else 
            {
                this.passwordRetrieval = true;
                this.userPR.setAuthQuestion(this.foundUser.getAuthQuestion());
                this.userPR.setPassword(this.foundUser.getLogonPassword());
                this.userPR.setLogonName(this.foundUser.getLogonName());
            }
        }
        if (errorFlag) 
        {
            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, errorMessage, null);
            FacesContext.getCurrentInstance().addMessage(null, message);
        }
    }
   

    public String compareAnswer()
    {
        String ret = "";
        if ( !Utility.isValidEmailAddress(this.userPR.getEmail()))
        {
            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Please provide a valid e-mail address.", null);
            FacesContext.getCurrentInstance().addMessage(null, message);
        }
        else if ( this.userPR.getAuthAnswer().trim().toLowerCase().equals(this.foundUser.getAuthAnswer().trim().toLowerCase()))
        {
            this.sendUserCredentials(this.userPR);
            ret = "MKRLogin";
            this.passwordRetrieval = false;
        }
        else
        {
            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Your secure answer does not match our record.", null);
            FacesContext.getCurrentInstance().addMessage(null, message);
        }
        return ret;
    }
    
  

    
    
    // for manager
    public Leader getManager() {
        return manager;
    }

    public void setManager(Leader manager) {
        this.manager = manager;
    }

    public boolean isManagerLogged() {
        return managerLogged;
    }

    public void setManagerLogged(boolean managerLogged) {
        this.managerLogged = managerLogged;
    }

    public boolean isManagerEmailConfirmed() {
        return managerEmailConfirmed;
    }

    public void setManagerEmailConfirmed(boolean managerEmailConfirmed) {
        this.managerEmailConfirmed = managerEmailConfirmed;
    }

    public boolean isExistingManagerEmailConfirmation() {
        return existingManagerEmailConfirmation;
    }

    public void setExistingManagerEmailConfirmation(boolean existingManagerEmailConfirmation) {
        this.existingManagerEmailConfirmation = existingManagerEmailConfirmation;
    }

    public Location getSelectedCenter() {
        return selectedCenter;
    }

    public void setSelectedCenter(Location selectedCenter) {
        this.selectedCenter = selectedCenter;
    }

    public LocationSession getSelectedSession() {
        return selectedSession;
    }

    public void setSelectedSession(LocationSession selectedSession) {
        this.selectedSession = selectedSession;
    }

    public String getManagerRedirectPage() {
        return managerRedirectPage;
    }

    public void setManagerRedirectPage(String managerRedirectPage) {
        this.managerRedirectPage = managerRedirectPage;
    }

    
    
    public void checkManagerLogIn() 
    {
        if (this.managerLogged) 
        {
            ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
            try {
                ec.redirect(ec.getRequestContextPath() + "/faces/MKRMgrHome.xhtml");
            } catch (IOException ex) {
                System.out.println("Exception redirecting: " + ex);
            }
        }
    }

        public String managerLogin() {
        String ret = "";
        if (!manager.getLeaderLogonName().equals("") && !manager.getLeaderLogonPassword().equals("")) 
        {
            Leader managerExisting = this.bc.getLeaderByLogon(manager.getLeaderLogonName(), manager.getLeaderLogonPassword());
            if (managerExisting != null) 
            {
                this.manager = managerExisting;
                User userFromManager = this.bc.getUserByManagerId(manager.getLeaderID());
                this.userEC = this.bc.getUserEmailConfirmation(userFromManager.getUserID(), managerExisting.getEmail());
                this.user = userFromManager;
                if (this.userEC.getEmailStatus().equals("N")) 
                {
                    this.existingManagerEmailConfirmation = true;
                } else {
                    this.managerLogged = true;
                    this.userLogged = true;
                    if ( this.user.getAdminRole() > 0 )
                    {
                        this.adminLogged = true;
                    }
                    this.userEmailConfirmation = false;
                    if ( this.redirectPage.equals(""))
                    {
                        ret = "MKRMgrHome";
                    }
                    else
                    {
                        ret = this.redirectPage;
                        this.redirectPage = "";
                    }
                }
            }
            else
            {
                FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Manager log-in name and password combination incorrect!",  null);
                FacesContext.getCurrentInstance().addMessage(null, message);
            }
        }
        else
        {
            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Please provide both manager log-in and password!",  null);
            FacesContext.getCurrentInstance().addMessage(null, message);

        }
        return ret;
    }

    public void managerLoginConfirm() 
    {
        if (manager.getEmail()!= null && !manager.getEmail().equals("") && Utility.isValidEmailAddress(manager.getEmail()) ) 
        {
            String code = sendEmailConfirmationCode(manager.getEmail());
            this.userEC.setAuthCode(code);
            this.setUserEmailConfirmation(true);
        }
        else
        {
            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Please provide valid e-mail address for verification.",  null);
            FacesContext.getCurrentInstance().addMessage(null, message);
        }
    }
    
    public void cancelManagerConfirm() 
    {
            this.setExistingManagerEmailConfirmation(false);
            this.setUserEmailConfirmation(false);
            this.manager = new Leader();
            this.user = new User();
    }

    public void resendManagerEmail(ActionEvent actionEvent) {
        sendEmailConfirmationCode(this.manager.getEmail());
        FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Please check your inbox for the e-mail sent to: " + manager.getEmail() + " with the 6-digit verification code.", null);
        FacesContext.getCurrentInstance().addMessage(null, message);
    }

    public String verifyManagerEmail() {
        String ret = "";
        if (this.userEC.getAuthCode().equals(this.authorizationCode)
                && manager.getLeaderLogonPassword().equals(this.password)) {
            this.userEC.setEmail(this.manager.getEmail());
            this.userEC.setEmailStatus("Y");
            this.bc.setUserEmailConfirmation(userEC);
            this.managerEmailConfirmed = true;
            this.managerLogged = true;
            this.userLogged = true;
            this.bc.updateManagerSimple(manager);
            if (this.redirectPage.equals("")) {
                ret = "MKRMgrHome";
            } else {
                ret = this.redirectPage;
                this.redirectPage = "";
            }
        } else {
            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_WARN, "Verification code or password is incorrect.", null);
            FacesContext.getCurrentInstance().addMessage(null, message);
        }
        return ret;
    }
    
    public void isManagerLoggedIn() {
        String calledPage = FacesContext.getCurrentInstance().getViewRoot().getViewId();
        if (!this.managerLogged) {
            ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
            try {
                ec.redirect(ec.getRequestContextPath() + "/faces/MKRMgrLogin.xhtml");
            } catch (IOException ex) {
                System.out.println("Exception redirecting: " + ex);
            }
        }
        /*
        else {
            if (this.getManager().isPrimaryManager() && this.getManager().getAgreementStatus().equals("N") && !calledPage.equals("/MKRMgrAgreement.xhtml")) {
                ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
                try {
                    ec.redirect(ec.getRequestContextPath() + "/faces/MKRMgrAgreement.xhtml");
                } catch (IOException ex) {
                    System.out.println("Exception redirecting: " + ex);
                }
            }
        
        */
    }

    
    // for admin
    public boolean isAdminLogged() {
        return adminLogged;
    }

    public void setAdminLogged(boolean adminLogged) {
        this.adminLogged = adminLogged;
    }

    public String getAdminRedirectPage() {
        return adminRedirectPage;
    }

    public void setAdminRedirectPage(String adminRedirectPage) {
        this.adminRedirectPage = adminRedirectPage;
    }

    public void isAdminLoggedIn() {
        String calledPage = FacesContext.getCurrentInstance().getViewRoot().getViewId();
        if (!this.userLogged) 
        {
            // below commented cause admin to admin remembers the other page
           //this.setRedirectPage(calledPage);
           ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
            try {
                ec.redirect(ec.getRequestContextPath() + "/faces/MKRLogin.xhtml");
            } catch (IOException ex) {
                System.out.println("Exception redirecting: " + ex);
            }
        }
        else if ( !this.adminLogged )
        {
           ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
            try {
                ec.redirect(ec.getRequestContextPath() + "/faces/MKRHome.xhtml");
            } catch (IOException ex) {
                System.out.println("Exception redirecting: " + ex);
            }
        }
    }
    
    public boolean isManagerAgreementModeActive()
    {
        return (ApplicationProperties.getManagersAgreementMode(dbMgr) == 0 ? false : true );
    }
    
    // for session tracking
    public String toString() {
        String ret = "User not logged in yet";
        if ( this.getUser() != null )
        {
            ret = "{ " + this.getUser().getUserFirstName() + " " + this.getUser().getUserLastName() + " ID: " + this.getUser().getUserID() + " }";
        }
        return ret; 
    }
    
    
}
