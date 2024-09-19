package com.example.ecomApp.service;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.razorpay.RazorpayClient;

@Service
public class IPaymentService {

    @Autowired
    private RazorpayClient razorpayClient;

    public com.razorpay.Order createOrder(double amount, String currency) throws Exception {
        JSONObject orderRequest = new JSONObject();
        orderRequest.put("amount", amount * 100); // Amount in paise
        orderRequest.put("currency", currency);
        orderRequest.put("receipt", "txn_" + System.currentTimeMillis());

        return razorpayClient.orders.create(orderRequest);
    }
}
