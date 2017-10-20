/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mk.beans;

import com.mk.classes.ActionLog;
import com.mk.classes.ApplicationProperties;
import com.mk.classes.DiscountCode;
import com.mk.classes.IDGenerator;
import com.mk.classes.Leader;
import com.mk.classes.ListCode;
import com.mk.classes.Location;
import com.mk.classes.LocationSession;
import com.mk.classes.LocationSimple;
import com.mk.classes.MKPaymentInfo;
import com.mk.classes.MKRefund;
import com.mk.classes.RegisteredStudent;
import com.mk.classes.StudentResult;
import com.mk.classes.User;
import com.mk.classes.Utility;
import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import org.primefaces.context.RequestContext;

/**
 *
 * @author irek
 */
@ManagedBean(name = "adminBean")
@SessionScoped
public class AdminBean implements Serializable {

    private UserLogin userLogin;
    private User userRequestor;
    private Leader leaderRequestor;
    private Location newCenter;
    
    // those 2 below are needed for admin only so it is in the admin bean
    private User selectedUser;
    private Leader selectedUserManager;
    private Leader selectedUserManagerCopy;
    private RegisteredStudent selectedStudent;
    private RegisteredStudent studentToUpdate;
    private MKPaymentInfo mkPayment;
    private MKPaymentInfo mkPaymentCreate = new MKPaymentInfo();
    
    private ArrayList<LocationSimple> mkCenters = new ArrayList<LocationSimple>();
    private LocationSimple centerSimple = new LocationSimple();
    private LocationSession centerSession = new LocationSession();
    
    private DiscountCode discountCode = new DiscountCode();
    
    private String dialogMessageText = "";
    private boolean flag1 = false;
    private boolean refundFlag = false;
    private boolean unregisterEmailFlag = false;
    private int refundAmount = 1;
    
    private ArrayList<StudentResult> studentResults;
    
    // if user is a manager keep list of his centers
    private ArrayList<Location> managerCenters;

    public AdminBean() {
        userLogin = (UserLogin) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("userLogin");
        this.mkCenters = userLogin.bc.getAdminCentersSimple();
    }

    public Leader getSelectedUserManager() {
        return selectedUserManager;
    }

    public void setSelectedUserManager(Leader selectedUserManager) {
        this.selectedUserManager = selectedUserManager;
    }

    public Leader getSelectedUserManagerCopy() {
        return selectedUserManagerCopy;
    }

    public void setSelectedUserManagerCopy(Leader selectedUserManagerCopy) {
        this.selectedUserManagerCopy = selectedUserManagerCopy;
    }

    public ArrayList<Location> getManagerCenters() {
        return managerCenters;
    }

    public void setManagerCenters(ArrayList<Location> managerCenters) {
        this.managerCenters = managerCenters;
    }

    public ArrayList<StudentResult> getStudentResults() {
        return studentResults;
    }

    public void setStudentResults(ArrayList<StudentResult> studentResults) {
        this.studentResults = studentResults;
    }

    public boolean isUnregisterEmailFlag() {
        return unregisterEmailFlag;
    }

    public void setUnregisterEmailFlag(boolean unregisterEmailFlag) {
        this.unregisterEmailFlag = unregisterEmailFlag;
    }

    public int getRefundAmount() {
        return refundAmount;
    }

    public void setRefundAmount(int refundAmount) {
        this.refundAmount = refundAmount;
    }

    public boolean isRefundFlag() {
        return refundFlag;
    }

    public void setRefundFlag(boolean refundFlag) {
        this.refundFlag = refundFlag;
    }

    public boolean isFlag1() {
        return flag1;
    }

    public void setFlag1(boolean flag1) {
        this.flag1 = flag1;
    }

    public LocationSession getCenterSession() {
        return centerSession;
    }

    public void setCenterSession(LocationSession centerSession) {
        this.centerSession = centerSession;
    }

    public LocationSimple getCenterSimple() {
        return centerSimple;
    }

    public void setCenterSimple(LocationSimple centerSimple) {
        this.centerSimple = centerSimple;
    }

    public ArrayList<LocationSimple> getMkCenters() {
        return mkCenters;
    }

    public void setMkCenters(ArrayList<LocationSimple> mkCenters) {
        this.mkCenters = mkCenters;
    }

