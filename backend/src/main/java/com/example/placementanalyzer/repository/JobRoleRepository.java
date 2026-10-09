package com.example.placementanalyzer.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.placementanalyzer.model.JobRole;

public interface JobRoleRepository extends JpaRepository<JobRole, Long> {

    Optional<JobRole> findByName(String name);
}