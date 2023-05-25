package com.cb.repository;

import com.cb.model.Admin;
import com.cb.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AdminRepository extends JpaRepository<Admin, Long> {
    Admin findByEmail(String email);
    Optional<Admin> findByName(String name);
}
