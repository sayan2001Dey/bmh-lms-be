package com.sample.service.record;

import com.sample.dto.record.MortgagedRes;
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

import javax.persistence.EntityNotFoundException;
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
        record.setInserted_on(ldt);
        record.setInserted_by(username);
        record.setUpdated_by("NA");
        record.setUpdated_on(null);
        record.setDeleted_by("NA");
        record.setDeleted_on(null);

        if (recordReq.getMortgaged().equalsIgnoreCase("true")) {
            Set<Mortgaged> mortgagedData = recordReq.getMortgagedData();
            mortgagedRepository.saveAll(mortgagedData);
        }

        boolean mortgagedDataAvailable = false;
        Set<Mortgaged> mortgagedData = recordReq.getMortgagedData();
        if (recordReq.getPartlySold().equalsIgnoreCase("true")) {
            for (Mortgaged mortgaged : mortgagedData) {
                mortgaged.setRecId(record.getRecId());
                mortgaged.setMortId(commonUtils.generateUID("Mortgaged", "MORT"));
                mortgaged.setModified_type("INSERTED");
                mortgaged.setInserted_on(ldt);
                mortgaged.setInserted_by(username);
                mortgaged.setUpdated_by("NA");
                mortgaged.setUpdated_on(null);
                mortgaged.setDeleted_by("NA");
                mortgaged.setDeleted_on(null);

                mortgagedDataAvailable = true;
            }
        }

        boolean partlySoldDataAvailable = false;
        Set<PartlySold> partlySoldData = recordReq.getPartlySoldData();
        if (recordReq.getPartlySold().equalsIgnoreCase("true")) {
            for (PartlySold partlySold : partlySoldData) {
                partlySold.setRecId(record.getRecId());
                partlySold.setPartId(commonUtils.generateUID("PartlySold", "PART"));
                partlySold.setModified_type("INSERTED");
                partlySold.setInserted_on(ldt);
                partlySold.setInserted_by(username);
                partlySold.setUpdated_by("NA");
                partlySold.setUpdated_on(null);
                partlySold.setDeleted_by("NA");
                partlySold.setDeleted_on(null);

                partlySoldDataAvailable = true;
            }
        }

        if(mortgagedDataAvailable)
            mortgagedRepository.saveAll(mortgagedData);

        if(partlySoldDataAvailable)
            partlySoldRepository.saveAll(partlySoldData);

        return recordRepository.save(record).getRecId();
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
        for (Record record : recordList) {
            recordResList.add(recordResMaker(record, record.getRecId()));
        }
        return recordResList;
    }

    @Override
    @Transactional
    public RecordRes updateRecord(RecordReq recordReq, String recId, String username) {
        LocalDateTime ldt = LocalDateTime.now();

        // Fetch the existing record
        Record existingRecord = recordRepository.findByRecId(recId)
                .orElseThrow(() -> new EntityNotFoundException("Record with recId " + recId + " not found"));

        // Update existing record
        existingRecord.setModified_type("UPDATED");
        existingRecord.setUpdated_by(username);
        existingRecord.setUpdated_on(ldt);

        Record record = basicDataFromReqDTO(existingRecord, recordReq);
        record.setUpdated_by(username);
        record.setUpdated_on(null);

        if (recordReq.getMortgaged().equalsIgnoreCase("true")) {
            Set<Mortgaged> mortgagedData = recordReq.getMortgagedData();
            mortgagedRepository.saveAll(mortgagedData);
        }

        //TODO: do same as partly sold hp
        boolean mortgagedDataAvailable = false;
        Set<Mortgaged> mortgagedData = recordReq.getMortgagedData();
        if (recordReq.getPartlySold().equalsIgnoreCase("true")) {
            for (Mortgaged mortgaged : mortgagedData) {
                mortgaged.setRecId(record.getRecId());
                mortgaged.setMortId(commonUtils.generateUID("Mortgaged", "MORT"));
                mortgaged.setModified_type("INSERTED");
                mortgaged.setInserted_on(ldt);
                mortgaged.setInserted_by(username);
                mortgaged.setUpdated_by("NA");
                mortgaged.setUpdated_on(null);
                mortgaged.setDeleted_by("NA");
                mortgaged.setDeleted_on(null);

                mortgagedDataAvailable = true;
            }
        }

        boolean partlySoldDataAvailable = false;
        Set<PartlySold> finalPartlySoldData = new HashSet<>();
        if (recordReq.getPartlySold().equalsIgnoreCase("true")) {
            Set<PartlySold> partlySoldData = recordReq.getPartlySoldData();
            Set<PartlySold> oldPartlySoldData = partlySoldRepository.findAllActive(recId);
            // needs optimization my puny brain ain't helping
            Set<PartlySold> temp = new HashSet<>();

            //new add
            for (PartlySold partlySold : partlySoldData) {
                if(partlySold.getPartId()==null || partlySold.getPartId().isEmpty()) {
                    partlySold.setRecId(record.getRecId());
                    partlySold.setPartId(commonUtils.generateUID("PartlySold", "PART"));
                    partlySold.setModified_type("INSERTED");
                    partlySold.setInserted_on(ldt);
                    partlySold.setInserted_by(username);
                    partlySold.setUpdated_by("NA");
                    partlySold.setUpdated_on(null);
                    partlySold.setDeleted_by("NA");
                    partlySold.setDeleted_on(null);

                    finalPartlySoldData.add(partlySold);
                    partlySoldDataAvailable = true;
                }
                else
                    temp.add(partlySold);
            }

            partlySoldData = temp;

            //old stuff
            // TODO: known lp bug, if any data in update queue doesn't exist in db it will behave unexpectedly. possible risk of data loss or corruption
            for (PartlySold oldPartlySold : oldPartlySoldData) {
                PartlySold found = null;
                for(PartlySold partlySold : partlySoldData) {
                    if(partlySold.getPartId().equals(oldPartlySold.getPartId())) {
                        found = partlySold;
                        break;
                    }
                }
                if (found != null) {
                    //update
                    oldPartlySold.setModified_type("UPDATED");
                    oldPartlySold.setUpdated_on(ldt);
                    oldPartlySold.setUpdated_by(username);

                    PartlySold tempPartlySold = copyPartlySold(oldPartlySold);
                    tempPartlySold.setId(null);
                    tempPartlySold.setRecId(record.getRecId());
                    tempPartlySold.setModified_type("INSERTED");
                    tempPartlySold.setUpdated_on(ldt);
                    tempPartlySold.setUpdated_by(username);

                    finalPartlySoldData.add(tempPartlySold);
                } else {
                    //delete
                    oldPartlySold.setModified_type("DELETED");
                    oldPartlySold.setDeleted_on(ldt);
                    oldPartlySold.setDeleted_by(username);
                }
                finalPartlySoldData.add(oldPartlySold);
                partlySoldDataAvailable = true;
            }
        } else {
            //TODO: delete? ask : confirmed need to delete :) mp
        }
        if(mortgagedDataAvailable)
            mortgagedRepository.saveAll(mortgagedData);

        if(partlySoldDataAvailable)
            partlySoldRepository.saveAll(finalPartlySoldData);

        recordRepository.save(existingRecord);
        return null;
    }


    @Override
    public boolean deleteRecord(String id, String username) {
        //TODO: lp delete needs to work
        LocalDateTime ldt = LocalDateTime.now();
        Optional<Record> optionalRecord = recordRepository.findByRecId(id);
        if (optionalRecord.isPresent()) {
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
    public byte[] getFileBytes(String fieldName, String fileName) {
        String uploadDir = env.getProperty("upload.dir"); // Assuming a property named upload.dir is defined

        if (uploadDir == null || uploadDir.isEmpty()) {
            throw new IllegalStateException("Upload directory not configured! Please set 'upload.dir' property.");
        }

        FileUpload fileUpload = fileUploadRepository.findFilesByFileName(fileName).orElse(null);
        if (fileUpload == null)
            return null;

        Path filePath = Paths.get(uploadDir, fieldName, fileName);

        try {
            return Files.readAllBytes(filePath);
        } catch (IOException e) {
            throw new RuntimeException("Failed to read file: " + fileName, e);
        }
    }

    @Override
    public ResponseEntity<String> saveAttachment(String fieldName, String id, byte[] blobData, String originalFileName, String ext, String insideId, String username) {
        LocalDateTime ldt = LocalDateTime.now();
        String uploadDir = env.getProperty("upload.dir"); // Assuming a property named upload.dir is defined

        if (uploadDir == null || uploadDir.isEmpty()) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Upload directory not configured! Please set 'upload.dir' property.");
        }

        Record record = recordRepository.findByRecId(id).orElse(null);
        if (record != null) {
            String fileName = originalFileName + "-" + UUID.randomUUID().toString().substring(0, 5) + id + '.' + ext;

            FileUpload fileUpload = new FileUpload();

            fileUpload.setRecId(id);
            fileUpload.setFieldName(fieldName);
            fileUpload.setFileName(fileName);
            fileUpload.setModified_type("INSERTED");
            fileUpload.setInserted_on(ldt);
            fileUpload.setInserted_by(username);
            switch (fieldName) {
                case "scanCopyFile":
                case "mutationFile":
                case "conversionFile":
                case "documentFile":
                case "areaMapFile":
                case "hcdocumentFile":
                    fileUploadRepository.save(fileUpload);
                    break;
                case "mortDocFile":
                    Optional<Mortgaged> mort = mortgagedRepository.findActiveByMortId(insideId);
                    if (insideId == null || (!mort.isPresent()))
                        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                                .body("mortId not supplied or doesn't exist: " + fieldName);
                    fileUpload.setInsideId(insideId);
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
            fileUploadRepository.save(fileUpload);
            return ResponseEntity.ok("File uploaded successfully: " + fileName);
        }

        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body("Entry not found.");
    }

    @Override
    @Transactional
    public boolean deleteFile(String id, String fieldName, String fileName, String insideId, String username) {
        LocalDateTime ldt = LocalDateTime.now();

        FileUpload fileUpload = fileUploadRepository.findFilesByFileName(fileName).orElse(null);
        if(fileUpload == null)
            return false;
        fileUpload.setModified_type("DELETED");
        fileUpload.setDeleted_by(username);
        fileUpload.setDeleted_on(ldt);
        fileUploadRepository.saveAndFlush(fileUpload);

        return true;
    }

    private Record basicDataFromReqDTO(Record dest, RecordReq src) {
        if(dest == null)
            dest = new Record();

        dest.setId(null);
        dest.setModified_type("INSERTED");
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

    private RecordRes basicDataToResDTO(Record src) {
        RecordRes dest = new RecordRes();
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
        RecordRes res = basicDataToResDTO(record);

        Set<Mortgaged> mortSet = mortgagedRepository.findAllActive(recId);
        Set<MortgagedRes> mortResSet = new HashSet<>();

        mortSet.forEach((mort)->{
            MortgagedRes mortRes = new MortgagedRes();
            mortRes.setMortId(mort.getMortId());
            mortRes.setMortDate(mort.getMortDate());
            mortRes.setParty(mort.getParty());
            mortRes.setMortDocFile(fileUploadListToNameList(
                    fileUploadRepository.findAllFilesByMortId(mort.getMortId())
            ));
        });

        res.setMortgagedData(mortResSet);
        res.setPartlySoldData(partlySoldRepository.findAllActive(recId));
        res.setConversionFile(fileUploadListToNameList(
                fileUploadRepository.findFilesByIdNFieldName(recId, "conversionFile")
        ));
        res.setAreaMapFile(fileUploadListToNameList(
                fileUploadRepository.findFilesByIdNFieldName(recId, "areaMapFile")
        ));
        res.setDocumentFile(fileUploadListToNameList(
                fileUploadRepository.findFilesByIdNFieldName(recId, "documentFile")
        ));
        res.setHcdocumentFile(fileUploadListToNameList(
                fileUploadRepository.findFilesByIdNFieldName(recId, "hcdocumentFile")
        ));
        res.setScanCopyFile(fileUploadListToNameList(
                fileUploadRepository.findFilesByIdNFieldName(recId, "scanCopyFile")
        ));
        res.setMutationFile(fileUploadListToNameList(
                fileUploadRepository.findFilesByIdNFieldName(recId, "mutationFile")
        ));

        return res;
    }

    private List<String> fileUploadListToNameList(List<FileUpload> files) {
        List<String> res = new ArrayList<>();
        files.forEach((fileUpload) -> {
            res.add(fileUpload.getFileName());
        });
        return res;
    }

    private PartlySold copyPartlySold(PartlySold src) {
        PartlySold res = new PartlySold();

        res.setId(src.getId());
        res.setPartId(src.getPartId());
        res.setRecId(src.getRecId());
        res.setSale(src.getSale());
        res.setDate(src.getDate());
        res.setQty(src.getQty());
        res.setDeedLink(src.getDeedLink());

        res.setModified_type(src.getModified_type());
        res.setInserted_by(src.getInserted_by());
        res.setInserted_on(src.getInserted_on());
        res.setUpdated_by(src.getUpdated_by());
        res.setUpdated_on(src.getUpdated_on());
        res.setDeleted_by(src.getDeleted_by());
        res.setDeleted_on(src.getDeleted_on());

        return res;
    }

    private Mortgaged copyMortgaged(Mortgaged src) {
        Mortgaged res = new Mortgaged();

        res.setId(src.getId());
        res.setRecId(src.getRecId());
        res.setMortId(src.getMortId());
        res.setParty(src.getParty());
        res.setMortDate(src.getMortDate());

        res.setModified_type(src.getModified_type());
        res.setInserted_by(src.getInserted_by());
        res.setInserted_on(src.getInserted_on());
        res.setUpdated_by(src.getUpdated_by());
        res.setUpdated_on(src.getUpdated_on());
        res.setDeleted_by(src.getDeleted_by());
        res.setDeleted_on(src.getDeleted_on());

        return res;
    }
}
