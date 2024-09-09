package com.example.demo.specifications;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.jpa.domain.Specification;

import com.example.demo.entity.Student;

import jakarta.persistence.criteria.Predicate;

public class StudentSpecification {
	
	public static Specification<Student> getStudentsByCriteria(Integer rollNo,String name, Integer score){
		return (root, query, criteriaBuilder) -> {
          List<Predicate> predicates= new ArrayList<Predicate>();

          // Check for rollNo
          if (rollNo != null) {
              predicates.add(criteriaBuilder.equal(root.get("rollNo"), rollNo));
          }

          // Check for name 
          if (name != null && !name.isEmpty()) {
              predicates.add(criteriaBuilder.like( criteriaBuilder.lower(root.get("name")), "%" + name.toLowerCase() + "%"));
          }

          // Check for score
          if (score != null) {
              predicates.add(criteriaBuilder.equal(root.get("score"), score));
          }

          return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
      };
		
		
	}

}
