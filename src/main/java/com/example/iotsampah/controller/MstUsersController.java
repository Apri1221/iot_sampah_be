package com.example.iotsampah.controller;

import com.example.iotsampah.entity.MstSchools;
import com.example.iotsampah.entity.MstUsers;
import com.example.iotsampah.service.MstUsersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
public class MstUsersController {

    @Autowired
    MstUsersService mstUsersService;

    @PostMapping("/store")
    public ResponseEntity storeUser(@RequestBody MstUsers mstUsers) {
        mstUsersService.storeUser(mstUsers);
        return ResponseEntity.ok("success");
    }

    @PostMapping("/update")
    public ResponseEntity updateUser(@RequestBody MstUsers mstUsers) {
        mstUsersService.updateUser(mstUsers);
        return ResponseEntity.ok("success");
    }

    @PostMapping("/get/{id}")
    public ResponseEntity getUser(@PathVariable(value = "id") Integer id) {
        MstUsers mstUsers = mstUsersService.getUser(id);
        return ResponseEntity.ok(mstUsers);
    }
}
