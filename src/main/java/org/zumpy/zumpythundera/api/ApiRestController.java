package org.zumpy.zumpythundera.api;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequestMapping(value = "/api/v1", produces = MediaType.APPLICATION_JSON_VALUE)
public class ApiRestController {

    private HashMap<String, Object> createResponse(String message, List<String> userRoles, String requiredRole) {
        HashMap<String, Object> response = new HashMap<>();
        response.put("message", message);
        response.put("userRoles", userRoles);
        response.put("requiredRole", requiredRole);
        return response;
    }

    private HashMap<String, Object> createResponse(String message) {
        return createResponse(message, null, null);
    }


    @SuppressWarnings("unchecked")
    private List<String>  extractResourceRoles(Jwt jwt) {
        Map<String, Object> resourceAccess = jwt.getClaim("resource_access");
        if (resourceAccess == null) return Collections.emptyList();

        Set<GrantedAuthority> roles = new HashSet<>();
        resourceAccess.forEach((resourceName, value) -> {
            Map<String, Object> resource = (Map<String, Object>) value;

            if (resource != null && resource.containsKey("roles")) {
                Collection<String> resourceRoles = (Collection<String>) resource.get("roles");
                roles.addAll(resourceRoles
                        .stream()
                        .map(role -> new SimpleGrantedAuthority(String.format("ROLE_%s", role)))
                        .collect(Collectors.toSet()));
            }
        });

        log.debug("Resource roles: {}", roles);
        return roles.stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());
    }

    @GetMapping("/public")
    public HashMap<String, Object> publicEndpoint() {
        return createResponse("Hello, World!");
    }

    @GetMapping("/")
    @PreAuthorize("hasRole('CLIENT_USER')")
    public HashMap<String, Object> userEndpoint(@AuthenticationPrincipal Jwt jwt) {
        String username = jwt.getClaimAsString("preferred_username");
        List<String> userRoles = extractResourceRoles(jwt);
        return createResponse(String.format("Hello, %s!", username), userRoles, "CLIENT_USER");
    }

    @GetMapping("/admin")
    @PreAuthorize("hasRole('CLIENT_ADMIN')")
    public HashMap<String, Object> adminEndpoint(@AuthenticationPrincipal Jwt jwt) {
        String username = jwt.getClaimAsString("preferred_username");
        List<String> userRoles = extractResourceRoles(jwt);
        return createResponse(String.format("Hello, %s!", username), userRoles, "CLIENT_ADMIN");
    }
}
