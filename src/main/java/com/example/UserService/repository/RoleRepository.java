package com.example.UserService.repository;

import com.example.UserService.Models.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Long> {
    public Optional<Role> findByValue(String aDefault);
}
