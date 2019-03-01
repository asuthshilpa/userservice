package io.testservice.service;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import io.testservice.model.User;
import io.testservice.model.UserRequest;
import io.testservice.repository.UserRepository;

@Service
public class UserService {

	@Autowired
	UserRepository userRepo;

	public UserService() {

	}

	/**
	 * gets all user from database - returns only which are not in Deleted Status
	 * 
	 * @return
	 */
	public List<User> getAllUsers() {
		List<User> activeUsers = new ArrayList<>();
		Iterable<User> userList = userRepo.findAll();
		
		userList.forEach(u -> {
			if (!u.getStatus().equalsIgnoreCase("D")) {
				activeUsers.add(u);
			}

		});

		return activeUsers;
	}

	/**
	 * gets user from database for a given Id
	 * 
	 * @param id
	 * @return
	 */
	public User getUserAtId(int id) {
		Optional<User> userOpt = userRepo.findById(id);
		if (userOpt.isPresent())
			return (User) userOpt.get();
		else
			return null;
	}

	/**
	 * update user in database for a given Id
	 * 
	 * @param id
	 * @param userReq
	 * @return
	 */
	public User updateUserAtId(int id, UserRequest userReq) {
		Optional<User> userOpt = userRepo.findById(id);
		if (userOpt.isPresent()) {

			User u = (User) userOpt.get();
			u.setEmailId(userReq.getEmailId());
			u.setFname(userReq.getFname());
			u.setLname(userReq.getLname());
			u.setStatus("U");  // set the updated status to U
			Instant timestamp = Instant.now();
			u.setUpdated_at(timestamp.toString()); // set the updated timestamp to now
			userRepo.save(u);
			return u;
		} else
			return null;
	}

	/**
	 * delete user in database for a given Id
	 * 
	 * @param id
	 * @return
	 */
	public User deleteUserAtId(int id) {
		Optional<User> userOpt = userRepo.findById(id);
		if (userOpt.isPresent()) {

			User u = (User) userOpt.get();
			u.setStatus("D"); // set the updated status to D
			Instant timestamp = Instant.now();
			u.setDeleted_at(timestamp.toString()); // set the deleted timestamp to now
			userRepo.save(u);
			return u;
		} else
			return null;
	}

	/**
	 * delete all user in database
	 */
	public void deleteAllUsers() {

		Iterable<User> userList = userRepo.findAll();
		userList.forEach(u -> {
			u.setStatus("D");
			userRepo.save(u);
		});

	}

	/**
	 * validate user - validates if another user is registered with same email id 
	 * @param req
	 * @return
	 */
	public boolean validateUser(UserRequest req) {
		List<User> users = userRepo.findByEmailId(req.getEmailId());
		List<User> filteredList = users.stream().filter(u -> !u.getStatus().equals("D")).collect(Collectors.toList());
		// if there is any user entry in DB with same emailid and is not having Deleted
		// / D status then we cannot register it again
		if (filteredList.size() > 0)
			return true;
		else
			return false;

	}

	/**
	 * add user to database
	 * @param req
	 * @return
	 */
	public int addUser(UserRequest req) {

		User user = new User();
		user.setFname(req.getFname());
		user.setLname(req.getLname());
		user.setStatus("C");
		user.setEmailId(req.getEmailId());
		Instant timestamp = Instant.now();
		user.setCreated_at(timestamp.toString());

		User u = userRepo.save(user);
		return u.getId();

	}

}
