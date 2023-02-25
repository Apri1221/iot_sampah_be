package com.example.iotsampah.service;

import com.example.iotsampah.entity.MstDevices;
import com.example.iotsampah.entity.MstSchools;
import com.example.iotsampah.repository.MstSchoolsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class MstSchoolsService {

    @Autowired
    MstSchoolsRepository mstSchoolsRepository;

    public void storeSchool(MstSchools mstSchools) {
        mstSchoolsRepository.save(mstSchools);
    }

    public void updateSchool(MstSchools mstSchools) {
        Optional<MstSchools> mstSchoolQuery = mstSchoolsRepository.findById(mstSchools.getId());
        MstSchools mstSchoolOld = mstSchoolQuery.orElse(null);

        if (mstSchoolOld != null) {
            mstSchools.setId(mstSchoolOld.getId());
            mstSchoolsRepository.save(mstSchools);
        }
    }

    public MstSchools getSchool(Integer id) {
        Optional<MstSchools> mstSchool = mstSchoolsRepository.findById(id);
        return mstSchool.orElse(null);
    }

    public List<MstSchools> getAllSchools() {
        List<MstSchools> mstSchools = mstSchoolsRepository.findAll();
        return mstSchools;
    }
}
