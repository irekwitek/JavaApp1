/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mk.classes;

import java.io.Serializable;

/**
 *
 * @author giw
 */
public class Student implements Serializable
{

    private int studentID = 0;
    private String firstName = "";
    private String middleName = "";
    private String lastName = "";
    private String studentEmail = "";
    private String parentGuardianEmail = "";
    private String gender = "";
    private int genderCode = 0;
    private int ethnicGroupCode = 0;
    private String ethnicGroupName = "";
    private String teacherFirstName = "";
    private String teacherLastName = "";
    private String teacherEmail = "";
    private String studentIdentificationCode = "";
    private String address1 = "";
    private String address2 = "";
    private String city = "";
    private String state = "";
    private String stateName = "";
    private String zipcode = "";
    private String schoolName = "";
    private String countryOfOrigin = "";
    private int ownerId;
    private String comment = "";

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getCountryOfOrigin()
    {
        return countryOfOrigin;
    }

    public void setCountryOfOrigin(String countryOfOrigin)
    {
        this.countryOfOrigin = countryOfOrigin;
    }

    public int getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(int ownerId) {
        this.ownerId = ownerId;
    }

    public int getGenderCode()
    {
        return genderCode;
    }

    public void setGenderCode(int genderCode)
    {
        this.genderCode = genderCode;
    }


    

    /**
     * Get the value of schoolName
     *
     * @return the value of schoolName
     */
    public String getSchoolName()
    {
        return schoolName;
    }

    /**
     * Set the value of schoolName
     *
     * @param schoolName new value of schoolName
     */
    public void setSchoolName(String schoolName)
    {
        this.schoolName = schoolName;
    }

    /**
     * Get the value of zipcode
     *
     * @return the value of zipcode
     */
    public String getZipcode()
    {
        return zipcode;
    }

    /**
     * Set the value of zipcode
     *
     * @param zipcode new value of zipcode
     */
    public void setZipcode(String zipcode)
    {
        this.zipcode = zipcode;
    }

    /**
     * Get the value of state
     *
     * @return the value of state
     */
    public String getState()
    {
        return state;
    }

    /**
     * Set the value of state
     *
     * @param state new value of state
     */
    public void setState(String state)
    {
        this.state = state;
    }

    /**
     * Get the value of city
     *
     * @return the value of city
     */
    public String getCity()
    {
        return city;
    }

    /**
     * Set the value of city
     *
     * @param city new value of city
     */
    public void setCity(String city)
    {
        this.city = city;
    }

    /**
     * Get the value of address2
     *
     * @return the value of address2
     */
    public String getAddress2()
    {
        return address2;
    }

    /**
     * Set the value of address2
     *
     * @param address2 new value of address2
     */
    public void setAddress2(String address2)
    {
        this.address2 = address2;
    }

    /**
     * Get the value of address1
     *
     * @return the value of address1
     */
    public String getAddress1()
    {
        return address1;
    }

    /**
     * Set the value of address1
     *
     * @param address1 new value of address1
     */
    public void setAddress1(String address1)
    {
        this.address1 = address1;
    }

    /**
     * Get the value of identificationCode
     *
     * @return the value of identificationCode
     */
    public String getStudentIdentificationCode()
    {
        return studentIdentificationCode;
    }

    /**
     * Set the value of identificationCode
     *
     * @param identificationCode new value of identificationCode
     */
    public void setStudentIdentificationCode(String studentIdentificationCode)
    {
        this.studentIdentificationCode = studentIdentificationCode;
    }

    /**
     * Get the value of teacherEmail
     *
     * @return the value of teacherEmail
     */
    public String getTeacherEmail()
    {
        return teacherEmail;
    }

    /**
     * Set the value of teacherEmail
     *
     * @param teacherEmail new value of teacherEmail
     */
    public void setTeacherEmail(String teacherEmail)
    {
        this.teacherEmail = teacherEmail;
    }

    /**
     * Get the value of teacherLastName
     *
     * @return the value of teacherLastName
     */
    public String getTeacherLastName()
    {
        return teacherLastName;
    }

