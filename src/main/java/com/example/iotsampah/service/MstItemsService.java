package com.example.iotsampah.service;

import com.example.iotsampah.entity.MstDevices;
import com.example.iotsampah.entity.MstItems;
import com.example.iotsampah.repository.MstDevicesRepository;
import com.example.iotsampah.repository.MstItemsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class MstItemsService {

    @Autowired
    MstItemsRepository mstItemsRepository;

    public void storeItem(MstItems mstItems) {
        mstItemsRepository.save(mstItems);
    }

    public void updateItem(MstItems mstItems) {
        Optional<MstItems> mstDevicesQuery = mstItemsRepository.findById(mstItems.getId());
        MstItems mstItemsOld = mstDevicesQuery.orElse(null);

        if (mstItemsOld != null) {
            mstItems.setId(mstItemsOld.getId());
            mstItemsRepository.save(mstItems);
        }
    }

    public MstItems getItem(Integer id) {
        Optional<MstItems> mstItems = mstItemsRepository.findById(id);
        return mstItems.orElse(null);
    }

    public MstItems getItemByCode(String code) {
        Optional<MstItems> mstItems = mstItemsRepository.findByCode(code);
        return mstItems.orElse(null);
    }

    public List<MstItems> getAllItems() {
        List<MstItems> mstDevices = mstItemsRepository.findAll();
        return mstDevices;
    }
}
