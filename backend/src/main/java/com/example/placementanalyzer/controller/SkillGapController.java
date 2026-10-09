package com.example.placementanalyzer.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.placementanalyzer.dto.SkillGapDTO;
import com.example.placementanalyzer.service.SkillGapService;

@RestController
@RequestMapping("/api/skill-gap")
@CrossOrigin(origins = "http://localhost:5173")
public class SkillGapController {

    private final SkillGapService skillGapService;

    public SkillGapController(SkillGapService skillGapService) {
        this.skillGapService = skillGapService;
    }

    @GetMapping("/student/{studentId}/job-role/{jobRoleId}")
    public ResponseEntity<List<SkillGapDTO>> analyzeSkillGap(
            @PathVariable Long studentId,
            @PathVariable Long jobRoleId) {

        List<SkillGapDTO> result =
                skillGapService.analyzeSkillGap(
                        studentId,
                        jobRoleId
                );

        return ResponseEntity.ok(result);
    }
}