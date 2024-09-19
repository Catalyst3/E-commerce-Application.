package com.example.ecomApp.globle;

import java.util.ArrayList;
import java.util.List;


import com.example.ecomApp.model.Products;

public class GlobalData {
	public static List<Products> cart;
	static {
		cart = new ArrayList<Products>();
	}
}
