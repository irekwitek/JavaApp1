/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mk.beans;

import com.mk.classes.ApplicationProperties;
import com.mk.classes.BusinessProcessControl;
import com.mk.classes.CommonData;
import com.mk.classes.DiscountCode;
import com.mk.classes.IDGenerator;
import com.mk.classes.Location;
import com.mk.classes.LocationSession;
import com.mk.classes.MKPaymentInfo;
import com.mk.classes.MKRefund;
import com.mk.classes.RegisteredStudent;
import com.mk.classes.Registration;
import com.mk.classes.User;
import com.mk.classes.Utility;
import com.mk.dao.DBManager;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import org.primefaces.context.RequestContext;
import org.primefaces.event.FlowEvent;
import org.primefaces.model.map.DefaultMapModel;
import org.primefaces.model.map.LatLng;
import org.primefaces.model.map.MapModel;
import org.primefaces.model.map.Marker;

/**
 *
 * @author irek
 */
@ManagedBean(name="registrationBean")
@ViewScoped
public class RegistrationBean implements Serializable 
{

    DBManager dbMgr;
    Hashtable htStates;
    Hashtable htCities;
    CommonData commonData;
    BusinessProcessControl bc;

    private UserLogin userLogin;
    
    private User user;
    private RegisteredStudent registeredStudent;
    private boolean userLogged = false;
    private boolean paymentFinished = false;
    private boolean paymentInProcess = false;
    private boolean registrationProcessFinished = false;
    private boolean refundError = false;
    private boolean agreementFlag = false;
    private boolean donationFlag = false;
    private boolean privateCenterFound = false;
    private ArrayList<DiscountCode> availableDiscountCodes = new ArrayList<DiscountCode>();
    private ArrayList<DiscountCode> userDiscountCodes = new ArrayList<DiscountCode>();
    private int discountCodeId;
    private int discountTypeId;
    private String discountTypeName;
    
    private String registrationProcessMessage = "";
    private ArrayList<RegisteredStudent> myStudents;
    private ArrayList<RegisteredStudent> myCurrentStudents;
    private ArrayList<RegisteredStudent> myPastStudents;
    private ArrayList<RegisteredStudent> studentsToRegister;
    private MKPaymentInfo payment = new MKPaymentInfo();
    private MKRefund mkRefund = new MKRefund();
    
   
    private ArrayList<String> states = new ArrayList<String>();
    private ArrayList<String> cities = new ArrayList<String>();
    private ArrayList<Location> centers = new ArrayList<Location>();
    private ArrayList<LocationSession> sessions = new ArrayList<LocationSession>();
    
    
    // location chooser properties
    private String state = "";
    private String city = "";
    private Location center = new Location();
    private LocationSession centerSession = new LocationSession();
    private Location centerChosen;
    private Location privateCenter = new Location();
    private LocationSession sessionChosen;
    private RegisteredStudent studentBeingUpdated = null;
    private RegisteredStudent studentToDelete = null;
    private RegisteredStudent studentToUnregister = null;
    private RegisteredStudent studentToDiscount = new RegisteredStudent();
    
    private MapModel myMapModel;  
    private String mapTitle;
    
    private String logonNameMessage = "";
    
    private String dialogMessageText = "";
    
    /**
     * Creates a new instance of RegistrationBean
     */
    public RegistrationBean() {
        initData();
    }

    private void initData()
    {
        userLogin = (UserLogin) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("userLogin");
        // initiate business process control
        dbMgr = userLogin.getDbMgr();
        bc = userLogin.getBc();
        commonData = bc.getCommonData();
        user = this.userLogin.getUser();
        //Hashtable ht = ApplicationProperties.getLocationTree(dbMgr, user.getRoleCode(), user.getOtherID());
        htStates = ApplicationProperties.getLocationTree(dbMgr, this.getUser().getRoleCode(), this.getUser().getOtherID());
        Enumeration e = htStates.keys();
        while ( e.hasMoreElements() )
        {
            states.add((String)e.nextElement());
        }
        Collections.sort(states);
        // initiate user data
        myStudents = bc.getMyStudents(user);
        this.myCurrentStudents = new ArrayList<RegisteredStudent>();
        this.myPastStudents = new ArrayList<RegisteredStudent>();
        this.studentsToRegister = new ArrayList<RegisteredStudent>();
        this.segragateMyStudents();
        this.clearStudentEntryData();
        this.user.setUserDiscountCodes(this.bc.getUserDiscountCodes(this.user.getUserID()));
        for ( DiscountCode dc : user.getUserDiscountCodes() )
        {
            if ( dc.getStatus().equals("O"))
            {
                this.userDiscountCodes.add(dc);
                this.availableDiscountCodes.add(dc);
            }
        }
    }
    
