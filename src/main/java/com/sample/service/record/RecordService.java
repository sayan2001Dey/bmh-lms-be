package com.sample.service.record;

import com.sample.dto.record.RecordReq;
import com.sample.dto.record.RecordRes;
import com.sample.model.Record;

import java.util.List;

import org.springframework.http.ResponseEntity;

public interface RecordService {

    String saveRecord(RecordReq recordReq, String username);

    RecordRes getRecordById(String id);

    List<RecordRes> getAllRecords();

    Record updateRecord(Record record, String id, String username);

    boolean deleteRecord(String id, String username);
    
    ResponseEntity<String> saveAttachment(String fieldName, String id, byte[] blobData,String originalFileName, String ext);
    
    byte[] getFileBytes(String fieldName, String fileName);
    
	boolean deleteFile(String id, String fieldName, String fileName);
}
