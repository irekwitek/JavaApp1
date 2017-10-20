/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mk.beans;

import com.mk.classes.BusinessProcessControl;
import com.mk.classes.CommonData;
import com.mk.classes.User;
import com.mk.dao.DBManager;
import java.io.Serializable;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

/**
 *
 * @author irek
 */
@ManagedBean(name="ViewScopeBeanTemplate")
@ViewScoped
public class ViewScopeBeanTemplate implements Serializable 
{

    DBManager dbMgr;
    CommonData commonData;
    BusinessProcessControl bc;

    private UserLogin userLogin;
    
    // user data
    private User user;
    
    /**
     * Creates a new instance of RegistrationBean
     */
    public ViewScopeBeanTemplate() {
        userLogin = (UserLogin) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("userLogin");
        // initiate business process control
        dbMgr = userLogin.getDbMgr();
        bc = userLogin.getBc();
        commonData = bc.getCommonData();
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


    // UI supporting methods
    


}
