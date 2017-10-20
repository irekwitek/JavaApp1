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
public class Registration implements Serializable
{

    private int studentID = 0;
    private String year = "";
    private int locationID = 0;
    private String locationCode = "";
    private int statusCode = 0;
    private int levelCode = 0;
    private String level = "";
    private int tshirtSizeCode;
    private String tshirtSize = "";
    private String contactPhone1 = "";
    private String contactPhone2 = "";
    private String paymentID = "";
    private String transactionID = "";
    private String dateTime = "";
    private String paymentDate = "";
    private String lastUpdated = "";
    private int updatedBy;
    private String updatedByName = "";
    private String memo = "";
    private String adminMemo = "";
    private String mgrMemo = "";
    private int adCode = 0;
    private String adCodeName = "";
    private String adNote = "";
    private int sessionID = 0;
    private String discountCode = "";

    public String getTransactionID() {
        return transactionID;
    }

    public void setTransactionID(String transactionID) {
        this.transactionID = transactionID;
    }

    public String getDiscountCode() {
        return discountCode;
    }

    public void setDiscountCode(String discountCode) {
        this.discountCode = discountCode;
    }

    public int getLocationID() {
        return locationID;
    }

    public void setLocationID(int locationID) {
        this.locationID = locationID;
    }

    public int getSessionID() {
        return sessionID;
    }

    public void setSessionID(int sessionID) {
        this.sessionID = sessionID;
    }

    public String getAdminMemo() {
        return adminMemo;
    }

    public void setAdminMemo(String adminMemo) {
        this.adminMemo = adminMemo;
    }

    public String getMgrMemo() {
        return mgrMemo;
    }

    public void setMgrMemo(String mgrMemo) {
        this.mgrMemo = mgrMemo;
    }

    
    public String getAdNote()
    {
        return adNote;
    }

    public void setAdNote(String adNote)
    {
        this.adNote = adNote;
    }


    public int getAdCode()
    {
        return adCode;
    }

    public void setAdCode(int adCode)
    {
        this.adCode = adCode;
    }

    public String getAdCodeName()
    {
        return adCodeName;
    }

    public void setAdCodeName(String adCodeName)
    {
        this.adCodeName = adCodeName;
    }

    
    
    public String getMemo()
    {
        return memo;
    }

    public void setMemo(String memo)
    {
        this.memo = memo;
    }

    
    
    public String getUpdatedByName()
    {
        return updatedByName;
    }

    public void setUpdatedByName(String updatedByName)
    {
        this.updatedByName = updatedByName;
    }

    /**
     * Get the value of userUpdated
     *
     * @return the value of userUpdated
     */
    public int getUpdatedBy()
    {
        return updatedBy;
    }

    /**
     * Set the value of userUpdated
     *
     * @param userUpdated new value of userUpdated
     */
    public void setUpdatedBy(int updatedBy)
    {
        this.updatedBy = updatedBy;
    }


    /**
     * Get the value of lastUpdate
     *
     * @return the value of lastUpdate
     */
    public String getLastUpdated()
    {
        return lastUpdated;
    }

    /**
     * Set the value of lastUpdate
     *
     * @param lastUpdate new value of lastUpdate
     */
    public void setLastUpdated(String lastUpdated)
    {
        this.lastUpdated = lastUpdated;
    }



    /**
     * Get the value of paymentDate
     *
     * @return the value of paymentDate
     */
    public String getPaymentDate()
    {
        return paymentDate;
    }

    /**
     * Set the value of paymentDate
     *
     * @param paymentDate new value of paymentDate
     */
    public void setPaymentDate(String paymentDate)
    {
        this.paymentDate = paymentDate;
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

    public int getLevelCode()
    {
        return levelCode;
    }

    public void setLevelCode(int levelCode)
    {
        this.levelCode = levelCode;
    }

    public String getLevel()
    {
        return level;
    }

    public void setLevel(String level)
    {
        this.level = level;
    }

    public int getLocationIDCode()
    {
        return locationID;
    }

    public void setLocationIDCode(int locationID)
    {
        this.locationID = locationID;
    }

    public String getLocationCode()
    {
        return locationCode;
    }

    public void setLocationCode(String locationCode)
    {
        this.locationCode = locationCode;
    }

    public String getPaymentID()
    {
        return paymentID;
    }

    public void setPaymentID(String paymentID)
    {
        this.paymentID = paymentID;
    }

    public int getStatusCode()
    {
        return statusCode;
    }

    public void setStatusCode(int statusCode)
    {
        this.statusCode = statusCode;
    }

    public int getStudentID()
    {
        return studentID;
    }

    public void setStudentID(int studentID)
    {
        this.studentID = studentID;
    }

    public int getTshirtSizeCode()
    {
        return tshirtSizeCode;
    }

    public void setTshirtSizeCode(int tshirtSizeCode)
    {
        this.tshirtSizeCode = tshirtSizeCode;
    }

    public String getTshirtSizeName()
    {
        return tshirtSize;
    }

    public void setTshirtSize(String tshirtSize)
    {
        this.tshirtSize = tshirtSize;
    }

    public String getYear()
    {
        return year;
    }

    public void setYear(String year)
    {
        this.year = year;
    }

    
}
