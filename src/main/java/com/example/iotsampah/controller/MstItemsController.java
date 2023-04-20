package com.example.iotsampah.controller;


import com.example.iotsampah.entity.MstItems;
import com.example.iotsampah.service.MstItemsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/items")
public class MstItemsController {

    @Autowired
    MstItemsService mstItemsService;

    @PostMapping("/store")
    public ResponseEntity storeSchool(@RequestBody MstItems mstItems) {
        mstItemsService.storeItem(mstItems);
        return ResponseEntity.ok("success");
    }

    @PostMapping("/update")
    public ResponseEntity updateSchool(@RequestBody MstItems mstItems) {
        mstItemsService.updateItem(mstItems);
        return ResponseEntity.ok("success");
    }

    @GetMapping("/get/{id}")
    public ResponseEntity getSchool(@PathVariable(value = "id") Integer id) {
        MstItems mstItems = mstItemsService.getItem(id);
        return ResponseEntity.ok(mstItems);
    }

    @PostMapping("/")
    public ResponseEntity getAllSchools() {
        List<MstItems> mstItemsList = mstItemsService.getAllItems();
        return ResponseEntity.ok(mstItemsList);
    }
}
