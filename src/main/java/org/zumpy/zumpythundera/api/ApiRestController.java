package org.zumpy.zumpythundera.api;

import lombok.extern.slf4j.Slf4j;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;


@Slf4j
@RestController
@RequestMapping("/api/v1")
public class ApiRestController {

    @GetMapping("/")
    @PreAuthorize("hasRole('CLIENT_USER')")
    public String index(@AuthenticationPrincipal Jwt jwt) {
        return String.format("Hello, %s!", jwt.getClaimAsString("preferred_username"));
    }

    @GetMapping("/admin")
    @PreAuthorize("hasRole('CLIENT_ADMIN')")
    public String admin(@AuthenticationPrincipal Jwt jwt) {
        return String.format("Hello, %s!", jwt.getClaimAsString("preferred_username"));
    }
}
