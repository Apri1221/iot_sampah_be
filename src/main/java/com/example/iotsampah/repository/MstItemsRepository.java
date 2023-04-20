package com.example.iotsampah.repository;

import com.example.iotsampah.entity.MstItems;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MstItemsRepository extends JpaRepository<MstItems, Integer> {

    Optional<MstItems> findByCode(String code);
}
