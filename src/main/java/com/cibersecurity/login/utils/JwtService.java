package com.cibersecurity.login.utils;

import com.cibersecurity.login.dto.TokenDTO;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;
import java.util.Date;

@Component
public class JwtService {

    @Value("${security.jwt.secret}")
    private String key;

    @Value("${security.jwt.issuer}")
    private String issuer;

    @Value("${security.jwt.expiration}")
    private long ttlMillis;

    public String createJwt(TokenDTO tokenDTO) {

        long nowMillis = System.currentTimeMillis();
        Date now = new Date(nowMillis);

        SecretKey key = new SecretKeySpec(DatatypeConverter.parseBase64Binary(this.key),
                "HmacSHA256");

        //  set the JWT Claims
        JwtBuilder builder = Jwts.builder()
                .issuer(issuer)
                .claim("username", tokenDTO.getUserName())
                .claim("admin", tokenDTO.isAdmin())
                .signWith(key, Jwts.SIG.HS256)
                .issuedAt(now);

        if (ttlMillis >= 0) {
            long expMillis = nowMillis + ttlMillis;
            Date exp = new Date(expMillis);
            builder.expiration(exp);
        }

        // Builds the JWT and serializes it to a compact, URL-safe string
        return builder.compact();
    }

    public TokenDTO getValue(String jwt) {
        SecretKey key = new SecretKeySpec(DatatypeConverter.parseBase64Binary(this.key),
                "HmacSHA256");
        Claims claims = Jwts.parser().verifyWith(key).build().parseSignedClaims(jwt).getPayload();
        TokenDTO tokenDTO = new TokenDTO();
        tokenDTO.setUserName(claims.get("username", String.class));
        tokenDTO.setAdmin(claims.get("admin", Boolean.class));
        tokenDTO.setValidTru(String.valueOf(claims.getExpiration().getTime()));
        return tokenDTO;
    }
}
