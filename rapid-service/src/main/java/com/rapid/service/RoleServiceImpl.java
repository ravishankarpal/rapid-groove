package com.rapid.service;


import com.rapid.core.dto.RoleDto;
import com.rapid.core.entity.Role;
import com.rapid.dao.RoleRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class RoleServiceImpl implements RoleService {

   @Autowired
    RoleRepository roleRepository;


    @Override
    public void createRole(RoleDto roleDto) {
        Role role = new Role();
        role.setRoleName(roleDto.getRoleName());
        role.setRoleDescriptions(roleDto.getRoleDescription());
        roleRepository.saveAndFlush(role);
    }
}
