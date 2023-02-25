package com.example.iotsampah.repository;

import com.example.iotsampah.entity.MstSchools;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MstSchoolsRepository extends JpaRepository<MstSchools, Integer> {
}
