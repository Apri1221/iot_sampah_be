package com.example.iotsampah.service;

import com.example.iotsampah.entity.MstSchools;
import com.example.iotsampah.entity.MstUsers;
import com.example.iotsampah.repository.MstUsersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class MstUsersService {

    @Autowired
    MstUsersRepository mstUsersRepository;

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

    public List<MstUsers> getAllUsers() {
        List<MstUsers> mstUsers = mstUsersRepository.findAll();
        return mstUsers;
    }
}
