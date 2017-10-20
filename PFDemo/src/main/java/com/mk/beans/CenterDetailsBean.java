/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mk.beans;

import com.mk.classes.ActionLog;
import com.mk.classes.ApplicationProperties;
import com.mk.classes.BusinessProcessControl;
import com.mk.classes.CommonData;
import com.mk.classes.EmailAdapter;
import com.mk.classes.Leader;
import com.mk.classes.Location;
import com.mk.classes.LocationSession;
import com.mk.classes.ManagerRequest;
import com.mk.classes.RegisteredStudent;
import com.mk.classes.User;
import com.mk.dao.DBManager;
import java.io.Serializable;
import java.sql.Time;
import java.util.ArrayList;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import org.primefaces.context.RequestContext;

/**
 *
 * @author irek
 */
@ManagedBean(name = "centerDetailsBean")
@ViewScoped
public class CenterDetailsBean implements Serializable {

    DBManager dbMgr;
    CommonData commonData;
    BusinessProcessControl bc;
    private UserLogin userLogin;
    
    private Location selectedCenter;
    private String selectedCenterActiveStatus;
    private ArrayList<LocationSession> centerSessions;
    private LocationSession sessionToDelete;
    private LocationSession sessionBeingUpdated;
    private LocationSession tempSession = new LocationSession();
    private ArrayList<String> levels;
    private String[] selectedLevels;
    private boolean inSessionEditMode;
    private boolean addingNewSession;
    private String dialogMessageText;
    private String supportManagerLogonName;
    private ArrayList<Leader> centerManagers;
    private Leader managerToDelete;
    private Leader managerToUpdate;
    private boolean findingUser = false;
    private User userToBeManager;
    private String managerRequestComment;
    private ArrayList<ManagerRequest> managerRequests;
    private ManagerRequest managerRequest;

    /**
     * Creates a new instance of RegistrationBean
     */
    public CenterDetailsBean() {
        userLogin = (UserLogin) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("userLogin");
        // initiate business process control
        dbMgr = userLogin.getDbMgr();
        bc = userLogin.getBc();
        commonData = bc.getCommonData();
        this.userLogin.setSelectedCenter(this.bc.getLocationByID(this.userLogin.getSelectedCenter().getLocationID()));
        this.selectedCenter = this.userLogin.getSelectedCenter();
        this.selectedCenterActiveStatus = this.selectedCenter.getActive();
        if ( this.userLogin.isManagerLogged())
        {
           this.selectedCenter.setManagerPrimary(this.userLogin.getManager().getLeaderID() == this.selectedCenter.getLeader().getLeaderID());
        }
        this.centerManagers = this.bc.getLocationManagers(this.selectedCenter.getLocationID());
        this.managerRequests = this.bc.getLocationManagerRequests(this.selectedCenter.getLocationID());
        this.centerSessions = this.bc.getLocationSessions(this.selectedCenter.getLocationID());
        for (LocationSession ls : this.centerSessions )
        {
            ls.setSeatsTaken(this.bc.getCenterSessionTakenSeats(this.selectedCenter.getLocationID(), ls.getSessionID()));
            ls.setEditDisabled( ls.getSeatsTaken() > 0 ? true : false );
        }
        levels = new ArrayList<String>();
        for ( int i = 1; i < 13; i++ )
        {
            levels.add("" + i);
        }
    }

    public ManagerRequest getManagerRequest() {
        return managerRequest;
    }

    public void setManagerRequest(ManagerRequest managerRequest) {
        this.managerRequest = managerRequest;
    }

    public Leader getManagerToUpdate() {
        return managerToUpdate;
    }

    public void setManagerToUpdate(Leader managerToUpdate) {
        this.managerToUpdate = managerToUpdate;
    }

    public ArrayList<ManagerRequest> getManagerRequests() {
        return managerRequests;
    }

    public void setManagerRequests(ArrayList<ManagerRequest> managerRequests) {
        this.managerRequests = managerRequests;
    }

    public String getManagerRequestComment() {
        return managerRequestComment;
    }

    public void setManagerRequestComment(String managerRequestComment) {
        this.managerRequestComment = managerRequestComment;
    }

    public User getUserToBeManager() {
        return userToBeManager;
    }

    public void setUserToBeManager(User userToBeManager) {
        this.userToBeManager = userToBeManager;
    }

