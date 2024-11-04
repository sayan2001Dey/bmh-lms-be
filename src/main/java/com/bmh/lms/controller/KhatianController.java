package com.bmh.lms.controller;

import com.bmh.lms.model.Khatian;
import com.bmh.lms.service.auth.AuthService;
import com.bmh.lms.service.khatian.KhatianService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("/api/khatian")
public class KhatianController {

    @Autowired
    private KhatianService khatianMasterService;

    @Autowired
    private AuthService authService;

    @PostMapping
    public ResponseEntity<Khatian> createKhatianMaster(
            @RequestHeader(value = HttpHeaders.AUTHORIZATION, required = false) String token,
            @RequestBody Khatian khatian
    ) {
        Object[] authData = authService.verifyToken(token);
        if (authData == null || !((Boolean) authData[1]))
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);

        try {
            return new ResponseEntity<>(
                    khatianMasterService.createKhatianMaster(khatian, (String) authData[0]),
                    HttpStatus.CREATED
            );
        } catch (Exception e) {
            System.out.println(e);
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }


    @GetMapping
    public ResponseEntity<List<Khatian>> getAllKhatianMasters(
            @RequestHeader(value = HttpHeaders.AUTHORIZATION, required = false) String token
    ) {
        Object[] authData = authService.verifyToken(token);
        if (authData == null)
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        return new ResponseEntity<>(khatianMasterService.getAllKhatianMaster(), HttpStatus.OK);
    }

    @GetMapping("/{khatianId}")
    public ResponseEntity<Khatian> getKhatianMasterById(
            @RequestHeader(value = HttpHeaders.AUTHORIZATION, required = false) String token,
            @PathVariable String khatianId
    ) {
        Object[] authData = authService.verifyToken(token);
        if (authData == null)
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        Khatian res = khatianMasterService.getKhatianMasterById(khatianId).orElse(null);
        return res == null ?
                new ResponseEntity<>(HttpStatus.NOT_FOUND) :
                new ResponseEntity<>(res, HttpStatus.OK);
    }


    @PatchMapping("/{khatianId}")
    public ResponseEntity<Khatian> updateKhatianMaster(
            @RequestHeader(value = HttpHeaders.AUTHORIZATION, required = false) String token,
            @PathVariable String khatianId,
            @RequestBody Khatian khatian
    ) {
        Object[] authData = authService.verifyToken(token);
        if (authData == null || !((Boolean) authData[1]))
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        Khatian res = khatianMasterService.updateKhatianMaster(khatianId, khatian, (String) authData[0]);
        return res == null ?
                new ResponseEntity<>(HttpStatus.NOT_FOUND) :
                new ResponseEntity<>(res, HttpStatus.OK);
    }


    @DeleteMapping("/{khatianId}")
    public ResponseEntity<Void> deleteKhatianMaster(
            @RequestHeader(value = HttpHeaders.AUTHORIZATION, required = false) String token,
            @PathVariable String khatianId
    ) {
        Object[] authData = authService.verifyToken(token);
        if (authData == null || !((Boolean) authData[1]))
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        return khatianMasterService.deleteKhatianMaster(khatianId, (String) authData[0]) ?
                new ResponseEntity<>(HttpStatus.OK) :
                new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
}
