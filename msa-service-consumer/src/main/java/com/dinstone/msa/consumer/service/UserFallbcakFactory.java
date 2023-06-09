
package com.dinstone.msa.consumer.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.stereotype.Component;

import com.dinstone.msa.model.User;


@Component
public class UserFallbcakFactory implements FallbackFactory<UserClientService> {

    @Override
    public UserClientService create(Throwable throwable) {
        return new UserClientService() {

            @Override
            public List<User> list() {
                List<User> list = new ArrayList<>();
                User user = new User();
                user.setUid(8888);
                user.setUsername("[fallback] username");
                user.setAddress("[fallback] address");
                list.add(user);
                return list;
            }

            @Override
            public User get(long uid) {
                User user = new User();
                user.setUid((int) uid);
                user.setUsername("[fallback] username");
                user.setAddress("[fallback] address");
                return user;
            }
        };
    }

}