    public Location getNewCenter() {
        return newCenter;
    }

    public void setNewCenter(Location newCenter) {
        this.newCenter = newCenter;
        userRequestor = userLogin.getBc().getUserById(newCenter.getCreatedByUser());
        if (userRequestor != null && userRequestor.getOtherID() != 0) {
            leaderRequestor = userLogin.getBc().getLeaderByID(userRequestor.getOtherID());
        } else {
            leaderRequestor = null;
        }
        this.newCenter.setLocationCode(ApplicationProperties.generateLocationCode(this.userLogin.getDbMgr(), this.newCenter.getLocationState(), this.newCenter.getLocationCity()));
        this.newCenter.setLocationRegistrationCode(this.newCenter.getLocationCode() + Utility.generateRandomNumberAsString(3));
    }

    public MKPaymentInfo getMkPayment() {
        return mkPayment;
    }

    public void setMkPayment(MKPaymentInfo mkPayment) {
        this.mkPayment = mkPayment;
    }

    public MKPaymentInfo getMkPaymentCreate() {
        return mkPaymentCreate;
    }

    public void setMkPaymentCreate(MKPaymentInfo mkPaymentCreate) {
        this.mkPaymentCreate = mkPaymentCreate;
    }

    public String getDialogMessageText() {
        return dialogMessageText;
    }

    public void setDialogMessageText(String dialogMessageText) {
        this.dialogMessageText = dialogMessageText;
    }

    public RegisteredStudent getStudentToUpdate() {
        return studentToUpdate;
    }

    public void setStudentToUpdate(RegisteredStudent studentToUpdate) {
        this.studentToUpdate = studentToUpdate;
    }

    public UserLogin getUserLogin() {
        return userLogin;
    }

    public void setUserLogin(UserLogin userLogin) {
        this.userLogin = userLogin;
    }

    public DiscountCode getDiscountCode() {
        return discountCode;
    }

    public void setDiscountCode(DiscountCode discountCode) {
        this.discountCode = discountCode;
    }

    public User getUserRequestor() {
        return userRequestor;
    }

    public void setUserRequestor(User userRequestor) {
        this.userRequestor = userRequestor;
    }

    public Leader getLeaderRequestor() {
        return leaderRequestor;
    }

    public void setLeaderRequestor(Leader leaderRequestor) {
        this.leaderRequestor = leaderRequestor;
    }

    public RegisteredStudent getSelectedStudent() {
        return selectedStudent;
    }

    public void setSelectedStudent(RegisteredStudent selectedStudent) {
        this.selectedStudent = this.userLogin.getBc().getStudentAndRegistrationForAdmin(selectedStudent.getStudentIdentificationCode(), ApplicationProperties.getCurrentRegistrationYear(this.userLogin.getDbMgr()));
        this.selectedUser = this.userLogin.getBc().getUserByStudentId(this.selectedStudent.getStudentIdentificationCode());
        this.mkPayment = this.userLogin.getBc().getMKPayment(this.selectedStudent.getTransactionID());
        this.studentResults = this.userLogin.bc.getAdminResultsForStudent(selectedStudent);
    }

    public User getSelectedUser() {
        return selectedUser;
    }

    public void setSelectedUser(User selectedUser) 
    {
        this.selectedUser = selectedUser;
        this.selectedUser.setUserDiscountCodes(this.userLogin.bc.getUserDiscountCodes(this.selectedUser.getUserID()));
        this.selectedUser.setUserStudents(this.userLogin.bc.getMyStudents(this.selectedUser));
        if (this.selectedUser.hasThisRole(2) && this.selectedUser.getOtherID() > 0) 
        {
            this.selectedUserManager = this.userLogin.bc.getLeaderByID(this.selectedUser.getOtherID());
            this.selectedUserManagerCopy = new Leader();
            this.userLogin.bc.copyManager(this.selectedUserManager, this.selectedUserManagerCopy);
            this.managerCenters = this.userLogin.bc.getManagerCenters(this.selectedUserManager, false );
        }
        else
        {
            this.selectedUserManager = new Leader();
            this.managerCenters = null;
        }
    }

