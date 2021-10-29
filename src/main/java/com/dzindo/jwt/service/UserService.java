package com.dzindo.jwt.service;

import com.dzindo.jwt.domain.Role;
import com.dzindo.jwt.domain.AppUser;

import java.util.List;

public interface UserService {
    AppUser saveUser(AppUser appUser);
    Role saveRole(Role role);
    void addRoleToUser(String username, String roleName);
    AppUser getUser(String username);
    List<AppUser> getUsers();
}
