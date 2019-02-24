package com.example.restsecurity.controller;

import com.example.restsecurity.forms.RegisterForm;
import com.example.restsecurity.model.Role;
import com.example.restsecurity.model.RoleName;
import com.example.restsecurity.model.User;
import com.example.restsecurity.repository.RoleRepository;
import com.example.restsecurity.repository.UserRepository;
import com.example.restsecurity.service.RegistrationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("api/auth/")
public class RegistrationController {

    @Autowired
    private RegistrationService registrationService;


    //return userdto or is string ok?
    @RequestMapping(value = "register", method = RequestMethod.POST)
    public ResponseEntity<String> register(@Valid @RequestBody RegisterForm registerRequest) {
        registrationService.registerUser(registerRequest);

        return new ResponseEntity<>("User registered successfully!", HttpStatus.OK);
    }
}
