package edu.sjsu.cmpe275.lab2.controller;

import java.util.List;
//import com.google.gson.Gson;







import edu.sjsu.cmpe275.lab2.entities.*;
import edu.sjsu.cmpe275.lab2.dao.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.view.json.MappingJackson2JsonView;

import com.google.gson.Gson;


@Controller
public class PersonController {

	@Autowired
	private PersonDAO personDao;

	@RequestMapping(value = "/person", method = RequestMethod.POST,produces={"application/json"})
	public  @ResponseBody ModelAndView create(
			@RequestParam(value = "firstname", required = true) String firstname,
			@RequestParam(value = "lastname", required = true) String lastname,
			@RequestParam(value="email", required=true) String email,
			@ModelAttribute("description") String description,
			@ModelAttribute("street") String street,
			@ModelAttribute("city") String city,
			@ModelAttribute("state") String state,
			@ModelAttribute("zip") String zip,
			@ModelAttribute("organizationid") int organizationid,
			BindingResult result){
			HttpHeaders responseHeaders = new HttpHeaders();
			ModelAndView model = new ModelAndView();
			model.setView(new MappingJackson2JsonView());
		if(result.hasErrors()){
			return  model.addObject("response", new ResponseEntity<String>("Invalid Request", responseHeaders, HttpStatus.BAD_REQUEST));

		}
		Person person = new Person();
	
		person.setFirstName(firstname);
		person.setLastName(lastname);
		person.setEmail(email);
		person.setDescription(description);
		person.setAddress(new Address(street,city,state,zip));
		person.setOrganization(new Organization(organizationid));
		Person createdPerson=personDao.add(person);
		
		if(createdPerson ==null){
			return  model.addObject("response", new ResponseEntity<String>("CreatePerson Failed", responseHeaders, HttpStatus.EXPECTATION_FAILED));
		}
			
		return model.addObject("createdPerson", createdPerson);
		
	}

	@RequestMapping(value = "/getPerson")
	public ModelAndView edit(
			@RequestParam(value = "personId", required = true) Long id) {
		System.out.println("Id= " + id);
		ModelAndView model = new ModelAndView("index");
		Person person = personDao.getPerson(id);
		// System.out.println("Person getfirstName in controller::"+person.getFirstName());
		model.addObject("person", person);
		// model.setViewName(viewName);
		//model.setView(view);
		return model;
	}
}