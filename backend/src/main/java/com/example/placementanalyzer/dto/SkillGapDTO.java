package com.example.placementanalyzer.dto;

public class SkillGapDTO {

    private Long skillId;
    private String skillName;
    private String studentLevel;
    private String requiredLevel;
    private String status;

    public SkillGapDTO() {
    }

    public SkillGapDTO(
            Long skillId,
            String skillName,
            String studentLevel,
            String requiredLevel,
            String status) {

        this.skillId = skillId;
        this.skillName = skillName;
        this.studentLevel = studentLevel;
        this.requiredLevel = requiredLevel;
        this.status = status;
    }

    public Long getSkillId() {
        return skillId;
    }

    public void setSkillId(Long skillId) {
        this.skillId = skillId;
    }

    public String getSkillName() {
        return skillName;
    }

    public void setSkillName(String skillName) {
        this.skillName = skillName;
    }

    public String getStudentLevel() {
        return studentLevel;
    }

    public void setStudentLevel(String studentLevel) {
        this.studentLevel = studentLevel;
    }

    public String getRequiredLevel() {
        return requiredLevel;
    }

    public void setRequiredLevel(String requiredLevel) {
        this.requiredLevel = requiredLevel;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}