package com.skapral.parrot.auth.common.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.AlgorithmMismatchException;
import com.auth0.jwt.exceptions.InvalidClaimException;
import com.auth0.jwt.exceptions.SignatureVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.Optional;

@Slf4j
public class JwtUtils {
    @Value("${jwt.secret}")
    private String jwtSecret;

    @Value("${jwt.expire}")
    private int jwtExpirationMs;

    public String generateJwtToken(String subject) {
        return JWT.create()
                .withSubject(subject)
                .withExpiresAt(new Date((new Date()).getTime() + jwtExpirationMs))
                .sign(Algorithm.HMAC256(jwtSecret));
    }

    public String getUserNameFromJwtToken(String token) {
        return JWT.decode(token).getSubject();
    }

    public boolean validateJwtToken(String authToken) {
        try {
            var verifier = JWT.require(Algorithm.HMAC256(jwtSecret)).build();
            verifier.verify(authToken);
            return true;
        } catch (SignatureVerificationException e) {
            log.error("Invalid JWT signature: {}", e.getMessage());
        } catch (AlgorithmMismatchException e) {
            log.error("JWT token's algorithm is unexpected: {}", e.getMessage());
        } catch (TokenExpiredException e) {
            log.error("JWT token is expired: {}", e.getMessage());
        } catch (InvalidClaimException e) {
            log.error("JWT token has invalid claim: {}", e.getMessage());
        }
        return false;
    }

    public Optional<String> parseJwt(HttpServletRequest request) {
        String headerAuth = request.getHeader("Authorization");
        if (StringUtils.hasText(headerAuth) && headerAuth.startsWith("Bearer ")) {
            return Optional.of(headerAuth.substring(7));
        }
        return Optional.empty();
    }
}
