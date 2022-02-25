package com.ledungcobra.configuration.security.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ledungcobra.common.EStatus;
import com.ledungcobra.common.CommonResponse;
import com.ledungcobra.common.EResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
@Slf4j
public class JwtAuthenticationEntryPointHandler implements AuthenticationEntryPoint {

    @Autowired
    private ObjectMapper mapper;

    @Override
    public void commence(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, AuthenticationException e) throws IOException, ServletException {
        log.error("Unauthorized error: {}", e.getMessage());
        httpServletResponse.setHeader("Content-Type", "application/json");
        httpServletResponse.setStatus(403);
        var url = httpServletRequest.getHeader("Referer");
        httpServletResponse.getWriter().write(mapper.writeValueAsString(CommonResponse.builder()
                .message("Unauthorized")
                .status(EStatus.Success)
                .result(EResult.Successful)
                .content(url + " được bảo vệ bạn không có quyền truy cập")
                .build()));
    }
}
