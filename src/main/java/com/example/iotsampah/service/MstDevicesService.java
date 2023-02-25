package com.example.iotsampah.service;

import com.example.iotsampah.entity.MstDevices;
import com.example.iotsampah.repository.MstDevicesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class MstDevicesService {

    @Autowired
    MstDevicesRepository mstDevicesRepository;

    public List<String> getTopicsBySchoolId(Integer schoolId) {
        List<String> topics = new ArrayList<>();
        List<MstDevices> devices =  mstDevicesRepository.findBySchoolId(schoolId);

        for (MstDevices device : devices) {
            topics.add(String.format("/%s-%s-r", device.getCode(), device.getType()));
        }
        return topics;
    }

    public void storeDevice(MstDevices mstDevices) {
        mstDevicesRepository.save(mstDevices);
    }

    public void updateDevice(MstDevices mstDevices) {
        Optional<MstDevices> mstDevicesQuery = mstDevicesRepository.findById(mstDevices.getId());
        MstDevices mstDevicesOld = mstDevicesQuery.orElse(null);

        if (mstDevicesOld != null) {
            mstDevices.setId(mstDevicesOld.getId());
            mstDevicesRepository.save(mstDevices);
        }
    }

    public MstDevices getDevice(Integer id) {
        Optional<MstDevices> mstDevice = mstDevicesRepository.findById(id);
        return mstDevice.orElse(null);
    }

    public List<MstDevices> getAllDevices() {
        List<MstDevices> mstDevices = mstDevicesRepository.findAll();
        return mstDevices;
    }
}
