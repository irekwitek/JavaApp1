/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mk.beans;

import com.mk.classes.BusinessProcessControl;
import com.mk.classes.CenterManagerRequest;
import com.mk.classes.CommonData;
import com.mk.classes.ManagerRequest;
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
@ManagedBean(name="adminManagerRequestBean")
@ViewScoped
public class AdminManagerRequestBean implements Serializable 
{

    DBManager dbMgr;
    CommonData commonData;
    BusinessProcessControl bc;

    private UserLogin userLogin;
    
    private ArrayList<CenterManagerRequest> centerManagerRequests;
    
    /**
     * Creates a new instance of RegistrationBean
     */
    public AdminManagerRequestBean() {
        userLogin = (UserLogin) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("userLogin");
        // initiate business process control
        dbMgr = userLogin.getDbMgr();
        bc = userLogin.getBc();
        commonData = bc.getCommonData();
        this.centerManagerRequests = this.bc.getAdminManagerRequests();
    }


    public CommonData getCommonData() {
        return commonData;
    }

    public void setCommonData(CommonData commonData) {
        this.commonData = commonData;
    }

    public UserLogin getUserLogin() {
        return userLogin;
    }

    public void setUserLogin(UserLogin userLogin) {
        this.userLogin = userLogin;
    }

 
    public ArrayList<CenterManagerRequest> getCenterManagerRequests() {
        return centerManagerRequests;
    }

    public void setCenterManagerRequests(ArrayList<CenterManagerRequest> centerManagerRequests) {
        this.centerManagerRequests = centerManagerRequests;
    }

}
