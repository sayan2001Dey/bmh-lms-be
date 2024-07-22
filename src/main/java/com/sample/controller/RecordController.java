package com.sample.controller;
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
    public Record saveRecord(@RequestBody Record record) {
        
        return recordService.saveRecord(record);
    }    

    // Get all Records
    @GetMapping()
    public ResponseEntity<List<Record>> getAllRecords() {
        List<Record> records = recordService.getAllRecords();
        return new ResponseEntity<>(records, HttpStatus.OK);
    }

    // Get a single Record by ID
    @GetMapping("{id}")
    public ResponseEntity<Record> getRecordById(@PathVariable Long id) {
        Record record = recordService.getRecordById(id);
        if (record != null) {
            return new ResponseEntity<>(record, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    // Update an existing Record
    @PatchMapping("{id}")
    public ResponseEntity<Record> updateRecord(@PathVariable Long id, @RequestBody Record record) {
    	Record updatedRecord = recordService.updateRecord(record, id);

    	if(updatedRecord != null) {
            return new ResponseEntity<>(updatedRecord, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    // Delete a Record
    @DeleteMapping("{id}")
    public ResponseEntity<Void> deleteRecord(@PathVariable Long id) {
        Record record = recordService.getRecordById(id);
        if (record != null) {
            recordService.deleteRecord(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}