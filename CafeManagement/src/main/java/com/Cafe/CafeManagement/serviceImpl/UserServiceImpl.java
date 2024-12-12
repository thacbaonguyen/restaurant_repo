package com.Cafe.CafeManagement.serviceImpl;

import com.Cafe.CafeManagement.DAO.UserRepository;
import com.Cafe.CafeManagement.JWT.CustomUserDetailsService;
import com.Cafe.CafeManagement.JWT.JwtUtils;
import com.Cafe.CafeManagement.POJO.User;
import com.Cafe.CafeManagement.constants.CafeConstants;
import com.Cafe.CafeManagement.service.UserService;
import com.Cafe.CafeManagement.utils.CafeResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Objects;
@Service
public class UserServiceImpl implements UserService {
    @Autowired
    UserRepository userRepository;
    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    CustomUserDetailsService customUserDetailsService;
    @Autowired
    JwtUtils jwtUtils;
    @Override
    public ResponseEntity<String> signUp(Map<String, String> request) {
        try {
            if(validateUser(request)){
                User user = userRepository.findByEmail(request.get("email"));
                if(Objects.isNull(user)){
                    userRepository.save(mapUser(request));
                    return CafeResponse.getResponseEntity(CafeConstants.CREATE_SUCCESSFULLY, HttpStatus.OK);

                }
                return CafeResponse.getResponseEntity(CafeConstants.EXISTING, HttpStatus.BAD_REQUEST);
            }
            return CafeResponse.getResponseEntity(CafeConstants.INVALID_DATA, HttpStatus.BAD_REQUEST);
        }
        catch (Exception ex){
            ex.printStackTrace();
        }
        return null;
    }

    @Override
    public ResponseEntity<String> login(Map<String, String> request) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.get("email"), request.get("password"))
            );
            if(authentication.isAuthenticated()){
                if(customUserDetailsService.getUserDetails().getStatus().equalsIgnoreCase("true")){
                    String userName = customUserDetailsService.getUserDetails().getEmail();
                    String role = customUserDetailsService.getUserDetails().getRole();
                    String token = jwtUtils.generateToken(userName, role);
                    return new ResponseEntity<>("token:" + token, HttpStatus.OK);
                }
            }
            return new ResponseEntity<>("Permission", HttpStatus.FORBIDDEN);
        }
        catch (Exception ex){
            ex.printStackTrace();
        }
        return new ResponseEntity<>("Bad credentials", HttpStatus.BAD_REQUEST);
    }

    private boolean validateUser(Map<String, String> request){
        return request.containsKey("name") && request.containsKey("email") && request.containsKey("phoneNumber")
                && request.containsKey("password");
    }
    private User mapUser(Map<String, String> request){
        return User.builder().name(request.get("name"))
                .email(request.get("email"))
                .phoneNumber(request.get("phoneNumber"))
                .password(request.get("password"))
                .role("user")
                .status("false")
                .build();
    }
}
