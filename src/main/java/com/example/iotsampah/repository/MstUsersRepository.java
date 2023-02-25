package com.example.iotsampah.repository;

import com.example.iotsampah.entity.MstUsers;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MstUsersRepository extends JpaRepository<MstUsers, Integer> {
}
