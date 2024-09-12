package com.bmh.lms.service.deed;

import com.bmh.lms.dto.deed.DeedReq;
import com.bmh.lms.dto.deed.DeedRes;
import com.bmh.lms.dto.record.RecordReq;
import com.bmh.lms.model.Deed;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface DeedService {

    DeedRes createDeed(DeedReq deedReq, String username);

    List<DeedRes> getAllDeed();

    DeedRes getDeedById(String deed_id);

    DeedRes updateDeed(DeedReq deeedReq, String deed_Id, String username);

    boolean deleteDeed(String deed_id, String username);

    byte[] getFileBytes(String fieldName, String fileName);

    ResponseEntity<String> saveAttachment(String fieldName, String id, byte[] blobData, String originalFileName, String ext, String insideId, String username);

    boolean deleteFile(String id, String fieldName, String fileName, String insideId, String username);

}
