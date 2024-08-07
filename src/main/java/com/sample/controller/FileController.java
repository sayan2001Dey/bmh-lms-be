package com.sample.controller;
import com.sample.service.auth.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.sample.service.record.RecordService;

@RestController
@CrossOrigin
@RequestMapping("/api/landrecord/attachments")
public class FileController {

    @Autowired
    private RecordService recordService;

    @Autowired
    private AuthService authService;
    
    
    @PostMapping("/{fieldName}")
    public ResponseEntity<String> uploadFile(
            @RequestHeader(value = HttpHeaders.AUTHORIZATION, required = false) String token,
            @RequestBody byte[] fileBytes,
            @PathVariable String fieldName,
            @RequestParam(value = "id") String id,
            @RequestParam(value = "file") String originalFileName,
            @RequestParam(value = "ext") String extension,
            @RequestParam(value = "mort", required = false) String mortId
    ) {
        Object[] authData = authService.verifyToken(token);
        if(authData == null) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }

        return recordService.saveAttachment(fieldName, id, fileBytes, originalFileName, extension, mortId, (String) authData[0]);
    }
    
    

    @GetMapping("/{fieldName}/{fileName}")
    public ResponseEntity<byte[]>
    getFile(@RequestHeader(value = HttpHeaders.AUTHORIZATION, required = false) String token,
            @PathVariable String fieldName,
            @PathVariable String fileName) {

        Object[] authData = authService.verifyToken(token);
        if(authData == null) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }

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
    public ResponseEntity<Void> deleteFile(
            @RequestHeader(value = HttpHeaders.AUTHORIZATION, required = false) String token,
            @PathVariable String fieldName,
            @RequestParam(value = "id") String id,
            @RequestParam(value = "filename") String fileName,
            @RequestParam(value = "mort", required = false) String mortId
    ) {
        Object[] authData = authService.verifyToken(token);
        if(authData == null) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }

        try {
            // Assuming recordService.deleteFile returns true if deletion is successful
            boolean deletionSuccessful = recordService.deleteFile(id, fieldName, fileName, mortId, (String) authData[0]);
            
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
