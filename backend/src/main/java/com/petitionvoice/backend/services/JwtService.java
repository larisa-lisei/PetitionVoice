package com.petitionvoice.backend.services;

import com.petitionvoice.backend.security.SecurityUser;
import com.petitionvoice.backend.model.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
public class JwtService {
    @Value("${security.jwt.secret-key}")
    private String secretKey;

    @Value("${security.jwt.expiration-time}")
    private long jwtExpiration;

    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    public String generateToken(UserDetails userDetails) {
        return generateToken(new HashMap<>(), userDetails);
    }

    public String generateToken(Map<String, Object> extraClaims, UserDetails userDetails) {
        String subject;

        if (userDetails instanceof SecurityUser) {
            SecurityUser securityUser = (SecurityUser) userDetails;
            subject = String.valueOf(securityUser.getId());
            User user = securityUser.getUserEntity();

            if (user != null) {
                extraClaims.put("id", user.getId());
                extraClaims.put("firstName", user.getFirst_name());
                extraClaims.put("lastName", user.getLast_name());
                extraClaims.put("email", securityUser.getUsername());
                if (user.getUserDetails() != null) {
                    extraClaims.put("telephone", user.getUserDetails().getTelephone());
                } else {
                    extraClaims.put("telephone", "");
                }
                if (user.getRole() != null) {
                    extraClaims.put("role", user.getRole().name());
                }

            }
        } else {
            subject = userDetails.getUsername();
        }

        return buildToken(extraClaims, subject, jwtExpiration);
    }

    public long getExpirationTime() {
        return jwtExpiration;
    }

    private String buildToken(
            Map<String, Object> extraClaims,
            String subject,
            long expiration
    ) {
        return Jwts
                .builder()
                .claims(extraClaims)
                .subject(subject)
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(getSignInKey(), Jwts.SIG.HS256)
                .compact();
    }

    public boolean isTokenValid(String token, UserDetails userDetails) {
        final String tokenSubject = extractUsername(token);
        String userIdFromDB;

        if (userDetails instanceof SecurityUser) {
            userIdFromDB = String.valueOf(((SecurityUser) userDetails).getId());
        } else {
            userIdFromDB = userDetails.getUsername();
        }

        return (tokenSubject.equals(userIdFromDB)) && !isTokenExpired(token);
    }

    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    private Claims extractAllClaims(String token) {
        return Jwts
                .parser()
                .verifyWith(getSignInKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    private SecretKey getSignInKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}