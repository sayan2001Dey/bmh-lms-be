package com.sample.service.record;

import com.sample.model.Record;


import java.util.List;

import org.springframework.http.ResponseEntity;

public interface RecordService {

    Record saveRecord(Record record);

    Record getRecordById(Long id);

    List<Record> getAllRecords();

    Record updateRecord(Record record, Long id);

    void deleteRecord(Long id);
    
     ResponseEntity<String> saveAttachment(String fieldName, Long id, byte[] blobData,String originalFileName, String ext);
    
    byte[] getFileBytes(String fieldName, String fileName);
    
	boolean deleteFile(Long id, String fieldName, String fileName);
}
