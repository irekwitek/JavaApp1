/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mk.classes;

import com.paypal.api.payments.Amount;
import com.paypal.api.payments.Refund;
import com.paypal.api.payments.Sale;
import com.paypal.base.rest.APIContext;
import com.paypal.base.rest.PayPalRESTException;
import java.io.Serializable;
import java.util.Date;

/**
 *
 * @author irek
 */
public class MKRefund implements Serializable{

    private int refundId;
    private int registrationId;
    private String paypalPaymentId = "";
    private String paypalTransactionId = "";
    private String parentPaypalPayment = "";
    private String parentPaypalTransactionId = "";
    private Date paypalDate;
    private int madeByUserId;
    private String madeByUserName = "";
    private Double amount;
    private String comment = "";
    private int responseCode;
    private String responseMessage = "";

    public int getRefundId() {
        return refundId;
    }

    public void setRefundId(int refundId) {
        this.refundId = refundId;
    }

    public int getRegistrationId() {
        return registrationId;
    }

    public void setRegistrationId(int registrationId) {
        this.registrationId = registrationId;
    }

    public String getPaypalPaymentId() {
        return paypalPaymentId;
    }

    public void setPaypalPaymentId(String paypalPaymentId) {
        this.paypalPaymentId = paypalPaymentId;
    }

    public String getPaypalTransactionId() {
        return paypalTransactionId;
    }

    public void setPaypalTransactionId(String paypalTransactionId) {
        this.paypalTransactionId = paypalTransactionId;
    }

    public String getParentPaypalTransactionId() {
        return parentPaypalTransactionId;
    }

    public String getParentPaypalPayment() {
        return parentPaypalPayment;
    }

    public void setParentPaypalPayment(String parentPaypalPayment) {
        this.parentPaypalPayment = parentPaypalPayment;
    }

    public void setParentPaypalTransactionId(String parentPaypalTransactionId) {
        this.parentPaypalTransactionId = parentPaypalTransactionId;
    }

    public Date getPaypalDate() {
        return paypalDate;
    }

    public void setPaypalDate(Date paypalDate) {
        this.paypalDate = paypalDate;
    }

    public int getMadeByUserId() {
        return madeByUserId;
    }

    public void setMadeByUserId(int madeByUserId) {
        this.madeByUserId = madeByUserId;
    }

    public String getMadeByUserName() {
        return madeByUserName;
    }

    public void setMadeByUserName(String madeByUserName) {
        this.madeByUserName = madeByUserName;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public int getResponseCode() {
        return responseCode;
    }

    public void setResponseCode(int responseCode) {
        this.responseCode = responseCode;
    }

    public String getResponseMessage() {
        return responseMessage;
    }

    public void setResponseMessage(String responseMessage) {
        this.responseMessage = responseMessage;
    }

    public int processPaypalRefund() {
        
        this.responseCode = ApplicationProperties.PAYMENT_RESPONSE_CODE_SUCCESS;

        APIContext apiContext = null;
        Refund responseRefund = null;

        try {
            String clientId = (ApplicationProperties.PAYMENT_MODE == ApplicationProperties.PAYMENT_MODE_LIVE ? ApplicationProperties.CLIENT_ID_LIVE : ApplicationProperties.CLIENT_ID_SANDBOX);
            String clientSecret = (ApplicationProperties.PAYMENT_MODE == ApplicationProperties.PAYMENT_MODE_LIVE ? ApplicationProperties.CLIENT_SECRET_LIVE : ApplicationProperties.CLIENT_SECRET_SANDBOX);
            String mode = (ApplicationProperties.PAYMENT_MODE == ApplicationProperties.PAYMENT_MODE_LIVE ? ApplicationProperties.PAYMENT_MODE_LIVE : ApplicationProperties.PAYMENT_MODE_SANDBOX);
            apiContext = new APIContext(clientId, clientSecret, mode);
            if (apiContext.fetchAccessToken() == null || apiContext.fetchAccessToken().equals("")) {
                this.responseMessage = "Paypal connection problem. Please notify the MathKangaroo team.";
                this.responseCode = ApplicationProperties.PAYMENT_RESPONSE_CODE_TOKEN_ERROR;
                return this.responseCode;
            }
            // ###Sale
            // A sale transaction.
            // Create a Sale object with the
            // given sale transaction id.
            Sale sale = new Sale();
            sale.setId(this.getParentPaypalTransactionId());

            // ###Refund
            // A refund transaction.
            // Use the amount to create
            // a refund object
            Refund refund = new Refund();
            // ###Amount
            // Create an Amount object to
            // represent the amount to be
            // refunded. Create the refund object, if the refund is partial
            Amount amount = new Amount();
            amount.setCurrency("USD");
            amount.setTotal(String.format("%.2f", this.getAmount()));
            refund.setAmount(amount);

            responseRefund = sale.refund(apiContext, refund);
            if (responseRefund.getState().equalsIgnoreCase("completed")) {
                this.setPaypalPaymentId(responseRefund.getParentPayment());
                this.setPaypalTransactionId(responseRefund.getId());
            }
            else
            {
                this.responseCode = ApplicationProperties.REFUND_RESPONSE_CODE_GENERAL_ERROR;
                this.responseMessage = "Refund failed. Please notify MK team.";
                
            }
        } catch (PayPalRESTException e) {
                this.responseCode = ApplicationProperties.REFUND_RESPONSE_CODE_GENERAL_ERROR;
                this.responseMessage = "Payment processor msg: " + e.getDetails().getMessage() + ". Please notify the MK team.";
            System.out.println(e);
        }
        return this.responseCode;
    }
}
