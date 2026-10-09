package com.example.placementanalyzer.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.example.placementanalyzer.dto.SkillGapDTO;
import com.example.placementanalyzer.model.JobRoleSkill;
import com.example.placementanalyzer.model.StudentSkill;
import com.example.placementanalyzer.repository.JobRoleSkillRepository;
import com.example.placementanalyzer.repository.StudentSkillRepository;

@Service
public class SkillGapService {

    private final StudentSkillRepository studentSkillRepository;
    private final JobRoleSkillRepository jobRoleSkillRepository;

    public SkillGapService(
            StudentSkillRepository studentSkillRepository,
            JobRoleSkillRepository jobRoleSkillRepository) {

        this.studentSkillRepository = studentSkillRepository;
        this.jobRoleSkillRepository = jobRoleSkillRepository;
    }

    public List<SkillGapDTO> analyzeSkillGap(
            Long studentId,
            Long jobRoleId) {

        List<StudentSkill> studentSkills =
                studentSkillRepository.findByStudentId(studentId);

        List<JobRoleSkill> requiredSkills =
                jobRoleSkillRepository.findByJobRoleId(jobRoleId);

        List<SkillGapDTO> results = new ArrayList<>();

        for (JobRoleSkill requiredSkill : requiredSkills) {

            String studentLevel = null;

            for (StudentSkill studentSkill : studentSkills) {

                if (studentSkill.getSkill().getId()
                        .equals(requiredSkill.getSkill().getId())) {

                    studentLevel = studentSkill.getLevel();
                    break;
                }
            }

            String status;

            if (studentLevel == null) {

                status = "MISSING";

            } else {

                int studentLevelValue =
                        getLevelValue(studentLevel);

                int requiredLevelValue =
                        getLevelValue(
                                requiredSkill.getRequiredLevel()
                        );

                if (studentLevelValue >= requiredLevelValue) {

                    status = "MATCH";

                } else {

                    status = "NEEDS_IMPROVEMENT";
                }
            }

            SkillGapDTO result = new SkillGapDTO(
                    requiredSkill.getSkill().getId(),
                    requiredSkill.getSkill().getName(),
                    studentLevel,
                    requiredSkill.getRequiredLevel(),
                    status
            );

            results.add(result);
        }

        return results;
    }

    private int getLevelValue(String level) {

        if (level == null) {
            return 0;
        }

        switch (level.toLowerCase()) {

            case "beginner":
                return 1;

            case "intermediate":
                return 2;

            case "advanced":
                return 3;

            default:
                return 0;
        }
    }
}