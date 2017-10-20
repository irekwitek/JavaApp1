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
@ManagedBean(name="websiteCentersReportBean")
@ViewScoped
public class WebsiteCentersReportBean implements Serializable 
{

    DBManager dbMgr;
    CommonData commonData;
    BusinessProcessControl bc;

    private UserLogin userLogin;
    
    // bean data
    ArrayList<Location> publicCenters;
    
    /**
     * Creates a new instance of RegistrationBean
     */
    public WebsiteCentersReportBean() {
        userLogin = (UserLogin) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("userLogin");
        if ( userLogin == null)
        {
            userLogin = new UserLogin();
        }
        // initiate business process control
        dbMgr = userLogin.getDbMgr();
        bc = userLogin.getBc();
        commonData = bc.getCommonData();
        publicCenters = this.bc.getPublicCenters();
    }
    

    public CommonData getCommonData() {
        return commonData;
    }

    public void setCommonData(CommonData commonData) {
        this.commonData = commonData;
    }

    public ArrayList<Location> getPublicCenters() {
        return publicCenters;
    }

    public void setPublicCenters(ArrayList<Location> publicCenters) {
        this.publicCenters = publicCenters;
    }


}