    public boolean isFindingUser() {
        return findingUser;
    }

    public void setFindingUser(boolean findingUser) {
        this.findingUser = findingUser;
    }

    public Leader getManagerToDelete() {
        return managerToDelete;
    }

    public void setManagerToDelete(Leader managerToDelete) {
        this.managerToDelete = managerToDelete;
    }

    public ArrayList<Leader> getCenterManagers() {
        return centerManagers;
    }

    public void setCenterManagers(ArrayList<Leader> centerManagers) {
        this.centerManagers = centerManagers;
    }

    public String getSupportManagerLogonName() {
        return supportManagerLogonName;
    }

    public void setSupportManagerLogonName(String supportManagerLogonName) {
        this.supportManagerLogonName = supportManagerLogonName;
    }

    public String getDialogMessageText() {
        return dialogMessageText;
    }

    public void setDialogMessageText(String dialogMessageText) {
        this.dialogMessageText = dialogMessageText;
    }

    public boolean isAddingNewSession() {
        return addingNewSession;
    }

    public void setAddingNewSession(boolean addingNewSession) {
        this.addingNewSession = addingNewSession;
    }

    public boolean isInSessionEditMode() {
        return inSessionEditMode;
    }

    public void setInSessionEditMode(boolean inSessionEditMode) {
        this.inSessionEditMode = inSessionEditMode;
    }

    public LocationSession getTempSession() {
        return tempSession;
    }

    public void setTempSession(LocationSession tempSession) {
        this.tempSession = tempSession;
    }

    public ArrayList<String> getLevels() {
        return levels;
    }

    public void setLevels(ArrayList<String> levels) {
        this.levels = levels;
    }

    public String[] getSelectedLevels() {
        return selectedLevels;
    }

    public void setSelectedLevels(String[] selectedLevels) {
        this.selectedLevels = selectedLevels;
    }

    public ArrayList<LocationSession> getCenterSessions() {
        return centerSessions;
    }

    public void setCenterSessions(ArrayList<LocationSession> centerSessions) {
        this.centerSessions = centerSessions;
    }

    public LocationSession getSessionToDelete() {
        return sessionToDelete;
    }

    public void setSessionToDelete(LocationSession sessionToDelete) {
        this.sessionToDelete = sessionToDelete;
    }

    public LocationSession getSessionBeingUpdated() {
        return sessionBeingUpdated;
    }

    public void setSessionBeingUpdated(LocationSession sessionBeingUpdated) {
        this.sessionBeingUpdated = sessionBeingUpdated;
    }

    public Location getSelectedCenter() {
        return selectedCenter;
    }

    public void setSelectedCenter(Location selectedCenter) {
        this.selectedCenter = selectedCenter;
    }

    public CommonData getCommonData() {
        return commonData;
    }

    public void setCommonData(CommonData commonData) {
        this.commonData = commonData;
    }

    public String getCurrentRegistrationYear()
    {
        return ApplicationProperties.getCurrentRegistrationYear(this.dbMgr);
    }

    public String managerCenterStatusText() {
        String ret = "You are the primary manager of this center";
        if (!selectedCenter.isManagerPrimary()) {
            ret = "You are a supporting manager of this center";
        }
        return ret;
    }

    public String confirmCenterText()
    {
        String ret = "Confirmed";
        if (!selectedCenter.getLocationConfirmationStatus().equals("Y"))
        {
            if (selectedCenter.isManagerPrimary() )
            {
                ret = "Click to Confirm";
            }
            else
            {
                ret = "Not confirmed";
            }
        }
        return ret;
    }
    
    public void confirmCenter()
    {
        selectedCenter.setLocationConfirmationStatus("Y");
        this.bc.sendCenterConfirmation(this.userLogin.getManager(), selectedCenter);
    }