    /**
     * Set the value of teacherLastName
     *
     * @param teacherLastName new value of teacherLastName
     */
    public void setTeacherLastName(String teacherLastName)
    {
        this.teacherLastName = teacherLastName;
    }

    /**
     * Get the value of teacherFirstName
     *
     * @return the value of teacherFirstName
     */
    public String getTeacherFirstName()
    {
        return teacherFirstName;
    }

    /**
     * Set the value of teacherFirstName
     *
     * @param teacherFirstName new value of teacherFirstName
     */
    public void setTeacherFirstName(String teacherFirstName)
    {
        this.teacherFirstName = teacherFirstName;
    }

    /**
     * Get the value of ethnicGroupName
     *
     * @return the value of ethnicGroupName
     */
    public String getEthnicGroupName()
    {
        return ethnicGroupName;
    }

    /**
     * Set the value of ethnicGroupName
     *
     * @param ethnicGroupName new value of ethnicGroupName
     */
    public void setEthnicGroupName(String ethnicGroupName)
    {
        this.ethnicGroupName = ethnicGroupName;
    }

    /**
     * Get the value of ethnicGroupCode
     *
     * @return the value of ethnicGroupCode
     */
    public int getEthnicGroupCode()
    {
        return ethnicGroupCode;
    }

    /**
     * Set the value of ethnicGroupCode
     *
     * @param ethnicGroupCode new value of ethnicGroupCode
     */
    public void setEthnicGroupCode(int ethnicGroupCode)
    {
        this.ethnicGroupCode = ethnicGroupCode;
    }

    /**
     * Get the value of genderName
     *
     * @return the value of genderName
     */
    public String getGender()
    {
        return gender;
    }

    /**
     * Set the value of genderName
     *
     * @param genderName new value of genderName
     */
    public void setGender(String gender)
    {
        this.gender = gender;
    }


    /**
     * Get the value of parentGuardianEmail
     *
     * @return the value of parentGuardianEmail
     */
    public String getParentGuardianEmail()
    {
        return parentGuardianEmail;
    }

    /**
     * Set the value of parentGuardianEmail
     *
     * @param parentGuardianEmail new value of parentGuardianEmail
     */
    public void setParentGuardianEmail(String parentGuardianEmail)
    {
        this.parentGuardianEmail = parentGuardianEmail;
    }

    /**
     * Get the value of studentEmail
     *
     * @return the value of studentEmail
     */
    public String getStudentEmail()
    {
        return studentEmail;
    }

    /**
     * Set the value of studentEmail
     *
     * @param studentEmail new value of studentEmail
     */
    public void setStudentEmail(String studentEmail)
    {
        this.studentEmail = studentEmail;
    }

    /**
     * Get the value of lastName
     *
     * @return the value of lastName
     */
    public String getLastName()
    {
        return lastName;
    }

    /**
     * Set the value of lastName
     *
     * @param lastName new value of lastName
     */
    public void setLastName(String lastName)
    {
        this.lastName = lastName;
    }

    /**
     * Get the value of middleName
     *
     * @return the value of middleName
     */
    public String getMiddleName()
    {
        return middleName;
    }

    /**
     * Set the value of middleName
     *
     * @param middleName new value of middleName
     */
    public void setMiddleName(String middleName)
    {
        this.middleName = middleName;
    }

    /**
     * Get the value of firstName
     *
     * @return the value of firstName
     */
    public String getFirstName()
    {
        return firstName;
    }

    /**
     * Set the value of firstName
     *
     * @param firstName new value of firstName
     */
    public void setFirstName(String firstName)
    {
        this.firstName = firstName;
    }

    /**
     * Get the value of studentID
     *
     * @return the value of studentID
     */
    public int getStudentID()
    {
        return studentID;
    }

    /**
     * Set the value of studentID
     *
     * @param studentID new value of studentID
     */
    public void setStudentID(int studentID)
    {
        this.studentID = studentID;
    }

    public String getStateName() {
        return stateName;
    }

    public void setStateName(String stateName) {
        this.stateName = stateName;
    }
    
    
    
    public String toString() {
        return this.studentID + " {" + this.studentIdentificationCode + "} [" + this.firstName + " " + this.lastName + "]";
    }

}
