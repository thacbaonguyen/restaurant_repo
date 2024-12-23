package com.Res.Restaurant.serviceImpl;

import com.Res.Restaurant.DAO.UserRepository;
import com.Res.Restaurant.JWT.CustomUserDetailsService;
import com.Res.Restaurant.JWT.JwtFilter;
import com.Res.Restaurant.JWT.JwtUtils;
import com.Res.Restaurant.Entity.User;
import com.Res.Restaurant.constants.MessageConstants;
import com.Res.Restaurant.service.UserService;
import com.Res.Restaurant.utils.ApiResponse;
import com.Res.Restaurant.wrapper.UserWrapper;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
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

    @Autowired
    JwtFilter jwtFilter;

    @Autowired
    private ModelMapper modelMapper;
    @Override
    public ResponseEntity<String> signUp(Map<String, String> request) {
        try {
            if(validateUser(request)){
                User user = userRepository.findByEmail(request.get("email"));
                if(Objects.isNull(user)){
                    userRepository.save(mapUser(request));
                    return ApiResponse.getResponseEntity(MessageConstants.CREATE_SUCCESSFULLY, HttpStatus.OK);
                }
                return ApiResponse.getResponseEntity("This user does existing!", HttpStatus.CONFLICT);
            }
            return ApiResponse.getResponseEntity(MessageConstants.INVALID_DATA, HttpStatus.BAD_REQUEST);
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
                    return new ResponseEntity<>("{\"token\":\"" + token + "\"}", HttpStatus.OK);
                }
                return ApiResponse.getResponseEntity("This account is non active", HttpStatus.FORBIDDEN);
            }
            return ApiResponse.getResponseEntity("Permission", HttpStatus.FORBIDDEN);
        }
        catch (Exception ex){
            ex.printStackTrace();
        }
        return ApiResponse.getResponseEntity("Bad credentials", HttpStatus.BAD_REQUEST);
    }

    @Override
    public ResponseEntity<List<UserWrapper>> getAllUser() {
        try {
            if(jwtFilter.isAdmin()){
                List<User> users = userRepository.findAll();
                List<UserWrapper> userWrappers = new ArrayList<>();
                for(User item : users){
                    if(!item.getRole().equalsIgnoreCase("admin")) {
                        userWrappers.add(modelMapper.map(item, UserWrapper.class));
                    }
                }
                return new ResponseEntity<>(userWrappers, HttpStatus.OK);
            }
            else{
                return new ResponseEntity<>(new ArrayList<>(), HttpStatus.UNAUTHORIZED);
            }
        }
        catch (Exception ex){
            ex.printStackTrace();
        }
        return null;
    }

    @Override
    public ResponseEntity<String> changePassword(Map<String, String> request) {
        try {
            User user = userRepository.findByEmail(jwtFilter.getCurrentUser());
            if(!user.equals(null)){
                if(user.getPassword().equals(request.get("oldPassword"))){
                    user.setPassword(request.get("newPassword"));
                    userRepository.save(user);
                    return ApiResponse.getResponseEntity("Update password successfully", HttpStatus.OK);
                }
                return ApiResponse.getResponseEntity("Incorrect password", HttpStatus.BAD_REQUEST);
            }
            return ApiResponse.getResponseEntity(MessageConstants.SOME_THING_WENT_WRONG, HttpStatus.BAD_REQUEST);
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return ApiResponse.getResponseEntity(MessageConstants.SOME_THING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<String> forgotPassword(Map<String, String> request) {
        return ApiResponse.getResponseEntity("Send successfully, please check your email!", HttpStatus.OK);
    }

    @Override
    public ResponseEntity<String> checkToken() {
        return ApiResponse.getResponseEntity("check done", HttpStatus.OK);
    }

    private boolean validateUser(Map<String, String> request){
        return request.containsKey("name") && request.containsKey("email") && request.containsKey("contactNumber")
                && request.containsKey("password");
    }
    private User mapUser(Map<String, String> request){
        return User.builder().name(request.get("name"))
                .email(request.get("email"))
                .phoneNumber(request.get("contactNumber"))
                .password(request.get("password"))
                .role("user")
                .status("false")
                .build();
    }
}
