/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mk.classes;

import com.paypal.api.payments.Address;
import com.paypal.api.payments.Amount;
import com.paypal.api.payments.CreditCard;
import com.paypal.api.payments.Details;
import com.paypal.api.payments.FundingInstrument;
import com.paypal.api.payments.Payer;
import com.paypal.api.payments.Payment;
import com.paypal.api.payments.RelatedResources;
import com.paypal.api.payments.Transaction;
import com.paypal.base.rest.APIContext;
import com.paypal.base.rest.PayPalRESTException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 *
 * @author irek
 */
public class MKPaymentInfo implements Serializable 
{
    private String cardType = "";
    private int cardTypeCode;
    private String cardNumber = "";
    private int cardExpiredMonth;
    private int cardExpiredYear;
    private int cardCode;
    private String cardUserFirstName = "";
    private String cardUserLastName = "";
    private String address = "";
    private String city = "";
    private String state = "";
    private String zipcode = "";
    
    // properties used to save payment into DB TPayment table
    private int responseCode;
    private String paypalPaymentId = "";
    private String paypalTransactionId = "";
    private Date paypalDate;
    private int paymentMethod;
    private int madeByUserId;
    private String madeByUserName = "";
    private Double amount;
    private int donationAmount = 10;
    private int otherDonationAmount = 0;
    private String year = "";
    private String comment = "";
    private String responseMessage = "";
    
    public int getCardTypeCode() {
        return cardTypeCode;
    }

    public String getResponseMessage() {
        return responseMessage;
    }

    public void setResponseMessage(String responseMessage) {
        this.responseMessage = responseMessage;
    }

    public void setCardTypeCode(int cardTypeCode) {
        this.cardTypeCode = cardTypeCode;
    }

    public String getCardType() {
        return cardType;
    }

    public void setCardType(String cardType) {
        this.cardType = cardType;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
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

    public String getCardNumber() {
        return cardNumber;
    }

    public void setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
    }

    public int getCardExpiredMonth() {
        return cardExpiredMonth;
    }

    public void setCardExpiredMonth(int cardExpiredMonth) {
        this.cardExpiredMonth = cardExpiredMonth;
    }

    public int getCardExpiredYear() {
        return cardExpiredYear;
    }

    public void setCardExpiredYear(int cardExpiredYear) {
        this.cardExpiredYear = cardExpiredYear;
    }

    public int getCardCode() {
        return cardCode;
    }

    public void setCardCode(int cardCode) {
        this.cardCode = cardCode;
    }

    public String getCardUserFirstName() {
        return cardUserFirstName;
    }

    public void setCardUserFirstName(String cardUserFirstName) {
        this.cardUserFirstName = cardUserFirstName;
    }

    public String getCardUserLastName() {
        return cardUserLastName;
    }

    public void setCardUserLastName(String cardUserLastName) {
        this.cardUserLastName = cardUserLastName;
    }

    public int getResponseCode() {
        return responseCode;
    }