    public void createDiscountCode() {
        this.discountCode.setUserIdFor(this.selectedUser.getUserID());
        this.discountCode.setIssuedBy(this.userLogin.getUser().getUserID());
        this.discountCode.setIssuedByName(this.userLogin.getUser().getUserFirstName() + " " + this.userLogin.getUser().getUserLastName());
        if ( this.discountCode.getValidUntil() == null )
        {
            String defaultDate = ApplicationProperties.getCurrentRegistrationYear(this.userLogin.dbMgr)+"-01-31";
            try {            
                Date date = new SimpleDateFormat("yyyy-MM-dd").parse(defaultDate);
                this.discountCode.setValidUntil(date);
            } catch (ParseException ex) {
                ;
            }
        }
        boolean ret = this.userLogin.bc.createDiscountCode(this.discountCode);
        if ( ret )
        {
            this.dialogMessageText = "Discount code(s) created successfully.";
        }
        else
        {
            this.dialogMessageText = "There was a problem creating discounts codes for this user.";
        }
        this.selectedUser.setUserDiscountCodes(this.userLogin.bc.getUserDiscountCodes(this.selectedUser.getUserID()));
        this.userLogin.bc.sendDiscountCodeInfoToUser(this.discountCode, this.selectedUser);
        this.discountCode = new DiscountCode();
        RequestContext.getCurrentInstance().execute("PF('dlgMessage').show();");
    }

    public void resendRegistrationEmail(RegisteredStudent rSt) {
        this.userLogin.bc.sendEmailRegistrationConfirmation(rSt,1);
        this.dialogMessageText = "Registration e-mail has been sent successfully.";
        RequestContext.getCurrentInstance().execute("PF('dlgMessage').show();");
    }
    
    public void resendThisRegistrationEmail() {
        this.userLogin.bc.sendEmailRegistrationConfirmation(this.selectedStudent,1);
        this.dialogMessageText = "Registration e-mail has been sent successfully.";
        RequestContext.getCurrentInstance().execute("PF('dlgMessage').show();");
    }

    public int getUserCodes(int code )
    {
        int cnt = 0;
        for ( DiscountCode dc : this.selectedUser.getUserDiscountCodes())
        {
            if ( dc.getStatus().equals("O"))
            {
                if ( code == 0 )
                {
                    cnt++;
                }
            }
            else
            {
                if ( code == 1)
                {
                    cnt++;
                }
            }
        }
        return cnt;
    }
    
    public ArrayList<ListCode> getDiscountPurposeCodes()
    {
        return ApplicationProperties.getDiscountPurposeCodes(this.userLogin.dbMgr);
    }
    
    
    public void updateStudentRegistration()
    {
        this.studentToUpdate.setUpdatedBy(this.userLogin.getUser().getUserID());
        this.studentToUpdate.setLevel( "" + this.studentToUpdate.getLevelCode());
        this.studentToUpdate.setTshirtSize(this.userLogin.commonData.getTshirtCodeName(this.studentToUpdate.getTshirtSizeCode()));
        if (this.userLogin.bc.adminUpdateRegistration(this.studentToUpdate) )
        {
            this.dialogMessageText = "Student registration was updated successfully.";
            RequestContext.getCurrentInstance().execute("PF('dlgMessage').show();");
            this.userLogin.bc.copyRegisteredStudent(studentToUpdate, selectedStudent);
        }
    }
    
    public void updateRegistrationCenter()
    {
        this.selectedStudent.setUpdatedBy(this.userLogin.getUser().getUserID());
        this.selectedStudent.setLocationID(this.centerSimple.getLocationID());
        this.selectedStudent.setSessionID(this.centerSession.getSessionID());

        for (LocationSimple locS : this.mkCenters) 
        {
            if (locS.getLocationID() == this.centerSimple.getLocationID() ) 
            {
                this.selectedStudent.setLocationCode(locS.getLocationCode());
                for ( LocationSession ls : locS.getLocationSessions())
                {
                    if ( ls.getSessionID() == this.centerSession.getSessionID() )
                    {
                        this.selectedStudent.setSessionName(ls.getSessionInfo());
                        break;
                    }
                }
                break;
            }
        }
        
        if (this.userLogin.bc.adminUpdateRegistration(this.selectedStudent) )
        {
            this.dialogMessageText = "Center for the registration was updated successfully.";
            ActionLog al = new ActionLog();
            al.setActionId(1);
            al.setTableName("TRegistration");
            al.setUserId(this.userLogin.getUser().getUserID());
            al.setComment("Updated center and/or session for registration ID:" + this.selectedStudent.getRegistrationID());
            this.userLogin.bc.createActionLog(al);
        }
        else
        {
             this.dialogMessageText = "There was an error updating registration center.";
        }
        RequestContext.getCurrentInstance().execute("PF('dlgMessage').show();");
    }
    
