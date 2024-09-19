package com.example.ecomApp.controller;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.ecomApp.globle.GlobalData;
import com.example.ecomApp.model.Products;
import com.example.ecomApp.service.IProductService;

@Controller
@RequestMapping("/cart")
public class CartController {

    private final IProductService productService;

    @Autowired
    public CartController(IProductService productService) {
        this.productService = productService;
    }

    @GetMapping("/addToCart/{id}")
    public String addToCart(@PathVariable int id, RedirectAttributes redirectAttributes) {
        Optional<Products> productOptional = productService.getProductByID(id);
        if (productOptional.isPresent()) {
            GlobalData.cart.add(productOptional.get());
            redirectAttributes.addFlashAttribute("successMessage", "Product added to cart successfully");
        } else {
            redirectAttributes.addFlashAttribute("errorMessage", "Product with id " + id + " not found");
        }
        return "redirect:/cart/viewCart";
    }

    @GetMapping("/viewCart")
    public String viewCart(Model model) {
        model.addAttribute("cartCount", GlobalData.cart.size());
        double totalPrice = GlobalData.cart.stream().mapToDouble(Products::getProductPrice).sum();
        model.addAttribute("total", totalPrice);
        model.addAttribute("cart", GlobalData.cart);
        return "cart";
    }
    
    @GetMapping("/removeItem/{index}")
    public String removeItemFromCart(@PathVariable("index") int index) {
        if (index >= 0 && index < GlobalData.cart.size()) {
            GlobalData.cart.remove(index);
        } else {
            // Handle the case where the index is out of bounds
            // Log the error or set a proper error message
            throw new IndexOutOfBoundsException("Index out of bounds: " + index);
        }
        return "redirect:/cart/viewCart";
    }

    @GetMapping("/checkout")
    public String showCheckoutPage(Model model) {
    	model.addAttribute("cartCount", GlobalData.cart.size());
        double totalPrice = GlobalData.cart.stream().mapToDouble(Products::getProductPrice).sum();
        model.addAttribute("total", totalPrice);
        model.addAttribute("cart", GlobalData.cart);
        model.addAttribute("totalPrice", totalPrice);
        return "checkout";
    }
}
