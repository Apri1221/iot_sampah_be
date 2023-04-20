package com.example.iotsampah.controller;

import com.example.iotsampah.entity.MstSchools;
import com.example.iotsampah.entity.MstUsers;
import com.example.iotsampah.service.MstSchoolsService;
import com.example.iotsampah.service.MstUsersService;
import com.example.iotsampah.service.WebClientService;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Objects;

@RestController
@RequestMapping("/users")
public class MstUsersController {

    @Autowired
    MstUsersService mstUsersService;

    @Autowired
    MstSchoolsService mstSchoolsService;

    @Autowired
    WebClientService webClientService;

    @PostMapping("/store")
    public ResponseEntity storeUser(@RequestBody MstUsers mstUsers) {
        mstUsersService.storeUser(mstUsers);
        return ResponseEntity.ok("success");
    }

    @PostMapping("/update")
    public ResponseEntity updateUser(@RequestBody MstUsers mstUsers) {
        boolean isUpdated = mstUsersService.updateUser(mstUsers);
        if (isUpdated) return ResponseEntity.ok("success");
        return ResponseEntity.badRequest().body("failed");
    }

    @GetMapping("/get/{id}")
    public ResponseEntity getUser(@PathVariable(value = "id") Integer id) {
        MstUsers mstUsers = mstUsersService.getUser(id);
        return ResponseEntity.ok(mstUsers);
    }

    @GetMapping("/{qrcode}")
    public ResponseEntity getUser(@PathVariable(value = "qrcode") String qrcode) throws JsonProcessingException {
        String[] data = qrcode.split("-");
        MstUsers mstUsers = mstUsersService.getUser(data[0]);
        if (mstUsers == null) {
            MstSchools mstSchools = mstSchoolsService.getDataSchool(data[1]);
            mstUsers = mstUsersService.getDataUser(mstSchools, data[0]);
        }
        Integer dataBalance = webClientService.getBalanceStudent(mstUsers.getSchool().getUrl(), mstUsers.getStudentId());
        mstUsers.setSaldo(dataBalance);
        return ResponseEntity.ok(mstUsers);
    }
}
