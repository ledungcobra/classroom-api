package com.ledungcobra.configuration.security.jwt;

import com.ledungcobra.configuration.security.userdetails.AppUserDetails;
import com.ledungcobra.user.service.UserService;
import io.jsonwebtoken.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;

@Component
@Slf4j
public class JwtUtils {

    // Đoạn JWT_SECRET này là bí mật, chỉ có phía server biết
    @Value("${spring.security.jwt.secret}")
    private String JWT_SECRET;


    @Value("${spring.security.jwt.expired-in-seconds}")
    private Integer jwtTokenExpiredInSeconds;

    @Value("${spring.security.jwt.refresh-token-expired-in-seconds}")
    private Integer jwtRefreshTokenExpiredInSeconds;

    private  UserService userService;

    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    /**
     * Generate jwt token for user
     *
     * @param userDetails
     * @return
     */
    public String generateToken(AppUserDetails userDetails) {

        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + jwtTokenExpiredInSeconds * 1000);
        log.info("Expired time {}", expiryDate);

        return Jwts.builder()
                .setSubject(userDetails.getUsername())
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .signWith(SignatureAlgorithm.HS512, JWT_SECRET)
                .compact();
    }




    public String generateRefreshToken(AppUserDetails userDetails) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + jwtRefreshTokenExpiredInSeconds * 1000);
        return Jwts.builder()
                .setSubject(userDetails.getUsername())
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

    /**
     * Call this method when request come to controller
     * Authorization header from request is validated by @see {@link JwtAuthenticationFilter#doFilterInternal(HttpServletRequest, HttpServletResponse, FilterChain)}
     *
     * @param request
     * @return
     */
    public String getUserNameFromRequest(HttpServletRequest request) {
        var token = getJwtFromRequest(request);
        return getUserNameFromJwtToken(token);
    }

    public HttpHeaders buildAuthorizationHeader(String username) {
        var headers = new HttpHeaders();
        if (username == null) return headers;
        var user = userService.findByUsername(username);
        String token = generateToken(new AppUserDetails(user));
        if (token == null) return headers;
        headers.add("Authorization", String.format("Bearer %s", token));
        return headers;
    }

}