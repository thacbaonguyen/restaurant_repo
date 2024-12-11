package com.Cafe.CafeManagement.serviceImpl;

import com.Cafe.CafeManagement.DAO.UserRepository;
import com.Cafe.CafeManagement.POJO.User;
import com.Cafe.CafeManagement.constants.CafeConstants;
import com.Cafe.CafeManagement.service.UserService;
import com.Cafe.CafeManagement.utils.CafeResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Objects;
@Service
public class UserServiceImpl implements UserService {
    @Autowired
    UserRepository userRepository;
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
