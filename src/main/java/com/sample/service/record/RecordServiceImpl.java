package com.sample.service.record;

import com.sample.model.Mortgaged;
import com.sample.model.PartlySold;
import com.sample.model.Record;


import com.sample.repository.MortgagedRepository;
import com.sample.repository.PartlySoldRepository;
import com.sample.repository.RecordRepository;

import org.hibernate.annotations.OptimisticLock;
import org.hibernate.annotations.OptimisticLocking;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.*;

import javax.persistence.LockModeType;

@Service
public class RecordServiceImpl implements RecordService {

    @Autowired
    private RecordRepository recordRepository;

    @Autowired
    private PartlySoldRepository partlySoldRepository;

    @Autowired
    private MortgagedRepository mortgagedRepository;

    @Autowired
    private Environment env;

    @Override
    public Record saveRecord(Record record) {
        LocalDateTime ldt = LocalDateTime.now();

        record.setDocumentFile(new ArrayList<>());
        record.setScanCopyFile(new ArrayList<>());
        record.setConversionFile(new ArrayList<>());
        record.setAreaMapFile(new ArrayList<>());
        record.setMutationFile(new ArrayList<>());
        record.setHcdocumentFile(new ArrayList<>());


        long slno=0;
        if(recordRepository.count() != 0){
            slno=recordRepository.count();
        }
        String insertedBy = "working";
        record.setRecId("REC" +  slno);
        record.setModified_type("INSERTED");
        record.setInserted_on(ldt);
        record.setInserted_by(insertedBy);
        record.setUpdated_by("NA");
        record.setUpdated_on(ldt);
        record.setDeleted_by("NA");
        record.setDeleted_on(ldt);

        Set<PartlySold> partly=new HashSet<PartlySold>();
        partly.addAll(record.getPartlySoldData());
        int count = 0;
        for(PartlySold partlySold : partly ){
             long partIdSuffix=0;
             if(recordRepository.count()!=0){
                 partIdSuffix  = partlySoldRepository.count()+ count;
                 count++;
             }

            partlySold.setPartId("COURSE-" + partIdSuffix);
            partlySold.setRecId(record.getRecId());
            partlySold.setModified_type("INSERTED");
            partlySold.setInserted_by(insertedBy);
            partlySold.setInserted_on(ldt);
            partlySold.setUpdated_by("NA");
            partlySold.setUpdated_on(ldt);
            partlySold.setDeleted_by("NA");
            partlySold.setDeleted_on(ldt);
        }
        record.setPartlySoldData(partly);

        Set<Mortgaged> mortgageds=new HashSet<Mortgaged>();
        mortgageds.addAll(record.getMortgagedData());
        count=0;
        for (Mortgaged mort:mortgageds){
            long mortId=0;
            if(recordRepository.count() != 0 ) {
                mortId=mortgagedRepository.count()+count;
                count++;
            }
            mort.setMortId("MORTGAGED"+mortId);
            mort.setRecId(record.getRecId());
            mort.setModified_type("INSERTED");
            mort.setInserted_by(insertedBy);
            mort.setInserted_on(ldt);
            mort.setUpdated_by("NA");
            mort.setUpdated_on(ldt);
            mort.setDeleted_by("NA");
            mort.setDeleted_on(ldt);

        }
        record.setMortgagedData(mortgageds);
        return recordRepository.save(record);
    }

    @Override
    public Record getRecordById(Long id) {
        Optional<Record> optionalRecord = recordRepository.findById(id);
        return optionalRecord.orElse(null);
    }

    @Override
    public List<Record> getAllRecords() {
        return recordRepository.findAll();
    }

    @Override
    @Transactional
    public Record updateRecord(Record updatedRecord, Long id) {
        Optional<Record> optionalOldRecord = recordRepository.findById(id);
        
        if (optionalOldRecord.isPresent()) {
            Record oldRecord = optionalOldRecord.get();
            
            // Copy fields from oldRecord to updatedRecord
            updatedRecord.setId(id); // Ensure the ID is set correctly
            
            // Copy collections if needed (assuming these are lists of filenames)
            updatedRecord.setScanCopyFile(new ArrayList<>(oldRecord.getScanCopyFile()));
            updatedRecord.setConversionFile(new ArrayList<>(oldRecord.getConversionFile()));
            updatedRecord.setDocumentFile(new ArrayList<>(oldRecord.getDocumentFile()));
            updatedRecord.setAreaMapFile(new ArrayList<>(oldRecord.getAreaMapFile()));
            updatedRecord.setMutationFile(new ArrayList<>(oldRecord.getMutationFile()));
            updatedRecord.setHcdocumentFile(new ArrayList<>(oldRecord.getHcdocumentFile()));
            
            return recordRepository.save(updatedRecord); // Update the existing record
        }
        
        return null; // Handle case where record with given ID does not exist
    }

    

    @Override
    public void deleteRecord(Long id) {
        recordRepository.deleteById(id);
    }
    

    @Override
    public ResponseEntity<String> saveAttachment(String fieldName, Long id, byte[] blobData, String originalFileName, String ext) {
    	String uploadDir = env.getProperty("upload.dir"); // Assuming a property named upload.dir is defined

        if (uploadDir == null || uploadDir.isEmpty()) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Upload directory not configured! Please set 'upload.dir' property.");
        }

        Record record = recordRepository.findById(id).orElse(null);
        if (record == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Entry not found.");
        }

        String fileName = originalFileName +"-"+ UUID.randomUUID().toString().substring(0,5) + id +'.' + ext;

        switch (fieldName) {
            case "scanCopyFile":
                record.getScanCopyFile().add(fileName);
                break;
            case "mutationFile":
                record.getMutationFile().add(fileName);
                break;
            case "conversionFile":
                record.getConversionFile().add(fileName);
                break;
            case "documentFile":
                record.getDocumentFile().add(fileName);
                break;
            case "areaMapFile":
                record.getAreaMapFile().add(fileName);
                break;
            case "hcdocumentFile":
                record.getHcdocumentFile().add(fileName);
                break;
            default:
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
        recordRepository.save(record);
        return ResponseEntity.ok("File uploaded successfully: " + fileName);
    }
    
    
    @Override
    public byte[] getFileBytes(String fieldName, String fileName) {
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
//    @Lock(LockModeType.PESSIMISTIC_READ)
    public boolean deleteFile(Long id, String fieldName, String fileName) {
        // Update record in database to remove the filename from the appropriate list
        	Record record = recordRepository.findById(id).orElse(null);

            boolean removed = false;
            if(record == null) return false;
            switch (fieldName) {
                case "scanCopyFile":
                	removed = record.getScanCopyFile().remove(fileName);
                    break;
                case "mutationFile":
                    removed = record.getMutationFile().remove(fileName);
                    break;
                case "conversionFile":
                    removed = record.getConversionFile().remove(fileName);
                    break;
                case "documentFile":
                    removed = record.getDocumentFile().remove(fileName);
                    break;
                case "areaMapFile":
                    removed = record.getAreaMapFile().remove(fileName);
                    break;
                case "hcdocumentFile":
                    removed = record.getHcdocumentFile().remove(fileName);
                    break;
            }

            // Save the record only if a file was removed
            if (removed) {
                recordRepository.save(record);
            }

        // Return true indicating successful deletion from database
        return removed;
       }
    }
    
    

