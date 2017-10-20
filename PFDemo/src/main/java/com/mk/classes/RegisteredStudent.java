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
public class RegisteredStudent extends Student implements Serializable
{

    private int status = 0;
    private String statusName = "";
    private String year = "";
    private int locationID = 0;
    private String locationCode = "";
    private int sessionID = 0;
    private String sessionName = "";
    private int levelCode = 0;
    private String level = "";
    private int tshirtSizeCode;
    private String tshirtSize = "";
    private String contactPhone1 = "";
    private String contactPhone2 = "";
    private String paymentID = "";
    private String transactionID = "";
    private double paymentAmount = 0.0;
    private String dateTime = "";
    private String locationNameCityState = "";
    private double registrationFee = 0.0;
    private int registrationFlag = 0;
    private int lateRegistrationDays = 0;
    private int updatedBy = 0;
    private String lastUpdated = "";
    private String parentNote = "";
    private int adCode = 0;
    private String adNote = "";
    private String discountTypeName = "";
    private int discountTypeId;
    private int discountCodeId;
    private String memo = "";
    private int registrationID = 0;
    private String selectedLSessionLevelsOfferred = "";
    private String stateOfResidency = "";
    private String managerNote = "";
    private String adminNote = "";
    private int paymentMethod;
    private String paymentDate = "";
    private boolean checked = false;
    private boolean photoRestricted = false;

    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }

    public boolean isPhotoRestricted() {
        return photoRestricted;
    }

    public void setPhotoRestricted(boolean photoRestricted) {
        this.photoRestricted = photoRestricted;
    }

    public String getPaymentDate() {
        return paymentDate;
    }

    public void setPaymentDate(String paymentDate) {
        this.paymentDate = paymentDate;
    }

    public String getStatusName() {
        return statusName;
    }

    public void setStatusName(String statusName) {
        this.statusName = statusName;
    }

    public int getDiscountCodeId() {
        return discountCodeId;
    }

    public void setDiscountCodeId(int discountCodeId) {
        this.discountCodeId = discountCodeId;
    }

    public String getAdminNote() {
        return adminNote;
    }

    public void setAdminNote(String adminNote) {
        this.adminNote = adminNote;
    }

    public String getStateOfResidency() {
        return stateOfResidency;
    }

    public void setStateOfResidency(String stateOfResidency) {
        this.stateOfResidency = stateOfResidency;
    }

    public String getSelectedLSessionLevelsOfferred() {
        return selectedLSessionLevelsOfferred;
    }

    public void setSelectedLSessionLevelsOfferred(String selectedLSessionLevelsOfferred) {
        this.selectedLSessionLevelsOfferred = selectedLSessionLevelsOfferred;
    }

    public int getRegistrationID() {
        return registrationID;
    }

    public void setRegistrationID(int registrationID) {
        this.registrationID = registrationID;
    }

    public String getPaymentID() {
        return paymentID;
    }

    public void setPaymentID(String paymentID) {
        this.paymentID = paymentID;
    }

    public String getTransactionID() {
        return transactionID;
    }

    public void setTransactionID(String transactionID) {
        this.transactionID = transactionID;
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

    public String getDiscountTypeName() {
        return discountTypeName;
    }

    public void setDiscountTypeName(String discountTypeName) {
        this.discountTypeName = discountTypeName;
    }

    public int getDiscountTypeId() {
        return discountTypeId;
    }

    public void setDiscountTypeId(int discountTypeId) {
        this.discountTypeId = discountTypeId;
    }

    public String getLastUpdated() {
        return lastUpdated;
    }

    public void setLastUpdated(String lastUpdated) {
        this.lastUpdated = lastUpdated;
    }

    public double getPaymentAmount() {
        return paymentAmount;
    }

    public void setPaymentAmount(double paymentAmount) {
        this.paymentAmount = paymentAmount;
    }

    
    public int getUpdatedBy()
    {
        return updatedBy;
    }

    public void setUpdatedBy(int updatedBy)
    {
        this.updatedBy = updatedBy;
    }

    public int getLateRegistrationDays() {
        return lateRegistrationDays;
    }

    public void setLateRegistrationDays(int lateRegistrationDays) {
        this.lateRegistrationDays = lateRegistrationDays;
    }

    
    public int getRegistrationFlag()
    {
        return registrationFlag;
    }

    public void setRegistrationFlag(int registrationFlag)
    {
        this.registrationFlag = registrationFlag;
    }

    
    
    public double getRegistrationFee()
    {
        return registrationFee;
    }

    public void setRegistrationFee(double registrationFee)
    {
        this.registrationFee = registrationFee;
    }

    
    public String getLocationNameCityState()
    {
        return locationNameCityState;
    }

    public void setLocationNameCityState(String locationNameCityState)
    {
        this.locationNameCityState = locationNameCityState;
    }


    public String getDateTime()
    {
        return dateTime;
    }

    public void setDateTime(String dateTime)
    {
        this.dateTime = dateTime;
    }



    public String getContactPhone1()
    {
        return contactPhone1;
    }

    public void setContactPhone1(String contactPhone1)
    {
        this.contactPhone1 = contactPhone1;
    }

    public String getContactPhone2()
    {
        return contactPhone2;
    }

    public void setContactPhone2(String contactPhone2)
    {
        this.contactPhone2 = contactPhone2;
    }

    public String getLevel()
    {
        return level;
    }

    public void setLevel(String level)
    {
        this.level = level;
    }

    public int getLevelCode()
    {
        return this.levelCode;
    }

    public void setLevelCode(int levelCode)
    {
        this.levelCode = levelCode;
    }

    public String getLocationCode()
    {
        return locationCode;
    }

    public void setLocationCode(String locationCode)
    {
        this.locationCode = locationCode;
    }

    public int getLocationID()
    {
        return locationID;
    }

    public void setLocationID(int locationID)
    {
        this.locationID = locationID;
    }


    public String getTshirtSize()
    {
        return tshirtSize;
    }

    public void setTshirtSize(String tshirtSize)
    {
        this.tshirtSize = tshirtSize;
    }

    public int getTshirtSizeCode()
    {
        return tshirtSizeCode;
    }

    public void setTshirtSizeCode(int tshirtSizeCode)
    {
        this.tshirtSizeCode = tshirtSizeCode;
    }

    public String getYear()
    {
        return year;
    }

    public void setYear(String year)
    {
        this.year = year;
    }



    public int getStatus()
    {
        return status;
    }

    public void setStatus(int status)
    {
        this.status = status;
    }

    public String getParentNote() {
        return parentNote;
    }

    public void setParentNote(String parentNote) {
        this.parentNote = parentNote;
    }

    public int getSessionID() {
        return sessionID;
    }

    public void setSessionID(int sessionID) {
        this.sessionID = sessionID;
    }

    public String getSessionName() {
        return sessionName;
    }

    public void setSessionName(String sessionName) {
        this.sessionName = sessionName;
    }

    public int getAdCode() {
        return adCode;
    }

    public void setAdCode(int adCode) {
        this.adCode = adCode;
    }

    public String getAdNote() {
        return adNote;
    }

    public void setAdNote(String adNote) {
        this.adNote = adNote;
    }

    public String getManagerNote() {
        return managerNote;
    }

    public void setManagerNote(String managerNote) {
        this.managerNote = managerNote;
    }

    public int getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(int paymentMethod) {
        this.paymentMethod = paymentMethod;
    }


}
