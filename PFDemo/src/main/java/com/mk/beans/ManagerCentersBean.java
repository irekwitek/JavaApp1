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
import com.mk.classes.Utility;
import com.mk.dao.DBManager;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;

/**
 *
 * @author irek
 */
@ManagedBean(name = "managerCentersBean")
@ViewScoped
public class ManagerCentersBean implements Serializable {

    DBManager dbMgr;
    CommonData commonData;
    BusinessProcessControl bc;
    private UserLogin userLogin;
    
    private ArrayList<Location> myCenters;
    
    private Location selectedCenter;

    /**
     * Creates a new instance of RegistrationBean
     */
    public ManagerCentersBean() {
        userLogin = (UserLogin) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("userLogin");
        // initiate business process control
        dbMgr = userLogin.getDbMgr();
        bc = userLogin.getBc();
        commonData = bc.getCommonData();
        this.myCenters = this.bc.getManagerCenters( this.userLogin.getManager(), false);
        for ( Location center : this.myCenters )
        {
            for ( LocationSession ls : center.getLocationSessions())
            {
                ls.setSeatsTaken(this.bc.getCenterSessionTakenSeats(center.getLocationID(), ls.getSessionID()));
            }
        }
    }

    public ArrayList<Location> getMyCenters() {
        return myCenters;
    }

    public void setMyCenters(ArrayList<Location> myCenters) {
        this.myCenters = myCenters;
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

    public String getConfirmationYear()
    {
        return ApplicationProperties.getCurrentRegistrationYear(this.dbMgr);
    }
    
    public String confirmCenterText(Location center)
    {
        String ret = "Confirmed";
        if (!center.getLocationConfirmationStatus().equals("Y"))
        {
            if (center.isManagerPrimary() )
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
    
    public boolean isCenterConfirmationModeActive()
    {
        return (ApplicationProperties.getCentersConfirmationMode(dbMgr) == 0 ? false : true );
    }

}
