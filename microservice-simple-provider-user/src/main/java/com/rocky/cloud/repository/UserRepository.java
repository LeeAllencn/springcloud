package com.rocky.cloud.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.rocky.cloud.entity.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

}
