package com.example.UserService.Service;

import com.example.UserService.Exception.IncorrectPasswordException;
import com.example.UserService.Exception.UserAlreadyExistException;
import com.example.UserService.Exception.UserNotExistException;
import com.example.UserService.Models.Role;
import com.example.UserService.Models.Session;
import com.example.UserService.Models.State;
import com.example.UserService.Models.User;
import com.example.UserService.dtos.UserToken;
import com.example.UserService.repository.RoleRepository;
import com.example.UserService.repository.SessionRepository;
import com.example.UserService.repository.UserRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import javax.swing.text.html.Option;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class AuthService implements  IAuthService{

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private SessionRepository sessionRepository;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    private SecretKey secretKey;


    @Override
    public User signup(String name, String email, String password) throws UserAlreadyExistException {

        Optional<User> optionalUser = userRepository.findByEmail(email);

        if(optionalUser.isPresent()){
            throw new UserAlreadyExistException("User with email " + email + " already exists");
        }

        User user = new User();
        user.setUsername(name);
        user.setEmail(email);
        user.setPasswordHash(bCryptPasswordEncoder.encode(password));

        user.setState(State.ACTIVE);


        //I want to assign a default role to the user
        Optional<Role> optionalRole = roleRepository.findByValue("GUEST");
        Role roleToBeSet;

        if(optionalRole.isEmpty()){
            Role role = new Role();
            role.setValue("GUEST");
            roleRepository.save(role);
            roleToBeSet = role;
        } else{
            roleToBeSet = optionalRole.get();
        }

        user.setRoles(List.of(roleToBeSet));
        return userRepository.save(user);
    }

    @Override
    public UserToken login(String email, String password) {
        Optional<User> optionalUser = userRepository.findByEmail(email);
        if(optionalUser.isEmpty()){
            throw new UserNotExistException("User with email " + email + " does not exist");
        }

        User user = optionalUser.get();

        if(bCryptPasswordEncoder.matches(password, user.getPasswordHash())){
            /*
            Generate JWT token and send it in response header
             */

            Map<String,Object> payload = new HashMap<>();
            Long nowInMillis = System.currentTimeMillis(); // gets us timestamp in epoch
            payload.put("iat",nowInMillis);
            payload.put("exp",nowInMillis+100000);
            payload.put("userId",user.getId());
            payload.put("iss","scaler");
            payload.put("scope",user.getRoles());
            //Payload generated

            //MacAlgorithm macAlgorithm = Jwts.SIG.HS256;
            //SecretKey secretKey = macAlgorithm.key().build();
            String token = Jwts.builder().claims(payload).signWith(secretKey).compact();

            /*
            Store the session's info in the db (source of truth for all generated tokens)
             */
            Session session = new Session();
            session.setUser(user);
            session.setToken(token);
            session.setState(State.ACTIVE);

            sessionRepository.save(session);

            return new UserToken(user, token);
        } else {
            throw new IncorrectPasswordException("Incorrect password for user with email " + email);
        }

    }

    @Override
    public Boolean validateToken(String token){

        Optional<Session> optionalSession = sessionRepository.findByToken(token);
        if(optionalSession.isEmpty()){
            return false;
        }

        // Parsing: If the signature has been tampered with, the parser will throw an exception.
        //Environment variables (within Ms)
        //Config service -- shared across multiple services

        JwtParser jwtParser = Jwts.parser().verifyWith(secretKey).build();
        Claims claims = jwtParser.parseSignedClaims(token).getPayload(); //A.B.C

        /*
        Internally
        A(headers).B(payload).C(signature)

        signing the payload using the secret key passed in the parser
         */

        Long currentTimeInMills = System.currentTimeMillis();
        Long expiryTime = (Long) claims.get("exp");

        if(expiryTime < currentTimeInMills){
            //expired
            Session session = optionalSession.get();
            session.setState(State.INACTIVE);
            sessionRepository.save(session);
            return false;
        }else{
            return true;
        }
    }

    @Override
    public User getUserById(Long Id) {
        Optional<User> optionalUser = userRepository.findById(Id);
        if(optionalUser.isEmpty()) {
            throw new UserNotExistException("Id = " + Id);
        }
        return optionalUser.get();
    }
}
