/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mk.beans;

import com.mk.classes.BusinessProcessControl;
import com.mk.classes.CommonData;
import com.mk.classes.Location;
import com.mk.classes.LocationSession;
import com.mk.classes.User;
import com.mk.classes.Utility;
import com.mk.dao.DBManager;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.Serializable;
import java.util.ArrayList;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import org.omg.CORBA.Environment;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;

/**
 *
 * @author irek
 */
@ManagedBean(name="adminReportsBean")
@ViewScoped
public class AdminReportsBean implements Serializable 
{

    DBManager dbMgr;
    CommonData commonData;
    BusinessProcessControl bc;

    private UserLogin userLogin;
    
    // user data
    private User user;
    private String state;
    private ArrayList<Location> stateCenters;
    
    private StreamedContent stateCentersfile;

    
    /**
     * Creates a new instance of RegistrationBean
     */
    public AdminReportsBean() {
        userLogin = (UserLogin) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("userLogin");
        // initiate business process control
        this.dbMgr = userLogin.getDbMgr();
        this.bc = userLogin.getBc();
        this.commonData = bc.getCommonData();
        this.user = userLogin.getUser();
        
    }

    public StreamedContent getStateCentersfile() {
        return stateCentersfile;
    }

    public void setStateCentersfile(StreamedContent stateCentersfile) {
        this.stateCentersfile = stateCentersfile;
    }

    public ArrayList<Location> getStateCenters() {
        return stateCenters;
    }

    public void setStateCenters(ArrayList<Location> stateCenters) {
        this.stateCenters = stateCenters;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }
    
    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public CommonData getCommonData() {
        return commonData;
    }

    public void setCommonData(CommonData commonData) {
        this.commonData = commonData;
    }

    public void loadStateCenters()
    {
        if (this.state.trim().equals("") )
        {
            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Provide state for the report.", null);
            FacesContext.getCurrentInstance().addMessage(null, message);
        } else {
            this.stateCenters = this.bc.getAdminStateCenters(this.state);
            if (this.stateCenters.size() == 0) {
                FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "No centers were found for " + this.state + " state.", null);
                FacesContext.getCurrentInstance().addMessage(null, message);
            }
        }
    }

    public StreamedContent getFile() 
    {
        ByteArrayOutputStream outputStream =  new ByteArrayOutputStream();
        try 
        {
            outputStream.write("Center Code,Center Name,Address,City,State,Zip code,Confirmed,Center Private Session Access Code,Capacity,Sessions,Mgr First Name,Mgr Last Name,E-mail,Mgr Address,Mgr City, Mgr State,Mgr Zip code,Mgr Phone,Mgr Cell Phone,Director User ID,Director Name\n".getBytes());
            for (Location loc: this.stateCenters)
            {
                String sessions = "";
                for (LocationSession ls : loc.getLocationSessions())
                {
                    sessions += (sessions.equals("") ? "" : " - ") + ls.getSessionInfo() + (ls.getSessionStatus() == 0 ? "(public)" : "(private)");
                }
                outputStream.write(new StringBuffer(loc.getLocationCode()).append(",").append(Utility.removeCommasFromString(loc.getLocationName())).append(",")
                        .append(Utility.removeCommasFromString(loc.getLocationAddress1())).append(",").append(loc.getLocationCity()).append(",")
                        .append(loc.getLocationState()).append(",").append(loc.getLocationZipcode()).append(",")
                        .append(loc.getLocationConfirmationStatus()).append(",").append(loc.getLocationRegistrationCode()).append(",")
                        .append(loc.getLocationCapacityAsString()).append(",").append(sessions).append(",")
                        .append(loc.getLeader().getFirstName()).append(",").append(loc.getLeader().getLastName()).append(",")
                        .append(loc.getLeader().getEmail()).append(",").append(Utility.removeCommasFromString(loc.getLeader().getAddress1())).append(",")
                        .append(loc.getLeader().getCity()).append(",").append(loc.getLeader().getState()).append(",").append(loc.getLeader().getZipcode()).append(",")
                        .append(loc.getLeader().getPhone()).append(",").append(loc.getLeader().getPhoneCell()).append(",")
                        .append(loc.getDirectorUserId()).append(",").append(loc.getDirectorName()).append(",")
                        .append("\n").toString().getBytes());
            }
        }
        catch (Exception e)
        {
            System.out.println("FileDownloadView error: " + e.toString());
        }
        InputStream is = new ByteArrayInputStream(outputStream.toByteArray());
        this.stateCentersfile = new DefaultStreamedContent(is, "text", "MK Center Report for " + this.state + " state.csv");
        return this.stateCentersfile;
    }
    


}
