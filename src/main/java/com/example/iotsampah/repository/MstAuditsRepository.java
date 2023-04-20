package com.example.iotsampah.repository;

import com.example.iotsampah.entity.MstAudits;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MstAuditsRepository extends JpaRepository<MstAudits, Integer> {
}
