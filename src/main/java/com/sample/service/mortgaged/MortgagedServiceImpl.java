package com.sample.service.mortgaged;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.sample.model.Mortgaged;
import com.sample.repository.MortgagedRepository;

@Service
public class MortgagedServiceImpl implements MortgagedService {
	
	@Autowired
	private MortgagedRepository mortgagedRepository;
	
	@Autowired
    private Environment env;


	@Override
	public Mortgaged saveMortgaged(Mortgaged mort) {
//		mort.setDocFile(new ArrayList<>());
		return mortgagedRepository.save(mort);
	}

	@Override
	public Mortgaged getMortgagedById(Long id) {
		Optional<Mortgaged> optionalMortgaged = mortgagedRepository.findById(id);
		return optionalMortgaged.orElse(null) ;
	}

	@Override
	public List<Mortgaged> getAllMortgaged() {
		
		return mortgagedRepository.findAll();
	}

	@Override
	@Transactional
	public Mortgaged updateMortgaged(Mortgaged updatedMortgage, Long id) {
	    Optional<Mortgaged> optionalMortgaged = mortgagedRepository.findById(id);
	    if (optionalMortgaged.isPresent()) {
	        Mortgaged existingMortgage = optionalMortgaged.get();
	        
	        // Copy updated fields to existing entity
	        existingMortgage.setParty(updatedMortgage.getParty());
	        existingMortgage.setMortDate(updatedMortgage.getMortDate());
//	        existingMortgage.setDocFile(new ArrayList<>(updatedMortgage.getDocFile()));

	        
	        return mortgagedRepository.save(existingMortgage);
	    }
	    return null; 
	}

	@Override
	public void deleteMortgaged(Long id) {
		
		 mortgagedRepository.deleteById(id);
		
	}
	
	
	    @Override
	    public ResponseEntity<String> saveDoc(String fieldName, Long id, byte[] blobData, String originalFileName, String ext) {
	    	String uploadDir = env.getProperty("upload.dir"); // Assuming a property named upload.dir is defined

	        if (uploadDir == null || uploadDir.isEmpty()) {
	            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
	                    .body("Upload directory not configured! Please set 'upload.dir' property.");
	        }

	        Mortgaged mort = mortgagedRepository.findById(id).orElse(null);
	        if (mort == null) {
	            return ResponseEntity.status(HttpStatus.NOT_FOUND)
	                    .body("Entry not found.");
	        }

	        String fileName = originalFileName +"-"+ UUID.randomUUID().toString().substring(0,5) + id +'.' + ext;

	        if ("docFile".equals(fieldName)) {
//	            mort.getDocFile().add(fileName);
	        } else {
	            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
	                    .body("Unsupported field name: " + fieldName);
	        }

	        try{
	            Files.write(Paths.get(uploadDir, fieldName, fileName), blobData);
	        } catch (IOException e) {
	            e.printStackTrace();
	            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
	                    .body("Failed to upload file: " + e.getMessage());
	        }
	        mortgagedRepository.save(mort);
	        return ResponseEntity.ok("File uploaded successfully: " + fileName);
	    }
	    
	 @Override
	    public byte[] getDocBytes(String fieldName, String fileName) {
	        String uploadDir = env.getProperty("upload.dir"); // Assuming a property named upload.dir is defined

	        if (uploadDir == null || uploadDir.isEmpty()) {
	            throw new IllegalStateException("Upload directory not configured! Please set 'upload.dir' property.");
	        }

	        Path filePath = Paths.get(uploadDir, fieldName, fileName);

	        try {
	            return Files.readAllBytes(filePath);
	        } catch (IOException e) {
	            throw new RuntimeException("Failed to read file: " + fileName, e);
	        }
	    }
	 
	 
	 
		@Override
		@Transactional
		public boolean deleteDoc(String fieldName, String fileName) {
			Iterable<Mortgaged> allMort = mortgagedRepository.findAll();

	        for (Mortgaged mort : allMort) {
	            boolean removed = false;
	            
	            if ("docFile".equals(fieldName)) {
//	                removed = mort.getDocFile().remove(fileName);
	            }

	            if (removed) {
	                mortgagedRepository.save(mort);
	            }
	        }
	        return true;
		}

}
