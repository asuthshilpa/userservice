package io.testservice.controllers;

import java.net.URI;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import io.testservice.model.User;
import io.testservice.model.UserRequest;
import io.testservice.service.UserService;

@RestController
@RequestMapping(value = "/userservice")
public class AppControler {

	@Autowired
	UserService service;
	@Autowired
	public JavaMailSender emailSender;

	@RequestMapping(value = "/users", method = RequestMethod.GET, produces = "application/json")
	@ResponseBody
	public ResponseEntity<List<User>> getAllUsers() throws Exception {

		return new ResponseEntity<List<User>>(service.getAllUsers(), HttpStatus.OK);

	}

	@RequestMapping(value = "/users/{id}", method = RequestMethod.GET, produces = "application/json")
	@ResponseBody
	public ResponseEntity<User> getUser(@PathVariable("id") int id) throws Exception {
		HttpHeaders responseHeaders = new HttpHeaders();

		User user = service.getUserAtId(id);
		if (user != null && !user.getStatus().equals("D")) {

			URI location = new URI("/users/" + id);
			responseHeaders.setLocation(location);
			return new ResponseEntity<User>(service.getUserAtId(id), responseHeaders, HttpStatus.OK);
		} else {
			throw new Exception("data not found for user " + id);
		}

	}

	@RequestMapping(value = "/users/{id}", method = RequestMethod.PUT, produces = "application/json", consumes = "application/json")
	@ResponseBody
	public ResponseEntity<User> updateUser(@PathVariable("id") int id, @RequestBody UserRequest userReq)
			throws Exception {
		HttpHeaders responseHeaders = new HttpHeaders();
		User user = service.updateUserAtId(id, userReq);

		if (user != null) {
			URI location = new URI("/users/" + id);
			responseHeaders.setLocation(location);
			return new ResponseEntity<User>(user, responseHeaders, HttpStatus.OK);

		} else {
			throw new Exception("data not found for user " + id);
		}

	}

	@RequestMapping(value = "/users/{id}", method = RequestMethod.DELETE, produces = "application/json", consumes = "application/json")
	@ResponseBody
	public Map<String, Boolean> deleteUser(@PathVariable("id") int id) throws Exception {

		User user = service.deleteUserAtId(id);

		Map<String, Boolean> response = new HashMap<>();
		if (user == null) {
			throw new Exception("data not found for user " + id);

		}

		else {

			response.put("deleted", Boolean.TRUE);
		}

		return response;
	}

	@RequestMapping(value = "/users", method = RequestMethod.DELETE, produces = "application/json", consumes = "application/json")
	@ResponseBody
	public Map<String, Boolean> deleteAllUsers() throws Exception {

		Map<String, Boolean> response = new HashMap<>();
		service.deleteAllUsers();
		response.put("deleted", Boolean.TRUE);
		return response;
	}

	@RequestMapping(value = "/users/register", method = RequestMethod.POST, consumes = "application/json", produces = "application/json")
	@ResponseBody
	public ResponseEntity<User> createUser(@RequestBody UserRequest userReq) throws Exception {

		// validate if the user already exists
		HttpHeaders responseHeaders = new HttpHeaders();
		boolean exists = service.validateUser(userReq);
		if (exists) {
			throw new Exception("user with same email Id already exists");
		}

		int id = service.addUser(userReq); // adds a new entry to DB and returns ID
		URI location = new URI("/users/" + id);

		responseHeaders.setLocation(location);
		//send email to the registered user
		SimpleMailMessage message = new SimpleMailMessage();
		message.setTo(userReq.getEmailId());
		message.setSubject("test email");
		message.setText("user registered");
		emailSender.send(message);

		return new ResponseEntity<User>(service.getUserAtId(id), responseHeaders, HttpStatus.CREATED);

	}

}
