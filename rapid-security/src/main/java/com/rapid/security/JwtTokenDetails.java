package com.rapid.security;

import com.rapid.dao.UserRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import static com.rapid.core.dto.Constant.TOKEN_VALIDITY;

@Component
public class JwtTokenDetails {

    @Autowired
    private UserRepository userRepository;

    // need to configure secret key
    public static final String SECRET_KEY = "CKsjknkdsKJKHSmcmxncj";
    public String getUserDetailFromToken(String token){
        return  getClaimFromToken(token, Claims::getSubject);
    }

    private <T> T getClaimFromToken(String token , Function<Claims, T> claimResolver){
        final Claims claims = getAllClaimFromToken(token);
        return  claimResolver.apply(claims);

    }

    private Claims getAllClaimFromToken(String token){
        return Jwts.parser().setSigningKey(SECRET_KEY)
                .parseClaimsJws(token).getBody();
    }

    public boolean tokenValidate(String token, UserDetails userDetails){
        String userName  = getUserDetailFromToken(token);
        return  (userName.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }

    private boolean isTokenExpired(String token){

       final Date expirationDate =  getExpirationDateFromToken(token);
       return  expirationDate.before(new Date());


    }

    private Date getExpirationDateFromToken(String token){
        return getClaimFromToken(token, Claims::getExpiration);
    }

    public String generateJwtToken(UserDetails userDetails){
        Map<String, Object> claims = new HashMap<>();
        return Jwts.builder().setClaims(claims)
                .setSubject(userDetails.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis()+ TOKEN_VALIDITY * 1000))
                .signWith(SignatureAlgorithm.HS512,SECRET_KEY)
                .compact();

    }

//    public String extractUserRole(String jwtToken) {
//        Jws<Claims> claimsJws = Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(jwtToken);
//        Claims body = claimsJws.getBody();
//        String userName = body.get("sub", String.class);
//        return userRepository.getUserRole(userName);
//    }

    public  String extractUserRole(){
        String currentUser = JwtRequestFilter.CURRENT_USER;
        return userRepository.getUserRole(currentUser);
    }



}
