package com.rapid.dao;

import com.rapid.core.entity.UserComplain;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserComplainRepository extends JpaRepository<UserComplain, Long> {
}
