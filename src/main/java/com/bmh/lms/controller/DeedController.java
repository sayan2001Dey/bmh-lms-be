package com.bmh.lms.controller;

import com.bmh.lms.dto.deed.DeedReq;
import com.bmh.lms.dto.deed.DeedRes;
import com.bmh.lms.dto.record.RecordRes;
import com.bmh.lms.model.Deed;
import com.bmh.lms.service.auth.AuthService;
import com.bmh.lms.service.deed.DeedService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("/api/deed")
public class DeedController {

    @Autowired
    private DeedService deedService;

    @Autowired
    private AuthService authService;

    @PostMapping
    public ResponseEntity<DeedRes> createDeed(
            @RequestHeader(value = HttpHeaders.AUTHORIZATION, required = false) String token,
            @RequestBody DeedReq deedReq
    ) {
        Object[] authData = authService.verifyToken(token);
        if (authData == null) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
        return new ResponseEntity<>(deedService.createDeed(deedReq,(String) authData[0]), HttpStatus.OK);

    }

    @GetMapping
    public ResponseEntity<List<DeedRes>> getAllDeed(
            @RequestHeader(value = HttpHeaders.AUTHORIZATION, required = false) String token
    ) {
        Object[] authData = authService.verifyToken(token);
        if(authData == null) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }

        List<DeedRes> deeds = deedService.getAllDeed();
        return new ResponseEntity<>(deeds, HttpStatus.OK);
    }

    @GetMapping("/{deed_id}")
    public ResponseEntity<DeedRes> getDeedById(
            @RequestHeader(value = HttpHeaders.AUTHORIZATION, required = false) String token,
            @PathVariable String deed_id
    ) {
        Object[] authData = authService.verifyToken(token);
        if (authData == null) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
       DeedRes deedRes = deedService.getDeedById(deed_id);
        if (deedRes != null) {
            return new ResponseEntity<>(deedRes, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PatchMapping("/{deed_id}")
    public ResponseEntity<DeedRes> updateDeed(
            @RequestHeader(value = HttpHeaders.AUTHORIZATION, required = false) String token,
            @PathVariable String deed_id,
            @RequestBody DeedReq deedReq
    ) {
        Object[] authData = authService.verifyToken(token);
        if (authData == null) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
        DeedRes updatedDeed = deedService.updateDeed(deedReq, deed_id, (String) authData[0]);
        if(updatedDeed != null) {
            return new ResponseEntity<>(updatedDeed,HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/{deed_id}")
    public ResponseEntity<Void> deleteDeed(
            @RequestHeader(value = HttpHeaders.AUTHORIZATION, required = false) String token,
            @PathVariable String deed_id
    )  {
        Object[] authData = authService.verifyToken(token);
        if(authData == null) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
        if (deedService.deleteDeed(deed_id, (String) authData[0])) {
            return new ResponseEntity<>(HttpStatus.ACCEPTED);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}


