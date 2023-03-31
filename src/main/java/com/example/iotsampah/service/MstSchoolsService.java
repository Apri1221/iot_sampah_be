package com.example.iotsampah.service;

import com.example.iotsampah.entity.MstDevices;
import com.example.iotsampah.entity.MstSchools;
import com.example.iotsampah.repository.MstSchoolsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class MstSchoolsService {

    @Autowired
    MstSchoolsRepository mstSchoolsRepository;

    @Autowired
    WebClientService webClientService;

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

    public MstSchools getDataSchool(String idSchool) {
        Optional<MstSchools> mstSchool = mstSchoolsRepository.findByCode(idSchool);
        if (mstSchool.isEmpty()) {
            Map<String, Object> dataSchool = webClientService.resolveDataSchool(idSchool);
            MstSchools mstSchools = new MstSchools();
            mstSchools.setCode(String.format("%s", dataSchool.get("id")));
            mstSchools.setName(String.format("%s", dataSchool.get("name")));
            mstSchools.setUrl(String.format("%s", dataSchool.get("school_url")));
            mstSchoolsRepository.save(mstSchools);
            return mstSchools;
        }
        return mstSchool.get();
    }
}
