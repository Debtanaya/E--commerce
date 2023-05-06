package com.niit.Frontend.handler;

import java.util.ArrayList;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.niit.Backend.DAO.CartLinesDAO;
import com.niit.Backend.DAO.UserDAO;
import com.niit.Backend.modal.Address;
import com.niit.Backend.modal.CartLines;
import com.niit.Backend.modal.User;
import com.niit.Frontend.model.CheckoutModel;


@Component("checkoutHandler")
public class CheckoutHandler 
{
	@Autowired
	UserDAO userDAO;
	
	@Autowired
	CartLinesDAO cartlinesDAO;
	
	public List<Address> getShippingAddresses(CheckoutModel model) 
	{
		
		List<Address> addresses = userDAO.getShippingAddress(model.getUser().getId());
		
		if(addresses.size() == 0) {
			addresses = new ArrayList<>();
		}

		addresses.add(addresses.size(), userDAO.getBillingAddress(model.getUser().getId()));			
		
		return addresses;
	}
	
	
	public CheckoutModel init(String name) throws Exception{

		User user = userDAO.getUser(name);
		
		CheckoutModel checkoutModel = null;	

		if(user!=null) {
			checkoutModel = new CheckoutModel();
			checkoutModel.setUser(user);
			checkoutModel.setCart(user.getCart());
			
			double checkoutTotal = 0.0;
			List<CartLines> cartLines = cartlinesDAO.list(user.getCart().getId());

			if(cartLines.size() == 0 ) {
				throw new Exception("There are no products available for checkout now!");
			}
			
			for(CartLines cartLine: cartLines) {
				checkoutTotal += cartLine.getTotal();
			}
			
			checkoutModel.setCartLines(cartLines);
			checkoutModel.setCheckoutTotal(checkoutTotal);			
		}			
		
		return checkoutModel;
	}
	
	public String saveAddress(CheckoutModel checkoutModel, Address shipping) 
	{

		String transitionValue = "success";

		shipping.setUserId(checkoutModel.getUser().getId());
		shipping.setShipping(true);
		userDAO.insertAddress(shipping);

		checkoutModel.setShipping(shipping);

		return transitionValue;
	}
	
	public String saveAddressSelection(CheckoutModel checkoutModel, int shippingId) {

		String transitionValue = "success";
		Address shipping = userDAO.getAddress(shippingId);
		checkoutModel.setShipping(shipping);
		return transitionValue;

	}

}