package com.example.restsecurity.service;

import com.example.restsecurity.forms.RegisterForm;
import com.example.restsecurity.model.Role;
import com.example.restsecurity.model.RoleName;
import com.example.restsecurity.model.User;
import com.example.restsecurity.repository.RoleRepository;
import com.example.restsecurity.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.HashSet;
import java.util.Set;

@Service
public class RegistrationService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder encoder;

    public User registerUser(@RequestBody RegisterForm registerRequest) {
        if (isUsernameAlreadyRegistered(registerRequest)) {
            usernameExists(registerRequest);
        }

        if (isEmailAlreadyRegistered(registerRequest)) {
            emailExists(registerRequest);
        }

        User user = createUserAccount(registerRequest);
        Set<Role> roles = setupRoles(registerRequest);

        user.setRoles(roles);
        return userRepository.save(user);
    }

    private ResponseEntity<String> usernameExists(@RequestBody RegisterForm registerRequest) {
        return new ResponseEntity<>("Username is already taken!", HttpStatus.BAD_REQUEST);
    }

    private boolean isUsernameAlreadyRegistered(@RequestBody RegisterForm registerRequest) {
        return userRepository.existsByUsername(registerRequest.getUsername());
    }

    private ResponseEntity<String> emailExists(@RequestBody RegisterForm registerRequest) {
        return new ResponseEntity<>("Fail -> Email is already in use!", HttpStatus.BAD_REQUEST);
    }

    private boolean isEmailAlreadyRegistered(@RequestBody RegisterForm registerRequest) {
        return userRepository.existsByEmail(registerRequest.getEmail());
    }

    private User createUserAccount(@RequestBody RegisterForm registerRequest) {
        return new User(
                registerRequest.getName(),
                registerRequest.getUsername(),
                registerRequest.getEmail(),
                encoder.encode(registerRequest.getPassword())
        );
    }

    private Set<Role> setupRoles(@RequestBody RegisterForm registerRequest) {
        Set<String> rolesFromRequest = registerRequest.getRoles();
        Set<Role> roles = new HashSet<>();

        rolesFromRequest.forEach(role -> {
            switch (role) {
                case "admin":
                    Role adminRole = roleRepository.findByName(RoleName.ROLE_ADMIN)
                            .orElseThrow(() -> new RuntimeException("Fail! -> Cause: User Role not find."));
                    roles.add(adminRole);

                    break;
                case "pm":
                    Role pmRole = roleRepository.findByName(RoleName.ROLE_PM)
                            .orElseThrow(() -> new RuntimeException("Fail! -> Cause: User Role not find."));
                    roles.add(pmRole);

                    break;
                default:
                    Role userRole = roleRepository.findByName(RoleName.ROLE_USER)
                            .orElseThrow(() -> new RuntimeException("Fail! -> Cause: User Role not find."));
                    roles.add(userRole);
            }
        });

        return roles;
    }
}
