package com.example.demo.service;

import jakarta.persistence.EntityNotFoundException;
import com.example.demo.config.jwt.JwtProvider;
import com.example.demo.dto.EditUserDto;
import com.example.demo.entity.Role;
import com.example.demo.entity.User;
import com.example.demo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;


@Service
public class UserService implements UserDetailsService {

    @Autowired
    private UserRepository repository;

    @Lazy
    @Autowired
    private PasswordEncoder encoder;

    @Lazy
    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtProvider provider;

    public boolean checkUserNameExists(String email) {
        return repository.findByEmail(email).isPresent();
    }

    public boolean createUser(User user) {
        user.setPassword(encoder.encode(user.getPassword()));
        user.setRole(Role.ROLE_USER);
        repository.save(user);
        return true;
    }

    public List<User> findUsersByRole(Role role) {
        return repository.searchUsersByRole(role);
    }


    public boolean verifyUser(String email, String password) {
        return repository.findByEmail(email).map(user -> encoder.matches(password, user.getPassword()))
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }

    public String generateToken(String email, String password) {
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(email, password));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        return provider.generateToken(authentication);
    }

    public String generateToken(User user){
        return provider.generateToken(user);
    }


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return repository.findByEmail(username).orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }

    public List<User> getAllUsers() {
        return repository.findAll();
    }

    public User getUserById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("User with not found"));
    }

    public User updateUser(Long id, EditUserDto userUpdateDetails) {
        User user = getUserById(id);
        if (user != null) {
            user.setFirstname(userUpdateDetails.getFirstname());
            user.setLastname(userUpdateDetails.getLastname());
            user.setEmail(userUpdateDetails.getEmail());
            if(userUpdateDetails.getPassword()!=null){
                user.setPassword(encoder.encode(userUpdateDetails.getPassword()));
            }
        return repository.save(user);
        } else {
            throw new EntityNotFoundException("User not found");
        }
}

    public boolean deleteUser(Long id) {
        User user = getUserById(id);
        if (user != null) {
            repository.delete(user);
            return true;
        }
        return false;
    }
}
