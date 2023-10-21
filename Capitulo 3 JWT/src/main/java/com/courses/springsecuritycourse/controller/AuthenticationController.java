package com.courses.springsecuritycourse.controller;


import com.courses.springsecuritycourse.dto.AuthenticationRequest;
import com.courses.springsecuritycourse.dto.AuthenticationResponse;
import com.courses.springsecuritycourse.service.AuthenticationService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthenticationController {

    @Autowired
    private AuthenticationService authenticationService;

    @PostMapping("/login")
    public ResponseEntity<AuthenticationResponse> login(
            @RequestBody @Valid AuthenticationRequest authenticationRequest){

        AuthenticationResponse jwtDto = authenticationService.login(authenticationRequest);
        return  ResponseEntity.ok(jwtDto);
    }

    @GetMapping("/public-access")
    public String publicAccessEndpoint(){
        return "este endpoint es publico";
    }
}
