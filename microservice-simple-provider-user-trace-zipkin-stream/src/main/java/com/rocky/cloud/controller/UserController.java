package com.rocky.cloud.controller;

import com.rocky.cloud.entity.User;
import com.rocky.cloud.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by Rocky on 2017-10-17.
 */
@RestController
public class UserController {
    @Autowired
    private UserRepository userRepository;

    @GetMapping("/{id}")
    public User findById(@PathVariable Long id) {
        User findOne = this.userRepository.findOne(id);
        return findOne;
    }
}
