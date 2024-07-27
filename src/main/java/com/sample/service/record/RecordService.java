package com.sample.service.record;

import com.sample.model.Record;


import java.util.List;

import org.springframework.http.ResponseEntity;

public interface RecordService {

    Record saveRecord(Record record);

    Record getRecordById(String id);

    List<Record> getAllRecords();

    Record updateRecord(Record record, String id);

    boolean deleteRecord(String id, String username);
    
    ResponseEntity<String> saveAttachment(String fieldName, String id, byte[] blobData,String originalFileName, String ext);
    
    byte[] getFileBytes(String fieldName, String fileName);
    
	boolean deleteFile(String id, String fieldName, String fileName);
}
