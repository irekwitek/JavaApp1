/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mk.classes;

import java.io.Serializable;
import java.util.ArrayList;

/**
 *
 * @author giw
 */
public class User implements Serializable
{

    private int userID;
    private String userFirstName = "";
    private String userLastName = "";
    private String userAddress1 = "";
    private String userAddress2 = "";
    private String userCity = "";
    private String userState = "";
    private String userZipcode = "";
    private String logonName = "";
    private String logonPassword = "";
    private String logonPasswordConfirm = "";
    private String authQuestion = "";
    private String authAnswer = "";
    private String authCodeTxt = "";
    private int roleCode;
    private String roleName = "";
    private String userGender = "";
    private String userPhone = "";
    private String userPhoneCell = "";
    private String userEmail = "";
    private int otherID = 0;
    private String otherIDLogonName = " ";
    private String otherIDLogonPassword = " ";
    private int adminRole = 0;
    private ArrayList<DiscountCode> userDiscountCodes;
    private ArrayList<RegisteredStudent> userStudents;

    public ArrayList<RegisteredStudent> getUserStudents() {
        return userStudents;
    }

    public void setUserStudents(ArrayList<RegisteredStudent> userStudents) {
        this.userStudents = userStudents;
    }

    
    public ArrayList<DiscountCode> getUserDiscountCodes() {
        return userDiscountCodes;
    }

    public void setUserDiscountCodes(ArrayList<DiscountCode> userDiscountCodes) {
        this.userDiscountCodes = userDiscountCodes;
    }

    public int getAdminRole() {
        return adminRole;
    }

    public void setAdminRole(int adminRole) {
        this.adminRole = adminRole;
    }

    public String getOtherIDLogonName()
    {
        return otherIDLogonName;
    }

    public void setOtherIDLogonName(String otherIDLogonName)
    {
        this.otherIDLogonName = otherIDLogonName;
    }

    public String getOtherIDLogonPassword()
    {
        return otherIDLogonPassword;
    }

    public void setOtherIDLogonPassword(String otherIDLogonPassword)
    {
        this.otherIDLogonPassword = otherIDLogonPassword;
    }


    public int getOtherID()
    {
        return otherID;
    }

    public void setOtherID(int otherID)
    {
        this.otherID = otherID;
    }
    

    public String getLogonPasswordConfirm()
    {
        return logonPasswordConfirm;
    }

    public void setLogonPasswordConfirm(String logonPasswordConfirm)
    {
        this.logonPasswordConfirm = logonPasswordConfirm;
    }

    

    public String getAuthCodeTxt()
    {
        return authCodeTxt;
    }

    public void setAuthCodeTxt(String authCodeTxt)
    {
        this.authCodeTxt = authCodeTxt;
    }



    public boolean hasThisRole(int roleCode )
    {
       if (this.getRoleCode() == roleCode )
       {
           return true;
       }
       else
       {
           return false;
       }
    }


    /**
     * Get the value of email
     *
     * @return the value of email
     */
    public String getUserEmail()
    {
        return userEmail;
    }

    /**
     * Set the value of email
     *
     * @param email new value of email
     */
    public void setUserEmail(String email)
    {
        this.userEmail = email;
    }

    /**
     * Get the value of phoneCell
     *
     * @return the value of phoneCell
     */
    public String getUserPhoneCell()
    {
        return userPhoneCell;
    }

    /**
     * Set the value of phoneCell
     *
     * @param phoneCell new value of phoneCell
     */
    public void setUserPhoneCell(String phoneCell)
    {
        this.userPhoneCell = phoneCell;
    }

    /**
     * Get the value of phone
     *
     * @return the value of phone
     */
    public String getUserPhone()
    {
        return userPhone;
    }

    /**
     * Set the value of phone
     *
     * @param phone new value of phone
     */
    public void setUserPhone(String phone)
    {
        this.userPhone = phone;
    }

    /**
     * Get the value of genderName
     *
     * @return the value of genderName
     */
    public String getUserGender()
    {
        return userGender;
    }

    /**
     * Set the value of genderName
     *
     * @param genderName new value of genderName
     */
    public void setUserGender(String gender)
    {
        this.userGender = gender;
    }

    /**
     * Get the value of roleName
     *
     * @return the value of roleName
     */
    public String getRoleName()
    {
        return roleName;
    }

    /**
     * Set the value of roleName
     *
     * @param roleName new value of roleName
     */
    public void setRoleName(String roleName)
    {
        this.roleName = roleName;
    }

    /**
     * Get the value of roleCode
     *
     * @return the value of roleCode
     */
    public int getRoleCode()
    {
        return roleCode;
    }

