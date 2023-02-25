package com.example.iotsampah.controller;

import com.example.iotsampah.entity.MstDevices;
import com.example.iotsampah.service.MstDevicesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/devices")
public class MstDevicesController {

    @Autowired
    MstDevicesService mstDevicesService;

    @PostMapping("/store")
    public ResponseEntity storeDevice(@RequestBody MstDevices mstDevices) {
        mstDevicesService.storeDevice(mstDevices);
        return ResponseEntity.ok("success");
    }

    @PostMapping("/update")
    public ResponseEntity updateDevice(@RequestBody MstDevices mstDevices) {
        mstDevicesService.updateDevice(mstDevices);
        return ResponseEntity.ok("success");
    }

    @PostMapping("/get/{id}")
    public ResponseEntity getDevice(@PathVariable(value = "id") Integer id) {
        MstDevices mstDevices = mstDevicesService.getDevice(id);
        return ResponseEntity.ok(mstDevices);
    }

    @PostMapping("/")
    public ResponseEntity getAllDevices() {
        List<MstDevices> mstDevices = mstDevicesService.getAllDevices();
        return ResponseEntity.ok(mstDevices);
    }
}
