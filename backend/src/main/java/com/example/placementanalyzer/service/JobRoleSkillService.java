package com.example.placementanalyzer.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.example.placementanalyzer.dto.JobRoleSkillDTO;
import com.example.placementanalyzer.model.JobRoleSkill;
import com.example.placementanalyzer.repository.JobRoleSkillRepository;

@Service
public class JobRoleSkillService {

    private final JobRoleSkillRepository jobRoleSkillRepository;

    public JobRoleSkillService(JobRoleSkillRepository jobRoleSkillRepository) {
        this.jobRoleSkillRepository = jobRoleSkillRepository;
    }

    public JobRoleSkillDTO createJobRoleSkill(JobRoleSkill jobRoleSkill) {

        JobRoleSkill savedJobRoleSkill =
                jobRoleSkillRepository.save(jobRoleSkill);

        return convertToDTO(savedJobRoleSkill);
    }

    public List<JobRoleSkillDTO> getSkillsByJobRoleId(Long jobRoleId) {

        return jobRoleSkillRepository.findByJobRoleId(jobRoleId)
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    private JobRoleSkillDTO convertToDTO(JobRoleSkill jobRoleSkill) {

        JobRoleSkillDTO dto = new JobRoleSkillDTO();

        dto.setId(jobRoleSkill.getId());
        dto.setJobRoleId(jobRoleSkill.getJobRole().getId());
        dto.setSkillId(jobRoleSkill.getSkill().getId());
        dto.setRequiredLevel(jobRoleSkill.getRequiredLevel());

        return dto;
    }
}