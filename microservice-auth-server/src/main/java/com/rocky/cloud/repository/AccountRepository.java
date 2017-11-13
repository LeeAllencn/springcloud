package com.rocky.cloud.repository;

/**
 * Created by Rocky on 2017-11-10.
 */

import com.rocky.cloud.entity.Account;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Component;

/**
 * 账户数据库操作类
 * MongoDB操作接口
 */
@Component
public interface AccountRepository extends MongoRepository<Account, String> {

    /**
     * 根据用户名查找账户信息
     * @param username 用户名
     * @return 账户信息
     */
    Account findByUserName(String username);
}
