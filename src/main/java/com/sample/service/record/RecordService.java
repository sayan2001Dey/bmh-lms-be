package com.sample.service.record;

import com.sample.model.Record;


import java.util.List;

import org.springframework.http.ResponseEntity;

public interface RecordService {

    Record saveRecord(Record record, String username);

    Record getRecordById(String id);

    List<Record> getAllRecords();

    Record updateRecord(Record record, String id, String username);

    boolean deleteRecord(String id, String username);
    
    ResponseEntity<String> saveAttachment(String fieldName, String id, byte[] blobData,String originalFileName, String ext);
    
    byte[] getFileBytes(String fieldName, String fileName);
    
	boolean deleteFile(String id, String fieldName, String fileName);
}
