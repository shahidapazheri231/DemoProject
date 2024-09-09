package com.example.demo.repo;





import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import com.example.demo.entity.Student;

import jakarta.transaction.Transactional;

@Repository
@Transactional
public interface StudentRepo extends JpaRepository<Student, Integer> ,JpaSpecificationExecutor<Student>{

	Optional<Student> findByRollNo(Integer rollNo);

	boolean existsByRollNo(Integer rollNo);

	void deleteByRollNo(Integer rollNo);
	

}