    public void updateStudentData()
    {
        if (this.userLogin.bc.adminUpdateStudent(this.studentToUpdate) )
        {
            this.dialogMessageText = "Student data was updated successfully.";
            this.userLogin.bc.copyRegisteredStudent(studentToUpdate, selectedStudent);
        }
        else
        {
             this.dialogMessageText = "There was an error updating student data.";
        }
        RequestContext.getCurrentInstance().execute("PF('dlgMessage').show();");
    }
    
    public void loadCenterSessions() 
    {
        this.centerSimple.setLocationSessions(null);
        this.centerSession.setSessionID(0);
        this.centerSession.setSessionName("");
        for (LocationSimple locS : this.mkCenters) 
        {
            if (locS.getLocationID() == this.centerSimple.getLocationID() ) 
            {
                this.centerSimple.setLocationSessions(locS.getLocationSessions());
                break;
            }
        }
    }
    
    public boolean isLevelAvailable()
    {
        boolean ret = false;
        LocationSession ls = this.userLogin.bc.getLocationSession(this.selectedStudent.getLocationID(), this.selectedStudent.getSessionID());
        if ( ls != null )
        {
            ret = (Utility.getStringPosition(ls.getLevels(),("" + this.selectedStudent.getLevelCode()),",") >= 0 ? true : false);
        }
        return ret;
    }
    
    public void createStudentRegistration() 
    {
        this.studentToUpdate.setUpdatedBy(this.userLogin.getUser().getUserID());
        this.studentToUpdate.setLevel("" + this.studentToUpdate.getLevelCode());
        this.studentToUpdate.setTshirtSize(this.userLogin.commonData.getTshirtCodeName(this.studentToUpdate.getTshirtSizeCode()));
        this.studentToUpdate.setStatus(2);
        this.studentToUpdate.setYear(ApplicationProperties.getCurrentRegistrationYear(this.userLogin.dbMgr));
        this.studentToUpdate.setLocationID(this.centerSimple.getLocationID());
        this.studentToUpdate.setSessionID(this.centerSession.getSessionID());
        this.studentToUpdate.setPaymentID("MK-ADMIN-" + this.userLogin.getUser().getUserID());
        this.studentToUpdate.setPaymentMethod(4);
        

        for (LocationSimple locS : this.mkCenters) 
        {
            if (locS.getLocationID() == this.centerSimple.getLocationID() ) 
            {
                this.selectedStudent.setLocationCode(locS.getLocationCode());
                for ( LocationSession ls : locS.getLocationSessions())
                {
                    if ( ls.getSessionID() == this.centerSession.getSessionID() )
                    {
                        this.selectedStudent.setSessionName(ls.getSessionInfo());
                        break;
                    }
                }
                break;
            }
        }
        boolean error = false;
        if (this.studentToUpdate.getRegistrationID() == 0) 
        {
            if (this.userLogin.bc.adminCreateRegistration(this.studentToUpdate) > 0 )
            {
                this.dialogMessageText = "Student registration was created successfully.";
                ActionLog al = new ActionLog();
                al.setActionId(5);
                al.setTableName("TRegistration");
                al.setUserId(this.userLogin.getUser().getUserID());
                al.setComment("Created registration ID: " + this.studentToUpdate.getRegistrationID() + " for student " + this.studentToUpdate.toString());
                this.userLogin.bc.createActionLog(al);
            }
            else
            {
                this.dialogMessageText = "There was an error creating registration.";
                error = true;
            }
        } 
        else 
        {
            if (this.userLogin.bc.adminUpdateRegistration(this.studentToUpdate))
            {
                this.dialogMessageText = "Student registration was updated successfully.";
                ActionLog al = new ActionLog();
                al.setActionId(8);
                al.setTableName("TRegistration");
                al.setUserId(this.userLogin.getUser().getUserID());
                al.setComment("Updated registration ID: " + this.studentToUpdate.getRegistrationID() + " for student " + this.studentToUpdate.toString());
                this.userLogin.bc.createActionLog(al);
             }
            else
            {
                this.dialogMessageText = "There was an error updating registration.";
                error = true;
            }
        }
        if ( this.flag1 )
        {
            this.mkPaymentCreate.setPaymentMethod(4);
            this.mkPaymentCreate.setPaypalPaymentId(this.studentToUpdate.getPaymentID());
            this.mkPaymentCreate.setPaypalTransactionId(this.studentToUpdate.getTransactionID());
            this.mkPaymentCreate.setMadeByUserId(this.userLogin.getUser().getUserID());
            this.mkPaymentCreate.setMadeByUserName(this.userLogin.getUser().getUserFirstName() + " " + this.userLogin.getUser().getUserLastName());
            if (this.userLogin.bc.adminCreatePayment(this.mkPaymentCreate))
            {
                this.dialogMessageText += " Payment record was created succesfully.";
                ActionLog al = new ActionLog();
                al.setActionId(9);
                al.setTableName("TPayments");
                al.setUserId(this.userLogin.getUser().getUserID());
                al.setComment("Created payment ID: " + this.mkPaymentCreate.getPaypalPaymentId());
                this.userLogin.bc.createActionLog(al);
                this.mkPaymentCreate = new MKPaymentInfo();
                this.flag1 = false;
            }
            else
            {
                this.dialogMessageText += "There was an error creating payment data record.";
            }
        }
        if ( !error )
        {
            this.setSelectedStudent(this.studentToUpdate);
        }
        RequestContext.getCurrentInstance().execute("PF('dlgMessage').show();");
    }
    
