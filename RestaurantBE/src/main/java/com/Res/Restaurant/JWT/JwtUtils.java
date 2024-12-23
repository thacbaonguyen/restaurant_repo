package com.Res.Restaurant.JWT;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;

@Service
public class JwtUtils {
    private final String secretKey = "baobaobao3";

    public String generateToken(String userName, String role){
        Map<String, Object> claims = new HashMap<>();
        claims.put("role", role);
        return createToken(claims, userName);
    }
    private String createToken(Map<String, Object> claims, String subject){
        return Jwts.builder().setClaims(claims)
                .setSubject(subject)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 *  24))
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();
    }
    public Claims extractAllClaims(String token){
        return Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody();
    }
    public <T> T extractClaim(String token, Function<Claims, T> claimResolver){
        Claims claims = extractAllClaims(token);
        return claimResolver.apply(claims);
    }
    public String extractUsername(String token){
        return extractClaim(token, claims -> claims.getSubject());
    }
    public Date extractExpiration(String token){
        return extractClaim(token, claims -> claims.getExpiration());
    }
    private boolean isExpired(String token){
        return extractExpiration(token).before(new Date());
    }
    private boolean correctUsername(String token, UserDetails userDetails){
        return extractUsername(token).equals(userDetails.getUsername());
    }
    public boolean validateToken(String token, UserDetails userDetails){
        return correctUsername(token, userDetails) && !isExpired(token);
    }
}
