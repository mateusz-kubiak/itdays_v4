package net.atos.itdays.controller;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.validation.Valid;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import net.atos.itdays.domain.Lecture;
import net.atos.itdays.domain.User;
import net.atos.itdays.domain.repository.LectureRepository;
import net.atos.itdays.domain.repository.UserRepository;

@Controller
public class UserController {

	private final static Logger LOG = Logger.getLogger(UserController.class);
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private LectureRepository lectureRepository;
	
	@RequestMapping(value = "/signUp", method = RequestMethod.GET)
	public String getAddNewUserForm(Model model){
		model.addAttribute("lectures", lectureRepository.findAll());
		User newUser = new User();
		model.addAttribute("newUser", newUser);
		return "signUp";
	}
	
	@RequestMapping(value = "/signUp", method = RequestMethod.POST)
	public String processAddNewUserForm(Model model, @ModelAttribute("signUp") @Valid User newUser, BindingResult result){

		if(result.hasErrors()){
			return "signUp";
		}
		
		java.sql.Timestamp registrationDate = new Timestamp(Calendar.getInstance().getTimeInMillis());
		newUser.setCreationDate(registrationDate);
		
//		for(Lecture lecture : newUser.getPrelections()){
//			lecture.getListOfusers().add(newUser);
//		}
		
		userRepository.save(newUser); 
		LOG.info("POST request to create new user was submitted: " + newUser);
		return "redirect:/signUp";
	}
	
	@InitBinder
	public void initialiseBinder(WebDataBinder binder){
		binder.setAllowedFields("userId", "firstName","lastName","email","phone","userStatus", "prelections");
	}
	
	@GetMapping(path="/userList")
	public String getAllUsers(Model model) {
		model.addAttribute("users", userRepository.findAll());
		return "/userList";
	}
	
}
