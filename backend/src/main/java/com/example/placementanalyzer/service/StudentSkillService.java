package com.example.placementanalyzer.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.example.placementanalyzer.dto.StudentSkillDTO;
import com.example.placementanalyzer.model.StudentSkill;
import com.example.placementanalyzer.repository.StudentSkillRepository;

@Service
public class StudentSkillService {

    private final StudentSkillRepository studentSkillRepository;

    public StudentSkillService(StudentSkillRepository studentSkillRepository) {
        this.studentSkillRepository = studentSkillRepository;
    }

    public StudentSkillDTO createStudentSkill(StudentSkill studentSkill) {

    Long studentId = studentSkill.getStudent().getId();
    Long skillId = studentSkill.getSkill().getId();

    List<StudentSkill> existingSkills =
            studentSkillRepository.findByStudentId(studentId);

    for (StudentSkill existing : existingSkills) {
        if (existing.getSkill().getId().equals(skillId)) {
            existing.setLevel(studentSkill.getLevel());

            StudentSkill updated =
                    studentSkillRepository.save(existing);

            return convertToDTO(updated);
        }
    }

    StudentSkill saved =
            studentSkillRepository.save(studentSkill);

    return convertToDTO(saved);
}

    public List<StudentSkillDTO> getSkillsByStudentId(Long studentId) {

        return studentSkillRepository.findByStudentId(studentId)
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    private StudentSkillDTO convertToDTO(StudentSkill studentSkill) {

        StudentSkillDTO dto = new StudentSkillDTO();

        dto.setId(studentSkill.getId());
        dto.setStudentId(studentSkill.getStudent().getId());
        dto.setSkillId(studentSkill.getSkill().getId());
        dto.setLevel(studentSkill.getLevel());

        return dto;
    }
}