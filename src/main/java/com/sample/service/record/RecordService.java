package com.sample.service.record;

import com.sample.dto.record.RecordReq;
import com.sample.dto.record.RecordRes;

import java.util.List;

import org.springframework.http.ResponseEntity;

public interface RecordService {

    String saveRecord(RecordReq recordReq, String username);

    RecordRes getRecordById(String id);

    List<RecordRes> getAllRecords();

    RecordRes updateRecord(RecordReq recordReq, String recId, String username);

    boolean deleteRecord(String id, String username);

    byte[] getFileBytes(String fieldName, String fileName);

    ResponseEntity<String> saveAttachment(String fieldName, String id, byte[] blobData, String originalFileName, String ext, String insideId, String username);

	boolean deleteFile(String id, String fieldName, String fileName, String insideId, String username);
}
