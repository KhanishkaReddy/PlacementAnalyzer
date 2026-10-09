package com.example.placementanalyzer.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.placementanalyzer.model.Project;

public interface ProjectRepository extends JpaRepository<Project, Long> {

    List<Project> findByStudentId(Long studentId);
}