    /**
     * Set the value of roleCode
     *
     * @param roleCode new value of roleCode
     */
    public void setRoleCode(int roleCode)
    {
        this.roleCode = roleCode;
    }


    /**
     * Get the value of authAnswer
     *
     * @return the value of authAnswer
     */
    public String getAuthAnswer()
    {
        return authAnswer;
    }

    /**
     * Set the value of authAnswer
     *
     * @param authAnswer new value of authAnswer
     */
    public void setAuthAnswer(String authAnswer)
    {
        this.authAnswer = authAnswer;
    }

    /**
     * Get the value of authQuestion
     *
     * @return the value of authQuestion
     */
    public String getAuthQuestion()
    {
        return authQuestion;
    }

    /**
     * Set the value of authQuestion
     *
     * @param authQuestion new value of authQuestion
     */
    public void setAuthQuestion(String authQuestion)
    {
        this.authQuestion = authQuestion;
    }

    /**
     * Get the value of logonPassword
     *
     * @return the value of logonPassword
     */
    public String getLogonPassword()
    {
        return logonPassword;
    }

    /**
     * Set the value of logonPassword
     *
     * @param logonPassword new value of logonPassword
     */
    public void setLogonPassword(String logonPassword)
    {
        this.logonPassword = logonPassword;
    }

    /**
     * Get the value of logonName
     *
     * @return the value of logonName
     */
    public String getLogonName()
    {
        return logonName;
    }

    /**
     * Set the value of logonName
     *
     * @param logonName new value of logonName
     */
    public void setLogonName(String logonName)
    {
        this.logonName = logonName;
    }

    /**
     * Get the value of zipcode
     *
     * @return the value of zipcode
     */
    public String getUserZipcode()
    {
        return userZipcode;
    }

    /**
     * Set the value of zipcode
     *
     * @param zipcode new value of zipcode
     */
    public void setUserZipcode(String zipcode)
    {
        this.userZipcode = zipcode;
    }


    /**
     * Get the value of state
     *
     * @return the value of state
     */
    public String getUserState()
    {
        return userState;
    }

    /**
     * Set the value of state
     *
     * @param state new value of state
     */
    public void setUserState(String state)
    {
        this.userState = state;
    }

    /**
     * Get the value of city
     *
     * @return the value of city
     */
    public String getUserCity()
    {
        return userCity;
    }

    /**
     * Set the value of city
     *
     * @param city new value of city
     */
    public void setUserCity(String city)
    {
        this.userCity = city;
    }

    /**
     * Get the value of address2
     *
     * @return the value of address2
     */
    public String getUserAddress2()
    {
        return userAddress2;
    }

    /**
     * Set the value of address2
     *
     * @param address2 new value of address2
     */
    public void setUserAddress2(String address2)
    {
        this.userAddress2 = address2;
    }

    /**
     * Get the value of address1
     *
     * @return the value of address1
     */
    public String getUserAddress1()
    {
        return userAddress1;
    }

    /**
     * Set the value of address1
     *
     * @param address1 new value of address1
     */
    public void setUserAddress1(String address1)
    {
        this.userAddress1 = address1;
    }

    /**
     * Get the value of lastName
     *
     * @return the value of lastName
     */
    public String getUserLastName()
    {
        return userLastName;
    }

    /**
     * Set the value of lastName
     *
     * @param lastName new value of lastName
     */
    public void setUserLastName(String lastName)
    {
        this.userLastName = lastName;
    }

    /**
     * Get the value of firstName
     *
     * @return the value of firstName
     */
    public String getUserFirstName()
    {
        return userFirstName;
    }

    /**
     * Set the value of firstName
     *
     * @param firstName new value of firstName
     */
    public void setUserFirstName(String firstName)
    {
        this.userFirstName = firstName;
    }

    /**
     * Get the value of userID
     *
     * @return the value of userID
     */
    public int getUserID()
    {
        return userID;
    }

    /**
     * Set the value of userID
     *
     * @param userID new value of userID
     */
    public void setUserID(int userID)
    {
        this.userID = userID;
    }
    
    public String listUserRoles()
    {
        String ret = "USER";
        if (this.getRoleCode() == 2 )
        {
            ret += ",MANAGER";
        }
        if (this.getAdminRole() == 1 )
        {
            ret += ",ADMIN";
        }
        return ret;
    }
    
    public String getFullName() {
        return this.userFirstName + " " + this.userLastName;
    }
 
    public String toString() {
        return this.logonName + " ID " + this.getUserID() + " {" + this.userFirstName + " " + this.userLastName + "}";
    }

}
