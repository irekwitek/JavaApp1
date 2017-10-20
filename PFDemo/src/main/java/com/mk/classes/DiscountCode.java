/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mk.classes;

import java.io.Serializable;
import java.util.Date;

/**
 *
 * @author irek
 */
public class DiscountCode implements Serializable 
{
    private int codeId;
    private int userIdFor;
    private int type;
    private String typeName = "";
    private int issuedBy;
    private String issuedByName = "";
    private Date validUntil;
    private String status = "";
    private int numberOfCodes = 1;
    private Date usedOnDate;
    private Date issuedDate;
    private String comment = "";
    private int purposeType;
    private String purposeTypeName = "";

    public int getCodeId() {
        return codeId;
    }

    public void setCodeId(int codeId) {
        this.codeId = codeId;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public int getPurposeType() {
        return purposeType;
    }

    public void setPurposeType(int purposeType) {
        this.purposeType = purposeType;
    }

    public String getPurposeTypeName() {
        return purposeTypeName;
    }

    public void setPurposeTypeName(String purposeTypeName) {
        this.purposeTypeName = purposeTypeName;
    }

    public int getUserIdFor() {
        return userIdFor;
    }

    public void setUserIdFor(int userIdFor) {
        this.userIdFor = userIdFor;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public int getIssuedBy() {
        return issuedBy;
    }

    public void setIssuedBy(int issuedBy) {
        this.issuedBy = issuedBy;
    }

    public Date getValidUntil() {
        return validUntil;
    }

    public void setValidUntil(Date validUntil) {
        this.validUntil = validUntil;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getNumberOfCodes() {
        return numberOfCodes;
    }

    public void setNumberOfCodes(int numberOfCodes) {
        this.numberOfCodes = numberOfCodes;
    }

    public Date getUsedOnDate() {
        return usedOnDate;
    }

    public void setUsedOnDate(Date usedOnDate) {
        this.usedOnDate = usedOnDate;
    }

    public String getIssuedByName() {
        return issuedByName;
    }

    public void setIssuedByName(String issuedByName) {
        this.issuedByName = issuedByName;
    }

    public Date getIssuedDate() {
        return issuedDate;
    }

    public void setIssuedDate(Date issuedDate) {
        this.issuedDate = issuedDate;
    }
    
}
