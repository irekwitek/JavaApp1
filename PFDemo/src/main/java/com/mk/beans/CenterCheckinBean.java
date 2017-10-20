/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mk.beans;

import com.mk.classes.ApplicationProperties;
import com.mk.classes.BusinessProcessControl;
import com.mk.classes.CommonData;
import com.mk.classes.Location;
import com.mk.classes.LocationSession;
import com.mk.classes.RegisteredStudent;
import com.mk.classes.StudentDataModel;
import com.mk.dao.DBConnectionManager;
import com.mk.dao.DBManager;
import java.io.Serializable;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import org.primefaces.model.StreamedContent;

/**
 *
 * @author irek
 */
@ManagedBean(name = "centerCheckinBean")
@ViewScoped
public class CenterCheckinBean implements Serializable {

    DBManager dbMgr;
    CommonData commonData;
    BusinessProcessControl bc;
    private UserLogin userLogin;
    
    private Location selectedCenter;
    private LocationSession selectedSession;

    private RegisteredStudent selectedStudent;
    private ArrayList<RegisteredStudent> filteredStudents;
    private StudentDataModel studentDataModel;

    /**
     * Creates a new instance of RegistrationBean
     */
    public CenterCheckinBean() {
        userLogin = (UserLogin) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("userLogin");
        // initiate business process control
        dbMgr = userLogin.getDbMgr();
        bc = userLogin.getBc();
        commonData = bc.getCommonData();
        this.selectedCenter = this.userLogin.getSelectedCenter();
        this.selectedSession = this.userLogin.getSelectedSession();
        this.studentDataModel = new StudentDataModel(this.bc.getLocationSessionStudents(this.selectedSession, ApplicationProperties.getCurrentRegistrationYear(dbMgr)));
    }

    public RegisteredStudent getSelectedStudent() {
        return selectedStudent;
    }

    public void setSelectedStudent(RegisteredStudent selectedStudent) {
        this.selectedStudent = selectedStudent;
    }

    public ArrayList<RegisteredStudent> getFilteredStudents() {
        return filteredStudents;
    }

    public void setFilteredStudents(ArrayList<RegisteredStudent> filteredStudents) {
        this.filteredStudents = filteredStudents;
    }

    public StudentDataModel getStudentDataModel() {
        return studentDataModel;
    }

    public void setStudentDataModel(StudentDataModel studentDataModel) {
        this.studentDataModel = studentDataModel;
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

    public String getCurrentRegistrationYear()
    {
        return ApplicationProperties.getCurrentRegistrationYear(this.dbMgr);
    }

    public LocationSession getSelectedSession() {
        return selectedSession;
    }

    public void setSelectedSession(LocationSession selectedSession) {
        this.selectedSession = selectedSession;
    }

    public int getCheckedinCount()
    {
        return this.bc.getCheckedinCount(this.selectedSession);
    }
    
    public void checkinStudent() 
    {
        this.selectedStudent.setChecked(true);
        this.bc.updateCheckedInStudent(this.selectedStudent, this.userLogin.getUser());
    }
 
    public void uncheckStudent() 
    {
        this.selectedStudent.setChecked(false);
        this.selectedStudent.setPhotoRestricted(false);
        this.bc.updateCheckedInStudent(this.selectedStudent, this.userLogin.getUser());
    }

}