    public void loadCities()
    {
        centerChosen = null;
        sessionChosen = null;
        cities.clear();
        city = "";
        centers.clear();
        center.setLocationCode("");
        center.setLocationName("");
        sessions.clear();
        centerSession.setSessionID(0);
        centerSession.setSessionName("");
        if (!state.equals(""))
        {
            Hashtable stateCities = (Hashtable)htStates.get(state);
            Enumeration e = stateCities.keys();
            while ( e.hasMoreElements() )
            {
                cities.add((String)e.nextElement());
            }
            Collections.sort(cities);
        }
    }

    public void loadCenters()
    {
        centerChosen = null;
        sessionChosen = null;
        centers.clear();
        center.setLocationCode("");
        center.setLocationName("");
        sessions.clear();
        centerSession.setSessionID(0);
        centerSession.setSessionName("");
        
        if (!state.equals("") && !city.equals(""))
        {
            Hashtable stateCities = (Hashtable)htStates.get(state);
            Vector v = (Vector)stateCities.get(city);
            for ( int i = 0; i < v.size(); i++)
            {
                Location l = (Location)v.get(i);
                centers.add(l);
            }
        }
    }

    public void loadSessions()
    {
        centerChosen = null;
        sessionChosen = null;
        sessions.clear();
        centerSession.setSessionID(0);
        centerSession.setSessionName("");
        if (center.getLocationCode()!= null && !center.getLocationCode().equals(""))
        {
            for ( Location l : centers)
            {
                if (l.getLocationCode().equals(center.getLocationCode()))
                {
                    centerChosen = l;
                    for ( LocationSession ls : l.getLocationSessions())
                    {
                        sessions.add(ls);
                    }
                    break;
                }
            }
        }
    }
    
    public void loadSessionInfo()
    {
        this.sessionChosen = null;
        for (LocationSession ls : this.sessions )
        {
            if (ls.getSessionID() == this.centerSession.getSessionID())
            {
                sessionChosen = ls;
                break;
            }
        }
    }

