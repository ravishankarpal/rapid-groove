package com.rapid.dao;

import com.rapid.core.entity.User;
import com.rapid.core.entity.UserAddress;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserAddressRepository extends JpaRepository<UserAddress, Integer> {

    List<UserAddress> findByUser(User user);
}
