package com.example.iotsampah.service;

import com.example.iotsampah.entity.MstSchools;
import com.example.iotsampah.entity.MstUsers;
import com.example.iotsampah.repository.MstUsersRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class MstUsersService {

    @Autowired
    MstUsersRepository mstUsersRepository;

    @Autowired
    WebClientService webClientService;

    public void storeUser(MstUsers mstUsers) {
        mstUsersRepository.save(mstUsers);
    }

    public void updateUser(MstUsers mstUsers) {
        Optional<MstUsers> mstUsersQuery = mstUsersRepository.findById(mstUsers.getId());
        MstUsers mstUsersOld = mstUsersQuery.orElse(null);

        if (mstUsersOld != null) {
            mstUsers.setId(mstUsersOld.getId());
            mstUsersRepository.save(mstUsers);
        }
    }

    public MstUsers getUser(Integer id) {
        Optional<MstUsers> mstUser = mstUsersRepository.findById(id);
        return mstUser.orElse(null);
    }

    public MstUsers getUser(String nis) {
        Optional<MstUsers> mstUser = mstUsersRepository.findByNis(nis);
        return mstUser.orElse(null);
    }

    public List<MstUsers> getAllUsers() {
        List<MstUsers> mstUsers = mstUsersRepository.findAll();
        return mstUsers;
    }

    public MstUsers getDataUser(MstSchools mstSchools, String nisn) throws JsonProcessingException {
        Map<String, Object> dataStudent = webClientService.resolveDataStudent(mstSchools.getUrl(), nisn);
        MstUsers mstUsers = new MstUsers();
        mstUsers.setName(String.format("%s",dataStudent.get("name")));
        mstUsers.setNis(nisn);
        mstUsers.setSchool(mstSchools);
        mstUsersRepository.save(mstUsers);
        return mstUsers;
    }
}
