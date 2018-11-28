package io.testservice.controllers;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import io.testservice.model.Questions;
import io.testservice.model.ReqQuestion;
import io.testservice.service.PollsService;

@RestController
public class AppControler {

	@Autowired
	PollsService service;

	@RequestMapping(value = "/questions", method = RequestMethod.GET, produces = "application/json")
	@ResponseBody
	public List<Questions> getQuestions() {

		return service.getAllQuestions();

	}

	@RequestMapping(value = "/questions", method = RequestMethod.POST, consumes = "application/json", produces = "application/json")
	@ResponseBody
	public ResponseEntity<Questions> createQuestion(@RequestBody ReqQuestion reqQuest) throws URISyntaxException {
		int id = service.addQuestion(reqQuest); // adds a new entry to DB and returns ID
		URI location = new URI("/questions/" + id);
		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.setLocation(location);

		return new ResponseEntity<Questions>(service.getQuestionAtId(id), responseHeaders, HttpStatus.CREATED);

	}

}
