package com.example.iotsampah.repository;

import com.example.iotsampah.entity.MstDevices;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MstDevicesRepository extends JpaRepository<MstDevices, Integer> {
    List<MstDevices> findBySchoolId(Integer schoolId);
}
