package com.edgar.bookrental.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.edgar.bookrental.exceptions.UserDoesntExistException;
import com.edgar.bookrental.exceptions.UserIsAdminException;
import com.edgar.bookrental.models.user.Role;
import com.edgar.bookrental.models.user.User;
import com.edgar.bookrental.repositories.UserRepository;

@Service
public class UserService {

	@Autowired
	private UserRepository repository;

	public void assignRole(long id, Role role) throws Exception {

		if (isExists(id)) {

			User user = repository.findById(id)
					.orElseThrow(() -> new UserDoesntExistException("user doesnt exist id ::" + id));

			if (user.getRole() == Role.ADMIN) {
				throw new UserIsAdminException(" user is an admin , their roles cant be changed");
			}			
			repository.assignRole(id, role);
		}

		else
			throw new  UserDoesntExistException("user doesnt exist id ::" + id);
	}

	public boolean isExists(long id) {
		if (repository.existsById(id)) {
			return true;
		} else
			return false;
	}

}
