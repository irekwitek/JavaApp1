/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mk.beans;

import com.mk.classes.ActionLog;
import com.mk.classes.Location;
import com.mk.classes.RegisteredStudent;
import com.mk.classes.StudentResult;
import java.io.Serializable;
import java.util.ArrayList;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import org.primefaces.context.RequestContext;

/**
 *
 * @author irek
 */
@ManagedBean(name="userStudentBean")
@ViewScoped
public class UserStudentBean implements Serializable 
{

    private UserLogin userLogin;
    
    private ArrayList<RegisteredStudent> myStudents;
    private RegisteredStudent studentToUpdate = new RegisteredStudent();
    private RegisteredStudent studentToDelete = new RegisteredStudent();
    private RegisteredStudent selectedStudent;
    private ArrayList<StudentResult> studentResults;
    
    private String dialogMessageText = "";
    
    /**
     * Creates a new instance of RegistrationBean
     */
    public UserStudentBean() {
        userLogin = (UserLogin) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("userLogin");
        // initiate business process control
        myStudents = userLogin.bc.getMyStudents(userLogin.getUser());
    }

    public ArrayList<StudentResult> getStudentResults() {
        return studentResults;
    }

    public void setStudentResults(ArrayList<StudentResult> studentResults) {
        this.studentResults = studentResults;
    }


    public RegisteredStudent getSelectedStudent() {
        return selectedStudent;
    }

    public void setSelectedStudent(RegisteredStudent selectedStudent) {
        this.selectedStudent = selectedStudent;
    }

    public RegisteredStudent getStudentToUpdate() {
        return studentToUpdate;
    }

    public void setStudentToUpdate(RegisteredStudent studentToUpdate) {
        this.studentToUpdate = studentToUpdate;
    }
    

    public String getDialogMessageText() {
        return dialogMessageText;
    }

    public void setDialogMessageText(String dialogMessageText) {
        this.dialogMessageText = dialogMessageText;
    }



    public RegisteredStudent getStudentToDelete() {
        return studentToDelete;
    }

    public void setStudentToDelete(RegisteredStudent studentToDelete) {
        this.studentToDelete = studentToDelete;
    }

    public ArrayList<RegisteredStudent> getMyStudents() {
        return myStudents;
    }

    public void setMyStudents(ArrayList<RegisteredStudent> myStudents) {
        this.myStudents = myStudents;
    }

    
    public void deleteStudent() 
    {
        this.studentToDelete.setOwnerId(this.userLogin.getUser().getUserID());
        if (this.userLogin.bc.deleteStudentAndRegistration(this.studentToDelete)) {
            this.dialogMessageText = "Student was removed successfully.";
            this.myStudents.remove(this.studentToDelete);

            // action log
            ActionLog al = new ActionLog();
            al.setActionId(2);
            al.setTableName("TStudents");
            al.setUserId(this.userLogin.getUser().getUserID());
            al.setComment("Deleted student " + this.studentToDelete.toString());
            this.userLogin.bc.createActionLog(al);
        } else {
            this.dialogMessageText = "There was an error deleting student.";
        }
        RequestContext.getCurrentInstance().execute("PF('dlgMessage').show();");
    }

     public void selectToUpdate( RegisteredStudent regSt )
     {
         this.selectedStudent = regSt;
         this.userLogin.bc.copyRegisteredStudent( regSt, this.studentToUpdate );
     }

    public void updateStudentData()
    {
        if (this.userLogin.bc.adminUpdateStudent(this.studentToUpdate) )
        {
            this.dialogMessageText = "Student data was updated successfully.";
            this.userLogin.bc.copyRegisteredStudent(studentToUpdate, selectedStudent);
            
            // action log
            ActionLog al = new ActionLog();
            al.setActionId(4);
            al.setTableName("TStudents");
            al.setUserId(this.userLogin.getUser().getUserID());
            al.setComment("Udated student " + this.studentToUpdate.toString());
            this.userLogin.bc.createActionLog(al);
        }
        else
        {
             this.dialogMessageText = "There was an error updating student data.";
        }
        RequestContext.getCurrentInstance().execute("PF('dlgMessage').show();");
    }

    public void generateStudentResults( RegisteredStudent regSt )
    {
        this.selectedStudent = regSt;
        this.studentResults = this.userLogin.bc.getResultsForStudent( regSt );
    }
    
    public void resendRegistrationEmail(RegisteredStudent rSt) {
        this.userLogin.bc.sendEmailRegistrationConfirmation(rSt,2);
        this.dialogMessageText = "Registration e-mail has been sent successfully.";
        RequestContext.getCurrentInstance().execute("PF('dlgMessage').show();");
    }
    
    public void getCenterInfo(RegisteredStudent rSt) 
    {
        if ( rSt.getLocationNameCityState() == null || rSt.getLocationNameCityState().trim().equals(""))
        {
            Location l = this.userLogin.bc.getLocationByID( rSt.getLocationID() );
            rSt.setLocationNameCityState(l.getLocationNameAndAddress());
        }
    }
}
