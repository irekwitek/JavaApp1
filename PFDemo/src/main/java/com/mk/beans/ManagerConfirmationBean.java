/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mk.beans;

import com.mk.classes.ApplicationProperties;
import com.mk.classes.BusinessProcessControl;
import com.mk.classes.CommonData;
import com.mk.classes.Location;
import com.mk.dao.DBManager;
import java.io.Serializable;
import java.util.ArrayList;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

/**
 *
 * @author irek
 */
@ManagedBean(name = "managerConfirmationBean")
@ViewScoped
public class ManagerConfirmationBean implements Serializable {

    DBManager dbMgr;
    CommonData commonData;
    BusinessProcessControl bc;
    private UserLogin userLogin;
    
    private ArrayList<Location> myPrimaryCenters;
    
    private Location selectedCenter;


    /**
     * Creates a new instance of RegistrationBean
     */
    public ManagerConfirmationBean() {
        userLogin = (UserLogin) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("userLogin");
        // initiate business process control
        dbMgr = userLogin.getDbMgr();
        bc = userLogin.getBc();
        commonData = bc.getCommonData();
        this.myPrimaryCenters = this.bc.getManagerCenters( this.userLogin.getManager(), true);
    }

    public Location getSelectedCenter() {
        return selectedCenter;
    }

    public void setSelectedCenter(Location selectedCenter) {
        this.selectedCenter = selectedCenter;
    }

    public ArrayList<Location> getMyPrimaryCenters() {
        return myPrimaryCenters;
    }

    public void setMyPrimaryCenters(ArrayList<Location> myPrimaryCenters) {
        this.myPrimaryCenters = myPrimaryCenters;
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
