package com.ledungcobra.controller;

import com.ledungcobra.dto.index.TestDto;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class IndexController {

    @GetMapping({"/",""})
    @ApiOperation("Test")
    public TestDto test() {
        return TestDto.builder()
                .name("Test")
                .title("OKE")
                .build();
    }
}
