package com.sample.controller;
import com.sample.dto.record.RecordReq;
import com.sample.dto.record.RecordRes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.sample.model.Record;
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
    
    // Save Record
    @PostMapping()
    public ResponseEntity<String> saveRecord(@RequestBody RecordReq recordReq) {
        recordService.saveRecord(recordReq, "NA");
        return new ResponseEntity<>("🔥", HttpStatus.OK);
    }    

    // Get all Records
    @GetMapping()
    public ResponseEntity<List<RecordRes>> getAllRecords() {
        List<RecordRes> records = recordService.getAllRecords();
        return new ResponseEntity<>(records, HttpStatus.OK);
    }

    // Get a single Record by ID
    @GetMapping("{id}")
    public ResponseEntity<RecordRes> getRecordById(@PathVariable String id) {
        RecordRes recordRes = recordService.getRecordById(id);
        if (recordRes != null) {
            return new ResponseEntity<>(recordRes, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    // Update an existing Record
    @PatchMapping("{id}")
    public ResponseEntity<Record> updateRecord(@PathVariable String id, @RequestBody Record record) {
    	Record updatedRecord = recordService.updateRecord(record, id, "NA");

    	if(updatedRecord != null) {
            return new ResponseEntity<>(updatedRecord, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    // Delete a Record
    @DeleteMapping("{id}")
    public ResponseEntity<Void> deleteRecord(@PathVariable String id) {
        if (recordService.deleteRecord(id, "NA")) {
            return new ResponseEntity<>(HttpStatus.ACCEPTED);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}