package com.dinstone.msa.provider.service;

import java.util.List;

import com.dinstone.msa.model.User;

public interface UserService {

	User findById(Integer uid);

	List<User> findAll();
}
