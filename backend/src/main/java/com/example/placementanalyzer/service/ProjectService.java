package com.example.placementanalyzer.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.example.placementanalyzer.dto.ProjectDTO;
import com.example.placementanalyzer.model.Project;
import com.example.placementanalyzer.repository.ProjectRepository;

@Service
public class ProjectService {

    private final ProjectRepository projectRepository;

    public ProjectService(ProjectRepository projectRepository) {
        this.projectRepository = projectRepository;
    }

    public ProjectDTO createProject(Project project) {

        Project savedProject = projectRepository.save(project);

        return convertToDTO(savedProject);
    }

    public List<ProjectDTO> getProjectsByStudentId(Long studentId) {

        return projectRepository.findByStudentId(studentId)
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    private ProjectDTO convertToDTO(Project project) {

        ProjectDTO dto = new ProjectDTO();

        dto.setId(project.getId());
        dto.setStudentId(project.getStudent().getId());
        dto.setTitle(project.getTitle());
        dto.setDescription(project.getDescription());
        dto.setTechnologies(project.getTechnologies());
        dto.setProjectUrl(project.getProjectUrl());

        return dto;
    }
}