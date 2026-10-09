package com.example.placementanalyzer.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.placementanalyzer.dto.SkillDTO;
import com.example.placementanalyzer.model.Skill;
import com.example.placementanalyzer.service.SkillService;
        
@RestController
@RequestMapping("/api/skills")
@CrossOrigin(origins = "http://localhost:5173")
public class SkillController {

    private final SkillService skillService;

    public SkillController(SkillService skillService) {
        this.skillService = skillService;
    }
    @GetMapping
public ResponseEntity<List<SkillDTO>> getAllSkills() {

    List<SkillDTO> skills = skillService.getAllSkills();

    return ResponseEntity.ok(skills);
}

    @PostMapping
    public ResponseEntity<SkillDTO> createSkill(
            @RequestBody Skill skill) {

        SkillDTO createdSkill =
                skillService.createSkill(skill);

        return ResponseEntity.ok(createdSkill);
    }

    @GetMapping("/{id}")
    public ResponseEntity<SkillDTO> getSkillById(
            @PathVariable Long id) {

        Optional<SkillDTO> skill =
                skillService.getSkillById(id);

        return skill
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/name/{name}")
    public ResponseEntity<SkillDTO> getSkillByName(
            @PathVariable String name) {

        Optional<SkillDTO> skill =
                skillService.getSkillByName(name);

        return skill
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }
}