    public boolean disableRegisterButton()
    {
        boolean ret = false;
        if ( this.studentToUpdate == null || this.studentToUpdate.getTransactionID() == null || this.studentToUpdate.getTransactionID().trim().equals("")
                || this.centerSession.getSessionID() == 0 
                || this.studentToUpdate.getLevelCode() == 0
                || this.studentToUpdate.getTshirtSizeCode() == 0)
        {
            ret = true;
        }
        return ret;
    }
    
    public void unregisterStudent() 
    {
        RegisteredStudent temp = new RegisteredStudent();
        this.userLogin.getBc().copyRegisteredStudent(this.studentToUpdate, temp);
        MKRefund mkRefund = new MKRefund();
        mkRefund.setParentPaypalPayment(this.studentToUpdate.getPaymentID());
        mkRefund.setParentPaypalTransactionId(this.studentToUpdate.getTransactionID());
        mkRefund.setAmount(0.0);
        if ( this.refundFlag && this.studentToUpdate.getPaymentMethod() == 1 ) 
        {
            mkRefund.setAmount( (double)this.refundAmount );
        }
        mkRefund.setMadeByUserId(this.userLogin.getUser().getUserID());
        mkRefund.setMadeByUserName(this.userLogin.getUser().getUserFirstName() + " " + this.userLogin.getUser().getUserLastName());

        this.dialogMessageText = "";
        mkRefund = this.userLogin.getBc().unregisterAndRefund(this.studentToUpdate, mkRefund );
        if ( mkRefund.getResponseCode() != ApplicationProperties.PAYMENT_RESPONSE_CODE_SUCCESS)
        {
            this.dialogMessageText = "Refund Error: " + mkRefund.getResponseMessage();
        }
        else
        {
            this.dialogMessageText = "Student was unregistered. " + this.dialogMessageText;
            if ( this.unregisterEmailFlag )
            {
                this.userLogin.bc.sendUnregisterConfirmation( temp, mkRefund );
            }
        }
        
        // action log
        ActionLog al = new ActionLog();
        al.setActionId(10);
        al.setTableName("TRegistration");
        al.setUserId(this.userLogin.getUser().getUserID());
        al.setComment("Unregistred student : " + temp.getStudentIdentificationCode() + " with refund = $" + mkRefund.getAmount() + "\nCenter: " + temp.getLocationCode() + " Session: " + temp.getSessionName());
        this.userLogin.bc.createActionLog(al);
        
        this.setSelectedStudent(this.studentToUpdate);
        RequestContext.getCurrentInstance().execute("PF('dlgMessage').show();");
    }

