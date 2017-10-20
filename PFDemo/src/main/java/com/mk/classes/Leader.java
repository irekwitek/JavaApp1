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
public class Leader  implements Serializable 
{

    private int leaderID;
    private String firstName = "";
    private String lastName = "";
    private String phone = "";
    private String phoneCell = "";
    private String email = "";
    private String address1 = "";
    private String address2 = "";
    private String city = "";
    private String state = "";
    private String zipcode = "";
    private String leaderLogonName = "";
    private String leaderLogonPassword = "";
    private String leaderLogonPasswordConfirm = "";
    private String tshirtSize = "";
    private int tshirtSizeCode;
    private String gender = "";
    private int genderCode;
    private String countryOfOrigin = "";
    private int role = 0;
    private int ethnicGroupCode;
    private String ethnicGroupName = "";
    private boolean primaryManager;
    private String managerRole;
    private String agreementStatus;

    public int getLeaderID() {
        return leaderID;
    }

    public void setLeaderID(int leaderID) {
        this.leaderID = leaderID;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPhoneCell() {
        return phoneCell;
    }

    public void setPhoneCell(String phoneCell) {
        this.phoneCell = phoneCell;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAddress1() {
        return address1;
    }

    public void setAddress1(String address1) {
        this.address1 = address1;
    }

    public String getAddress2() {
        return address2;
    }

    public void setAddress2(String address2) {
        this.address2 = address2;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getZipcode() {
        return zipcode;
    }

    public void setZipcode(String zipcode) {
        this.zipcode = zipcode;
    }

    public String getLeaderLogonName() {
        return leaderLogonName;
    }

    public void setLeaderLogonName(String leaderLogonName) {
        this.leaderLogonName = leaderLogonName;
    }

    public String getLeaderLogonPassword() {
        return leaderLogonPassword;
    }

    public void setLeaderLogonPassword(String leaderLogonPassword) {
        this.leaderLogonPassword = leaderLogonPassword;
    }

    public String getLeaderLogonPasswordConfirm() {
        return leaderLogonPasswordConfirm;
    }

    public void setLeaderLogonPasswordConfirm(String leaderLogonPasswordConfirm) {
        this.leaderLogonPasswordConfirm = leaderLogonPasswordConfirm;
    }

    public String getTshirtSize() {
        return tshirtSize;
    }

    public void setTshirtSize(String tshirtSize) {
        this.tshirtSize = tshirtSize;
    }

    public int getTshirtSizeCode() {
        return tshirtSizeCode;
    }

    public void setTshirtSizeCode(int tshirtSizeCode) {
        this.tshirtSizeCode = tshirtSizeCode;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public int getGenderCode() {
        return genderCode;
    }

    public void setGenderCode(int genderCode) {
        this.genderCode = genderCode;
    }

    public String getCountryOfOrigin() {
        return countryOfOrigin;
    }

    public void setCountryOfOrigin(String countryOfOrigin) {
        this.countryOfOrigin = countryOfOrigin;
    }

    public int getRole() {
        return role;
    }

    public void setRole(int role) {
        this.role = role;
    }

    public int getEthnicGroupCode() {
        return ethnicGroupCode;
    }

    public void setEthnicGroupCode(int ethnicGroupCode) {
        this.ethnicGroupCode = ethnicGroupCode;
    }

    public String getEthnicGroupName() {
        return ethnicGroupName;
    }

    public void setEthnicGroupName(String ethnicGroupName) {
        this.ethnicGroupName = ethnicGroupName;
    }

    public boolean isPrimaryManager() {
        return primaryManager;
    }

    public void setPrimaryManager(boolean primaryManager) {
        this.primaryManager = primaryManager;
    }

    public String getManagerRole() {
        return managerRole;
    }

    public void setManagerRole(String managerRole) {
        this.managerRole = managerRole;
    }

    public String getAgreementStatus() {
        return agreementStatus;
    }

    public void setAgreementStatus(String agreementStatus) {
        this.agreementStatus = agreementStatus;
    }


    
    public String toString()
    {
	return this.leaderLogonName + " {" + this.firstName + " " + this.lastName + "}";
    }

}
