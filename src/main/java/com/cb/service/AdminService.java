package com.cb.service;

import com.cb.dto.AdminDto;
import com.cb.dto.UserDto;
import com.cb.model.Admin;
import com.cb.model.User;

public interface AdminService {
    void saveUser(AdminDto userDto);
    Admin findUserByEmail(String email);
}
