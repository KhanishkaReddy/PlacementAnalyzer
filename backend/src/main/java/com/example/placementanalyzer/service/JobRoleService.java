package com.example.placementanalyzer.service;

import java.util.Optional;

import org.springframework.stereotype.Service;

import com.example.placementanalyzer.dto.JobRoleDTO;
import com.example.placementanalyzer.model.JobRole;
import com.example.placementanalyzer.repository.JobRoleRepository;

@Service
public class JobRoleService {

    private final JobRoleRepository jobRoleRepository;

    public JobRoleService(JobRoleRepository jobRoleRepository) {
        this.jobRoleRepository = jobRoleRepository;
    }

    public JobRoleDTO createJobRole(JobRole jobRole) {

        JobRole savedJobRole = jobRoleRepository.save(jobRole);

        return convertToDTO(savedJobRole);
    }

    public Optional<JobRoleDTO> getJobRoleById(Long id) {

        return jobRoleRepository.findById(id)
                .map(this::convertToDTO);
    }

    public Optional<JobRoleDTO> getJobRoleByName(String name) {

        return jobRoleRepository.findByName(name)
                .map(this::convertToDTO);
    }

    private JobRoleDTO convertToDTO(JobRole jobRole) {

        JobRoleDTO dto = new JobRoleDTO();

        dto.setId(jobRole.getId());
        dto.setName(jobRole.getName());
        dto.setDescription(jobRole.getDescription());

        return dto;
    }
}