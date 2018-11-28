package io.testservice.model;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.TableGenerator;

@Entity
public class Questions {
	@TableGenerator(name = "Quest_Gen", initialValue = 2)
	@Id
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "Quest_Gen")
	
	int id;

	String question;
	String published_at;
	@OneToMany(cascade = CascadeType.ALL)
	List<Choices> choicesList = new ArrayList<>();

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getQuestion() {
		return question;
	}

	public void setQuestion(String question) {
		this.question = question;
	}

	public String getPublished_at() {
		return published_at;
	}

	public void setPublished_at(String published_at) {
		this.published_at = published_at;
	}

	public List<Choices> getChoicesList() {
		return choicesList;
	}

	public void setChoicesList(List<Choices> choicesList) {
		this.choicesList = choicesList;
	}

}
