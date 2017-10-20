/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mk.beans;

import com.mk.classes.ApplicationProperties;
import com.mk.classes.BusinessProcessControl;
import com.mk.classes.CommonData;
import com.mk.classes.EmailAdapter;
import com.mk.classes.Location;
import com.mk.dao.DBManager;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

/**
 *
 * @author irek
 */
@ManagedBean(name = "adminNewCenterRequestBean")
@ViewScoped
public class AdminNewCenterRequestBean implements Serializable {

    DBManager dbMgr;
    CommonData commonData;
    BusinessProcessControl bc;
    private UserLogin userLogin;
    private AdminBean adminBean;
    boolean canCreateCenter = false;
    
    private ArrayList<Location> newCenters;
    

    /**
     * Creates a new instance of RegistrationBean
     */
    public AdminNewCenterRequestBean() {
        userLogin = (UserLogin) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("userLogin");
        adminBean = (AdminBean) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("adminBean");
        if ( adminBean == null )
        {
            adminBean = new AdminBean();
        }
        // initiate business process control
        dbMgr = userLogin.getDbMgr();
        bc = userLogin.getBc();
        commonData = bc.getCommonData();
        this.newCenters = this.bc.getNewCenterRequests();
        if ( this.adminBean.getLeaderRequestor() != null )
        {
            this.canCreateCenter = true;
        }
    }

    
    public ArrayList<Location> getNewCenters() {
        return newCenters;
    }

    public void setNewCenters(ArrayList<Location> newCenters) {
        this.newCenters = newCenters;
    }

    public CommonData getCommonData() {
        return commonData;
    }

    public void setCommonData(CommonData commonData) {
        this.commonData = commonData;
    }

    public String getConfirmationYear()
    {
        return ApplicationProperties.getCurrentRegistrationYear(this.dbMgr);
    }

    public boolean isCanCreateCenter() {
        return canCreateCenter;
    }

