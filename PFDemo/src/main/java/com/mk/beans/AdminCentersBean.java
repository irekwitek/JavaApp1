/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mk.beans;

import com.mk.classes.Location;
import java.io.Serializable;
import java.util.ArrayList;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

/**
 *
 * @author irek
 */
@ManagedBean(name="adminCentersBean")
@ViewScoped
public class AdminCentersBean implements Serializable {

    private UserLogin userLogin;
    private AdminBean adminBean;
    private ArrayList<Location> adminCenters;
    
    private String centerCodeSearch = "";
    private String centerNameSearch = "";
    
    
    public AdminCentersBean() 
    {    
        userLogin = (UserLogin) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("userLogin");
        adminBean = (AdminBean) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("adminBean");
        if ( adminBean == null )
        {
            adminBean = new AdminBean();
        }
        //this.adminCenters = this.userLogin.getBc().getAdminCenters();
    }

    public ArrayList<Location> getAdminCenters() {
        return adminCenters;
    }

    public void setAdminCenters(ArrayList<Location> adminCenters) {
        this.adminCenters = adminCenters;
    }

    public String getCenterCodeSearch() {
        return centerCodeSearch;
    }

    public void setCenterCodeSearch(String centerCodeSearch) {
        this.centerCodeSearch = centerCodeSearch;
    }

    public String getCenterNameSearch() {
        return centerNameSearch;
    }

    public void setCenterNameSearch(String centerNameSearch) {
        this.centerNameSearch = centerNameSearch;
    }

    public void searchCenters() {
        if (this.userLogin.getBc().searchCentersCount(this.centerCodeSearch.trim(), this.centerNameSearch.trim()) > 40 ) {
            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Too many centers were found. Please narrow down search criteria.", null);
            FacesContext.getCurrentInstance().addMessage(null, message);
        } else {
            this.adminCenters = this.userLogin.getBc().getAdminCenters(this.centerCodeSearch.trim(), this.centerNameSearch.trim());
            if (this.adminCenters.size() == 0) {
                FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "No centers were found matching your criteria.", null);
                FacesContext.getCurrentInstance().addMessage(null, message);
            }
        }
    }

    public String confirmCenterText()
    {
        return this.userLogin.getSelectedCenter().getLocationConfirmationStatus().equals("Y") ? "Confirmed" : "Not confirmed";
    }

    public void clearCriteria() 
    {
        this.centerCodeSearch = "";
        this.centerNameSearch = "";
    }

}
