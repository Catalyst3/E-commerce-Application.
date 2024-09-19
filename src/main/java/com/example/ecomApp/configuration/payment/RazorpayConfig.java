package com.example.ecomApp.configuration.payment;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.razorpay.RazorpayClient;

@Configuration
public class RazorpayConfig {
	@Bean
	RazorpayClient razorpayClient() throws Exception {
		String keyId = "rzp_test_jWtXKQJAYxTDWJ"; // Replace with your Razorpay Key ID
		String keySecret = "mLhFktDsROr0R89SX5jqtjwi"; // Replace with your Razorpay Key Secret
		return new RazorpayClient(keyId, keySecret);
	}
}
