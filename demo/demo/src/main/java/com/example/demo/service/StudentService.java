package com.example.demo.service;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.example.demo.dto.StudentDTO;
import com.example.demo.dto.StudentResponse;
import com.example.demo.entity.Student;
import com.example.demo.exception.MyEntityNotFoundException;

@Component
public interface StudentService {

	StudentResponse createStudent(StudentDTO studentDto);

	List<StudentResponse> getStudents(Pageable pageable);

	List<StudentResponse> getListOfEligibleStudents();

	List<StudentResponse> searchStudents(Integer rollNo, String name, Integer score);
	
	

	Optional<Student> findById(Integer rollNo);

	StudentResponse patchStudent(Integer rollNo, StudentDTO studentDTO);

	StudentResponse updateStudent(Integer rollNo, StudentDTO updatedStudent);

	void deleteStudent(Integer id);

	StudentResponse getStudentDetails(Integer rollNo) throws MyEntityNotFoundException;

}
