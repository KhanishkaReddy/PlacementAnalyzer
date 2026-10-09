package com.example.placementanalyzer.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.example.placementanalyzer.dto.SkillDTO;
import com.example.placementanalyzer.model.Skill;
import com.example.placementanalyzer.repository.SkillRepository;

@Service
public class SkillService {

    private final SkillRepository skillRepository;

    public SkillService(SkillRepository skillRepository) {
        this.skillRepository = skillRepository;
    }

    public SkillDTO createSkill(Skill skill) {

        Skill savedSkill = skillRepository.save(skill);

        return convertToDTO(savedSkill);
    }

    public List<SkillDTO> getAllSkills() {

        return skillRepository.findAll()
                .stream()
                .map(this::convertToDTO)
                .toList();
    }

    public Optional<SkillDTO> getSkillById(Long id) {

        return skillRepository.findById(id)
                .map(this::convertToDTO);
    }

    public Optional<SkillDTO> getSkillByName(String name) {

        return skillRepository.findByName(name)
                .map(this::convertToDTO);
    }

    private SkillDTO convertToDTO(Skill skill) {

        SkillDTO dto = new SkillDTO();

        dto.setId(skill.getId());
        dto.setName(skill.getName());

        return dto;
    }
}