package com.example.UserService.Controller;

import com.example.UserService.Models.User;
import com.example.UserService.Service.IAuthService;
import com.example.UserService.dtos.LoginRequestDTO;
import com.example.UserService.dtos.SignupRequestDTO;
import com.example.UserService.dtos.UserDTO;
import com.example.UserService.dtos.UserToken;
import jakarta.persistence.GeneratedValue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private IAuthService authService;

    @GetMapping
    public String get(){
        return "ggdthetg";
    }

    @PostMapping("/signup")
    public ResponseEntity<UserDTO> signup(@RequestBody SignupRequestDTO requestDTO){
        System.out.println("hdg");
        User user = authService.signup(requestDTO.getUserName(), requestDTO.getEmail(), requestDTO.getPassword());

        return new ResponseEntity<>(user.convertToUserDTO(), HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<UserDTO> login(@RequestBody LoginRequestDTO requestDTO){
        UserToken userToken = authService.login(requestDTO.getEmail(), requestDTO.getPassword());

        HttpHeaders headers = new HttpHeaders();

        headers.add(HttpHeaders.SET_COOKIE,
                "token=" + userToken.getToken() + "; Path=/; HttpOnly");

        return new ResponseEntity<>(userToken.getUser().convertToUserDTO(), headers, HttpStatus.OK);

    }

    @PostMapping("/validateToken")
    public String validateToken(@RequestHeader("Authorization") String authHeader){

        return authHeader;
    }

    @GetMapping("/user/{Id}")
    public UserDTO getUserById(@PathVariable Long Id){
        return authService.getUserById(Id).convertToUserDTO();
    }

}
