package com.example.placementanalyzer.controller;

import java.util.Optional;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.placementanalyzer.dto.JobRoleDTO;
import com.example.placementanalyzer.model.JobRole;
import com.example.placementanalyzer.service.JobRoleService;

@RestController
@RequestMapping("/api/job-roles")
@CrossOrigin(origins = "http://localhost:5173")
public class JobRoleController {

    private final JobRoleService jobRoleService;

    public JobRoleController(JobRoleService jobRoleService) {
        this.jobRoleService = jobRoleService;
    }

    @PostMapping
    public ResponseEntity<JobRoleDTO> createJobRole(
            @RequestBody JobRole jobRole) {

        JobRoleDTO createdJobRole =
                jobRoleService.createJobRole(jobRole);

        return ResponseEntity.ok(createdJobRole);
    }

    @GetMapping("/{id}")
    public ResponseEntity<JobRoleDTO> getJobRoleById(
            @PathVariable Long id) {

        Optional<JobRoleDTO> jobRole =
                jobRoleService.getJobRoleById(id);

        return jobRole
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/name/{name}")
    public ResponseEntity<JobRoleDTO> getJobRoleByName(
            @PathVariable String name) {

        Optional<JobRoleDTO> jobRole =
                jobRoleService.getJobRoleByName(name);

        return jobRole
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }
}