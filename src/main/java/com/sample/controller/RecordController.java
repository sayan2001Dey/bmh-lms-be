package com.sample.controller;
import com.sample.dto.record.RecordReq;
import com.sample.dto.record.RecordRes;
import com.sample.service.auth.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.sample.service.record.RecordService;
import java.util.List;

import javax.servlet.annotation.MultipartConfig;

@MultipartConfig
@RestController
@CrossOrigin()
@RequestMapping("api/landrecord")
public class RecordController {

    @Autowired
    private RecordService recordService;

    @Autowired
    private AuthService authService;
    
    // Save Record
    @PostMapping()
    public ResponseEntity<String> saveRecord(
            @RequestHeader(value = HttpHeaders.AUTHORIZATION, required = false) String token,
            @RequestBody RecordReq recordReq) {
        Object[] authData = authService.verifyToken(token);
        if(authData == null) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }

        return new ResponseEntity<>(recordService.saveRecord(recordReq, (String) authData[0]), HttpStatus.OK);
    }    

    // Get all Records
    @GetMapping()
    public ResponseEntity<List<RecordRes>> getAllRecords(
            @RequestHeader(value = HttpHeaders.AUTHORIZATION, required = false) String token
    ) {
        Object[] authData = authService.verifyToken(token);
        if(authData == null) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }

        List<RecordRes> records = recordService.getAllRecords();
        return new ResponseEntity<>(records, HttpStatus.OK);
    }

    // Get a single Record by ID
    @GetMapping("{id}")
    public ResponseEntity<RecordRes> getRecordById(
            @RequestHeader(value = HttpHeaders.AUTHORIZATION, required = false) String token,
            @PathVariable String id
    ) {
        Object[] authData = authService.verifyToken(token);
        if(authData == null) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }

        RecordRes recordRes = recordService.getRecordById(id);
        if (recordRes != null) {
            return new ResponseEntity<>(recordRes, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    // Update an existing Record
    @PatchMapping("{id}")
    public ResponseEntity<RecordRes> updateRecord(
            @RequestHeader(value = HttpHeaders.AUTHORIZATION,required = false) String token,
            @PathVariable String id,
            @RequestBody RecordReq recordReq) {
        Object[] authData = authService.verifyToken(token);
        if(authData == null) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }

    	RecordRes updatedRecord = recordService.updateRecord(recordReq, id, (String) authData[0]);

    	if(updatedRecord != null) {
            return new ResponseEntity<>(updatedRecord, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    // Delete a Record
    @DeleteMapping("{id}")
    public ResponseEntity<Void> deleteRecord(
            @RequestHeader(value = HttpHeaders.AUTHORIZATION, required = false) String token,
            @PathVariable String id
    ) {
        Object[] authData = authService.verifyToken(token);
        if(authData == null) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
        if (recordService.deleteRecord(id, (String) authData[0])) {
            return new ResponseEntity<>(HttpStatus.ACCEPTED);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}