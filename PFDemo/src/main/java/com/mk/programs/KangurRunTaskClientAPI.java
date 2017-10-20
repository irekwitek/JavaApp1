/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mk.programs;

import com.paypal.api.payments.Address;
import com.paypal.api.payments.Amount;
import com.paypal.api.payments.Authorization;
import com.paypal.api.payments.CreditCard;
import com.paypal.api.payments.Details;
import com.paypal.api.payments.FundingInstrument;
import com.paypal.api.payments.Payer;
import com.paypal.api.payments.Payment;
import com.paypal.api.payments.Refund;
import com.paypal.api.payments.RelatedResources;
import com.paypal.api.payments.Sale;
import com.paypal.api.payments.Transaction;
import com.paypal.base.rest.APIContext;
import com.paypal.base.rest.OAuthTokenCredential;
import com.paypal.base.rest.PayPalRESTException;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.api.client.config.DefaultClientConfig;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;



/**
 *
 * @author giw
 */
public class KangurRunTaskClientAPI
{

    public static void main(String[] arg) throws PayPalRESTException
    {
        //String clientId = "Af5m1LdIknK_yO7UQ88rFS7GWFoMRqNgq1CbdrG0zHJqQwflF3nCH3EdjkfvAojOyUR3xDP8CIxcWlSM";
        //String clientSecret = "EHaDC2SxIbNE2WEaxwcpOn6BKL7eyVzqO6X8dh8lTuzL1-WMMRrGqG4wqrfTuiZWmy0VSef8E8m3rnLW";
        String clientId = "ATXmMja2qFiqSPmywjCDr0JgpZJo78TuXym5zRhCvjYwinhlq9TTLjMzy1x2I2pxF2hxMzGiRzz33GOp";
        String clientSecret = "EEDjMnGr0jNj02QgFgDr-9zwXIa7951FxiDibxrEgqVan9HKFRhklQ7OMb-i0dDI_c1_0wUlrKJV7Tq6";
        String mode = "sandbox";

        /*
        
        System.out.println("PRODUCTION:");
        APIContext apiContext = new APIContext(clientId, clientSecret,"live");
        System.out.println(apiContext.fetchAccessToken());
        System.out.println("getAccessToken:");
        System.out.println(apiContext.getAccessToken());
        System.out.println("More info:");
        System.out.println(apiContext.getConfigurationMap());
    
        System.out.println("SANDBOX:");
        APIContext apiContext2 = new APIContext(clientId2, clientSecret2,"sandbox");
        System.out.println(apiContext2.fetchAccessToken());
        System.out.println("getAccessToken:");
        System.out.println(apiContext2.getAccessToken());
        System.out.println("More info:");
        System.out.println(apiContext2.getConfigurationMap());
        */
        
        APIContext apiContext = null;

        try {

            apiContext = new APIContext(clientId, clientSecret, mode);
            System.out.println(apiContext.fetchAccessToken());
            String transId = createPayment(apiContext);
            apiContext = new APIContext(clientId, clientSecret, mode);
            refundPayment(apiContext, transId);
        } catch (PayPalRESTException e) {
            System.out.println("Dupa Jas: " + e.getMessage());
        }

    }
	
            private static String createPayment(APIContext apiContext)
			{
                
                            String transId = "";
                                   
                            List<Transaction> transactions = new ArrayList<Transaction>();

		// ###Details
		// Let's you specify details of a payment amount.
		Details details = new Details();
		details.setShipping("0.00");
		details.setSubtotal("3.00");
		details.setTax("0.00");

		// ###Amount
		// Let's you specify a payment amount.
		Amount amount = new Amount();
		amount.setCurrency("USD");
		amount.setTotal("3.00");
		amount.setDetails(details);

		// ###Transaction
		// A transaction defines the contract of a
		// payment - what is the payment for and who
		// is fulfilling it. Transaction is created with
		// a `Payee` and `Amount` types
		Transaction transaction = new Transaction();
		transaction.setAmount(amount);
		transaction.setDescription("This is the payment transaction description.");

		// The Payment creation API requires a list of
		// Transaction; add the created `Transaction`
		// to a List
		transactions.add(transaction);
		// ###Address
		// Base Address object used as shipping or billing
		// address in a payment. [Optional]
		Address billingAddress = new Address();
		billingAddress.setCity("Downers Grove");
		billingAddress.setCountryCode("US");
		billingAddress.setLine1("5717 Aubrey Terr");
		billingAddress.setPostalCode("60516");
		billingAddress.setState("IL");

		// ###CreditCard
		// A resource representing a credit card that can be
		// used to fund a payment.
		CreditCard creditCard = new CreditCard();
		creditCard.setBillingAddress(billingAddress);
		creditCard.setCvv2(874);
		creditCard.setExpireMonth(11);
		creditCard.setExpireYear(2018);
		creditCard.setFirstName("Irek");
		creditCard.setLastName("Witek");
		creditCard.setNumber("4032039843783699");
		creditCard.setType("visa");
                
                
		// ###FundingInstrument
		// A resource representing a Payeer's funding instrument.
		// Use a Payer ID (A unique identifier of the payer generated
		// and provided by the facilitator. This is required when
		// creating or using a tokenized funding instrument)
		// and the `CreditCardDetails`
		FundingInstrument fundingInstrument = new FundingInstrument();
		fundingInstrument.setCreditCard(creditCard);

		// The Payment creation API requires a list of
		// FundingInstrument; add the created `FundingInstrument`
		// to a List
		List<FundingInstrument> fundingInstruments = new ArrayList<FundingInstrument>();
		fundingInstruments.add(fundingInstrument);

		// ###Payer
		// A resource representing a Payer that funds a payment
		// Use the List of `FundingInstrument` and the Payment Method
		// as 'credit_card'
		Payer payer = new Payer();
		payer.setFundingInstruments(fundingInstruments);
		payer.setPaymentMethod("credit_card");

		// ###Payment
		// A Payment Resource; create one using
		// the above types and intent as 'authorize'
		Payment payment = new Payment();
		payment.setIntent("sale");
		payment.setPayer(payer);
		payment.setTransactions(transactions);

                System.out.println("Before payment create...");
                Payment responsePayment = null;
        try {
            responsePayment = payment.create(apiContext);
                System.out.println("After payment create...\nResponse:\n" + responsePayment);
                System.out.println("Details:\n" + responsePayment.getState());
                Transaction t = responsePayment.getTransactions().get(0);
                {
                    for (RelatedResources rr : t.getRelatedResources())
                    {
                        transId = rr.getSale().getId();
                        System.out.println(transId);
                    }
                }
        } catch (PayPalRESTException ex) {
                        System.out.println("exeption:\n"+ex.getDetails().getName());
        }
        return transId;
                        }
           private static void refundPayment(APIContext apiContext, String transId)
           {
        // refund here
            
            		// ###Sale
		// A sale transaction.
		// Create a Sale object with the
		// given sale transaction id.
		Sale sale = new Sale();
		sale.setId(transId);

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
		 amount.setTotal("1.00");
		 refund.setAmount(amount);
		try {
			sale.refund(apiContext, refund);
			System.out.println("Sale Refunded" + Sale.getLastRequest() + " --- " + Sale.getLastResponse());
		} catch (PayPalRESTException e) {
                        System.out.println("exeption:\n"+e.getDetails().getName());
		}

        
                        }

}
