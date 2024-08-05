package com.sample.controller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.sample.service.record.RecordService;

import java.util.Optional;

@RestController
@CrossOrigin
@RequestMapping("/api/landrecord/attachments")
public class FileController {

    @Autowired
    private RecordService recordService;
    
    
    @PostMapping("/{fieldName}")
    public ResponseEntity<String> uploadFile(@RequestBody byte[] fileBytes,
                                             @PathVariable String fieldName,
                                             @RequestParam(value = "id") String id,
                                             @RequestParam(value = "file") String originalFileName,
                                             @RequestParam(value = "ext") String extension,
                                             @RequestParam(value = "mort", required = false) String mortId) {
        return recordService.saveAttachment(fieldName, id, fileBytes, originalFileName, extension, mortId, "NA");
    }
    
    

    @GetMapping("/{fieldName}/{fileName}")
    public ResponseEntity<byte[]> getFile(@PathVariable String fieldName,@PathVariable String fileName) {
    	 byte[] fileBytes = recordService.getFileBytes(fieldName, fileName);
    	    if (fileBytes == null) {
    	        return ResponseEntity.notFound().build();
    	    }
    	   
    	    HttpHeaders headers = new HttpHeaders();

            int extPtr = -1;
            for(int i=fileName.length()-1; -1<i; i--) {
                if(fileName.charAt(i)=='.') {
                    extPtr = i;
                    break;
                }
            }

            if(extPtr>-1) {
                String ext = fileName.substring(extPtr+1);
                switch (ext.toLowerCase()) {
                    case "pdf":
    	                headers.setContentType(MediaType.APPLICATION_PDF);
                        break;
                    case "jpg":
                    case "jpeg":
                        headers.setContentType(MediaType.IMAGE_JPEG);
                        break;
                    case "png":
                        headers.setContentType(MediaType.IMAGE_PNG);
                        break;
                    default:
                        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
                }

            }

    	    return ResponseEntity.ok()
    	            .headers(headers)
    	            .body(fileBytes);
    }
    
    
   
    @DeleteMapping("/{fieldName}")
    public ResponseEntity<Void> deleteFile(@PathVariable String fieldName,
    		 @RequestParam(value = "id") String id,
             @RequestParam(value = "filename") String fileName,
             @RequestParam(value = "mort", required = false) String mortId) {
        try {
            // Assuming recordService.deleteFile returns true if deletion is successful
            boolean deletionSuccessful = recordService.deleteFile(id, fieldName, fileName, mortId, "NA");
            
            if (deletionSuccessful) {
                return ResponseEntity.accepted().build(); // 204 No Content
            } else {
                return ResponseEntity.notFound().build(); // 404 Not Found
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    


}
