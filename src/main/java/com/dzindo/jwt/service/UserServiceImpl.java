package com.dzindo.jwt.service;

import com.dzindo.jwt.domain.AppUser;
import com.dzindo.jwt.domain.Role;
import com.dzindo.jwt.repository.RoleRepository;
import com.dzindo.jwt.repository.AppUserRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Service
@AllArgsConstructor
@Transactional
@Slf4j
public class UserServiceImpl implements UserService, UserDetailsService {

    private final AppUserRepository userRepository;
    private final RoleRepository roleRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @Override
    public AppUser saveUser(AppUser appUser) {
        log.info("Saving new user with name: {} in database.", appUser.getName());
        appUser.setPassword(bCryptPasswordEncoder.encode(appUser.getPassword()));
        return userRepository.save(appUser);
    }

    @Override
    public Role saveRole(Role role) {
        log.info("Saving new role with name: {} in database.", role.getName());

        return roleRepository.save(role);
    }

    @Override
    public void addRoleToUser(String username, String roleName) {
        log.info("Adding role with name: {} to user with username: {}.", roleName, username);

        AppUser appUser = userRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("User with username: " + username + " does not exists."));

        Role role = roleRepository.findByName(roleName)
                .orElseThrow(() -> new IllegalArgumentException("Role with name: " + roleName + "does not exists."));

        appUser.getRoles().add(role);
    }

    @Override
    public AppUser getUser(String username) {
        log.info("Fetching user with username: {} from database.", username);

        return userRepository.findByUsername(username)
               .orElseThrow(() -> new IllegalArgumentException("User with username: " + username + " does not exists."));

    }

    @Override
    public List<AppUser> getUsers() {
        log.info("Fetching all users from database.");
        return userRepository.findAll();
    }

    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        AppUser appUser = userRepository.findByUsername(s)
                .orElseThrow(() -> new UsernameNotFoundException("User not found."));

        Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();
        appUser.getRoles().forEach(
                role -> authorities.add(new SimpleGrantedAuthority(role.getName()))
        );
        return new User(appUser.getUsername(), appUser.getPassword(), authorities);
    }
}