    public void setCanCreateCenter(boolean canCreateCenter) {
        this.canCreateCenter = canCreateCenter;
    }

    
    public void createNewManager()
    {
       int leaderId = userLogin.getBc().createNewManager(adminBean.getUserRequestor());
       if ( leaderId == 0 )
       {
            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "There was an error when creating a new manager.", null);
            FacesContext.getCurrentInstance().addMessage(null, message);
       }
       else
       {
           this.adminBean.setLeaderRequestor(userLogin.getBc().getLeaderByID(leaderId));
           this.canCreateCenter = true;
           FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Manager was created successfully.", null);
           FacesContext.getCurrentInstance().addMessage(null, message);
       }
    }

    public void createNewCenter() {
        int newCenterId = userLogin.getBc().createNewCenter(this.adminBean.getNewCenter());
        if (newCenterId == 0) {
            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "There was an error when creating a new center.", null);
            FacesContext.getCurrentInstance().addMessage(null, message);
        } else if (newCenterId < 0) {
            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "There was an error when creating new center session(s).", null);
            FacesContext.getCurrentInstance().addMessage(null, message);
        } else {
            this.canCreateCenter = false;
            String actionComment = "Request was Accepted on " + Calendar.getInstance().getTime().toString() + " by " + this.userLogin.toString();
            this.adminBean.getNewCenter().setComment(actionComment + this.adminBean.getNewCenter().getComment());
            this.userLogin.getBc().updateNewCenterRequestStatus(this.adminBean.getNewCenter(), "Y");
            this.adminBean.getNewCenter().setConverted("Y");
            this.sendApprovedEmail();
            if (this.userLogin.getBc().addManagerToCenter(newCenterId, this.adminBean.getLeaderRequestor().getLeaderID(), "Y")) {
                FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "New center was created successfully.", null);
                FacesContext.getCurrentInstance().addMessage(null, message);
            } else {
                FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "New center was created but the link with primary manager was not created. Verify tables.", null);
                FacesContext.getCurrentInstance().addMessage(null, message);
            }
        }
    }

    public String rejectRequest() {
        String ret = "MKRAdminNewCenterRequests";
        String actionComment = "\nRequest processed on " + Calendar.getInstance().getTime().toString() + " by " + this.userLogin.getUser().getFullName();
        this.adminBean.getNewCenter().setComment(this.adminBean.getNewCenter().getComment() + actionComment);
        this.userLogin.getBc().updateNewCenterRequestStatus(this.adminBean.getNewCenter(), "R");
        this.canCreateCenter = false;
        sendRejectionEmail();
        return ret;
        //FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "This new center request was rejected.", null);
        //FacesContext.getCurrentInstance().addMessage(null, message);
    }
    
    private void sendRejectionEmail()
    {
        EmailAdapter ea = new EmailAdapter();
        ea.setEmailTo(this.adminBean.getUserRequestor().getUserEmail() + "," + this.userLogin.getUser().getUserEmail());
        ea.setEmailReplyTo(ApplicationProperties.MATH_KANGAROO_EMAIL_INFO);
        ea.setEmailCC(ApplicationProperties.MATH_KANGAROO_EMAIL_PRESIDENT);
        ea.setEmailSubject("New Center Request status");

        String bodyText = "The request to create a new center submitted by " + this.adminBean.getUserRequestor().getUserFirstName() + " " + this.adminBean.getUserRequestor().getUserLastName()
                + " has been rejected.\n\n"
                + "Center: " + this.adminBean.getNewCenter().getLocationNameAndAddress()
                + "\n\nRejection Reason: " + this.adminBean.getNewCenter().getComment() + "\n\nThe Math Kangaroo team.";
        ea.setEmailBodyText(bodyText);
        ea.sendEmailPlain();
    }

    public void sendApprovedEmail() {
                String bodyText = "<html><body style='font-size:12.0pt;font-family:\"Times New Roman\",\"serif\"'>"
                + "  <p>Dear New Math Kangaroo Manager - we are happy to have you on our team!</p>"
                + "  <p>Your center has been approved and you are welcome to confirm your participation in the upcoming Math Kangaroo competition.</p>"
                + "  <p>Use your logon name and password on User or Manager Login page (upper right corner of MK homepage): <a href=\"https://www.mathkangaroo.org/MKR/\"><span style='background-color:yellow'>www.mathkangaroo.org/MKR/</span></a>. Be aware you have two accounts: manager's and user's.</p>"
                + "  <p>Confirm your e-mail address (once in a lifetime) and acknowledge <span style='font-style:\"italic\"'>Our Agreement between you and Math Kangaroo in USA.</span></p>"
                + "  <p>Verify/edit number of sessions, their time, capacity and levels of participation available at each session (one session is fine).</p>"
                + "  <p>Decide and mark your session(s) as a <span style='font-style:\"italic\"'>private</span> or a <span style='font-style:\"italic\"'>public</span>. If you choose a <span style='font-style:\"italic\"'>private</span> status for your session, look for the <span style='font-style:\"italic\"'>Center Private Session Access Code</span> within your center information. Please note it is a different set of letters and numbers than in the <span style='font-style:\"italic\"'>Center Code</span> alone. You would share this code with your families. We would suggest setting your session(s) as <span style='font-style:\"italic\"'>private</span> first to make sure your participants get the spot. You would later ask us for changing your place status to <span style='font-style:\"italic\"'>public</span> if you have empty space left.</p>"
                + "  <p>Be aware you can edit your session information only until the first student is registered.</p>"
                + "  <p>Confirm your center's participation. Once you click <span style='font-style:\"italic\"'>confirm</span> button, the registration to your Math Kangaroo center is enabled.</p>"
                + "  <p><span style='color:red'>The functionality is very new</span>. Your patience and feedback are always appreciated.</p>"

                + "  <p>Children of Primary Managers participate free AND need to be registered. Your supportive managers enjoy 50% discount of the $20 participation fee which is $10."
                + " About obtaining discounts please contact us at <a href=\"mailto:finance@mathkangaroo.org\" target=\"_blank\">finance@mathkangaroo.org</a> Send an email signifying you are a primary or supporting manager of your center. Provide the center name, city, state, and the name of your child (children).</p>"
                
                + "  <p>Registration of students will close on <span style='color:red'>December 15th. Late registration will be $35 from 12.16 to 12.31</span> where space is available.</p>"
                + "  <p>Please contact us with difficulties and anything you wish to share.</p>"
                  
                + "<p>Sincerely,\n Maria Omelanczuk, Izabela Szpiech, Joanna Matthiesen</p>"
                + "</body></html>";

        EmailAdapter ea = new EmailAdapter();

        ea.setEmailTo(this.adminBean.getUserRequestor().getUserEmail());
        ea.setEmailReplyTo(ApplicationProperties.MATH_KANGAROO_EMAIL_INFO);
        ea.setEmailCC(ApplicationProperties.MATH_KANGAROO_EMAIL_PRESIDENT + "," + ApplicationProperties.MATH_KANGAROO_EMAIL_CFO + "," + ApplicationProperties.MATH_KANGAROO_EMAIL_CIO);
        ea.setEmailSubject("APPROVED: " + this.adminBean.getNewCenter().getLocationName() + ", " + this.adminBean.getNewCenter().getLocationCity() + ", " + this.adminBean.getNewCenter().getLocationState());

        ea.setEmailBodyText(bodyText);
        ea.sendEmailHtml();

    }


}
