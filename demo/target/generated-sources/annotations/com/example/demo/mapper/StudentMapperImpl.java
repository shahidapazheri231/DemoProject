package com.example.demo.mapper;

import com.example.demo.dto.StudentDTO;
import com.example.demo.dto.StudentResponse;
import com.example.demo.entity.Student;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2024-09-10T00:52:28+0530",
    comments = "version: 1.5.3.Final, compiler: javac, environment: Java 21.0.2 (Oracle Corporation)"
)
@Component
public class StudentMapperImpl implements StudentMapper {

    @Override
    public StudentDTO mapStudentToDto(Student student) {
        if ( student == null ) {
            return null;
        }

        String name = null;
        Integer score = null;

        name = student.getName();
        score = student.getScore();

        StudentDTO studentDTO = new StudentDTO( name, score );

        return studentDTO;
    }

    @Override
    public Student mapDtoToStudent(StudentDTO dto) {
        if ( dto == null ) {
            return null;
        }

        Student student = new Student();

        student.setName( dto.name() );
        student.setScore( dto.score() );

        return student;
    }

    @Override
    public StudentResponse mapStudentToStudentRes(Student student) {
        if ( student == null ) {
            return null;
        }

        Integer rollNo = null;
        String name = null;
        Integer score = null;

        rollNo = student.getRollNo();
        name = student.getName();
        score = student.getScore();

        StudentResponse studentResponse = new StudentResponse( rollNo, name, score );

        return studentResponse;
    }

    @Override
    public Student mapStudentresToStudent(StudentResponse res) {
        if ( res == null ) {
            return null;
        }

        Student student = new Student();

        student.setRollNo( res.rollNo() );
        student.setName( res.name() );
        student.setScore( res.score() );

        return student;
    }

    @Override
    public List<StudentResponse> mapListOfstudentsToResponse(List<Student> students) {
        if ( students == null ) {
            return null;
        }

        List<StudentResponse> list = new ArrayList<StudentResponse>( students.size() );
        for ( Student student : students ) {
            list.add( mapStudentToStudentRes( student ) );
        }

        return list;
    }

    @Override
    public List<Student> mapListOfResToStudent(List<StudentResponse> dtos) {
        if ( dtos == null ) {
            return null;
        }

        List<Student> list = new ArrayList<Student>( dtos.size() );
        for ( StudentResponse studentResponse : dtos ) {
            list.add( mapStudentresToStudent( studentResponse ) );
        }

        return list;
    }
}
