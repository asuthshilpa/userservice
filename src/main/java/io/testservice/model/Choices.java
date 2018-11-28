package io.testservice.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.TableGenerator;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
public class Choices {
	@TableGenerator(name = "Choice_Gen", initialValue = 5)
	@Id
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "Choice_Gen")

	@JsonIgnore
	int choiceId;

	String choice;
	String votes = "0";

	public Choices() {

	}

	public Choices(String choice, String votes) {
		super();
		this.choice = choice;
		this.votes = votes;

	}

	public int getChoiceId() {
		return choiceId;
	}

	public void setChoiceId(int choiceId) {
		this.choiceId = choiceId;
	}

	public String getChoice() {
		return choice;
	}

	public void setChoice(String choice) {
		this.choice = choice;
	}

	public String getVotes() {
		return votes;
	}

	public void setVotes(String votes) {
		this.votes = votes;
	}

}
