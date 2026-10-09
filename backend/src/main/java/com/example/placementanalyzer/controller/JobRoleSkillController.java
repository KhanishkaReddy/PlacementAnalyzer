package com.example.placementanalyzer.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.placementanalyzer.dto.JobRoleSkillDTO;
import com.example.placementanalyzer.model.JobRoleSkill;
import com.example.placementanalyzer.service.JobRoleSkillService;

@RestController
@RequestMapping("/api/job-role-skills")
@CrossOrigin(origins = "http://localhost:5173")
public class JobRoleSkillController {

    private final JobRoleSkillService jobRoleSkillService;

    public JobRoleSkillController(JobRoleSkillService jobRoleSkillService) {
        this.jobRoleSkillService = jobRoleSkillService;
    }

    @PostMapping
    public ResponseEntity<JobRoleSkillDTO> createJobRoleSkill(
            @RequestBody JobRoleSkill jobRoleSkill) {

        JobRoleSkillDTO createdJobRoleSkill =
                jobRoleSkillService.createJobRoleSkill(jobRoleSkill);

        return ResponseEntity.ok(createdJobRoleSkill);
    }

    @GetMapping("/job-role/{jobRoleId}")
    public ResponseEntity<List<JobRoleSkillDTO>> getSkillsByJobRoleId(
            @PathVariable Long jobRoleId) {

        List<JobRoleSkillDTO> skills =
                jobRoleSkillService.getSkillsByJobRoleId(jobRoleId);

        return ResponseEntity.ok(skills);
    }
}