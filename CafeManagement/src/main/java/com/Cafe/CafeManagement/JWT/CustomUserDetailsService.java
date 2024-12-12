package com.Cafe.CafeManagement.JWT;

import com.Cafe.CafeManagement.DAO.UserRepository;
import com.Cafe.CafeManagement.POJO.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Objects;
@Service
public class CustomUserDetailsService implements UserDetailsService {
    @Autowired
    UserRepository userRepository;
    private User user;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        user = userRepository.findByEmail(username);
        if(!Objects.isNull(user)){
            return new org.springframework.security.core.userdetails.User(user.getEmail(), user.getPassword(), new ArrayList<>());
        }
        else{
            throw new UsernameNotFoundException("Not found user!");
        }
    }
    public User getUserDetails(){
        return this.user;
    }
}
