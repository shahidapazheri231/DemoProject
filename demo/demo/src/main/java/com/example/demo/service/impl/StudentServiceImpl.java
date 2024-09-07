package com.example.demo.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.example.demo.dto.StudentDTO;
import com.example.demo.dto.StudentResponse;
import com.example.demo.entity.Student;
import com.example.demo.exception.MyEntityNotFoundException;
import com.example.demo.mapper.StudentMapper;
import com.example.demo.repo.StudentRepo;
import com.example.demo.service.StudentService;
import com.example.demo.specifications.StudentSpecification;

@Service
public class StudentServiceImpl implements StudentService {
	private static final Logger logger = LogManager.getLogger(StudentService.class);

	@Autowired
	private StudentRepo studentRepo;

	@Autowired
	private StudentMapper studentMapper;

	@Override
	public StudentResponse createStudent(StudentDTO studentDto) {
		logger.info("Entering method: {}", Thread.currentThread().getStackTrace()[2].getMethodName());
		Student student = studentMapper.mapDtoToStudent(studentDto);
		System.out.println(student.getName());
		student = studentRepo.save(student);

		return studentMapper.mapStudentToStudentRes(student);
	}

	@Override
	public List<StudentResponse> getStudents(Pageable pageable) {
		logger.info("Entering method: {}", Thread.currentThread().getStackTrace()[2].getMethodName());
		List<Student> students = new ArrayList<Student>();
		try {
			 students = studentRepo.findAll(pageable).getContent();

			 logger.info("Successfully retreived student details of size: "+students.size());
			
		} catch (Exception e) {
			logger.error("method:getStudents::Error occurred while fetching students", e);
		}
		return studentMapper.mapListOfstudentsToResponse(students);
	}

	@Override
	public List<StudentResponse> getListOfEligibleStudents() {
		logger.info("Entering method: {}", Thread.currentThread().getStackTrace()[2].getMethodName());
		List<Student> students = studentRepo.findAll();

		List<Student> eligibleStudents = students.stream()
				.filter(student -> Objects.nonNull(student.getScore()) && student.getScore() > 25)
				.collect(Collectors.toList());
		return studentMapper.mapListOfstudentsToResponse(eligibleStudents);
	}

	@Override
	public List<StudentResponse> searchStudents(Integer rollNo, String name, Integer score) {
		logger.info("Entering method: {}", Thread.currentThread().getStackTrace()[2].getMethodName());

		List<Student> students = new ArrayList<Student>();
		try {
			logger.info("Fetching all students");
			students = studentRepo.findAll(StudentSpecification.getStudentsByCriteria(rollNo, name, score));

		} catch (Exception e) {
			logger.error("Error occurred while fetching students", e);
		}
		logger.info("Successfully fetched all students");
		return studentMapper.mapListOfstudentsToResponse(students);
	}

	@Override
	public Optional<Student> findById(Integer rollNo) {
		logger.info("Entering method: {}", Thread.currentThread().getStackTrace()[2].getMethodName());
		return studentRepo.findByRollNo(rollNo);
	}

	@Override
	public StudentResponse patchStudent(Integer rollNo, StudentDTO studentDTO) {
		logger.info("Entering method: {}", Thread.currentThread().getStackTrace()[2].getMethodName());
		logger.info("roll number:" + studentRepo.findByRollNo(rollNo));

		return studentRepo.findByRollNo(rollNo).map(student -> {
			if (studentDTO.name() != null) {
				student.setName(studentDTO.name());
			}
			if (studentDTO.score() != null) {
				student.setScore(studentDTO.score());
			}
			Student patchedStudent = studentRepo.save(student);
			return studentMapper.mapStudentToStudentRes(patchedStudent);
		}).orElseThrow(() -> new RuntimeException("Student not found with roll no " + rollNo));

	}

	@Override
	public StudentResponse updateStudent(Integer rollNo, StudentDTO updatedStudent) {
		logger.info("Entering method: {}", Thread.currentThread().getStackTrace()[2].getMethodName());

		Function<Integer, Student> findStudentByRollNo = (studentId) -> studentRepo.findByRollNo(rollNo)
				.orElseThrow(() -> new RuntimeException("Student with ID " + studentId + " not found"));

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
		logger.info("Entering method: {}", Thread.currentThread().getStackTrace()[2].getMethodName());
		logger.info("roll number exists:" + studentRepo.existsByRollNo(rollNo));
		if (studentRepo.existsByRollNo(rollNo)) {
            studentRepo.deleteByRollNo(rollNo);
        } else {
            throw new RuntimeException("Student with roll number " + rollNo + " not found");
        }
		
	}

	@Override
	public StudentResponse getStudentDetails(Integer rollNo) throws MyEntityNotFoundException {
		logger.info("Entering method: {}", Thread.currentThread().getStackTrace()[2].getMethodName());
		logger.info("Fetching student with roll number: {}", rollNo);

        Optional<Student> student = studentRepo.findByRollNo(rollNo);
        
        if (student.isPresent()) {
            logger.debug("Entity found: {}", student.get());
            
            return studentMapper.mapStudentToStudentRes(student.get());
        } else {
            logger.error("Entity with roll number {} not found", rollNo);
            throw new MyEntityNotFoundException("student with roll number " + rollNo + " not found");
        }
	}

}
