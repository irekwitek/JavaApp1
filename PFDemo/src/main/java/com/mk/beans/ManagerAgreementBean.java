/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mk.beans;

import com.mk.classes.ApplicationProperties;
import com.mk.classes.BusinessProcessControl;
import com.mk.classes.CommonData;
import com.mk.classes.EmailAdapter;
import com.mk.classes.Leader;
import com.mk.classes.User;
import com.mk.classes.UserEmailConfirmation;
import com.mk.classes.Utility;
import com.mk.dao.DBManager;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;

/**
 *
 * @author irek
 */
@ManagedBean(name = "managerAgreementBean")
@ViewScoped
public class ManagerAgreementBean implements Serializable {

    DBManager dbMgr;
    CommonData commonData;
    BusinessProcessControl bc;
    private UserLogin userLogin;

    private ArrayList<String> agreementItems = null;
    private ArrayList<String> agreementAdditionalItems = null;
    private String[] selectedAgreementItems;

    /**
     * Creates a new instance of RegistrationBean
     */
    public ManagerAgreementBean() {
        userLogin = (UserLogin) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("userLogin");
        // initiate business process control
        dbMgr = userLogin.getDbMgr();
        bc = userLogin.getBc();
        commonData = bc.getCommonData();
        this.agreementItems = ApplicationProperties.getManagerAgreementItems(this.dbMgr);
        this.agreementAdditionalItems = ApplicationProperties.getManagerAgreementAdditionalItems(this.dbMgr);
    }

    public ArrayList<String> getAgreementItems() {
        return agreementItems;
    }

    public void setAgreementItems(ArrayList<String> agreementItems) {
        this.agreementItems = agreementItems;
    }

    public ArrayList<String> getAgreementAdditionalItems() {
        return agreementAdditionalItems;
    }

    public void setAgreementAdditionalItems(ArrayList<String> agreementAdditionalItems) {
        this.agreementAdditionalItems = agreementAdditionalItems;
    }

    public String[] getSelectedAgreementItems() {
        return selectedAgreementItems;
    }

    public void setSelectedAgreementItems(String[] selectedAgreementItems) {
        this.selectedAgreementItems = selectedAgreementItems;
    }

    public CommonData getCommonData() {
        return commonData;
    }

    public void setCommonData(CommonData commonData) {
        this.commonData = commonData;
    }

    public String getConfirmationYear()
    {
        return ApplicationProperties.getCurrentRegistrationYear(this.dbMgr);
    }

    public void submitAgreement() {
        if (this.getSelectedAgreementItems().length == this.getAgreementItems().size()) {
            this.userLogin.getManager().setAgreementStatus("Y");
            this.bc.sendAgreementAcceptance(this.userLogin.getManager());
            ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
            try {
                ec.redirect(ec.getRequestContextPath() + "/faces/MKRMgrHome.xhtml");
            } catch (IOException ex) {
                System.out.println("Exception redirecting: " + ex);
            }

        } else {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "You must accept all conditions.", ""));
        }
    }

}
