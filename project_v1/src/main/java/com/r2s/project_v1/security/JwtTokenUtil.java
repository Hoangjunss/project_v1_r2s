package com.r2s.project_v1.security;

import com.r2s.project_v1.exception.CustomJwtException;
import com.r2s.project_v1.exception.Error;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.security.Key;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.function.Function;

@Component
@Slf4j
public class JwtTokenUtil {

    private final SecretKey secretKeyForAccessToken ;
    private final SecretKey secretKeyForRefreshToken = Keys.secretKeyFor(SignatureAlgorithm.HS256);

//    private  static  final long EXPIRATION_TIME_FOR_TOKEN = 604_800_000; //1 Day
//    private  static  final long EXPIRATION_TIME_FOR_REFRSH_TOKEN = 604_800_000; //1 Day

    private static final long EXPIRATION_TIME_FOR_TOKEN = 2_592_000_000L; // 1 Month (30 Days)
    private static final long EXPIRATION_TIME_FOR_REFRSH_TOKEN = 2_592_000_000L; // 1 Month (30 Days)


    public JwtTokenUtil(){
        //Khởi tạo Secret key
        String secreteString = "843567893696976453275974432697R634976R738467TR678T34865R6834R8763T478378637664538745673865783678548735687R3";
        byte[] keyBytes = Base64.getDecoder().decode(secreteString.getBytes(StandardCharsets.UTF_8));
        this.secretKeyForAccessToken = new SecretKeySpec(keyBytes, "HmacSHA256");
    }

    //Tạo Token
    public String generateToken(UserDetails userDetails){
        return Jwts.builder()
                .subject(userDetails.getUsername())
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME_FOR_TOKEN))
                .signWith(secretKeyForAccessToken)
                .compact();
    }

    // Tạo refresh Token
    public String generateRefreshToken( UserDetails userDetails){
        return Jwts.builder()
                .subject(userDetails.getUsername())
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME_FOR_REFRSH_TOKEN))
                .signWith(secretKeyForRefreshToken)
                .compact();
    }


    // TODO: Các phương thức có sẵn

    //Tách email ra từ JWT Token
    public String extractUsernameAccessToken(String token){
        return extractClaims(true, token, Claims::getSubject);
    }


    //Tách email ra từ JWT Token
    public String extractUsernameRefreshToken(String token){
        return extractClaims(false, token, Claims::getSubject);
    }

    private <T> T extractClaims(Boolean isAccessToken, String token, Function<Claims, T> claimsTFunction){

        if (isAccessToken){
            return claimsTFunction.apply(
                    Jwts.parser().verifyWith(secretKeyForAccessToken).build().parseSignedClaims(token).getPayload()
            );
        }

        return claimsTFunction.apply(
                Jwts.parser().verifyWith(secretKeyForRefreshToken).build().parseSignedClaims(token).getPayload()
        );

    }
    public boolean isTokenValid(String token, UserDetails userDetails) {
        final String username = extractUsernameAccessToken(token);
        if (!username.equals(userDetails.getUsername())) {
            throw new CustomJwtException(Error.USER_NOT_FOUND);
        }
        if (isTokenExpired(true,token)) {
            throw new CustomJwtException(Error.JWT_EXPIRED);
        }
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(true,token));
    }

    public boolean isRefreshValid(String token, UserDetails userDetails) {
        final String username = extractUsernameRefreshToken(token);
        if (!username.equals(userDetails.getUsername())) {
            throw new CustomJwtException(Error.USER_NOT_FOUND);
        }
        if (!isTokenExpired(false,token)) {
            throw new CustomJwtException(Error.JWT_EXPIRED);
        }
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(false,token));
    }

    public boolean isTokenExpired(Boolean isAccessToken, String token) {
        return extractClaims(isAccessToken, token, Claims::getExpiration).before(new Date());
    }



}