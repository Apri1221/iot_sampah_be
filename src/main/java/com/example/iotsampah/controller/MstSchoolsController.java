package com.example.iotsampah.controller;

import com.example.iotsampah.entity.MstSchools;
import com.example.iotsampah.entity.MstUsers;
import com.example.iotsampah.service.MstSchoolsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/schools")
public class MstSchoolsController {

    @Autowired
    MstSchoolsService mstSchoolsService;

    @PostMapping("/store")
    public ResponseEntity storeSchool(@RequestBody MstSchools mstSchools) {
        mstSchoolsService.storeSchool(mstSchools);
        return ResponseEntity.ok("success");
    }

    @PostMapping("/update")
    public ResponseEntity updateSchool(@RequestBody MstSchools mstSchools) {
        mstSchoolsService.updateSchool(mstSchools);
        return ResponseEntity.ok("success");
    }

    @GetMapping("/get/{id}")
    public ResponseEntity getSchool(@PathVariable(value = "id") Integer id) {
        MstSchools mstSchools = mstSchoolsService.getSchool(id);
        return ResponseEntity.ok(mstSchools);
    }

    @GetMapping("/")
    public ResponseEntity getAllSchools() {
        List<MstSchools> mstSchoolsList = mstSchoolsService.getAllSchools();
        return ResponseEntity.ok(mstSchoolsList);
    }
}
