package com.example.demo.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.example.demo.dto.StudentDTO;
import com.example.demo.dto.StudentResponse;
import com.example.demo.entity.Student;
import com.example.demo.exception.StudentNotFoundException;
import com.example.demo.mapper.StudentMapper;
import com.example.demo.repo.StudentRepo;
import com.example.demo.service.StudentService;
import com.example.demo.specifications.StudentSpecification;

@Service
public class StudentServiceImpl implements StudentService {

	@Autowired
	private StudentRepo studentRepo;

	@Autowired
	private StudentMapper studentMapper;

	@Override

	public StudentResponse createStudent(StudentDTO studentDto) {

		Student student = studentMapper.mapDtoToStudent(studentDto);
		student = studentRepo.save(student);
		return studentMapper.mapStudentToStudentRes(student);
		
		
	}

	@Override
	public List<StudentResponse> getStudents(Pageable pageable) {

		List<Student> students = new ArrayList<Student>();

		students = studentRepo.findAll(pageable).getContent();

		if (students.isEmpty()) {
			throw new StudentNotFoundException("No students added yet");
		}

		return studentMapper.mapListOfstudentsToResponse(students);
	}

	@Override
	public List<StudentResponse> getListOfEligibleStudents() {

		List<Student> students = studentRepo.findAll();

		List<Student> eligibleStudents = students.stream()
				.filter(student -> Objects.nonNull(student.getScore()) && student.getScore() > 25)
				.collect(Collectors.toList());
		return studentMapper.mapListOfstudentsToResponse(eligibleStudents);
	}

	@Override
	public List<StudentResponse> searchStudents(Integer rollNo, String name, Integer score) {

		List<Student> students = new ArrayList<Student>();
		try {

			students = studentRepo.findAll(StudentSpecification.getStudentsByCriteria(rollNo, name, score));

		} catch (Exception e) {

		}

		return studentMapper.mapListOfstudentsToResponse(students);
	}

	@Override
	public Optional<Student> findById(Integer rollNo) {

		return studentRepo.findByRollNo(rollNo);
	}

	@Override
	public StudentResponse patchStudent(Integer rollNo, StudentDTO studentDTO) {

		return studentRepo.findByRollNo(rollNo).map(student -> {
			if (studentDTO.name() != null) {
				student.setName(studentDTO.name());
			}
			if (studentDTO.score() != null) {
				student.setScore(studentDTO.score());
			}
			Student patchedStudent = studentRepo.save(student);
			return studentMapper.mapStudentToStudentRes(patchedStudent);
		}).orElseThrow(() -> new StudentNotFoundException("Student with ID " + rollNo + " not found"));

	}

	@Override
	public StudentResponse updateStudent(Integer rollNo, StudentDTO updatedStudent) {

		Function<Integer, Student> findStudentByRollNo = (studentId) -> studentRepo.findByRollNo(rollNo)
				.orElseThrow(() -> new StudentNotFoundException("Student with ID " + studentId + " not found"));

		Consumer<Student> updateStudentFields = (student) -> {
			student.setName(updatedStudent.name());
			student.setScore(updatedStudent.score());
		};

		// Retrieve
		Student student = findStudentByRollNo.apply(rollNo);

		// Update
		updateStudentFields.accept(student);

		// Save and return the updated student
		return studentMapper.mapStudentToStudentRes(studentRepo.save(student));

	}

	@Override
	public void deleteStudent(Integer rollNo) {

		if (studentRepo.existsByRollNo(rollNo)) {
			studentRepo.deleteByRollNo(rollNo);
		} else {
			throw new StudentNotFoundException("Student with roll number " + rollNo + " not found");
		}

	}

	@Override
	public StudentResponse getStudentDetails(Integer rollNo) {

		Optional<Student> student = studentRepo.findByRollNo(rollNo);

		Student foundStudent = student
				.orElseThrow(() -> new StudentNotFoundException("Student with roll number " + rollNo + " not found"));

		return studentMapper.mapStudentToStudentRes(foundStudent);
	}
}
