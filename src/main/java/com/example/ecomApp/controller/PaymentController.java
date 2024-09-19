package com.example.ecomApp.controller;

import com.razorpay.Order;
import com.example.ecomApp.globle.GlobalData;
import com.example.ecomApp.model.Products;
import com.example.ecomApp.service.IPaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/payment")
public class PaymentController {

    @Autowired
    private IPaymentService paymentService;

    @PostMapping("/create-order")
    public Order createOrder(@RequestParam double amount, @RequestParam String currency,Model model) {
    	model.addAttribute("cartCount", GlobalData.cart.size());
        double totalPrice = GlobalData.cart.stream().mapToDouble(Products::getProductPrice).sum();
        model.addAttribute("total", totalPrice);
        model.addAttribute("cart", GlobalData.cart);
        model.addAttribute("totalPrice", totalPrice);
        try {
            return paymentService.createOrder(amount, currency);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
