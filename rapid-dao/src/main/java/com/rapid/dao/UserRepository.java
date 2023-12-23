package com.rapid.dao;

import com.rapid.core.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.RequestParam;

@Repository
public interface UserRepository  extends JpaRepository<User, String> {

    @Query(value = "select ur.role_id from user u join user_role ur\n" +
            "on u.user_name = ur.user_id\n" +
            "where u.user_name =:userName",nativeQuery = true)
    String getUserRole(@RequestParam("userName") String userName);
}
