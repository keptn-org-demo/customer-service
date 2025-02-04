package com.ewolff.microservice.customer.web;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Calendar;
import java.util.Date; 

import com.ewolff.microservice.customer.Customer;
import com.ewolff.microservice.customer.CustomerRepository;

import java.io.*;

@Controller
public class CustomerController {

	private CustomerRepository customerRepository;

	@Autowired
	public CustomerController(CustomerRepository customerRepository) {
		this.customerRepository = customerRepository;
	}

	@RequestMapping(value = "/{id}.html", method = RequestMethod.GET, produces = MediaType.TEXT_HTML_VALUE)
	public ModelAndView customer(@PathVariable("id") long id) {
		return new ModelAndView("customer", "customer",
				customerRepository.findById(id).get());
	}

	@RequestMapping("/list.html")
	public ModelAndView customerList() {
		return new ModelAndView("customerlist", "customers",
				customerRepository.findAll());
	}

	@RequestMapping(value = "/form.html", method = RequestMethod.GET)
	public ModelAndView add() {
		return new ModelAndView("customer", "customer", new Customer());
	}

	@RequestMapping(value = "/form.html", method = RequestMethod.POST)
	public ModelAndView post(Customer customer, HttpServletRequest httpRequest) {
		customer = customerRepository.save(customer);
		return new ModelAndView("success");
	}

	@RequestMapping(value = "/{id}.html", method = RequestMethod.PUT)
	public ModelAndView put(@PathVariable("id") long id, Customer customer,
			HttpServletRequest httpRequest) {
		customer.setId(id);
		customerRepository.save(customer);
		return new ModelAndView("success");
	}

	@RequestMapping(value = "/{id}.html", method = RequestMethod.DELETE)
	public ModelAndView delete(@PathVariable("id") long id) {
		customerRepository.deleteById(id);
		return new ModelAndView("success");
	}

   @RequestMapping(value = "/version", method = RequestMethod.GET)
   @ResponseBody
   public String getVersion() {
		File file = new File("version"); 
		String version = "version not found";
		try {
			BufferedReader br = new BufferedReader(new FileReader(file));
			version = br.readLine();
		}
		catch(Exception e) {
			version = e.getMessage();
		}
		return version;
   }

   @RequestMapping(value = "/health", method = RequestMethod.GET)
   @ResponseBody
   public String getHealth() {

	   Date dateNow = Calendar.getInstance().getTime();
	   String health = "{ \"health\":[{\"service\":\"customer-service\",\"status\":\"OK\",\"date\":\"" + dateNow + "\" }]}";
	   return health;
   }

}
