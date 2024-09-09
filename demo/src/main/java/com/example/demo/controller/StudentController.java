package com.example.demo.controller;

import java.util.List;

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

import com.example.demo.dto.StudentDTO;
import com.example.demo.dto.StudentResponse;
import com.example.demo.exception.StudentNotFoundException;
import com.example.demo.service.StudentService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/student")
@Tag(name = "Student API", description = "Operations related to managing students")
public class StudentController {

	@Autowired
	private StudentService studentService;

	@Operation(summary = "Get the student details ", description = "Get student details with roll number")
	@GetMapping("/{rollNo}")
	public ResponseEntity<?> getstudentById(@PathVariable(name = "rollNo") Integer rollNo)
			throws StudentNotFoundException {
		StudentResponse student = studentService.getStudentDetails(rollNo);

		return ResponseEntity.ok(student);
	}

	@Operation(summary = "List all students  ", description = "Get students details with pagination")
	@GetMapping("/get-all")
	public ResponseEntity<?> getStudents(@RequestParam(defaultValue = "0", name = "page") int page,
			@RequestParam(defaultValue = "10", name = "size") int size) {

		Pageable pageable = PageRequest.of(page, size);
		List<StudentResponse> response = studentService.getStudents(pageable);

		return new ResponseEntity<>(response, HttpStatus.OK);

	}

	@PostMapping("/create")
	@Operation(summary = "Create a new student", description = "This API creates a new student in the system. You need to provide a name and a score.By default score is set to zero.", tags = {
			"Student API" })
	public ResponseEntity<?> createStudent(@RequestBody StudentDTO studentDto) {

		StudentResponse response = studentService.createStudent(studentDto);
		return new ResponseEntity<>(response, HttpStatus.OK);

	}

	@Operation(summary = "Update student details  ", description = "Update the student details. params: rollNo body: StudentDTO")
	@PutMapping("/update")
	public ResponseEntity<?> updateStudent(@RequestParam(name = "rollNo") Integer rollNo,
			@RequestBody StudentDTO updatedStudent) {

		StudentResponse response = studentService.updateStudent(rollNo, updatedStudent);
		return ResponseEntity.ok(response);

	}

	@Operation(summary = "Patch student details  ", description = "Patch students details. params: rollNo body: StudentDTO")
	@PatchMapping("/patch")
	public ResponseEntity<?> patchStudent(@RequestParam(name = "rollNo") Integer rollNo,
			@RequestBody StudentDTO studentDTO) {

		StudentResponse updatedStudent = studentService.patchStudent(rollNo, studentDTO);

		return ResponseEntity.ok(updatedStudent);

	}

	@Operation(summary = "List all students eligible for higher studies", description = "List the students whose score is greater than 25")
	@GetMapping("/eligible-students")
	public List<StudentResponse> getListOfEligibleStudents() {

		return studentService.getListOfEligibleStudents();

	}

	@Operation(summary = "Search students", description = "Search for student based on the provided filters. You can search by name, score, roll number.")
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

	@Operation(summary = "Delete student", description = "Delete student by passing roll number")
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
