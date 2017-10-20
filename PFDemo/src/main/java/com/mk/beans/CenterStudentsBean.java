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
import com.mk.dao.DBConnectionManager;
import com.mk.dao.DBManager;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.Serializable;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Time;
import java.util.ArrayList;
import java.util.Hashtable;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;

/**
 *
 * @author irek
 */
@ManagedBean(name = "centerStudentsBean")
@ViewScoped
public class CenterStudentsBean implements Serializable {

    DBManager dbMgr;
    CommonData commonData;
    BusinessProcessControl bc;
    private UserLogin userLogin;
    
    private Location selectedCenter;
    private LocationSession selectedSession;
    private ArrayList<RegisteredStudent> centerStudents;
    private RegisteredStudent selectedStudent;

    
    private StreamedContent registeredStudentsfile;


    /**
     * Creates a new instance of RegistrationBean
     */
    public CenterStudentsBean() {
        userLogin = (UserLogin) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("userLogin");
        // initiate business process control
        dbMgr = userLogin.getDbMgr();
        bc = userLogin.getBc();
        commonData = bc.getCommonData();
        this.selectedCenter = this.userLogin.getSelectedCenter();
        this.selectedSession = this.userLogin.getSelectedSession();
        this.centerStudents = this.bc.getLocationSessionStudents(this.selectedSession, ApplicationProperties.getCurrentRegistrationYear(dbMgr));
    }

    public StreamedContent getRegisteredStudentsfile() {
        return registeredStudentsfile;
    }

    public void setRegisteredStudentsfile(StreamedContent registeredStudentsfile) {
        this.registeredStudentsfile = registeredStudentsfile;
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

    public ArrayList<RegisteredStudent> getCenterStudents() {
        return centerStudents;
    }

    public void setCenterStudents(ArrayList<RegisteredStudent> centerStudents) {
        this.centerStudents = centerStudents;
    }

    public RegisteredStudent getSelectedStudent() {
        return selectedStudent;
    }

    public void setSelectedStudent(RegisteredStudent selectedStudent) {
        this.selectedStudent = selectedStudent;
    }

    public LocationSession getSelectedSession() {
        return selectedSession;
    }

    public void setSelectedSession(LocationSession selectedSession) {
        this.selectedSession = selectedSession;
    }

    public String managerCenterStatusText() {
        String ret = "You are the primary manager of this center";
        if (!selectedCenter.isManagerPrimary()) {
            ret = "You are not a primary manager of this center";
        }
        return ret;
    }

    public String managerNoteTitle(RegisteredStudent rSt)
        {
            String ret = "";
            if (rSt.getManagerNote()!= null && rSt.getManagerNote().trim().length() > 0 )
            {
                ret = "Update manager note";
            }
            else
            {
                ret = "Add manager note";
            }
            return ret;
        }

   public void updateStudentComment() 
   {
        DBConnectionManager cm = new DBConnectionManager(dbMgr.getDBConnectionData(DBManager.MYSQL));
        PreparedStatement stm = null;

        try {
            String updateSql = "UPDATE TRegistration set MGR_NOTE = ? WHERE REGISTRATION_ID = ?";
            stm = cm.getConnection().prepareStatement(updateSql);
            stm.setString(1, this.getSelectedStudent().getManagerNote());
            stm.setInt(2, this.getSelectedStudent().getRegistrationID());
            int result = stm.executeUpdate();
            if (result != 1) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Update error!", ""));
            } 
            else 
            {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Status updated successfully.", ""));
            }

        } catch (SQLException se) {
            System.out.println(se);
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "SQL error!", ""));
        } 
        finally 
        {
            DBConnectionManager.dropConnObject(stm);
            cm.freeConnection();
            cm = null;
        }
    }

    public StreamedContent getFile() 
    {
    ByteArrayOutputStream outputStream =  new ByteArrayOutputStream();
        try 
        {
            outputStream.write("First Name,Last Name,Student Code,Level,T-Shirt,Parent/Guardian Email,Contact,Registration Date,Parent Note, Manager Note\n".getBytes());
            for (RegisteredStudent rSt: this.centerStudents)
            {
                outputStream.write(new StringBuffer(rSt.getFirstName()).append(",").append(rSt.getLastName()).append(",")
                        .append(rSt.getStudentIdentificationCode()).append(",").append(rSt.getLevel()).append(",")
                        .append(rSt.getTshirtSize()).append(",").append(rSt.getParentGuardianEmail()).append(",")
                        .append(rSt.getContactPhone1()).append(",").append(rSt.getDateTime()).append(",")
                        .append(rSt.getParentNote() == null ? "": rSt.getParentNote()).append(",").append(rSt.getManagerNote() == null ? "": rSt.getManagerNote())
                        .append("\n").toString().getBytes());
            }
        }
        catch (Exception e)
        {
            System.out.println("FileDownloadView error: " + e.toString());
        }
        InputStream is = new ByteArrayInputStream(outputStream.toByteArray());
        registeredStudentsfile = new DefaultStreamedContent(is, "text", this.userLogin.getSelectedCenter().getLocationCode() + " - " + this.userLogin.getSelectedSession().getSessionName() + "_" + this.getCurrentRegistrationYear() + " registration.csv");
        return registeredStudentsfile;
    }

    public StreamedContent getEmailFile() 
    {
        ByteArrayOutputStream outputStream =  new ByteArrayOutputStream();
        Hashtable ht = new Hashtable();
        try 
        {
            for (RegisteredStudent rSt: this.centerStudents)
            {
                if ( rSt.getParentGuardianEmail() != null )
                {
                    String email = rSt.getParentGuardianEmail().trim().toLowerCase();
                    if ( !ht.containsKey(email))
                    {
                        ht.put(email, email);
                        outputStream.write((email + ";\n").getBytes());
                    }
                }
            }
        }
        catch (Exception e)
        {
            System.out.println("FileDownloadView error: " + e.toString());
        }
        InputStream is = new ByteArrayInputStream(outputStream.toByteArray());
        registeredStudentsfile = new DefaultStreamedContent(is, "text", this.userLogin.getSelectedCenter().getLocationCode() + " - " + this.userLogin.getSelectedSession().getSessionName() + "_" + this.getCurrentRegistrationYear() + " emails.txt");
        return registeredStudentsfile;
    }

}
