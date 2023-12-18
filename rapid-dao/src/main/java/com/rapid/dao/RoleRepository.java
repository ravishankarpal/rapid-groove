package com.rapid.dao;


import com.rapid.core.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.nio.ByteBuffer;

@Repository
public interface RoleRepository extends JpaRepository<Role, String> {

}