    public void prepareCreateStudent()
    {
        this.studentToUpdate = new RegisteredStudent();
        this.studentToUpdate.setParentGuardianEmail(this.userLogin.getUser().getUserEmail());
    }
    
    public void createStudentData() 
    {
        if (this.checkStudentData()) 
        {
            this.studentToUpdate.setStudentIdentificationCode(IDGenerator.getStudentIdentificationCode(this.userLogin.dbMgr));
            int studentId = this.userLogin.bc.createStudent(this.studentToUpdate);
            this.studentToUpdate.setStudentID(studentId);
            ArrayList<RegisteredStudent> arr = new ArrayList<RegisteredStudent>();
            arr.add(this.studentToUpdate);
            this.userLogin.bc.addStudentsToUser(this.userLogin.getUser().getUserID(), arr);
            this.dialogMessageText = "Student data for " + this.studentToUpdate.getFirstName().toUpperCase() + " " + this.studentToUpdate.getLastName().toUpperCase() + " was created successfully.";

            // action log
            ActionLog al = new ActionLog();
            al.setActionId(6);
            al.setTableName("TStudents");
            al.setUserId(this.userLogin.getUser().getUserID());
            al.setComment("Created student " + this.studentToUpdate.toString());
            this.userLogin.bc.createActionLog(al);
            
            RequestContext.getCurrentInstance().execute("PF('dlgCreateStudent').hide();");
            RequestContext.getCurrentInstance().execute("PF('dlgMessage').show();");
        } 
        else 
        {
            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Message","Student first and last name are required!");
            RequestContext.getCurrentInstance().showMessageInDialog(message);
        }
    }

    public boolean checkStudentData()
    {
        boolean ret = true;
        if ( this.studentToUpdate == null 
                || this.studentToUpdate.getFirstName().trim().equals("")
                || this.studentToUpdate.getLastName().trim().equals("")
        )
        {
            ret = false;
        }
        return ret;
    }

    public void changeUser(User user) 
    {
        ArrayList<RegisteredStudent> arr = new ArrayList<RegisteredStudent>();
        arr.add(this.selectedStudent);
        this.userLogin.bc.addStudentsToUser(user.getUserID(), arr);
            
        // action log
        ActionLog al = new ActionLog();
        al.setActionId(11);
        al.setTableName("TUsers_Students");
        al.setUserId(this.userLogin.getUser().getUserID());
        al.setComment("Reassigned user [" + user.toString() + "] for student " + this.selectedStudent.toString());
        this.userLogin.bc.createActionLog(al);
        
        this.dialogMessageText = "Username " + user.toString() + " has been connected to this student.";
        RequestContext.getCurrentInstance().execute("PF('dlgMessage').show();");
        this.selectedUser = this.userLogin.getBc().getUserById(user.getUserID());
    }

    public void updateManagerData() 
    {
        Leader l = this.userLogin.bc.getLeaderByLogon(this.selectedUserManager.getLeaderLogonName());
        if ( l != null && l.getLeaderID() != this.selectedUserManager.getLeaderID())
        {
            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "This manager logon name is already taken.", null);
            FacesContext.getCurrentInstance().addMessage(null, message);
        }
        else
        {
            if ( this.userLogin.bc.updateManagerProfileFull(this.selectedUserManager))
            {
                this.userLogin.bc.copyManager(this.selectedUserManager, this.selectedUserManagerCopy);
                FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Manager data have been updated successfully.", null);
                FacesContext.getCurrentInstance().addMessage(null, message);
            }
            else
            {
                FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "There was a problem updating manager's data. Please report this incident.", null);
                FacesContext.getCurrentInstance().addMessage(null, message);
            }
        }
    }

    public void restoreManagerData() 
    {
        this.userLogin.bc.copyManager(this.selectedUserManagerCopy, this.selectedUserManager);
        FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Manager data have been restored to the last update.", null);
        FacesContext.getCurrentInstance().addMessage(null, message);
    }
}
