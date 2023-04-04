package com.niit.Frontend.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.niit.Backend.DAO.CartLinesDAO;
import com.niit.Frontend.service.CartLinesService;

@Controller
@RequestMapping(value="/cart")
public class CartController 
{

	@Autowired
	CartLinesDAO cartlinesDAO;
	
	@Autowired
	CartLinesService cartlinesService;
	
	
	@RequestMapping(value="/show")
	public ModelAndView showCart(@RequestParam(name = "result", required = false) String result)
	{
		ModelAndView mv = new ModelAndView("index");
		
		mv.addObject("userclickshowcart",true);
		mv.addObject("title" , "Online Shopping - User Cart");
		if(result != null)
		{
				switch(result) 
				{
					case "added":
						mv.addObject("message", "CartLine has been successfully added!");
						break;
				}
		}
		
		mv.addObject("cartlines" , cartlinesDAO.listAvailable(37) );
		return mv;
	}
	
	
	@RequestMapping(value="/add/{id}/product")
	public String addCartProduct(@PathVariable("id") int p_id)
	{
		String result = cartlinesService.addCartLineProduct(p_id);
		
		return "redirect:/cart/show?"+result;
	}
	
	
}