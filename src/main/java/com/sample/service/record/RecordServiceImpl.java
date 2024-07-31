package com.sample.service.record;

import com.sample.dto.record.RecordReq;
import com.sample.dto.record.RecordRes;
import com.sample.model.FileUpload;
import com.sample.model.Mortgaged;
import com.sample.model.PartlySold;
import com.sample.model.Record;
import com.sample.repository.FileUploadRepository;
import com.sample.repository.MortgagedRepository;
import com.sample.repository.PartlySoldRepository;
import com.sample.repository.RecordRepository;
import com.sample.service.utils.CommonUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.*;


@Service
public class RecordServiceImpl implements RecordService {

    @Autowired
    private RecordRepository recordRepository;

    @Autowired
    private PartlySoldRepository partlySoldRepository;

    @Autowired
    private MortgagedRepository mortgagedRepository;

    @Autowired
    private FileUploadRepository fileUploadRepository;

    @Autowired
    private Environment env;

    @Autowired
    private CommonUtils commonUtils;

    @Override
    @Transactional
    public String saveRecord(RecordReq recordReq, String username) {
        LocalDateTime ldt = LocalDateTime.now();

        Record record = basicDataFromReqDTO(null, recordReq);

        record.setRecId(commonUtils.generateUID("Record", "REC"));
        record.setModified_type("INSERTED");
        record.setInserted_on(ldt);
        record.setInserted_by(username);
        record.setUpdated_by("NA");
        record.setUpdated_on(null);
        record.setDeleted_by("NA");
        record.setDeleted_on(null);

        if(recordReq.getMortgaged().equalsIgnoreCase("true")) {
            Set<Mortgaged> mortgagedData = recordReq.getMortgagedData();
            mortgagedRepository.saveAll(mortgagedData);
        }

        Set<PartlySold> partlySoldData = recordReq.getPartlySoldData();
        if(recordReq.getPartlySold().equalsIgnoreCase("true")) {
            for(PartlySold partlySold : partlySoldData) {
                partlySold.setRecId(record.getRecId());
                partlySold.setPartId(commonUtils.generateUID("PartlySold", "PART"));
                partlySold.setModified_type("INSERTED");
                partlySold.setInserted_on(ldt);
                partlySold.setInserted_by(username);
                partlySold.setUpdated_by("NA");
                partlySold.setUpdated_on(null);
                partlySold.setDeleted_by("NA");
                partlySold.setDeleted_on(null);
            }
        }

        Set<Mortgaged> mortgagedData = recordReq.getMortgagedData();
        if(recordReq.getPartlySold().equalsIgnoreCase("true")) {
            for(Mortgaged mortgaged : mortgagedData) {
                mortgaged.setRecId(record.getRecId());
                mortgaged.setMortId(commonUtils.generateUID("Mortgaged", "MORT"));
                mortgaged.setModified_type("INSERTED");
                mortgaged.setInserted_on(ldt);
                mortgaged.setInserted_by(username);
                mortgaged.setUpdated_by("NA");
                mortgaged.setUpdated_on(null);
                mortgaged.setDeleted_by("NA");
                mortgaged.setDeleted_on(null);
            }
        }

        partlySoldRepository.saveAll(partlySoldData);
        mortgagedRepository.saveAll(mortgagedData);
        return recordRepository.save(record).getRecId();
    }

    private Record basicDataFromReqDTO(Record dest, RecordReq src) {
        if(dest == null)
            dest = new Record();
        dest.setGroupName(src.getGroupName());
        dest.setState(src.getState());
        dest.setCity(src.getCity());
        dest.setMouza(src.getMouza());
        dest.setBlock(src.getBlock());
        dest.setJLno(src.getJLno());
        dest.setBuyerOwner(src.getBuyerOwner());
        dest.setSellers(src.getSellers());
        dest.setDeedName(src.getDeedName());
        dest.setDeedNo(src.getDeedNo());
        dest.setDeedDate(src.getDeedDate());
        dest.setOldRsDag(src.getOldRsDag());
        dest.setNewLrDag(src.getNewLrDag());
        dest.setOldKhatian(src.getOldKhatian());
        dest.setNewKhatian(src.getNewKhatian());
        dest.setCurrKhatian(src.getCurrKhatian());
        dest.setTotalQty(src.getTotalQty());
        dest.setPurQty(src.getPurQty());
        dest.setMutedQty(src.getMutedQty());
        dest.setUnMutedQty(src.getUnMutedQty());
        dest.setLandStatus(src.getLandStatus());
        dest.setConversionLandStus(src.getConversionLandStus());
        dest.setDeedLoc(src.getDeedLoc());
        dest.setPhotoLoc(src.getPhotoLoc());
        dest.setGovtRec(src.getGovtRec());
        dest.setKhazanaStatus(src.getKhazanaStatus());
        dest.setDueDate(src.getDueDate());
        dest.setLegalMatters(src.getLegalMatters());
        dest.setLedueDate(src.getLedueDate());
        dest.setHistoryChain(src.getHistoryChain());

        return dest;
    }

