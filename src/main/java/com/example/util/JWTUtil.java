package com.example.util;

import com.example.dto.JwtDTO;
import com.example.enums.ProfileRole;
import io.jsonwebtoken.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import javax.crypto.spec.SecretKeySpec;
import java.util.Date;

public class JWTUtil {

    private static final int tokenLiveTime = 1000 * 3600 * 24; // 1-day
    private static final String secretKey = "mazgissddfskfekssssssssssssssssssssssssssssssssssssssssssssssssssssfkekekgekgkegkekerkgkegkrkgrkgkrgkrgkrkkrgkrkrkgnrgrgjrgkjrkjfdjekfekf";

    public static String encode(Integer profileId, ProfileRole role) {
        JwtBuilder jwtBuilder = Jwts.builder();
        jwtBuilder.issuedAt(new Date());

        SignatureAlgorithm sa = SignatureAlgorithm.HS512;
        SecretKeySpec secretKeySpec = new SecretKeySpec(secretKey.getBytes(), sa.getJcaName());

        jwtBuilder.signWith(secretKeySpec);

        jwtBuilder.claim("id", profileId);
        jwtBuilder.claim("role", role);

        jwtBuilder.expiration(new Date(System.currentTimeMillis() + (tokenLiveTime)));
        jwtBuilder.issuer("KunUzTest");
        return jwtBuilder.compact();
    }


    public static JwtDTO decode(String token) {
        SignatureAlgorithm sa = SignatureAlgorithm.HS512;
        SecretKeySpec secretKeySpec = new SecretKeySpec(secretKey.getBytes(), sa.getJcaName());
        JwtParser jwtParser = Jwts.parser()
                .verifyWith(secretKeySpec)
                .build();

        Jws<Claims> jws = jwtParser.parseSignedClaims(token);
        Claims claims = jws.getPayload();

        Integer id = (Integer) claims.get("id");
        String role = (String) claims.get("role");
        ProfileRole profileRole = ProfileRole.valueOf(role);

        return new JwtDTO(id, profileRole);
    }
    public static Boolean checkRole(String jwt) {
        JwtDTO jwtDTO = JWTUtil.decode(jwt);
        return jwtDTO.getRole().equals(ProfileRole.ADMIN);
    }
    public static Boolean checkRoleProfile(String jwt,Integer id) {
        JwtDTO jwtDTO = JWTUtil.decode(jwt);
        return jwtDTO.getRole().equals(ProfileRole.USER) && jwtDTO.getId().equals(id) ||
                jwtDTO.getRole().equals(ProfileRole.MODERATOR) && jwtDTO.getId().equals(id) ||
                jwtDTO.getRole().equals(ProfileRole.PUBLISHER) && jwtDTO.getId().equals(id);
    }


}
