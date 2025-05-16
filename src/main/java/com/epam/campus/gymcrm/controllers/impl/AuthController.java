package com.epam.campus.gymcrm.controllers.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.epam.campus.gymcrm.controllers.IAuthController;
import com.epam.campus.gymcrm.models.dtos.JwtResponse;
import com.epam.campus.gymcrm.models.dtos.LoginDto;
import com.epam.campus.gymcrm.security.BruteForceProtectionService;
import com.epam.campus.gymcrm.security.JwtBlacklistService;
import com.epam.campus.gymcrm.security.JwtUtil;

@RestController
@RequestMapping("/gym-crm")
public class AuthController implements IAuthController{

    private AuthenticationManager authenticationManager;
    private JwtUtil jwtUtil;
    private UserDetailsService userDetailsService;
    private JwtBlacklistService jwtBlacklistService;
    private BruteForceProtectionService bruteForceProtectionService;

    
    @Autowired
    public AuthController(AuthenticationManager authenticationManager, JwtUtil jwtUtil,
            UserDetailsService userDetailsService, JwtBlacklistService jwtBlacklistService,
            BruteForceProtectionService bruteForceProtectionService) {
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
        this.userDetailsService = userDetailsService;
        this.jwtBlacklistService = jwtBlacklistService;
        this.bruteForceProtectionService = bruteForceProtectionService;
    }

    @Override
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginDto loginDto) {
        String username = loginDto.getUsername();

        if (bruteForceProtectionService.isAccountLocked(username)) {
            return ResponseEntity
                    .status(HttpStatus.FORBIDDEN)
                    .body("Account locked due to too many failed login attempts.");
        }

        try {
            Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(username, loginDto.getPassword())
            );

            bruteForceProtectionService.registerSuccessfulAttempt(username);

            SecurityContextHolder.getContext().setAuthentication(authentication);

            UserDetails userDetails = userDetailsService.loadUserByUsername(username);
            String jwt = jwtUtil.generateToken(userDetails);
            String role = userDetails.getAuthorities().iterator().next().getAuthority();

            JwtResponse response = new JwtResponse(jwt, username, role);
            return ResponseEntity.ok(response);

        } catch (BadCredentialsException ex) {
            bruteForceProtectionService.registerFailedAttempt(username);
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid username or password.");
        }
    }

    @Override
    @PostMapping("/logout")
    public ResponseEntity<String> logout(@RequestHeader("Authorization") String token) {
        String jwt = token.substring(7); // Delete "Bearer " from the token
        jwtBlacklistService.blacklistToken(jwt); // Invelidate token
        return ResponseEntity.ok("Successfully logged out.");
    }
}