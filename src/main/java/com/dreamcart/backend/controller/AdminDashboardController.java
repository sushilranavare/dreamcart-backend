/*
* This controller provides administrator dashboard statistics.
* Only user with the ADMIN role can access these endpoints.
* */

package com.dreamcart.backend.controller;

import com.dreamcart.backend.dto.response.AdminDashboardResponse;
import com.dreamcart.backend.service.AdminDashboardService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web .bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.security.access.prepost.PreAuthorize;

@RestController
@RequestMapping("/api/admin/dashboard")
public class AdminDashboardController {

    private final AdminDashboardService adminDashboardService;

    public AdminDashboardController( AdminDashboardService adminDashboardService) {
        this.adminDashboardService = adminDashboardService;
    }
    /* Returns statistics for admin dashboard */
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public AdminDashboardResponse getDashboardStatistics(){
        return adminDashboardService.getDashboardStatistics();
    }
}
