package com.example.placementanalyzer.service;

import java.util.Optional;

import org.springframework.stereotype.Service;

import com.example.placementanalyzer.dto.StudentDTO;
import com.example.placementanalyzer.model.Student;
import com.example.placementanalyzer.repository.StudentRepository;

@Service
public class StudentService {

    private final StudentRepository studentRepository;

    public StudentService(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    public StudentDTO createStudent(Student student) {

        Student savedStudent = studentRepository.save(student);

        return convertToDTO(savedStudent);
    }

    public Optional<StudentDTO> getStudentById(Long id) {

        return studentRepository.findById(id)
                .map(this::convertToDTO);
    }

    public Optional<StudentDTO> getStudentByUserId(Long userId) {

        return studentRepository.findByUserId(userId)
                .map(this::convertToDTO);
    }

    private StudentDTO convertToDTO(Student student) {

        StudentDTO dto = new StudentDTO();

        dto.setId(student.getId());
        dto.setUserId(student.getUser().getId());
        dto.setCollege(student.getCollege());
        dto.setCourse(student.getCourse());
        dto.setBranch(student.getBranch());
        dto.setYear(student.getYear());
        dto.setCgpa(student.getCgpa());

        return dto;
    }
}