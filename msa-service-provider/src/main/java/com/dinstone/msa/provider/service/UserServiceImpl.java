package com.dinstone.msa.provider.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.dinstone.msa.model.User;

@Service
public class UserServiceImpl implements UserService {

	private String version = "2.0.0";

	private Map<Integer, User> usersMap = new HashMap<>();

	public UserServiceImpl() {
		usersMap.put(1, new User(1, "dinstone", "beijing"));
		usersMap.put(2, new User(2, "jackson", "shanghai"));
		usersMap.put(3, new User(3, "hover", "henan"));
	}

	@Override
	public User findById(Integer uid) {
		return usersMap.get(uid).setVersion(version);
	}

	@Override
	public List<User> findAll() {
		ArrayList<User> list = new ArrayList<>();
		for (User u : usersMap.values()) {
			list.add(u.setVersion(version));
		}
		return list;
	}

}
