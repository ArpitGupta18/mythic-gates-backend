package com.arpit.mythicgates.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/tests")
public class TestController {
    @GetMapping("/user")
    @PreAuthorize("hasRole('USER')")
    public String worksForUser() {
        return "This route works for User Role";
    }

    @GetMapping("/admin")
    @PreAuthorize("hasRole('ADMIN')")
    public String worksForAdmin() {
        return "This route works for Admin Role";
    }

    @GetMapping("/both")
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public String worksForBoth() {
        return "This route works for User and Admin Role";
    }
}
