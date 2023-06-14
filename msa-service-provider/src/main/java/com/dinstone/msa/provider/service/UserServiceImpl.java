package com.dinstone.msa.provider.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Service;

import com.dinstone.msa.model.User;

@Service
@RefreshScope
public class UserServiceImpl implements UserService {

	@Value("${version}")
	private String version = "2.0.0";

	@Autowired
	private UserConfig userConfig;

	private Map<Integer, User> usersMap = new HashMap<>();

	public UserServiceImpl() {
		usersMap.put(1, new User(1, "dinstone", "beijing"));
		usersMap.put(2, new User(2, "jackson", "shanghai"));
		usersMap.put(3, new User(3, "hover", "henan"));
	}

	@Override
	public User findById(Integer uid) {
		return usersMap.get(uid).setVersion(version + " : " + userConfig.getVersion() + " : " + userConfig.getGroup());
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
