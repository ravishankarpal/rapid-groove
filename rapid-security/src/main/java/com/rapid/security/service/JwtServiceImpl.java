package com.rapid.security.service;
import com.rapid.core.dto.JwtRequest;
import com.rapid.core.dto.JwtResponse;
import com.rapid.core.entity.User;
import com.rapid.dao.UserRepository;
import com.rapid.security.JwtTokenDetails;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Service
@Slf4j
public class JwtServiceImpl implements UserDetailsService,JwtService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtTokenDetails jwtTokenDetails;

    @Autowired
    private AuthenticationManager authenticationManager;

    private static final String TAG = "JwtServiceImpl";
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> userOptional = userRepository.findById(username);
        if (userOptional.isPresent()){
            User user = userOptional.get();
            return new org.springframework.security.core.userdetails.User(
                    user.getUserName(),user.getUserPassword(),
                    getAuthorities(user)
            );
        }else {
            log.error("Invalid username");
            throw new UsernameNotFoundException("Invalid username!");
        }

    }

    @Override
    public JwtResponse createJwtToken(JwtRequest jwtRequest) throws Exception {
        String userName = jwtRequest.getUserName();
        String userPassword = jwtRequest.getUserPassword();
        authenticate(userName,userPassword);
        final  UserDetails userDetails = loadUserByUsername(userName);
        String generatedToken  = jwtTokenDetails.generateJwtToken(userDetails);
        Optional<User> userOptional  = userRepository.findById(userName);
        return userOptional.map(user -> new JwtResponse(user, generatedToken)).
                orElse(null);

    }

    private void authenticate(String userName, String userPassword) throws  Exception{
        try {
            authenticationManager.authenticate(new
                    UsernamePasswordAuthenticationToken(userName, userPassword));
        }
        catch (DisabledException e){
            log.error("User has disabled");
            throw new Exception("User has disabled");
        }
        catch (BadCredentialsException e){
            log.error("Bad credentials!");
            throw  new Exception("Bad credentials!");
        }

    }

    private Set getAuthorities(User user){
        Set  authorities  = new HashSet();
        user.getRole().forEach(role -> {
            authorities.add(new SimpleGrantedAuthority("ROLE_"+role.getRoleName()));
        });

        return  authorities;
    }

}
