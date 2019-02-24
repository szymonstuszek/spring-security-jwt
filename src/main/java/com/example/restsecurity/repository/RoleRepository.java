package com.example.restsecurity.repository;

import com.example.restsecurity.model.Role;
import com.example.restsecurity.model.RoleName;
import com.example.restsecurity.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Long> {

    Optional<Role> findByName(RoleName name);
}