    private RecordRes basicDataToResDTO(RecordRes dest, Record src) {
        if(dest == null)
            dest = new RecordRes();
        dest.setGroupName(src.getGroupName());
        dest.setState(src.getState());
        dest.setCity(src.getCity());
        dest.setMouza(src.getMouza());
        dest.setBlock(src.getBlock());
        dest.setJLno(src.getJLno());
        dest.setBuyerOwner(src.getBuyerOwner());
        dest.setSellers(src.getSellers());
        dest.setDeedName(src.getDeedName());
        dest.setDeedNo(src.getDeedNo());
        dest.setDeedDate(src.getDeedDate());
        dest.setOldRsDag(src.getOldRsDag());
        dest.setNewLrDag(src.getNewLrDag());
        dest.setOldKhatian(src.getOldKhatian());
        dest.setNewKhatian(src.getNewKhatian());
        dest.setCurrKhatian(src.getCurrKhatian());
        dest.setTotalQty(src.getTotalQty());
        dest.setPurQty(src.getPurQty());
        dest.setMutedQty(src.getMutedQty());
        dest.setUnMutedQty(src.getUnMutedQty());
        dest.setLandStatus(src.getLandStatus());
        dest.setConversionLandStus(src.getConversionLandStus());
        dest.setDeedLoc(src.getDeedLoc());
        dest.setPhotoLoc(src.getPhotoLoc());
        dest.setGovtRec(src.getGovtRec());
        dest.setKhazanaStatus(src.getKhazanaStatus());
        dest.setDueDate(src.getDueDate());
        dest.setLegalMatters(src.getLegalMatters());
        dest.setLedueDate(src.getLedueDate());
        dest.setHistoryChain(src.getHistoryChain());

        dest.setRecId(src.getRecId());

        return dest;
    }

    private RecordRes recordResMaker(Record record, String recId) {
        RecordRes res = basicDataToResDTO(null, record);
        res.setMortgagedData(mortgagedRepository.findAllActive(recId));
        res.setPartlySoldData(partlySoldRepository.findAllActive(recId));
        //TODO: file and stuff
        return res;
    }

    @Override
    public RecordRes getRecordById(String id) {
        Optional<Record> optionalRecord = recordRepository.findByRecId(id);
        if (optionalRecord.isPresent()) {
            Record record = optionalRecord.get();
            return recordResMaker(record, id);
        }
        return null;
    }

    @Override
    public List<RecordRes> getAllRecords() {
        List<Record> recordList = recordRepository.findAllActive();
        List<RecordRes> recordResList = new ArrayList<>();
        for(Record record : recordList){
            recordResList.add(recordResMaker(record, record.getRecId()));
        }
        return recordResList;
    }