    public void setResponseCode(int responseCode) {
        this.responseCode = responseCode;
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
 
    public Date getPaypalDate() {
        return paypalDate;
    }

    public void setPaypalDate(Date paypalDate) {
        this.paypalDate = paypalDate;
    }


    public int getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(int paymentMethod) {
        this.paymentMethod = paymentMethod;
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

    public int getDonationAmount() {
        return donationAmount;
    }

    public void setDonationAmount(int donationAmount) {
        this.donationAmount = donationAmount;
    }

    public int getOtherDonationAmount() {
        return otherDonationAmount;
    }

    public void setOtherDonationAmount(int otherDonationAmount) {
        this.otherDonationAmount = otherDonationAmount;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
    
    public boolean isPaymentInfoValid( boolean donationFlag )
    {
        boolean ret = true;
        this.responseMessage = "";
        // check if payment needs to be sent to PP
        double totalAmount = this.getAmount();
        if ( donationFlag )
        {
            totalAmount +=  this.getDonationAmount() + this.getOtherDonationAmount();
        }
        if ( totalAmount == 0.0 )
        {
            return ret;
        }
        if (this.getAddress().trim().equals("")
                || this.getCardCode() == 0
                || this.getCardNumber().trim().equals("")
                || this.getCardType().trim().equals("")
                || this.getCardUserFirstName().trim().equals("")
                || this.getCardUserLastName().trim().equals("")
                || this.getCity().trim().equals("")
                || this.getZipcode().trim().equals("")
                || this.getCardExpiredMonth() == 0
                || this.getCardExpiredYear() == 0 )
        {
            ret = false;
        }
        return ret;
    }

    public int processPaypalPayment() {
        int ret = ApplicationProperties.PAYMENT_RESPONSE_CODE_SUCCESS;

        APIContext apiContext = null;
        Payment responsePayment = null;

        try {
            String clientId = (ApplicationProperties.PAYMENT_MODE == ApplicationProperties.PAYMENT_MODE_LIVE ? ApplicationProperties.CLIENT_ID_LIVE : ApplicationProperties.CLIENT_ID_SANDBOX);
            String clientSecret = (ApplicationProperties.PAYMENT_MODE == ApplicationProperties.PAYMENT_MODE_LIVE ? ApplicationProperties.CLIENT_SECRET_LIVE : ApplicationProperties.CLIENT_SECRET_SANDBOX);
            String mode = (ApplicationProperties.PAYMENT_MODE == ApplicationProperties.PAYMENT_MODE_LIVE ? ApplicationProperties.PAYMENT_MODE_LIVE : ApplicationProperties.PAYMENT_MODE_SANDBOX);
            apiContext = new APIContext(clientId, clientSecret, mode);
            if (apiContext.fetchAccessToken() == null || apiContext.fetchAccessToken().equals("")) {
                this.responseMessage = "Paypal connection problem. Please notify the MathKangaroo team.";
                return ApplicationProperties.PAYMENT_RESPONSE_CODE_TOKEN_ERROR;
            }
            
            Details details = new Details();
            details.setShipping("0.00");
            details.setSubtotal(String.format("%.2f", this.getAmount()));
            details.setTax("0.00");

            Amount amount = new Amount();
            amount.setCurrency("USD");
            amount.setTotal(String.format("%.2f", this.getAmount()));
            amount.setDetails(details);

            List<Transaction> transactions = new ArrayList<Transaction>();
            Transaction transaction = new Transaction();
            transaction.setAmount(amount);
            transaction.setDescription("Student registration fee.");

            transactions.add(transaction);
            // ###Address
            // Base Address object used as shipping or billing
            // address in a payment. [Optional]
            Address billingAddress = new Address();
            billingAddress.setCity(this.getCity());
            billingAddress.setCountryCode("US");
            billingAddress.setLine1(this.getAddress());
            billingAddress.setPostalCode(this.getZipcode());
            billingAddress.setState(this.getState());

            CreditCard creditCard = new CreditCard();
            creditCard.setBillingAddress(billingAddress);
            creditCard.setCvv2(this.getCardCode());
            creditCard.setExpireMonth(this.getCardExpiredMonth());
            creditCard.setExpireYear(this.getCardExpiredYear());
            creditCard.setFirstName(this.getCardUserFirstName());
            creditCard.setLastName(this.getCardUserLastName());
            creditCard.setNumber(this.getCardNumber());
            creditCard.setType(this.getCardType());

            FundingInstrument fundingInstrument = new FundingInstrument();
            fundingInstrument.setCreditCard(creditCard);

            List<FundingInstrument> fundingInstruments = new ArrayList<FundingInstrument>();
            fundingInstruments.add(fundingInstrument);

            Payer payer = new Payer();
            payer.setFundingInstruments(fundingInstruments);
            payer.setPaymentMethod("credit_card");

            Payment payment = new Payment();
            payment.setIntent("sale");
            payment.setPayer(payer);
            payment.setTransactions(transactions);

            responsePayment = payment.create(apiContext);
            if (responsePayment.getState().equalsIgnoreCase("approved")) {
                this.setPaypalPaymentId(responsePayment.getId());
                Transaction t = responsePayment.getTransactions().get(0);
                RelatedResources rr = t.getRelatedResources().get(0);
                this.setPaypalTransactionId(rr.getSale().getId());
            }
            else
            {
                ret = ApplicationProperties.PAYMENT_RESPONSE_CODE_GENERAL_ERROR;
                this.responseMessage = "Payment failed. Please verify your payment information.";
            }
        } catch (PayPalRESTException e) {
                ret = ApplicationProperties.PAYMENT_RESPONSE_CODE_GENERAL_ERROR;
                this.responseMessage = "Payment processing error: " + e.getDetails().getDetails().get(0).getIssue();
                System.out.println(e);
        }
        catch (Exception e) {
                ret = ApplicationProperties.PAYMENT_RESPONSE_CODE_GENERAL_ERROR;
                this.responseMessage = "General payment processing error. Please notify the MK team.";
                System.out.println(e);
        }
        return ret;
    }

}
