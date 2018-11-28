package io.testservice.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import com.fasterxml.jackson.databind.ObjectMapper;

import io.testservice.controllers.AppControler;
import io.testservice.model.ReqQuestion;
import io.testservice.service.PollsService;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class testAppController {

	@Autowired
	private AppControler controller;

	@Test
	public void contexLoads() throws Exception {
		assertThat(controller).isNotNull();
	}

	@Autowired
	PollsService service;
	@Autowired
	ObjectMapper objectMapper;
	@Autowired
	TestRestTemplate restTemplate;

	HttpHeaders headers = new HttpHeaders();

	@Test
	public void getQuestionsTest() throws Exception {

		HttpEntity<String> entity = new HttpEntity<String>(null, headers);

		ResponseEntity<String> response = restTemplate.exchange("http://localhost:8080/questions", HttpMethod.GET,
				entity, String.class);

		String expected = objectMapper.writeValueAsString(service.getAllQuestions()); // gets the initial data set

		assertThat(response.getBody().contains(expected));

		assertTrue(response.getStatusCodeValue() == 200);
	}

	@Test
	public void addQuestionTest() {

		ReqQuestion reqquestion = new ReqQuestion();
		reqquestion.setQuestion("Favourite programming language?");

		reqquestion.setChoices(Arrays.asList("Swift", "Python", "Objective-C", "Ruby"));

		HttpEntity<ReqQuestion> entity = new HttpEntity<ReqQuestion>(reqquestion, headers);

		ResponseEntity<String> response = restTemplate.exchange("http://localhost:8080/questions", HttpMethod.POST,
				entity, String.class);

		String actual = response.getHeaders().get(HttpHeaders.LOCATION).get(0);

		assertTrue(response.getStatusCodeValue() == 201);
		assertTrue(actual.contains("/questions/"));

	}

}
