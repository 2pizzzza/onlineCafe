package com.cb.service;

import com.cb.dto.AdminDto;
import com.cb.model.Admin;
import com.cb.model.Role;
import com.cb.model.User;
import com.cb.repository.AdminRepository;
import com.cb.repository.RoleRepository;
import com.cb.util.TbConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Arrays;

@Service
public class AdminServicelmpl implements AdminService{

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AdminRepository adminRepository;

    @Override
    public void saveUser(AdminDto adminDto) {
        Role role = roleRepository.findByName(TbConstants.Roles.ADMIN);
        if (role == null)
            role = roleRepository.save(new Role(TbConstants.Roles.ADMIN));
        Admin admin = new Admin(adminDto.getName(), adminDto.getEmail(), passwordEncoder.encode(adminDto.getPassword()));
                Arrays.asList(role);
        adminRepository.save(admin);
    }

    @Override
    public Admin findUserByEmail(String email) {
        return adminRepository.findByEmail(email);
    }
}
