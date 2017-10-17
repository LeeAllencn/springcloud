package com.rocky.cloud.repository;

import com.rocky.cloud.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by Rocky on 2017-10-17.
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {
}
