package com.sample.service.mortgaged;

import java.util.List;


import org.springframework.http.ResponseEntity;

import com.sample.model.Mortgaged;

public interface MortgagedService {

	
	Mortgaged saveMortgaged(Mortgaged mort);
	
	Mortgaged getMortgagedById(Long id);
	
	List<Mortgaged> getAllMortgaged();
	
	Mortgaged updateMortgaged(Mortgaged mort,Long id);
	
	void deleteMortgaged(Long id);
	
    ResponseEntity<String> saveDoc(String fieldName, Long id, byte[] blobData,String originalFileName, String ext);
    
    byte[] getDocBytes(String fieldName, String fileName);
    
	boolean deleteDoc(String fieldName, String fileName);
}
