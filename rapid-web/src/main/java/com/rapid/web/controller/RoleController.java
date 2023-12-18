package com.rapid.web.controller;

import com.rapid.core.dto.RoleDto;
import com.rapid.core.entity.Role;
import com.rapid.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.lang.annotation.Retention;
import java.net.http.HttpResponse;

@RestController
@RequestMapping("/rapid/role")

public class RoleController {

    @Autowired
    RoleService roleService;

   // @RequestMapping(value ="/create_role" , method = RequestMethod.POST)

    @PostMapping(value = "/create_role")
    public ResponseEntity<RoleDto>  createRole(@RequestBody RoleDto roleDto){
          roleService.createRole(roleDto);
        return  new ResponseEntity<>(HttpStatus.CREATED);

    }

}
