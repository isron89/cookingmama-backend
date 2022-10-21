package com.cookingmama.cookingmamabackend.controllers;

import com.cookingmama.cookingmamabackend.models.ERole;
import com.cookingmama.cookingmamabackend.models.Role;
import com.cookingmama.cookingmamabackend.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.web.bind.annotation.RestController;

import javax.persistence.PrePersist;

@RestController
public class RoleController {

    @Autowired
    RoleRepository roleRepository;

    @EventListener(ApplicationReadyEvent.class)
    public void initRole(){
        if(roleRepository.findAll().isEmpty()){
            Role role1 = new Role();
            role1.setName(ERole.ROLE_USER);

            Role role2 = new Role();
            role2.setName(ERole.ROLE_ADMIN);

            Role role3 = new Role();
            role3.setName(ERole.ROLE_MODERATOR);

            roleRepository.save(role1);
            roleRepository.save(role2);
            roleRepository.save(role3);
        }
    }
}
