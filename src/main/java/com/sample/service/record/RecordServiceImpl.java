package com.sample.service.record;

import com.sample.model.Mortgaged;
import com.sample.model.PartlySold;
import com.sample.model.Record;
import com.sample.repository.MortgagedRepository;
import com.sample.repository.PartlySoldRepository;
import com.sample.repository.RecordRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
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

        // Ensure collections are initialized
        record.setDocumentFile(record.getDocumentFile() != null ? record.getDocumentFile() : new ArrayList<>());
        record.setScanCopyFile(record.getScanCopyFile() != null ? record.getScanCopyFile() : new ArrayList<>());
        record.setConversionFile(record.getConversionFile() != null ? record.getConversionFile() : new ArrayList<>());
        record.setAreaMapFile(record.getAreaMapFile() != null ? record.getAreaMapFile() : new ArrayList<>());
        record.setMutationFile(record.getMutationFile() != null ? record.getMutationFile() : new ArrayList<>());
        record.setHcdocumentFile(record.getHcdocumentFile() != null ? record.getHcdocumentFile() : new ArrayList<>());

        long slno = recordRepository.count();
        String insertedBy = "working";
        record.setRecId("REC" + slno);
        record.setModified_type("INSERTED");
        record.setInserted_on(ldt);
        record.setInserted_by(insertedBy);
        record.setUpdated_by("NA");
        record.setUpdated_on(ldt);
        record.setDeleted_by("NA");
        record.setDeleted_on(ldt);

        Set<PartlySold> partly = new HashSet<>(record.getPartlySoldData() != null ? record.getPartlySoldData() : new HashSet<>());
        int count = 0;
        for (PartlySold partlySold : partly) {
            long partIdSuffix = recordRepository.count() + count;
            count++;

            partlySold.setPartId("PART-" + partIdSuffix);
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

        Set<Mortgaged> mortgageds = new HashSet<>(record.getMortgagedData() != null ? record.getMortgagedData() : new HashSet<>());
        count = 0;
        for (Mortgaged mort : mortgageds) {
            long mortId = recordRepository.count() + count;
            count++;

            mort.setMortId("MORTGAGED" + mortId);
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
    public Record getRecordById(String id) {
        Optional<Record> optionalRecord = recordRepository.findByRecId(id);
        return optionalRecord.orElse(null);
    }

    @Override
    public List<Record> getAllRecords() {
        List<Record>recordList=recordRepository.findAll();
        for(Record record : recordList){
            record.setMortgagedData(mortgagedRepository.findAllActive(record.getRecId()));
            record.setPartlySoldData(partlySoldRepository.findAllActive(record.getRecId()));
        }
        return recordList;
    }

    @Override
    @Transactional
    public Record updateRecord(Record updatedRecord, String id) {
        LocalDateTime ldt = LocalDateTime.now();
        Optional<Record> optionalOldRecord = recordRepository.findByRecId(id);
        
        if (optionalOldRecord.isPresent()) {
            Record oldRecord = optionalOldRecord.get();
            
            // Copy fields from oldRecord to updatedRecord
            updatedRecord.setRecId(id); // Ensure the ID is set correctly
            
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
    public boolean deleteRecord(String id, String username) {
        LocalDateTime ldt = LocalDateTime.now();
        Optional<Record> optionalRecord = recordRepository.findByRecId(id);
        if(optionalRecord.isPresent()) {
            Record record = optionalRecord.get();
            record.setDeleted_by(username);
            record.setDeleted_on(ldt);
            record.setModified_type("DELETED");
            recordRepository.save(record);
            return true;
        } else {
            return false;
        }
    }
    

    @Override
    public ResponseEntity<String> saveAttachment(String fieldName, String id, byte[] blobData, String originalFileName, String ext) {
    	String uploadDir = env.getProperty("upload.dir"); // Assuming a property named upload.dir is defined

        if (uploadDir == null || uploadDir.isEmpty()) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Upload directory not configured! Please set 'upload.dir' property.");
        }

        Record record = recordRepository.findByRecId(id).orElse(null);
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
    public boolean deleteFile(String id, String fieldName, String fileName) {
        // Update record in database to remove the filename from the appropriate list
        	Record record = recordRepository.findByRecId(id).orElse(null);

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
    
    