    public String onFlowProcess(FlowEvent event) {
        String retString = event.getNewStep();
        if (event.getOldStep().equals("student"))
        {
            if (!this.checkStudentsToRegister())
            {
                //FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "One or more students were removed from 'Students To Register' and moved to 'Unregistered Students' list due to their centers being filled up already. They are marked in yellow. Select new centers for them.",null));
                RequestContext.getCurrentInstance().execute("PF('dlgFixMessage').show();");
                this.segragateMyStudents();
                this.clearStudentEntryData();
                retString = "student";
            }
            if ( this.studentsToRegister.isEmpty())
            {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "You have no students to register.",null));
                this.segragateMyStudents();
                retString = "student";
            }
            else
            {
                this.paymentFinished = false;
            }
            return retString;
        }
        else
        {
            if ( this.paymentFinished || this.paymentInProcess )
            {
                retString = "payment";
            }
        }
        return retString;
    }

    public int getDiscountCodeId() {
        return discountCodeId;
    }

    public void setDiscountCodeId(int discountCodeId) {
        this.discountCodeId = discountCodeId;
    }

    public int getDiscountTypeId() {
        return discountTypeId;
    }

    public void setDiscountTypeId(int discountTypeId) {
        this.discountTypeId = discountTypeId;
    }

    public String getDiscountTypeName() {
        return discountTypeName;
    }

    public void setDiscountTypeName(String discountTypeName) {
        this.discountTypeName = discountTypeName;
    }


    public RegisteredStudent getStudentToDiscount() {
        return studentToDiscount;
    }

    public void setStudentToDiscount(RegisteredStudent studentToDiscount) {
        this.studentToDiscount = studentToDiscount;
    }

    public ArrayList<DiscountCode> getAvailableDiscountCodes() {
        return availableDiscountCodes;
    }

    public void setAvailableDiscountCodes(ArrayList<DiscountCode> availableDiscountCodes) {
        this.availableDiscountCodes = availableDiscountCodes;
    }

    public String getDialogMessageText() {
        return dialogMessageText;
    }

    public void setDialogMessageText(String dialogMessageText) {
        this.dialogMessageText = dialogMessageText;
    }

    public Location getPrivateCenter() {
        return privateCenter;
    }

    public void setPrivateCenter(Location privateCenter) {
        this.privateCenter = privateCenter;
    }

    
    public boolean isPrivateCenterFound() {
        return privateCenterFound;
    }

    public void setPrivateCenterFound(boolean privateCenterFound) {
        this.privateCenterFound = privateCenterFound;
    }

    public boolean isAgreementFlag() {
        return agreementFlag;
    }

    public void setAgreementFlag(boolean agreementFlag) {
        this.agreementFlag = agreementFlag;
    }

    public boolean isDonationFlag() {
        return donationFlag;
    }

    public void setDonationFlag(boolean donationFlag) {
        this.donationFlag = donationFlag;
    }


    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public MKRefund getMkRefund() {
        return mkRefund;
    }

    public void setMkRefund(MKRefund mkRefund) {
        this.mkRefund = mkRefund;
    }

    public boolean isRefundError() {
        return refundError;
    }

    public void setRefundError(boolean refundError) {
        this.refundError = refundError;
    }

    public boolean isPaymentFinished() {
        return paymentFinished;
    }

    public void setPaymentFinished(boolean paymentFinished) {
        this.paymentFinished = paymentFinished;
    }

    public boolean isPaymentInProcess() {
        return paymentInProcess;
    }

    public void setPaymentInProcess(boolean paymentInProcess) {
        this.paymentInProcess = paymentInProcess;
    }

    public boolean isRegistrationProcessFinished() {
        return registrationProcessFinished;
    }

    public void setRegistrationProcessFinished(boolean registrationProcessFinished) {
        this.registrationProcessFinished = registrationProcessFinished;
    }

    public String getRegistrationProcessMessage() {
        return registrationProcessMessage;
    }

    public void setRegistrationProcessMessage(String registrationProcessMessage) {
        this.registrationProcessMessage = registrationProcessMessage;
    }

    public boolean isUserLogged() {
        return userLogged;
    }

    public void setUserLogged(boolean userLogged) {
        this.userLogged = userLogged;
    }

    public RegisteredStudent getRegisteredStudent() {
        return registeredStudent;
    }

    public void setRegisteredStudent(RegisteredStudent registeredStudent) {
        this.registeredStudent = registeredStudent;
        if (this.registeredStudent.getContactPhone1() == null || this.registeredStudent.getContactPhone1().trim().equals("") )
        {
            this.registeredStudent.setContactPhone1(this.user.getUserPhoneCell());
        }
    }

    public String getLogonNameMessage() {
        return logonNameMessage;
    }

    public void setLogonNameMessage(String logonNameMessage) {
        this.logonNameMessage = logonNameMessage;
    }

    public MKPaymentInfo getPayment() {
        return payment;
    }

    public void setPayment(MKPaymentInfo payment) {
        this.payment = payment;
    }
    
    

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }


    public ArrayList<String> getStates() {
        return states;
    }

    public void setStates(ArrayList<String> states) {
        this.states = states;
    }

    public ArrayList<String> getCities() {
        return cities;
    }

    public void setCities(ArrayList<String> cities) {
        this.cities = cities;
    }

    public ArrayList<Location> getCenters() {
        return centers;
    }

    public void setCenters(ArrayList<Location> centers) {
        this.centers = centers;
    }

    public ArrayList<LocationSession> getSessions() {
        return sessions;
    }

    public void setSessions(ArrayList<LocationSession> sessions) {
        this.sessions = sessions;
    }

    public Location getCenter() {
        return center;
    }

    public void setCenter(Location center) {
        this.center = center;
    }

    public LocationSession getCenterSession() {
        return centerSession;
    }

    public void setCenterSession(LocationSession centerSession) {
        this.centerSession = centerSession;
    }

    public Location getCenterChosen() {
        return centerChosen;
    }

    public void setCenterChosen(Location centerChosen) {
        this.centerChosen = centerChosen;
    }

    public LocationSession getSessionChosen() {
        return sessionChosen;
    }

    public void setSessionChosen(LocationSession sessionChosen) {
        this.sessionChosen = sessionChosen;
    }

    public RegisteredStudent getStudentBeingUpdated() {
        return studentBeingUpdated;
    }

    public void setStudentBeingUpdated(RegisteredStudent studentBeingUpdated) {
        this.studentBeingUpdated = studentBeingUpdated;
    }

    public RegisteredStudent getStudentToDelete() {
        return studentToDelete;
    }

    public void setStudentToDelete(RegisteredStudent studentToDelete) {
        this.studentToDelete = studentToDelete;
    }

    public RegisteredStudent getStudentToUnregister() {
        return studentToUnregister;
    }

    public void setStudentToUnregister(RegisteredStudent studentToUnregister) {
        this.studentToUnregister = studentToUnregister;
    }

    public MapModel getMyMapModel() {
        return myMapModel;
    }

    public void setMyMapModel(MapModel myMapModel) {
        this.myMapModel = myMapModel;
    }

    public String getMapTitle() {
        if ( centerChosen == null )
        {
            return "";
        }
        myMapModel = new DefaultMapModel();
        Marker marker = new Marker(new LatLng(centerChosen.getLatitude(), centerChosen.getLongitude()), centerChosen.getLocationName());  
        myMapModel.addOverlay(marker);  
        return "for " + centerChosen.getLocationName();
    }

    public void setMapTitle(String mapTitle) {
        this.mapTitle = mapTitle;
    }

    public CommonData getCommonData() {
        return commonData;
    }

    public void setCommonData(CommonData commonData) {
        this.commonData = commonData;
    }

    public String chosenSessionAvailableSeatCount()
    {
        return this.getSeatCounter(this.centerChosen.getLocationID(),sessionChosen.getSessionID());
    }
    
    public String getSeatCounter() 
    {
        return this.getSeatCounter(this.registeredStudent.getLocationID(),this.registeredStudent.getSessionID());
    }
    
    private String getSeatCounter(int centerId, int sessionId) {
        String ret = "";
        if (centerId != 0 && sessionId != 0) {
            int cnt = this.bc.getCenterAvailableSeats(centerId, sessionId);
            if (cnt == 999) {
                ret = "UNLIMITED";
            } else {
                cnt = cnt - this.getMyStudentsToRegisterCenterCount(centerId, sessionId);
                if (cnt <= 0) {
                    ret = "Session is full.";
                }
                else {
                    ret = "" + cnt;
                }
            }
        }
        return ret;
    }
    
     private int getMyStudentsToRegisterCenterCount(int centerId, int sessionId) {
        int ret = 0;
        for (RegisteredStudent rs : this.studentsToRegister) {
            if (rs != this.studentBeingUpdated) {
                if (rs.getLocationID() == centerId && rs.getSessionID() == sessionId) {
                    ret += 1;
                }
            }
        }
        return ret;
    }

    public ArrayList<RegisteredStudent> getMyStudents() {
        return myStudents;
    }

    public void setMyStudents(ArrayList<RegisteredStudent> myStudents) {
        this.myStudents = myStudents;
    }

    public ArrayList<RegisteredStudent> getMyPastStudents() {
        return myPastStudents;
    }

    public void setMyPastStudents(ArrayList<RegisteredStudent> myPastStudents) {
        this.myPastStudents = myPastStudents;
    }

    public ArrayList<RegisteredStudent> getMyCurrentStudents() {
        return myCurrentStudents;
    }

    public void setMyCurrentStudents(ArrayList<RegisteredStudent> myCurrentStudents) {
        this.myCurrentStudents = myCurrentStudents;
    }

    public ArrayList<RegisteredStudent> getStudentsToRegister() {
        return studentsToRegister;
    }

    public void setStudentsToRegister(ArrayList<RegisteredStudent> studentsToRegister) {
        this.studentsToRegister = studentsToRegister;
    }


    
    // UI supporting methods
    
    public String getStudentModeName()
    {
        return "{ " + this.registeredStudent.getStudentIdentificationCode() + " }";
    }
    
    public void segragateMyStudents()
    {
        this.myCurrentStudents.clear();
        this.myPastStudents.clear();
        this.studentsToRegister.clear();
        for (RegisteredStudent rs : this.myStudents )
        {
            if (rs.getStatus() == 1 )
            {
                this.studentsToRegister.add(rs);
            }
            else if ( rs.getStatus() == 0 || rs.getStatus() == 4 || rs.getStatus() == 5 )
            {
                rs.setDiscountCodeId(0);
                this.myPastStudents.add(rs);
            }
            else if ( rs.getStatus() == 2 || rs.getStatus() == 3 )
            {
                this.myCurrentStudents.add(rs);
            }
        }
    }
            
    public String getSelectingCenterLinkName() {
        if ( this.registeredStudent.getLocationID() != 0 && this.registeredStudent.getSessionID() != 0 ) {
            return "Change Center";
        }
        return "Select Center";
    }

    public String getStudentUpdateButtonName() 
    {
        if ( this.registeredStudent.getStudentID() == 0 && !this.registeredStudent.getStudentIdentificationCode().equalsIgnoreCase("PENDING") ) 
        {
            return "Add This Student";
        }
        return "Update This Student";
    }

    
    public boolean isStudentInfoComplete()
    {
        boolean ret = true;
        
        if ( registeredStudent.getFirstName().equals("") 
                || registeredStudent.getLastName().equals("")
                || registeredStudent.getLevelCode() == 0
                || registeredStudent.getTshirtSizeCode() == 0 
                || registeredStudent.getContactPhone1().equals("")
                )
        {
            ret = false;
        }
        return ret;
    }

    public boolean isStudentCenterSelected()
    {
        boolean ret = true;
        
        if ( registeredStudent.getLocationID() == 0 || registeredStudent.getSessionID() == 0 )
        {
            ret = false;
        }
        return ret;
    }

    public boolean hasStudentCenterAvailableSeats()
    {
         boolean ret = false;
         if ( this.registeredStudent.getLocationID() != 0 && this.registeredStudent.getSessionID() != 0 )
         {
             LocationSession ls = this.bc.getLocationSession(this.registeredStudent.getLocationID(), this.registeredStudent.getSessionID());
             ret = ( ls.getSeatCapacity() == 0 || (ls.getSeatCapacity() - this.bc.getCenterSessionTakenSeats(this.registeredStudent.getLocationID(), this.registeredStudent.getSessionID()) - this.getMyStudentsToRegisterCenterCount(this.registeredStudent.getLocationID(), this.registeredStudent.getSessionID()) ) > 0 ? true : false);
         }
         return ret;
    }
    
    public boolean isLevelAvailable()
    {
        boolean ret = false;
        LocationSession ls = this.bc.getLocationSession(this.registeredStudent.getLocationID(), this.registeredStudent.getSessionID());
        if ( ls != null )
        {
            ret = (Utility.getStringPosition(ls.getLevels(),("" + this.registeredStudent.getLevelCode()),",") >= 0 ? true : false);
        }
        return ret;
    }

    
    private boolean verifyStudentInfoSection()
    {
        boolean error = false;
        boolean error2 = false;
        if ( !this.isStudentInfoComplete() )
        {
            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Please provide information in the section marked with *",  null);
            FacesContext.getCurrentInstance().addMessage(null, message);
            error2 = true;
            error = true;
        }
        if ( !this.isStudentCenterSelected() )
        {
            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Please select MK center for this student.",  null);
            FacesContext.getCurrentInstance().addMessage(null, message);
            error2 = true;
            error = true;
        }
        if ( !error2 && !this.hasStudentCenterAvailableSeats() )
        {
            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "This center has no available seats. Select another.",  null);
            FacesContext.getCurrentInstance().addMessage(null, message);
            error = true;
        }
        if ( !error2 && !this.isLevelAvailable() )
        {
            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "This center does not offer competition level you selected for this student.",  null);
            FacesContext.getCurrentInstance().addMessage(null, message);
            error = true;
        }
        if ( !this.registeredStudent.getStudentEmail().trim().equals("") && !Utility.isValidEmailAddress(this.registeredStudent.getStudentEmail()) )
        {
            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "The email address is not valid.",  null);
            FacesContext.getCurrentInstance().addMessage(null, message);
            error = true;
        }
        return error;
    }

    private boolean verifyStudentInfoSectionForUpdate( boolean wasCenterChanged )
    {
        boolean error = false;
        boolean error2 = false;
        if ( !this.isStudentInfoComplete() )
        {
            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Please provide information in the section marked with *",  null);
            FacesContext.getCurrentInstance().addMessage(null, message);
            error2 = true;
            error = true;
        }
/*        if ( !this.isStudentCenterSelected() )
        {
            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Please select MK center for this student.",  null);
            FacesContext.getCurrentInstance().addMessage(null, message);
            error2 = true;
            error = true;
        }
*/
        if ( !error2 && wasCenterChanged && !this.hasStudentCenterAvailableSeats() )
        {
            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "This center has no available seats. Select another.",  null);
            FacesContext.getCurrentInstance().addMessage(null, message);
            error = true;
        }
        if ( !error2 && !this.isLevelAvailable() )
        {
            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "This center does not offer competition level you selected for this student.",  null);
            FacesContext.getCurrentInstance().addMessage(null, message);
            error = true;
        }
        if ( !this.registeredStudent.getStudentEmail().trim().equals("") && !Utility.isValidEmailAddress(this.registeredStudent.getStudentEmail()) )
        {
            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "The email address is not valid.",  null);
            FacesContext.getCurrentInstance().addMessage(null, message);
            error = true;
        }
        return error;
    }
    
    
    public void validateAndAddStudent(ActionEvent actionEvent) 
    {
        if ( !verifyStudentInfoSection()) 
        {
            // add this student to the current students table
            if (this.registeredStudent.getStatus() != 2 && this.registeredStudent.getStatus() != 3) {
                this.registeredStudent.setStatus(1);
            }
            this.registeredStudent.setTshirtSize(this.commonData.getTshirtCodeName(this.registeredStudent.getTshirtSizeCode()));
            if (this.registeredStudent.getStudentID() == 0) {
                this.registeredStudent.setStudentIdentificationCode("PENDING");
            }
            if (this.studentBeingUpdated == null) {
                this.myStudents.add(registeredStudent);
            } else {
                this.bc.copyRegisteredStudent(this.registeredStudent, this.studentBeingUpdated);
            }
            this.segragateMyStudents();
            this.clearStudentEntryData();
        }
    }

    public void validateAndUpdateStudent(ActionEvent actionEvent) 
    {
        boolean wasCenterChanged = this.registeredStudent.getLocationID() != this.studentBeingUpdated.getLocationID() 
                    || this.registeredStudent.getSessionID() != this.studentBeingUpdated.getSessionID();
        if (!verifyStudentInfoSectionForUpdate( wasCenterChanged ))  
        {
            if ( wasCenterChanged )
            {
                this.dialogMessageText = "Student registration was updated successfully. Because the student's registration center or session was changed an e-mail notification to both centers has been sent.";
                RequestContext.getCurrentInstance().execute("PF('dlgMessage').show();");
                // send email to parent and managers confirming the change
                this.bc.sendRegistrationUpdateOldCenterConfirmation(this.studentBeingUpdated);
                this.bc.sendEmailRegistrationConfirmation(this.registeredStudent,3);
            }
            this.registeredStudent.setUpdatedBy(this.user.getUserID());
            this.bc.copyRegisteredStudent(this.registeredStudent, this.studentBeingUpdated);
            this.bc.updateStudentAndRegistration(registeredStudent, ApplicationProperties.getCurrentRegistrationYear(dbMgr));
            this.clearStudentEntryData();
            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Student registration was updated successfully.", null);
            FacesContext.getCurrentInstance().addMessage(null, message);
        }
    }

    
    public void clearStudentData(ActionEvent actionEvent)
    {
        this.clearStudentEntryData();
    }

    private void clearStudentEntryData()
    {
        this.registeredStudent = new RegisteredStudent();
        this.registeredStudent.setContactPhone1(this.user.getUserPhoneCell());
        this.registeredStudent.setParentGuardianEmail(this.user.getUserEmail());
        this.studentBeingUpdated = null;
    }

    
    public void deleteStudent() {
        
        this.studentToDelete.setDiscountCodeId(0);
        this.studentToDelete.setDiscountTypeId(0);
        this.studentToDelete.setDiscountTypeName("");
        if (!this.studentToDelete.getStudentIdentificationCode().equalsIgnoreCase("PENDING")) 
        {
            this.studentToDelete.setOwnerId(this.user.getUserID());
            this.bc.deleteStudentAndRegistration(this.studentToDelete);
        }
        this.myStudents.remove(this.studentToDelete);
        this.segragateMyStudents();
        this.updateAvailableDiscountCodes();
        this.clearStudentEntryData();
    }

    public void unregisterStudent() 
    {
        //temp will be used to send email after successful unregistering 
        RegisteredStudent temp = new RegisteredStudent();
        this.bc.copyRegisteredStudent(this.studentToUnregister, temp);
        this.refundError = false;
        mkRefund = new MKRefund();
        mkRefund.setParentPaypalPayment(this.studentToUnregister.getPaymentID());
        mkRefund.setParentPaypalTransactionId(this.studentToUnregister.getTransactionID());
        mkRefund.setAmount(this.studentToUnregister.getPaymentAmount() * ApplicationProperties.REFUND_PERCENTAGE / 100.00);
        mkRefund.setMadeByUserId(this.user.getUserID());
        mkRefund.setMadeByUserName(this.user.getUserFirstName() + " " + this.user.getUserLastName());

        mkRefund = this.bc.unregisterAndRefund(this.studentToUnregister, mkRefund );
        if ( mkRefund.getResponseCode() != ApplicationProperties.PAYMENT_RESPONSE_CODE_SUCCESS)
        {
            this.refundError = true;
            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Refund Error", mkRefund.getResponseMessage());
            RequestContext.getCurrentInstance().showMessageInDialog(message);
        }
        else
        {
            this.bc.sendUnregisterConfirmation( temp, mkRefund );
            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Refund Confirmation", "Your student has been unregistered and the refund has been issued by Paypal.");
            RequestContext.getCurrentInstance().showMessageInDialog(message);
        }
        this.segragateMyStudents();
    }

    
     public Double getRegistrationPaymentAmount() {
        Double ret = 0.00;

        for (RegisteredStudent regSt : this.studentsToRegister) {
            if (regSt.getLocationID() > 0) {
                double studentFee = bc.getLocationRegistrationFee(regSt.getLocationID());
                if ( regSt.getDiscountCodeId()!= 0 )
                {
                    studentFee -= (double)(studentFee / regSt.getDiscountTypeId());
                }
                regSt.setPaymentAmount(studentFee);
                ret += studentFee;
            }
        }
        this.payment.setAmount(ret);
        return ret;
    }
     
     
     public void setCenterForRegisteredStudent()
     {
         this.registeredStudent.setLocationID(this.centerChosen.getLocationID());
         this.registeredStudent.setLocationCode(this.centerChosen.getLocationCode());
         this.registeredStudent.setSessionID(this.sessionChosen.getSessionID());
         this.registeredStudent.setSessionName(this.sessionChosen.getSessionInfo());
         this.registeredStudent.setSelectedLSessionLevelsOfferred(this.sessionChosen.getLevels());
     }

     public String calculateDonation()
     {
         int donation = this.payment.getDonationAmount() == 0 ? this.payment.getOtherDonationAmount() : this.payment.getDonationAmount();
         return "" + donation;
     }

     public int calculateDonationAmount()
     {
         return this.payment.getDonationAmount() == 0 ? this.payment.getOtherDonationAmount() : this.payment.getDonationAmount();
     }

    public void saveRegistration() 
    {
        if ( !this.checkStudentsToRegister() )
        {
            //FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "becaus more students were removed from 'Students To Register' and moved to 'Unregistered Students' list due to their centers being filled up already. They are marked in yellow. Select new centers for them.",null));
            this.segragateMyStudents();
            this.dialogMessageText = "One or more students were removed from 'Students To Register' and moved to 'Unregistered Students' list due to their centers being filled up already. They are marked in yellow. Select new centers for them.";
            RequestContext.getCurrentInstance().execute("PF('dlgMessage').show();");
            return;
        }
        if ( this.donationFlag && this.calculateDonationAmount() == 0 )
        {
            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Donation amount cannot be zero.",  null);
            FacesContext.getCurrentInstance().addMessage(null, message);
            return;
        }
        if ( !this.payment.isPaymentInfoValid(this.donationFlag))
        {
            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Payment data incomplete, please verify!",  null);
            FacesContext.getCurrentInstance().addMessage(null, message);
            return;
        }
        // check donation
        if (this.donationFlag) 
        {
            int donation = this.calculateDonationAmount();
            if (donation != 0) 
            {
                this.payment.setAmount(this.payment.getAmount() + donation);
                this.payment.setComment("User made donation of $" + donation);
            }
        }

        this.paymentInProcess = true;
        int returnCode = ApplicationProperties.PAYMENT_RESPONSE_CODE_SUCCESS;
        if ( this.payment.getAmount() == 0.0 )
        {
            // no PP processing; registration for all students is 100% discounted 
            this.payment.setPaypalPaymentId("MK-DISCOUNT");
            this.payment.setPaypalTransactionId("MK-DISCOUNT-"+this.userLogin.getUser().getUserID());
        }
        else
        {
            returnCode = this.payment.processPaypalPayment();
        }
        this.paymentInProcess = false;
        if ( returnCode != ApplicationProperties.PAYMENT_RESPONSE_CODE_SUCCESS )
        {
            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, this.payment.getResponseMessage(),  null);
            FacesContext.getCurrentInstance().addMessage(null, message);
            return;
        }
        this.paymentFinished = true;
        this.registrationProcessFinished = false;
        this.registrationProcessMessage = "";
        this.payment.setMadeByUserId(this.user.getUserID());
        this.payment.setMadeByUserName(this.user.getUserFirstName() + " " + this.user.getUserLastName());
        if ( this.donationFlag && this.calculateDonationAmount() != 0 )
        {
            this.bc.addUserDonation(this.payment);
        }
        for (RegisteredStudent rs : this.studentsToRegister) 
        {
            rs.setUpdatedBy(this.user.getUserID());
            rs.setStatus(2);
            rs.setYear(ApplicationProperties.getCurrentRegistrationYear(dbMgr));
            if (rs.getStudentID() == 0) 
            {
                rs.setStudentIdentificationCode(IDGenerator.getStudentIdentificationCode(dbMgr));
                int stID = this.bc.createStudent(rs);
                rs.setStudentID(stID);
            } 
            else 
            {
                this.bc.updateStudent(rs);
            }
            if (rs.getRegistrationID() == 0) 
            {
                this.bc.createRegistration(rs);
            } 
            else 
            {
                this.bc.updateRegistration(rs);
            }
        }
        this.bc.addStudentsToUser(this.user.getUserID(), this.studentsToRegister);
        if (!this.bc.createRegistrationPayment(this.studentsToRegister, this.payment))
        {
            this.registrationProcessMessage = "WARNING: There was a problem with finishing the registration process. Your payment has been received but there may be some issues with student data. Please verify your registered students data by chosing 'Update Student Registration' from the Registration Home page. Notify the MK team of any problems";
        }
        else
        {
            this.registrationProcessFinished = true;
        }
        this.segragateMyStudents();
    }
    
     
     public void selectToUpdate( RegisteredStudent regSt )
     {
         this.bc.copyRegisteredStudent( regSt, this.registeredStudent );
         if (this.registeredStudent.getContactPhone1() == null || this.registeredStudent.getContactPhone1().trim().equals(""))
         {
             this.registeredStudent.setContactPhone1(this.user.getUserPhoneCell());
         }
     }
     
     public String checkExitAction()
     {
         String ret = "";
         if ( this.studentsToRegister == null || this.studentsToRegister.isEmpty())
         {
             ret = "MKRHome";
         }
         return ret;
     }

     public boolean hasChosenCenterOpenSeats()
     {
         boolean ret = false;
         if ( centerChosen != null && this.sessionChosen != null )
         {
             if ( this.bc.getCenterAvailableSeats(this.centerChosen.getLocationID(), this.sessionChosen.getSessionID()) - this.getMyStudentsToRegisterCenterCount(this.centerChosen.getLocationID(), this.sessionChosen.getSessionID()) > 0 )
             {
                ret = true;
             }
         }
         return ret;
     }
 
    public boolean checkStudentsToRegister() {
        boolean ret = true;
        Hashtable ht = new Hashtable(); // to keep locations occupancy of my students
        for (RegisteredStudent rSt : this.studentsToRegister) {
            if ( rSt.getLocationID() != 0 && rSt.getSessionID() != 0) 
            {
               int myStLocCnt = 0;
               String pattern = "" + rSt.getLocationID() + rSt.getSessionID();
               if ( ht.containsKey( pattern ) )
               {
                   myStLocCnt = ((Integer) (ht.get(pattern))).intValue();
               }
               LocationSession ls = this.bc.getLocationSession(rSt.getLocationID(), rSt.getSessionID());
               if ( ls.getSeatCapacity() != 0 && (ls.getSeatCapacity() - this.bc.getCenterSessionTakenSeats(rSt.getLocationID(),rSt.getSessionID()) - myStLocCnt < 1 ))
               {
                    rSt.setLocationID(0);
                    rSt.setSessionID(0);
                    rSt.setLocationCode("");
                    rSt.setSessionName("");
                    rSt.setStatus(5);
                    ret = false;
               }
               else
               {
                   int curCnt = 1;
                   if (ht.containsKey(pattern))
                   {
                        curCnt = ((Integer) (ht.get(pattern))).intValue() + 1;
                   }
                   ht.put(pattern, new Integer(curCnt));
               }
            }
        }
        return ret;
    }
     
    public double getRefundAmount()
    {
        if ( this.studentToUnregister != null )
        {
            return (this.studentToUnregister.getPaymentAmount() * ApplicationProperties.REFUND_PERCENTAGE / 100.00);
        }
        return 0.00;
    }

    public String getRefundPercentage()
    {
        return "" + ApplicationProperties.REFUND_PERCENTAGE;
    }

    public String getToolTipMessage()
    {
        if ( this.agreementFlag )
        {
            return "";
        }
        return "You must accept the disclaimer to continue";
    }

    public void findPrivateCenter()
    {
        Location center = this.bc.getLocationByCenterRegCode(this.privateCenter.getLocationRegistrationCode());
        if (center != null)
        {
            this.privateCenter = center;
            this.privateCenterFound = true;
        }
        else
        {
            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "There is no center matching this code!",  null);
            FacesContext.getCurrentInstance().addMessage(null, message);
            this.privateCenterFound = false;
        }
    }
    
    public void addPrivateCenter()
    {
        htStates = this.bc.addLocationToList(this.htStates, this.privateCenter);
        Enumeration e = htStates.keys();
        states.clear();
        while ( e.hasMoreElements() )
        {
            states.add((String)e.nextElement());
        }
        Collections.sort(states);
        this.state = this.privateCenter.getLocationState();
        this.loadCities();
        this.city = this.privateCenter.getLocationCity();
        this.loadCenters();
        this.center.setLocationCode(this.privateCenter.getLocationCode());
        this.center.setLocationName(this.privateCenter.getLocationName());
        this.loadSessions();
        this.clearPrivateCenter();
    }
    
    public void clearPrivateCenter()
    {
        this.privateCenter = new Location();
        this.privateCenterFound = false;
    }
    
    public boolean isReadyToPayRegistration()
    {
        boolean ret = true;
        if ( !this.agreementFlag  || this.studentsToRegister.isEmpty() )
        {
            ret = false;
        }
        return ret;
    }
    
    public boolean canApplyDiscountCode( RegisteredStudent regSt )
    {
        boolean ret = false;
        if ( regSt.getDiscountCodeId() == 0 && this.availableDiscountCodes.size() > 0 )
        {
            ret = true;
        }
        return ret;
    }
    
    public void applyDiscountCode()
    {
        this.studentToDiscount.setDiscountCodeId(this.discountCodeId);
        this.updateAvailableDiscountCodes();
    }

    public void removeDiscountCode(RegisteredStudent regSt)
    {
        regSt.setDiscountCodeId(0);
        this.updateAvailableDiscountCodes();
    }

    public void updateAvailableDiscountCodes() {
        this.availableDiscountCodes = new ArrayList<DiscountCode>();
        for (RegisteredStudent rs : this.studentsToRegister) {
            if (rs.getDiscountCodeId() == 0) {
                rs.setDiscountTypeId(0);
                rs.setDiscountTypeName("");
            }
        }
        for (DiscountCode dc : this.userDiscountCodes) {
            boolean add = true;
            for (RegisteredStudent rs : this.studentsToRegister) {
                if (dc.getCodeId() == rs.getDiscountCodeId()) {
                    rs.setDiscountTypeName(dc.getTypeName());
                    rs.setDiscountTypeId(dc.getType());
                    add = false;
                    break;
                }
            }
            if (add) {
                this.availableDiscountCodes.add(dc);
            }
        }
    }
   
    public String submitButtonName()
    {
        String ret = "Submit Payment";
        if ( this.getTotalPaymentAmount() == 0.0 )
        {
            ret = "Finish Registration";
        }
        return ret;
    }
    
    public double getTotalPaymentAmount()
    {
        double totalPayment = this.getRegistrationPaymentAmount();
        if ( this.donationFlag ) 
        {
            totalPayment += this.calculateDonationAmount();
        }
        return totalPayment;
    }
    
    public String getDiscountIcon( RegisteredStudent regSt)
    {
        String ret = "";
        if ( regSt.getDiscountTypeId() == 1 )
        {
            ret = "free.png";
        }
        else if ( regSt.getDiscountTypeId() == 2 )
        {
            ret = "50off.png";
        }
        else if ( regSt.getDiscountTypeId() == 4 )
        {
            ret = "25off.png";
        }
        return ret;
    }
}
