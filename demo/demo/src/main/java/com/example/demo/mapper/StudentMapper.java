package com.example.demo.mapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import com.example.demo.dto.StudentDTO;
import com.example.demo.dto.StudentResponse;
import com.example.demo.entity.Student;
import com.example.demo.service.StudentService;
import com.example.demo.service.impl.StudentServiceImpl;

@Mapper(componentModel = "spring")
public interface StudentMapper {

	StudentDTO mapStudentToDto(Student student);

	Student mapDtoToStudent(StudentDTO dto);

	StudentResponse mapStudentToStudentRes(Student student);

	Student mapStudentresToStudent(StudentResponse res);

	List<StudentResponse> mapListOfstudentsToResponse(List<Student> students);

	
	List<Student> mapListOfResToStudent(List<StudentResponse> dtos);

}
