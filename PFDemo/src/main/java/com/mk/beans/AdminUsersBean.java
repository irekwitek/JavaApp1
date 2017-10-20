/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mk.beans;

import com.mk.classes.Location;
import com.mk.classes.User;
import java.io.Serializable;
import java.util.ArrayList;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import org.primefaces.context.RequestContext;

/**
 *
 * @author irek
 */
@ManagedBean(name="adminUsersBean")
@ViewScoped
public class AdminUsersBean implements Serializable {

    private UserLogin userLogin;
    private AdminBean adminBean;
    private ArrayList<User> adminUsers;
    
    private String userFirstSearch = "";
    private String userLastSearch = "";
    private String userStudentIdSearch = "";
    private String userEmailSearch = "";
    private String userLogonNameSearch = "";
    private boolean dialogAccess = false;
    
    
    public AdminUsersBean() 
    {    
        userLogin = (UserLogin) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("userLogin");
        adminBean = (AdminBean) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("adminBean");
        if ( adminBean == null )
        {
            adminBean = new AdminBean();
        }
    }

    public boolean isDialogAccess() {
        return dialogAccess;
    }

    public void setDialogAccess(boolean dialogAccess) {
        this.dialogAccess = dialogAccess;
    }

    public ArrayList<User> getAdminUsers() {
        return adminUsers;
    }

    public void setAdminUsers(ArrayList<User> adminUsers) {
        this.adminUsers = adminUsers;
    }

    public String getUserFirstSearch() {
        return userFirstSearch;
    }

    public void setUserFirstSearch(String userFirstSearch) {
        this.userFirstSearch = userFirstSearch;
    }

    public String getUserLastSearch() {
        return userLastSearch;
    }

    public void setUserLastSearch(String userLastSearch) {
        this.userLastSearch = userLastSearch;
    }

    public String getUserStudentIdSearch() {
        return userStudentIdSearch;
    }

    public void setUserStudentIdSearch(String userStudentIdSearch) {
        this.userStudentIdSearch = userStudentIdSearch;
    }


    public String getUserEmailSearch() {
        return userEmailSearch;
    }

    public void setUserEmailSearch(String userEmailSearch) {
        this.userEmailSearch = userEmailSearch;
    }

    public String getUserLogonNameSearch() {
        return userLogonNameSearch;
    }

    public void setUserLogonNameSearch(String userLogonNameSearch) {
        this.userLogonNameSearch = userLogonNameSearch;
    }


    public void searchUsers() 
    {
        if ((this.userStudentIdSearch != null && this.userStudentIdSearch.equals("")) && this.userLogin.getBc().searchUsersCount(this.userFirstSearch.trim(), this.userLastSearch.trim(), this.userLogonNameSearch.trim(),this.userEmailSearch.trim()) > 50 ) 
        {
            if (this.isDialogAccess())
            {
                FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Message","Too many users were found. Please narrow down search criteria.");
                RequestContext.getCurrentInstance().showMessageInDialog(message);
            }
            else
            {
                FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Too many users were found. Please narrow down search criteria.", null);
                FacesContext.getCurrentInstance().addMessage(null, message);
            }
        } 
        else 
        {
            this.adminUsers = this.userLogin.getBc().getAdminUsers(this.userFirstSearch.trim(), this.userLastSearch.trim(), this.userLogonNameSearch.trim(),this.userEmailSearch.trim(), this.userStudentIdSearch.trim());
            if (this.adminUsers.size() == 0) 
            {
                if (this.isDialogAccess())
                {
                    FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Message","No users were found matching your criteria.");
                    RequestContext.getCurrentInstance().showMessageInDialog(message);
                }
                else
                {
                    FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "No users were found matching your criteria.", null);
                    FacesContext.getCurrentInstance().addMessage(null, message);
                }
            }
        }
    }

    public void clearCriteria() 
    {
        this.userEmailSearch = "";
        this.userFirstSearch = "";
        this.userStudentIdSearch = "";
        this.userLastSearch = "";
        this.userLogonNameSearch = "";
    }

}
