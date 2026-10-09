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

import com.example.placementanalyzer.dto.StudentSkillDTO;
import com.example.placementanalyzer.model.StudentSkill;
import com.example.placementanalyzer.service.StudentSkillService;

@RestController
@RequestMapping("/api/student-skills")
@CrossOrigin(origins = "http://localhost:5173")
public class StudentSkillController {

    private final StudentSkillService studentSkillService;

    public StudentSkillController(StudentSkillService studentSkillService) {
        this.studentSkillService = studentSkillService;
    }

    @PostMapping
    public ResponseEntity<StudentSkillDTO> createStudentSkill(
            @RequestBody StudentSkill studentSkill) {

        StudentSkillDTO createdStudentSkill =
                studentSkillService.createStudentSkill(studentSkill);

        return ResponseEntity.ok(createdStudentSkill);
    }

    @GetMapping("/student/{studentId}")
    public ResponseEntity<List<StudentSkillDTO>> getSkillsByStudentId(
            @PathVariable Long studentId) {

        List<StudentSkillDTO> skills =
                studentSkillService.getSkillsByStudentId(studentId);

        return ResponseEntity.ok(skills);
    }
}