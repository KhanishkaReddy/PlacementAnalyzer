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

import com.example.placementanalyzer.dto.ProjectDTO;
import com.example.placementanalyzer.model.Project;
import com.example.placementanalyzer.service.ProjectService;

@RestController
@RequestMapping("/api/projects")
@CrossOrigin(origins = "http://localhost:5173")
public class ProjectController {

    private final ProjectService projectService;

    public ProjectController(ProjectService projectService) {
        this.projectService = projectService;
    }

    @PostMapping
    public ResponseEntity<ProjectDTO> createProject(
            @RequestBody Project project) {

        ProjectDTO createdProject =
                projectService.createProject(project);

        return ResponseEntity.ok(createdProject);
    }

    @GetMapping("/student/{studentId}")
    public ResponseEntity<List<ProjectDTO>> getProjectsByStudentId(
            @PathVariable Long studentId) {

        List<ProjectDTO> projects =
                projectService.getProjectsByStudentId(studentId);

        return ResponseEntity.ok(projects);
    }
}