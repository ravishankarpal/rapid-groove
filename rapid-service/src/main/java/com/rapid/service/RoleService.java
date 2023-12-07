package com.rapid.service;


import com.rapid.core.dto.RoleDto;
import com.rapid.dao.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@SuppressWarnings("ALL")
public interface RoleService {

    @Autowired
    RoleRepository roleRepository = null;


    void createRole(com.rapid.core.dto.RoleDto roleDto);
}