    @Override
    @Transactional
    public Record updateRecord(Record updatedRecord, String id, String username) {
          LocalDateTime ldt = LocalDateTime.now();

         // Fetch the existing record
//        Record existingRecord = recordRepository.findByRecId(id)
//                .orElseThrow(() -> new EntityNotFoundException("Record with id " + id + " not found"));
//
//        // Update basic fields
//        existingRecord.setGroupName(updatedRecord.getGroupName());
//        existingRecord.setState(updatedRecord.getState());
//        existingRecord.setCity(updatedRecord.getCity());
//        existingRecord.setMouza(updatedRecord.getMouza());
//        existingRecord.setBlock(updatedRecord.getBlock());
//        existingRecord.setJLno(updatedRecord.getJLno());
//        existingRecord.setBuyerOwner(updatedRecord.getBuyerOwner());
//        existingRecord.setSellers(updatedRecord.getSellers());
//        existingRecord.setDeedName(updatedRecord.getDeedName());
//        existingRecord.setDeedNo(updatedRecord.getDeedNo());
//        existingRecord.setDeedDate(updatedRecord.getDeedDate());
//        existingRecord.setScanCopyFile(updatedRecord.getScanCopyFile());
//        existingRecord.setOldRsDag(updatedRecord.getOldRsDag());
//        existingRecord.setNewLrDag(updatedRecord.getNewLrDag());
//        existingRecord.setOldKhatian(updatedRecord.getOldKhatian());
//        existingRecord.setNewKhatian(updatedRecord.getNewKhatian());
//        existingRecord.setCurrKhatian(updatedRecord.getCurrKhatian());
//        existingRecord.setTotalQty(updatedRecord.getTotalQty());
//        existingRecord.setPurQty(updatedRecord.getPurQty());
//        existingRecord.setMutedQty(updatedRecord.getMutedQty());
//        existingRecord.setUnMutedQty(updatedRecord.getUnMutedQty());
//        existingRecord.setMutationFile(updatedRecord.getMutationFile());
//        existingRecord.setLandStatus(updatedRecord.getLandStatus());
//        existingRecord.setConversionLandStus(updatedRecord.getConversionLandStus());
//        existingRecord.setConversionFile(updatedRecord.getConversionFile());
//        existingRecord.setDeedLoc(updatedRecord.getDeedLoc());
//        existingRecord.setPhotoLoc(updatedRecord.getPhotoLoc());
//        existingRecord.setGovtRec(updatedRecord.getGovtRec());
//        existingRecord.setKhazanaStatus(updatedRecord.getKhazanaStatus());
//        existingRecord.setDueDate(updatedRecord.getDueDate());
//        existingRecord.setDocumentFile(updatedRecord.getDocumentFile());
//        existingRecord.setAreaMapFile(updatedRecord.getAreaMapFile());
//        existingRecord.setLegalMatters(updatedRecord.getLegalMatters());
//        existingRecord.setLedueDate(updatedRecord.getLedueDate());
//        existingRecord.setHistoryChain(updatedRecord.getHistoryChain());
//        existingRecord.setHcdocumentFile(updatedRecord.getHcdocumentFile());
//
//        // Handle PartlySold data
//        Set<PartlySold> updatedPartlySoldData = new HashSet<>();
//        if(!(updatedRecord.getPartlySoldData()==null)) {
//            for (PartlySold newPartlySold : updatedRecord.getPartlySoldData()) {
//                if (newPartlySold.getPartId() == null || newPartlySold.getPartId().isEmpty()) {
//                    // New PartlySold entity
//                    long partIdSuffix = partlySoldRepository.count() + 1;
//                    newPartlySold.setPartId("PART-" + partIdSuffix);
//                    newPartlySold.setRecId(existingRecord.getRecId());
//                    newPartlySold.setModified_type("INSERTED");
//                    newPartlySold.setInserted_by(username);
//                    newPartlySold.setInserted_on(ldt);
//                    newPartlySold.setUpdated_by("NA");
//                    newPartlySold.setUpdated_on(ldt);
//                    newPartlySold.setDeleted_by("NA");
//                    newPartlySold.setDeleted_on(ldt);
//                } else {
//                    // Existing PartlySold entity
//                    PartlySold oldPartlySold = partlySoldRepository.findActiveByPartId(newPartlySold.getPartId()).orElse(null);
//                    oldPartlySold.setModified_type("UPDATED");
//                    oldPartlySold.setUpdated_by(username);
//                    oldPartlySold.setUpdated_on(ldt);
//                    partlySoldRepository.save(oldPartlySold);
//
//                    newPartlySold.setInserted_on(oldPartlySold.getInserted_on());
//                    newPartlySold.setInserted_by(oldPartlySold.getInserted_by());
//                    newPartlySold.setUpdated_by(username);
//                    newPartlySold.setUpdated_on(ldt);
//                    newPartlySold.setDeleted_by(oldPartlySold.getDeleted_by());
//                    newPartlySold.setDeleted_on(oldPartlySold.getDeleted_on());
//                    newPartlySold.setRecId(oldPartlySold.getRecId());
//                    newPartlySold.setPartId(oldPartlySold.getPartId());
//                }
//                updatedPartlySoldData.add(newPartlySold);
//            }
//            existingRecord.setPartlySoldData(updatedPartlySoldData);
//        }
//        // Handle Mortgaged data
//        Set<Mortgaged> updatedMortgagedData = new HashSet<>();
//        if(!(updatedRecord.getMortgagedData()== null)){
//            for (Mortgaged newMortgaged : updatedRecord.getMortgagedData()) {
//                if (newMortgaged.getMortId() == null || newMortgaged.getMortId().isEmpty()) {
//                    // New Mortgaged entity
//                    long mortIdSuffix = mortgagedRepository.count() + 1;
//                    newMortgaged.setMortId("MORTGAGED" + mortIdSuffix);
//                    newMortgaged.setRecId(existingRecord.getRecId());
//                    newMortgaged.setModified_type("INSERTED");
//                    newMortgaged.setInserted_by(username);
//                    newMortgaged.setInserted_on(ldt);
//                    newMortgaged.setUpdated_by("NA");
//                    newMortgaged.setUpdated_on(ldt);
//                    newMortgaged.setDeleted_by("NA");
//                    newMortgaged.setDeleted_on(ldt);
//                } else {
//                    // Existing Mortgaged entity
//                    Mortgaged oldMortgaged = mortgagedRepository.findActiveByMortId(newMortgaged.getMortId())
//                            .orElseThrow(() -> new EntityNotFoundException("Mortgaged with id " + newMortgaged.getMortId() + " not found"));
//                    oldMortgaged.setModified_type("UPDATED");
//                    oldMortgaged.setUpdated_by(username);
//                    oldMortgaged.setUpdated_on(ldt);
//                    mortgagedRepository.save(oldMortgaged);
//
//                    newMortgaged.setInserted_on(oldMortgaged.getInserted_on());
//                    newMortgaged.setInserted_by(oldMortgaged.getInserted_by());
//                    newMortgaged.setUpdated_by(username);
//                    newMortgaged.setUpdated_on(ldt);
//                    newMortgaged.setDeleted_by(oldMortgaged.getDeleted_by());
//                    newMortgaged.setDeleted_on(oldMortgaged.getDeleted_on());
//                    newMortgaged.setRecId(oldMortgaged.getRecId());
//                    newMortgaged.setMortId(oldMortgaged.getMortId());
//                }
//                updatedMortgagedData.add(newMortgaged);
//            }
//        }
//        existingRecord.setMortgagedData(updatedMortgagedData);
//
//        // Update and save the record
//        existingRecord.setModified_type("UPDATED");
//        existingRecord.setUpdated_by(username);
//        existingRecord.setUpdated_on(ldt);
//
//        return recordRepository.save(existingRecord);
        return null;
    }



//    @Override
//    @Transactional
//    public Record updateRecord(Record updatedRecord, String id, String username) {
//        LocalDateTime ldt = LocalDateTime.now();
////        Record optionalOldRecord = recordRepository.findByRecId(id).orElse(null);
////        optionalOldRecord.setModified_type("UPDATED");
//
//        updatedRecord.setId(null);
//        long slno = recordRepository.count();
//        String insertedBy = "working";
//        updatedRecord.setRecId("REC" + slno);
//        updatedRecord.setModified_type("UPDATED");
//        updatedRecord.setInserted_on(ldt);
//        updatedRecord.setInserted_by(insertedBy);
//        updatedRecord.setUpdated_by("NA");
//        updatedRecord.setUpdated_on(ldt);
//        updatedRecord.setDeleted_by("NA");
//        updatedRecord.setDeleted_on(ldt);
//
//
//        Set<PartlySold> partly = new HashSet<>(updatedRecord.getPartlySoldData() != null ? updatedRecord.getPartlySoldData() : new HashSet<>());
//        int count = 0;
//        for (PartlySold partlySold : partly) {
//            long partIdSuffix = recordRepository.count() + count;
//            count++;
//
//            partlySold.setPartId("PART-" + partIdSuffix);
//            partlySold.setRecId(updatedRecord.getRecId());
//            partlySold.setModified_type("INSERTED");
//            partlySold.setInserted_by(insertedBy);
//            partlySold.setInserted_on(ldt);
//            partlySold.setUpdated_by("NA");
//            partlySold.setUpdated_on(ldt);
//            partlySold.setDeleted_by("NA");
//            partlySold.setDeleted_on(ldt);
//        }
//        updatedRecord.setPartlySoldData(partly);
//
//        Set<Mortgaged> mortgageds = new HashSet<>(updatedRecord.getMortgagedData() != null ? updatedRecord.getMortgagedData() : new HashSet<>());
//        count = 0;
//        for (Mortgaged mort : mortgageds) {
//            long mortId = recordRepository.count() + count;
//            count++;
//            mort.setMortId("MORTGAGED" + mortId);
//            mort.setRecId(updatedRecord.getRecId());
//            mort.setModified_type("INSERTED");
//            mort.setInserted_by(insertedBy);
//            mort.setInserted_on(ldt);
//            mort.setUpdated_by("NA");
//            mort.setUpdated_on(ldt);
//            mort.setDeleted_by("NA");
//            mort.setDeleted_on(ldt);
//        }
//        updatedRecord.setMortgagedData(mortgageds);
////        if (optionalOldRecord.isPresent()) {
////            Record oldRecord = optionalOldRecord.get();
////            oldRecord.setModified_type("UPDATED");
////            oldRecord.setUpdated_by(username);
////            oldRecord.setUpdated_on(ldt);
////            recordRepository.save(oldRecord);
////
////            // Copy fields from oldRecord to updatedRecord
////            updatedRecord.setRecId(id); // Ensure the ID is set correctly
////            updatedRecord.setId(null);
////
////            // Copy collections if needed (assuming these are lists of filenames)
////            updatedRecord.setScanCopyFile(new ArrayList<>(oldRecord.getScanCopyFile()));
////            updatedRecord.setConversionFile(new ArrayList<>(oldRecord.getConversionFile()));
////            updatedRecord.setDocumentFile(new ArrayList<>(oldRecord.getDocumentFile()));
////            updatedRecord.setAreaMapFile(new ArrayList<>(oldRecord.getAreaMapFile()));
////            updatedRecord.setMutationFile(new ArrayList<>(oldRecord.getMutationFile()));
////            updatedRecord.setHcdocumentFile(new ArrayList<>(oldRecord.getHcdocumentFile()));
////
////            updatedRecord.setRecId(id);
////            updatedRecord.setModified_type("INSERTED");
////            updatedRecord.setUpdated_by(username);
////            updatedRecord.setUpdated_on(ldt);
////            updatedRecord.setInserted_on(oldRecord.getInserted_on());
////            updatedRecord.setInserted_by(oldRecord.getInserted_by());
////            updatedRecord.setDeleted_by(oldRecord.getDeleted_by());
////            updatedRecord.setDeleted_on(oldRecord.getDeleted_on());
////
////            Set<Mortgaged> newMortgaged = oldRecord.getMortgagedData();
////
////            int count=0;
////            for(Mortgaged newMort : newMortgaged) {
////                if (newMort.getMortId() == null || newMort.getMortId().equals("")) {
////                    long mortSiffix = 0;
////                    if (recordRepository.count() != 0) {
////                        mortSiffix = mortgagedRepository.count() + count;
////                        count++;
////                    }
////
////                    newMort.setMortId("MORTGAGED" + mortSiffix);
////                    newMort.setRecId(oldRecord.getRecId());
////                    newMort.setModified_type("INSERTED");
////                    newMort.setInserted_by(username);
////                    newMort.setInserted_on(ldt);
////                    newMort.setUpdated_by("NA");
////                    newMort.setUpdated_on(ldt);
////                    newMort.setDeleted_by("NA");
////                    newMort.setDeleted_on(ldt);
////                }
////                else {
////                    Mortgaged oldMort = mortgagedRepository.findActiveByMortId(newMort.getMortId());
////                    oldMort.setModified_type("UPDATED");
////                    oldMort.setUpdated_by(username);
////                    oldMort.setUpdated_on(ldt);
////                    mortgagedRepository.save(oldMort);
////
////                    newMort.setModified_type("INSERTED");
////                    newMort.setInserted_on(oldMort.getInserted_on());
////                    newMort.setInserted_by(oldMort.getInserted_by());
////                    newMort.setUpdated_by(username);
////                    newMort.setUpdated_on(ldt);
////                    newMort.setDeleted_by(oldMort.getDeleted_by());
////                    newMort.setDeleted_on(oldMort.getDeleted_on());
////                    newMort.setRecId(oldMort.getRecId());
////                    newMort.setMortId(oldMort.getMortId());
////
//////                    if(newMort.getSlno() == null)
//////                        newMort.setSlno(oldMort.getSlno());
//////
//////                    if(newMort.getParty()==null)
//////                        newMort.setParty(oldMort.getParty());
////                }
////
////            }
////         //   updatedRecord.setMortgagedData(newMortgaged);
////            Set<PartlySold> newSold=oldRecord.getPartlySoldData();
////            int c=0;
////            for(PartlySold newPart : newSold ) {
////                if (newPart.getPartId() == null || newPart.getPartId().equals("")) {
////                    long partSiffix = 0;
////                    if (recordRepository.count() != 0) {
////                        partSiffix = partlySoldRepository.count() + c;
////                        c++;
////                    }
////                    newPart.setPartId("PART" + partSiffix);
////                    newPart.setRecId(oldRecord.getRecId());
////                    newPart.setModified_type("INSERTED");
////                    newPart.setInserted_by(username);
////                    newPart.setInserted_on(ldt);
////                    newPart.setUpdated_by("NA");
////                    newPart.setUpdated_on(ldt);
////                    newPart.setDeleted_by("NA");
////                    newPart.setDeleted_on(ldt);
////                }
////                else{
////                    PartlySold oldPart=partlySoldRepository.findActiveByPartId(newPart.getPartId());
////                    oldPart.setModified_type("UPDATED");
////                    oldPart.setUpdated_by(username);
////                    oldPart.setUpdated_on(ldt);
////                    partlySoldRepository.save(oldPart);
////
////                    newPart.setModified_type("INSERTED");
////                    newPart.setInserted_on(oldPart.getInserted_on());
////                    newPart.setInserted_by(oldPart.getInserted_by());
////                    newPart.setUpdated_by(username);
////                    newPart.setUpdated_on(ldt);
////                    newPart.setDeleted_by(oldPart.getDeleted_by());
////                    newPart.setDeleted_on(oldPart.getDeleted_on());
////                    newPart.setRecId(oldPart.getRecId());
////                    newPart.setPartId(oldPart.getPartId());
////
//////                    if(newPart.getSlno() == null)
//////                        newPart.setSlno(oldPart.getSlno());
//////
//////                    if(newPart.getSale()==null)
//////                        newPart.setSale(oldPart.getSale());
////                }
////
////            }
//          //  updatedRecord.setPartlySoldData(newSold);
////            return recordRepository.save(updatedRecord); // Update the existing record
////        }
//
//        return recordRepository.save(updatedRecord);
//    }



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
        Optional<Mortgaged> mort = mortgagedRepository.findActiveByMortId(id);
        if (record != null || mort.isPresent()) {
            String fileName = originalFileName + "-" + UUID.randomUUID().toString().substring(0, 5) + id + '.' + ext;

            FileUpload fileUpload = new FileUpload();

            fileUpload.setRecId(id);
            fileUpload.setFieldName(fieldName);
            fileUpload.setFileName(fileName);


            switch (fieldName) {
                case "scanCopyFile":
                case "mutationFile":
                case "conversionFile":
                case "documentFile":
                case "areaMapFile":
                case "hcdocumentFile":
                case "mortDocFile":
                    fileUploadRepository.save(fileUpload);
                    break;
                default:
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                            .body("Unsupported field name: " + fieldName);
            }

            try {
                Files.write(Paths.get(uploadDir, fieldName, fileName), blobData);
            } catch (IOException e) {
                e.printStackTrace();
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body("Failed to upload file: " + e.getMessage());
            }
            recordRepository.save(record);
            return ResponseEntity.ok("File uploaded successfully: " + fileName);
        }

        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body("Entry not found.");
    }


    @Override
    public byte[] getFileBytes(String fieldName, String fileName) {
        String uploadDir = env.getProperty("upload.dir"); // Assuming a property named upload.dir is defined

        if (uploadDir == null || uploadDir.isEmpty()) {
            throw new IllegalStateException("Upload directory not configured! Please set 'upload.dir' property.");
        }
        //TODO: check if deleted in db
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
        Mortgaged mort = mortgagedRepository.findActiveByMortId(id).orElse(null);
        boolean removed = false;
        if(record == null) return false;
        //TODO: remake
//        switch (fieldName) {
//            case "scanCopyFile":
//                removed = record.getScanCopyFile().remove(fileName);
//                break;
//            case "mutationFile":
//                removed = record.getMutationFile().remove(fileName);
//                break;
//            case "conversionFile":
//                removed = record.getConversionFile().remove(fileName);
//                break;
//            case "documentFile":
//                removed = record.getDocumentFile().remove(fileName);
//                break;
//            case "areaMapFile":
//                removed = record.getAreaMapFile().remove(fileName);
//                break;
//            case "hcdocumentFile":
//                removed = record.getHcdocumentFile().remove(fileName);
//                break;
////            case "docFile":
////                removed = mort.getDocFile().remove(fileName);
////                break;
//        }

        // Save the record only if a file was removed
        if (removed) {
            recordRepository.save(record);
        }

        // Return true indicating successful deletion from database
        return removed;
    }
}
    
    

