package com.bt.cricketacademy.student.repository;

import com.bt.cricketacademy.student.model.Student;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StudentRepository extends JpaRepository<Student, Long> {
}
