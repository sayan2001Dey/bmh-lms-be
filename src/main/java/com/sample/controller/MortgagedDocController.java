package com.sample.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.sample.service.mortgaged.MortgagedService;

@RestController
@CrossOrigin
@RequestMapping("/api/mortgaged/doc")
public class MortgagedDocController {

	@Autowired
	private MortgagedService mortgagedService;
	
	@PostMapping("/{fieldName}")
	public ResponseEntity<String> uploadDoc(@RequestBody byte[] docBytes,
			                                @PathVariable String fieldName,
			                                @RequestParam(value= "id") Long id,
			                                @RequestParam(value = "file")String originalFileName,
			                                @RequestParam(value = "ext") String extension){
		return mortgagedService.saveDoc(fieldName, id, docBytes, originalFileName, extension);
	}
	
	
	@GetMapping("/{fieldName}/{fileName}")
    public ResponseEntity<byte[]> getFile(@PathVariable String fieldName,@PathVariable String fileName) {
    	 byte[] fileBytes = mortgagedService.getDocBytes(fieldName, fileName);
    	    if (fileBytes == null) {
    	        return ResponseEntity.notFound().build();
    	    }
    	   
    	    HttpHeaders headers = new HttpHeaders();
    	    headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);

    	    return ResponseEntity.ok()
    	            .headers(headers)
    	            .body(fileBytes);
    }
    
    
    @DeleteMapping("/{fieldName}/{fileName}")
    public ResponseEntity<Void> deleteFile(@PathVariable String fieldName,
                                           @PathVariable String fileName) {
        try {
            // Assuming recordService.deleteFile returns true if deletion is successful
            boolean deletionSuccessful = mortgagedService.deleteDoc(fieldName, fileName);
            
            if (deletionSuccessful) {
                return ResponseEntity.noContent().build(); // 204 No Content
            } else {
                return ResponseEntity.notFound().build(); // 404 Not Found
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
	
}