    public void updateCenterStatus()
    {
        if (!this.bc.updateCenterStatus(selectedCenter) )
        {
            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "There was a problem updateing center status!", null);
            FacesContext.getCurrentInstance().addMessage(null, message);
        }
        else
        {
            this.selectedCenterActiveStatus = this.selectedCenter.getActive();
            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Center status was updated successfully.", null);
            FacesContext.getCurrentInstance().addMessage(null, message);
        }
    }
    
    public boolean updateCenterReady()
    {
        return this.selectedCenter.getActive().equalsIgnoreCase(this.selectedCenterActiveStatus);
    }
    
    public void prepareSessionToEdit( LocationSession ses)
    {
        this.inSessionEditMode = true;
        ses.setCompetitionDate(ses.getCompetitionTime());
        this.copySession(ses, this.tempSession);
        this.selectedLevels = ses.getLevels().split(",");
    }

    public void addNewSession()
    {
        this.tempSession = new LocationSession();
        int newSessionId = this.getNewSessionId();
        this.tempSession.setSessionID(newSessionId);
        this.tempSession.setSessionName("SESSION " + newSessionId);
        this.tempSession.setLevels("1,2,3,4,5,6,7,8,9,10,11,12");
        this.selectedLevels = this.tempSession.getLevels().split(",");
        this.addingNewSession = true;
        this.inSessionEditMode = true;
    }

    private int getNewSessionId() 
    {
        int ret = 5;
        for (int i = 1; i < 5; i++) 
        {
            boolean assign = true;
            for (LocationSession ls : this.centerSessions) 
            {
                if (ls.getSessionID() == i) 
                {
                    assign = false;
                    break;
                }
            }
            if ( assign )
            {
                ret = i;
                break;
            }
        }
        return ret;
    }
 
    public void deleteSession() {
        if (this.bc.deleteLocationSession(this.selectedCenter.getLocationID(), this.sessionToDelete)) {
            this.centerSessions.remove(this.sessionToDelete);
            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Session was deleted successfully.", null);
            FacesContext.getCurrentInstance().addMessage(null, message);
        } else {
            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "There was an error when deleting this session.", null);
            FacesContext.getCurrentInstance().addMessage(null, message);
        }
    }
   
    public void exitEditSession()
    {
        this.inSessionEditMode = false;
        this.addingNewSession = false;
        this.tempSession = new LocationSession();
        this.selectedLevels = null;
    }
    
    public void copySession( LocationSession from, LocationSession to )
    {
        to.setAvailableSeatCount(from.getAvailableSeatCount());
        to.setCompetitionDate(from.getCompetitionDate());
        to.setCompetitionTime(from.getCompetitionTime());
        to.setLevels(from.getLevels());
        to.setSeatCapacity(from.getSeatCapacity());
        to.setSessionID(from.getSessionID());
        to.setSessionName(from.getSessionName());
        to.setSessionStatus(from.getSessionStatus());
        to.setEditDisabled(from.isEditDisabled());
    }
    
    public void saveSession() {
        String levels = "";
        int cnt = 0;
        if (this.selectedLevels != null) {
            for (String level : this.selectedLevels) {
                levels += levels.equals("") ? level : "," + level.trim();
                cnt++;
            }
        }
        this.tempSession.setLevels(levels);

        if (this.isSessionComplete(this.tempSession)) {
            this.tempSession.setCompetitionTime(new Time(this.tempSession.getCompetitionDate().getTime()));
            this.tempSession.setChangeMade(true);
            if (this.addingNewSession) {
                if (this.bc.addLocationSession(this.selectedCenter.getLocationID(), tempSession)) {
                    this.centerSessions.add(tempSession);
                    FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Session was added successfully.", null);
                    FacesContext.getCurrentInstance().addMessage(null, message);
                    this.addingNewSession = false;
                    this.inSessionEditMode = false;
                } else {
                    FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "There was an error when adding this session.", null);
                    FacesContext.getCurrentInstance().addMessage(null, message);
                }
            } else {
                if (this.bc.updateLocationSession(this.selectedCenter.getLocationID(), tempSession)) {
                    // add ActionLog if the session updated had regs (editDisabled flag). Important for update history
                    if ( this.tempSession.isEditDisabled())
                    {
                        ActionLog al = new ActionLog();
                        al.setActionId(3);
                        al.setTableName("TLocationSession");
                        al.setUserId(this.userLogin.getUser().getUserID());
                        String comment = "Session: " + this.sessionBeingUpdated.getSessionName() + " for center: " + this.sessionBeingUpdated.getLocationID() + " with existing registrations has been updated.\nSeat Capacity OLD/NEW: " + 
                                this.sessionBeingUpdated.getSeatCapacity() + "/" + this.tempSession.getSeatCapacity() +
                                "\nStatus OLD/NEW: " + this.sessionBeingUpdated.getSessionStatus() + "/" + this.tempSession.getSessionStatus();
                        al.setComment(comment);
                        this.bc.createActionLog(al);
                    }
                    this.copySession(this.tempSession, this.sessionBeingUpdated);
                    FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Session was updated successfully.", null);
                    FacesContext.getCurrentInstance().addMessage(null, message);
                    this.addingNewSession = false;
                    this.inSessionEditMode = false;
                } else {
                    FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "There was an error when updating this session.", null);
                    FacesContext.getCurrentInstance().addMessage(null, message);
                }
            }
        } else {
            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Levels Offered must be provided.", null);
            FacesContext.getCurrentInstance().addMessage(null, message);
        }
    }
   
    private boolean isSessionComplete( LocationSession ls )
    {
        boolean ret = true;
        if ( ls.getLevels().trim().equals(""))
        {
            ret = false;
        }
        return ret;
    }

    public void deleteManager()
    {
        this.bc.deleteManagerFromCenter ( this.selectedCenter.getLocationID(), this.managerToDelete );
        this.centerManagers.remove(this.managerToDelete);
        this.dialogMessageText = this.managerToDelete.getFirstName() + " " + this.managerToDelete.getLastName() + " has been removed from this center supporting managers.";
        RequestContext.getCurrentInstance().execute("PF('dlgMessage').show();");
    }

    
    public void findUser()
    {
        this.userToBeManager = this.bc.getUserByLogon(this.supportManagerLogonName);
        if ( this.userToBeManager == null )
        {
            this.findingUser = false;
            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "User with this logon not found!", null);
            FacesContext.getCurrentInstance().addMessage(null, message);
        }
        else
        {
            this.findingUser = true;
            if ( this.userToBeManager.getRoleCode() == 2 && this.userToBeManager.getOtherID() != 0)
            {
                boolean exists = false;
                for ( Leader l : this.centerManagers)
                {
                    if ( l.getLeaderID() == this.userToBeManager.getOtherID() )
                    {
                        exists = true;
                        break;
                    }
                }
                if ( exists )
                {
                    FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "User is already a manager for this center!", null);
                    FacesContext.getCurrentInstance().addMessage(null, message);
                    this.findingUser = false;
                }
            }
        }
    }
    
    public void requestSupportingManager()
    {
        EmailAdapter ea = new EmailAdapter();
        ea.setEmailTo(ApplicationProperties.MATH_KANGAROO_EMAIL_INFO);
        ea.setEmailCC(this.userToBeManager.getUserEmail() + "," + this.userLogin.getUser().getUserEmail() + "," + ApplicationProperties.MATH_KANGAROO_EMAIL_PRESIDENT);
        ea.setEmailReplyTo(ApplicationProperties.MATH_KANGAROO_EMAIL_INFO);
        ea.setEmailSubject("Supporting Manager Request for Center: " + this.selectedCenter.getLocationNameAndAddress() );
        
        String bodyText = "Manager " + this.userLogin.getUser().getUserFirstName() + " " + this.userLogin.getUser().getUserLastName() + " UID:" + this.userLogin.getUser().getUserID() + " MID:" + this.userLogin.getUser().getOtherID()
                + " has submitted a request to add the following user as a supporting manager for the center [ ID:" + this.selectedCenter.getLocationID() + " CODE:" + this.selectedCenter.getLocationCode() + " ]" + this.selectedCenter.getLocationNameAndAddress()
                + "\n\nUser First Name: " + this.userToBeManager.getUserFirstName()
                + "\nUser Last Name: " + this.userToBeManager.getUserLastName()
                + "\nUser Logon Name: " + this.userToBeManager.getLogonName()
                + "\nUser ID: " + this.userToBeManager.getUserID() + ( this.userToBeManager.getOtherID() != 0 ? " [ MID: " + this.userToBeManager.getOtherID() + " ]" : "" )
                + "\n-----\nRequesting manager comment: " + this.managerRequestComment
                + "\n-----\n\nPlease notify the MK team if this request was generated in error.";

        ea.setEmailBodyText(bodyText);
        ea.sendEmailPlain();
        
        // create a DB manager request
        ManagerRequest mr = new ManagerRequest();
        mr.setCenterId(this.selectedCenter.getLocationID());
        mr.setRequestorManager(this.userLogin.getManager());
        mr.setRequestorUser(this.userLogin.getUser());
        mr.setUser(this.userToBeManager);
        mr.setComment(this.managerRequestComment);
        mr.setStatus("O");
        this.bc.createManagerRequest( mr );
        this.supportManagerLogonName = "";
        this.managerRequests = this.bc.getLocationManagerRequests(this.selectedCenter.getLocationID());
        this.dialogMessageText = "Request for supporting manager has been submitted. Please allow up to 24 hours to process.";
        RequestContext.getCurrentInstance().execute("PF('dlgMessage').show();");
    }
    
    public void setPrimaryManager()
    {
        this.bc.setLocationPrimaryManager(this.selectedCenter, this.managerToUpdate);
        this.dialogMessageText = "Manager " + this.managerToUpdate.getFirstName() + " " + this.managerToUpdate.getLastName() + " has been set as a new primary manager.";
        this.centerManagers = this.bc.getLocationManagers(this.selectedCenter.getLocationID());
        RequestContext.getCurrentInstance().execute("PF('dlgMessage').show();");
    }
    
    public void rejectManager()
    {
        this.managerRequest.setStatus("R");
        String comment = "\n-----\nAdmin Action:\nRejected with Comment:\n" + this.managerRequestComment + "\n" + this.userLogin.toString() + "\n-----";
        this.bc.updateManagerRequest(this.managerRequest, comment );
        this.dialogMessageText = "Request to add supporting manager for " + this.managerRequest.getUser().getUserFirstName() + " " + this.managerRequest.getUser().getUserLastName() + " has been rejected and closed.";
        this.managerRequests = this.bc.getLocationManagerRequests(this.selectedCenter.getLocationID());
        RequestContext.getCurrentInstance().execute("PF('dlgMessage').show();");
    }
    
    public void approveManager() {
        int newLeaderId = this.managerRequest.getUser().getOtherID();
        if (newLeaderId == 0) 
        {
            newLeaderId = this.bc.createNewManager(this.managerRequest.getUser());
        }
        if (newLeaderId == 0) 
        {
            this.dialogMessageText = "There was a problem creating a manger from this user. Most likely the user logon name is already taken in the Managers table.";
        } else 
        {
            this.bc.addManagerToCenter(this.selectedCenter.getLocationID(), newLeaderId, "N");
            this.managerRequest.setStatus("A");
            String comment = "\n-----\nAdmin Action:\nApproved and added by " + this.userLogin.toString() + "\n-----";
            this.bc.updateManagerRequest(this.managerRequest, comment);

            this.dialogMessageText = "Request to add supporting manager for " + this.managerRequest.getUser().getUserFirstName() + " " + this.managerRequest.getUser().getUserLastName() + " has been approved and closed.";
            this.centerManagers = this.bc.getLocationManagers(this.selectedCenter.getLocationID());
            this.managerRequests = this.bc.getLocationManagerRequests(this.selectedCenter.getLocationID());
        }
        RequestContext.getCurrentInstance().execute("PF('dlgMessage').show();");
    }
    
    public void cancelRequest() {
        this.managerRequest.setStatus("C");
        String comment = "\n-----\nManager Action:\nClose by the manager.\n" + this.userLogin.getUser().toString() + "\n-----";
        this.bc.updateManagerRequest(this.managerRequest, comment);
        this.dialogMessageText = "Request to add supporting manager for " + this.managerRequest.getUser().getUserFirstName() + " " + this.managerRequest.getUser().getUserLastName() + " has been closed.";
        this.managerRequests = this.bc.getLocationManagerRequests(this.selectedCenter.getLocationID());
        RequestContext.getCurrentInstance().execute("PF('dlgMessage').show();");
    }

    public String getAdminReturnPage()
    {
        String ret = this.userLogin.getAdminRedirectPage();
        this.userLogin.setAdminRedirectPage("");
        if ( ret == null )
        {
            ret = "";
        }
        return ret.equals("") ? "MKRAdminCenters" : ret;
    }

    public String getManagerReturnPage()
    {
        String ret = this.userLogin.getManagerRedirectPage();
        this.userLogin.setManagerRedirectPage("");
        if ( ret == null )
        {
            ret = "";
        }
        return ret.equals("") ? "MKRMgrCenters" : ret;
    }

}
