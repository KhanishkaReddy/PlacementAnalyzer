package com.example.placementanalyzer.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.placementanalyzer.model.JobRoleSkill;

public interface JobRoleSkillRepository extends JpaRepository<JobRoleSkill, Long> {

    List<JobRoleSkill> findByJobRoleId(Long jobRoleId);
}