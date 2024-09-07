package com.example.demo.controller;

import java.util.List;
import java.util.Optional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.dto.Result;
import com.example.demo.dto.StudentDTO;
import com.example.demo.dto.StudentResponse;
import com.example.demo.entity.Student;
import com.example.demo.exception.MyEntityNotFoundException;
import com.example.demo.repo.StudentRepo;
import com.example.demo.service.StudentService;

import io.swagger.v3.oas.annotations.Operation;

@RestController
@RequestMapping("/student")
public class StudentController {

	private static final Logger logger = LogManager.getLogger(StudentController.class);

	@Autowired
	private StudentService studentService;

	@GetMapping("/{rollNo}")
	public ResponseEntity<?> getStudentDetails(@PathVariable Integer rollNo) throws MyEntityNotFoundException {
		try {
			StudentResponse response = studentService.getStudentDetails(rollNo);
			logger.info("Student with roll number {} retrieved successfully", rollNo);
			return new ResponseEntity<>(response, HttpStatus.OK);

		} catch (MyEntityNotFoundException e) {
			logger.error("Student record not found {}: {}", rollNo, e.getMessage());
			return new ResponseEntity<>("Student record with roll number: " + rollNo + " not found",
					HttpStatus.NOT_FOUND);
		}
	}

	@GetMapping("/get-all")
	public ResponseEntity<?> getStudents(@RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "10") int size) {
		try {
			Pageable pageable = PageRequest.of(page, size);
			List<StudentResponse> response = studentService.getStudents(pageable);
			logger.info("Student details retrieved successfully of page {} size {}", page, size);
			return new ResponseEntity<>(response, HttpStatus.OK);

		} catch (Exception e) {
			logger.error("Fetching student details failed");
			return new ResponseEntity<>(e, HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}

	@PostMapping("/create")
	public ResponseEntity<?> createStudent(@RequestBody StudentDTO studentDto) {

		try {
			StudentResponse response = studentService.createStudent(studentDto);
			return new ResponseEntity<>(response, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(e, HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}

	@PutMapping("/update")
	public ResponseEntity<StudentResponse> updateStudent(@RequestParam(name = "rollNo") Integer rollNo,
			@RequestBody StudentDTO updatedStudent) {
		try {
			StudentResponse response = studentService.updateStudent(rollNo, updatedStudent);
			return ResponseEntity.ok(response);
		} catch (RuntimeException e) {
			return ResponseEntity.notFound().build();
		}
	}

	@PatchMapping("/patch")
	public ResponseEntity<StudentResponse> patchStudent(@RequestParam(name = "rollNo") Integer rollNo,
			@RequestBody StudentDTO studentDTO) {

		Optional<Student> existingStudent = studentService.findById(rollNo);
		if (existingStudent.isPresent()) {
			StudentResponse updatedStudent = studentService.patchStudent(rollNo, studentDTO);
			return ResponseEntity.ok(updatedStudent);
		} else {
			return ResponseEntity.notFound().build();
		}
	}

	@Operation(summary = "List all students eligible for higher studies", description = "List the students whose score is greater than 25")
	@GetMapping("/eligible-students")
	public List<StudentResponse> getListOfEligibleStudents() {

		return studentService.getListOfEligibleStudents();

	}



	@GetMapping("/search")
	public ResponseEntity<?> searchStudents(@RequestParam(required = false, name = "rollNo") Integer rollNo,
			@RequestParam(required = false, name = "name") String name,
			@RequestParam(required = false, name = "score") Integer score) {

		try {
			List<StudentResponse> response = studentService.searchStudents(rollNo, name, score);
			return new ResponseEntity<>(response, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(e, HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}

	@DeleteMapping("/{rollNo}")
	public ResponseEntity<Void> deleteStudent(@PathVariable Integer rollNo) {
		try {
			studentService.deleteStudent(rollNo);
			return ResponseEntity.noContent().build(); // 204- No Content if successful
		} catch (RuntimeException e) {
			return ResponseEntity.notFound().build(); // 404 -Not Found if student doesn't exist
		}

	}
}
