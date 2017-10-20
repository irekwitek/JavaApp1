/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mk.beans;

import com.mk.classes.Location;
import com.mk.classes.RegisteredStudent;
import com.mk.classes.User;
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
@ManagedBean(name="adminStudentsBean")
@ViewScoped
public class AdminStudentsBean implements Serializable {

    private UserLogin userLogin;
    //private AdminBean adminBean;
    private ArrayList<RegisteredStudent> adminStudents;
    
    private String studentFirstSearch = "";
    private String studentLastSearch = "";
    private String studentIdSearch;
    private String emailSearch = "";
    private String userLogonNameSearch = "";
    private String centerSearch = "";
    
    
    public AdminStudentsBean() 
    {    
        userLogin = (UserLogin) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("userLogin");
        //adminBean = (AdminBean) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("adminBean");
        //if ( adminBean == null )
        //{
        //    adminBean = new AdminBean();
        //}
    }

    public ArrayList<RegisteredStudent> getAdminStudents() {
        return adminStudents;
    }

    public void setAdminStudents(ArrayList<RegisteredStudent> adminStudents) {
        this.adminStudents = adminStudents;
    }

    public String getStudentFirstSearch() {
        return studentFirstSearch;
    }

    public void setStudentFirstSearch(String studentFirstSearch) {
        this.studentFirstSearch = studentFirstSearch;
    }

    public String getStudentLastSearch() {
        return studentLastSearch;
    }

    public void setStudentLastSearch(String studentLastSearch) {
        this.studentLastSearch = studentLastSearch;
    }

    public String getStudentIdSearch() {
        return studentIdSearch;
    }

    public void setStudentIdSearch(String studentIdSearch) {
        this.studentIdSearch = studentIdSearch;
    }

    public String getEmailSearch() {
        return emailSearch;
    }

    public void setEmailSearch(String emailSearch) {
        this.emailSearch = emailSearch;
    }

    public String getCenterSearch() {
        return centerSearch;
    }

    public void setCenterSearch(String centerSearch) {
        this.centerSearch = centerSearch;
    }

    public String getUserLogonNameSearch() {
        return userLogonNameSearch;
    }

    public void setUserLogonNameSearch(String userLogonNameSearch) {
        this.userLogonNameSearch = userLogonNameSearch;
    }


    public void searchStudents() 
    {
        if ((this.studentIdSearch != null && this.studentIdSearch.trim().equals("")) && this.userLogin.getBc().searchStudentsCount(this.studentFirstSearch.trim(), this.studentLastSearch.trim(), this.userLogonNameSearch.trim(), this.emailSearch.trim(), this.centerSearch.trim() ) > 100 ) 
        {
            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Too many students were found. Please narrow down search criteria.", null);
            FacesContext.getCurrentInstance().addMessage(null, message);
        } 
        else 
        {
            this.adminStudents = this.userLogin.getBc().getAdminStudentsSearch(this.studentFirstSearch.trim(), this.studentLastSearch.trim(), this.userLogonNameSearch.trim(), this.emailSearch.trim(), this.centerSearch.trim(), this.studentIdSearch.trim());
            if (this.adminStudents.size() == 0) {
                FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "No students were found matching your criteria.", null);
                FacesContext.getCurrentInstance().addMessage(null, message);
            }
        }
    }

    public void clearCriteria() 
    {
        this.setUserLogonNameSearch("");
        this.setCenterSearch("");
        this.setEmailSearch("");
        this.setStudentFirstSearch("");
        this.setStudentIdSearch("");
        this.setStudentLastSearch("");
    }
}
