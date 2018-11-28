package io.testservice.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import io.testservice.model.Questions;
@Repository
public interface PollsRepository extends CrudRepository<Questions, Integer>{

}
