package com.example.placementanalyzer.service;

import com.example.placementanalyzer.dto.StudentSkillDTO;
import com.example.placementanalyzer.model.StudentSkill;
import com.example.placementanalyzer.repository.StudentSkillRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class StudentSkillService {

    private final StudentSkillRepository studentSkillRepository;

    public StudentSkillService(StudentSkillRepository studentSkillRepository) {
        this.studentSkillRepository = studentSkillRepository;
    }

    public StudentSkillDTO createStudentSkill(StudentSkill studentSkill) {

        StudentSkill savedStudentSkill =
                studentSkillRepository.save(studentSkill);

        return convertToDTO(savedStudentSkill);
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