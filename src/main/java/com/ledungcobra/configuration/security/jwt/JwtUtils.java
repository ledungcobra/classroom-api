package com.ledungcobra.configuration.security.jwt;

import com.ledungcobra.configuration.security.userdetails.AppUserDetails;
import io.jsonwebtoken.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.HashMap;
import java.util.stream.Collectors;

@Component
@Slf4j
public class JwtUtils {

    // Đoạn JWT_SECRET này là bí mật, chỉ có phía server biết
    @Value("${spring.security.jwt.secret}")
    private String JWT_SECRET;


    @Value("${spring.security.jwt.expired-in-seconds}")
    private Integer jwtExpiredInSeconds;

    /**
     * Generate jwt token for user
     *
     * @param userDetails
     * @return
     */
    public String generateToken(AppUserDetails userDetails) {
        Date now = new Date();

        final String authorities = userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));

        Date expiryDate = new Date(now.getTime() + jwtExpiredInSeconds * 1000);

        var authorityClaims = new HashMap<String, Object>();
        authorityClaims.put("authorities", authorities);

        return Jwts.builder()
                .setSubject(Long.toString(userDetails.getUserId()))
                .setClaims(authorityClaims)
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .signWith(SignatureAlgorithm.HS512, JWT_SECRET)
                .compact();
    }

    /**
     * Get user information from token
     *
     * @param token
     * @return
     */
    public String getUserNameFromJwtToken(String token) {
        Claims claims = Jwts.parser()
                .setSigningKey(JWT_SECRET)
                .parseClaimsJws(token)
                .getBody();
        return claims.getSubject();
    }

    public boolean validateToken(String authToken) {
        try {
            Jwts.parser().setSigningKey(JWT_SECRET).parseClaimsJws(authToken);
            return true;
        } catch (MalformedJwtException ex) {
            log.error("Invalid JWT token");
        } catch (ExpiredJwtException ex) {
            log.error("Expired JWT token");
        } catch (UnsupportedJwtException ex) {
            log.error("Unsupported JWT token");
        } catch (IllegalArgumentException ex) {
            log.error("JWT claims string is empty.");
        }
        return false;
    }

    /**
     * Extract JWT from request
     *
     * @param request
     * @return
     */
    public String getJwtFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        // Kiểm tra xem header Authorization có chứa thông tin jwt không
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }

}