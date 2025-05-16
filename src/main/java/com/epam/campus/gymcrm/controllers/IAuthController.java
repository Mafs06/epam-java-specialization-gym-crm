package com.epam.campus.gymcrm.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

import com.epam.campus.gymcrm.models.dtos.JwtResponse;
import com.epam.campus.gymcrm.models.dtos.LoginDto;

public interface IAuthController {

    @Operation(summary = "Authenticate user and generate JWT")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Login successful",
            content = @Content(schema = @Schema(implementation = JwtResponse.class))),
        @ApiResponse(responseCode = "403", description = "Account locked due to too many failed login attempts",
            content = @Content(mediaType = "text/plain")),
        @ApiResponse(responseCode = "401", description = "Invalid username or password",
            content = @Content(mediaType = "text/plain")),
        @ApiResponse(responseCode = "500", description = "Internal server error",
            content = @Content)
    })
    ResponseEntity<?> login(@RequestBody LoginDto loginDto);

    @Operation(summary = "Invalidate the JWT and log the user out", security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Logout successful",
            content = @Content(mediaType = "text/plain")),
        @ApiResponse(responseCode = "401", description = "Invalid or missing token",
            content = @Content(mediaType = "text/plain")),
        @ApiResponse(responseCode = "500", description = "Internal server error",
            content = @Content)
    })
    ResponseEntity<String> logout(@RequestHeader("Authorization") String token);
}