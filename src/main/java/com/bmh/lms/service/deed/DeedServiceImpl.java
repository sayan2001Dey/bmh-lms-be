package com.bmh.lms.service.deed;

import com.bmh.lms.dto.deed.DeedReq;
import com.bmh.lms.dto.deed.DeedRes;
import com.bmh.lms.dto.record.MortgagedRes;
import com.bmh.lms.model.*;
import com.bmh.lms.repository.*;
import com.bmh.lms.service.utils.CommonUtils;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.*;

@Service
public class DeedServiceImpl implements DeedService{

    @Autowired
    private DeedRepository deedRepository;

    @Autowired
    private PartlySoldRepository partlySoldRepository;

    @Autowired
    private MortgagedRepository mortgagedRepository;

    @Autowired
    private DeedMouzaCollectionRepository dmcRepo;

    @Autowired
    private FileUploadRepository fileUploadRepository;

    @Autowired
    private Environment env;

    @Autowired
    private CommonUtils commonUtils;

    @Override
    @Transactional
    public DeedRes createDeed(DeedReq deedReq, String username) {
        LocalDateTime ldt = LocalDateTime.now();

        Deed deed = basicDataFromReqDTO(null, deedReq);

        deed.setDeedId(commonUtils.generateUID("Deed", "DM"));
        deed.setModified_type("INSERTED");
        deed.setInserted_by(username);
        deed.setInserted_on(ldt);
        deed.setUpdated_by("NA");
        deed.setUpdated_on(null);
        deed.setDeleted_by("NA");
        deed.setDeleted_on(null);

        DeedMouzaCollection newDeedMouzaCollection = new DeedMouzaCollection();
        newDeedMouzaCollection.setMouza(
                deedReq.getMouza() == null ? new ArrayList<>() : deedReq.getMouza()
        );
        deed.setMouzaRefId(dmcRepo.save(newDeedMouzaCollection).getId());

        boolean mortgagedDataAvailable = false;
        Set<Mortgaged> mortgagedData = deedReq.getMortgagedData();
        if (deedReq.getMortgaged()) {
            for (Mortgaged mortgaged : mortgagedData) {
                mortgaged.setDeedId(deed.getDeedId());
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
        Set<PartlySold> partlySoldData = deedReq.getPartlySoldData();
        if (deedReq.getPartlySold()) {
            for (PartlySold partlySold : partlySoldData) {
                partlySold.setDeedId(deed.getDeedId());
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

        Deed res = deedRepository.save(deed);

        return deedResMaker(res, res.getDeedId());
    }

    @Override
    public List<DeedRes> getAllDeed() {
        List<Deed> deedList = deedRepository.findAllActiveByDeedId();
        List<DeedRes> deedResList = new ArrayList<>();
        for(Deed deed : deedList){
            deedResList.add(deedResMaker(deed, deed.getDeedId()));
        }
        return deedResList;
    }

    @Override
    public DeedRes getDeedById(String deed_id) {
      Optional<Deed> optionalDeed = deedRepository.findByDeedId(deed_id);
      if(optionalDeed.isPresent()){
          Deed deed = optionalDeed.get();
          return deedResMaker(deed,deed_id);
      }
        return null;
    }

    @Override
    @Transactional
    public DeedRes updateDeed(DeedReq deedReq, String deed_id, String username) {
        LocalDateTime ldt = LocalDateTime.now();

        Deed existingDeed = deedRepository.findByDeedId(deed_id).orElseThrow(() -> new EntityNotFoundException("Deed with deedId " + deed_id + " not found"));

        if(existingDeed == null) return null;

        existingDeed.setModified_type("UPDATED");
        existingDeed.setUpdated_by(username);
        existingDeed.setUpdated_on(ldt);

        Deed deed = basicDataFromReqDTO(existingDeed,deedReq);
        deed.setUpdated_by(username);
        deed.setUpdated_on(ldt);

        DeedMouzaCollection newDeedMouzaCollection = new DeedMouzaCollection();
        newDeedMouzaCollection.setMouza(
                deedReq.getMouza() == null ? new ArrayList<>() : deedReq.getMouza()
        );
        deed.setMouzaRefId(dmcRepo.save(newDeedMouzaCollection).getId());

        Set<Mortgaged> finalMortgagedData = new HashSet<>();
        Set<FileUpload> finalMortFileChanges = new HashSet<>();
        Set<Mortgaged> mortgagedData = deedReq.getMortgagedData();
        Set<Mortgaged> oldMortgagedData = mortgagedRepository.findAllActive(deed_id);
        if (deedReq.getMortgaged() && mortgagedData!=null) {
            Set<Mortgaged> temp = new HashSet<>();

            //new add
            for (Mortgaged mortgaged : mortgagedData) {
                if(mortgaged.getMortId()==null || mortgaged.getMortId().isEmpty()) {
                    mortgaged.setDeedId(deed.getDeedId());
                    mortgaged.setMortId(commonUtils.generateUID("Mortgaged", "MORT"));
                    mortgaged.setModified_type("INSERTED");
                    mortgaged.setInserted_on(ldt);
                    mortgaged.setInserted_by(username);
                    mortgaged.setUpdated_by("NA");
                    mortgaged.setUpdated_on(null);
                    mortgaged.setDeleted_by("NA");
                    mortgaged.setDeleted_on(null);

                    finalMortgagedData.add(mortgaged);
                }
                else
                    temp.add(mortgaged);
            }

            mortgagedData = temp;

            //old stuff
            for (Mortgaged oldMortgaged : oldMortgagedData) {
                Mortgaged found = null;
                for(Mortgaged mortgaged : mortgagedData) {
                    if(mortgaged.getMortId().equals(oldMortgaged.getMortId())) {
                        found = mortgaged;
                        break;
                    }
                }
                if (found != null) {
                    //update
                    oldMortgaged.setModified_type("UPDATED");
                    oldMortgaged.setUpdated_on(ldt);
                    oldMortgaged.setUpdated_by(username);

                    Mortgaged tempMortgaged = copyMortgaged(oldMortgaged);
                    tempMortgaged.setId(null);
                    tempMortgaged.setParty(found.getParty());
                    tempMortgaged.setMortDate(found.getMortDate());
                    tempMortgaged.setDeedId(deed.getDeedId());
                    tempMortgaged.setMortQty(found.getMortQty());
                    tempMortgaged.setModified_type("INSERTED");
                    tempMortgaged.setUpdated_on(ldt);
                    tempMortgaged.setUpdated_by(username);

                    finalMortgagedData.add(tempMortgaged);
                } else {
                    //delete
                    oldMortgaged.setModified_type("DELETED");
                    oldMortgaged.setDeleted_on(ldt);
                    oldMortgaged.setDeleted_by(username);
                }
                finalMortgagedData.add(oldMortgaged);
            }
        } else {
            oldMortgagedData.forEach(mort-> {
                fileUploadRepository.findAllFilesByMortId(mort.getMortId()).forEach(file->{
                    file.setModified_type("DELETED");
                    file.setDeleted_on(ldt);
                    file.setDeleted_by(username);
                    finalMortFileChanges.add(file);
                });
                mort.setModified_type("DELETED");
                mort.setDeleted_on(ldt);
                mort.setDeleted_by(username);
                finalMortgagedData.add(mort);
            });
        }

        Set<PartlySold> finalPartlySoldData = new HashSet<>();
        Set<PartlySold> partlySoldData = deedReq.getPartlySoldData();
        Set<PartlySold> oldPartlySoldData = partlySoldRepository.findAllActive(deed_id);
        if (deedReq.getPartlySold() && partlySoldData!=null) {
            // needs optimization my puny brain ain't helping
            Set<PartlySold> temp = new HashSet<>();

            //new add
            for (PartlySold partlySold : partlySoldData) {
                if(partlySold.getPartId()==null || partlySold.getPartId().isEmpty()) {
                    partlySold.setDeedId(deed.getDeedId());
                    partlySold.setPartId(commonUtils.generateUID("PartlySold", "PART"));
                    partlySold.setModified_type("INSERTED");
                    partlySold.setInserted_on(ldt);
                    partlySold.setInserted_by(username);
                    partlySold.setUpdated_by("NA");
                    partlySold.setUpdated_on(null);
                    partlySold.setDeleted_by("NA");
                    partlySold.setDeleted_on(null);

                    finalPartlySoldData.add(partlySold);
                }
                else
                    temp.add(partlySold);
            }

            partlySoldData = temp;

            //old stuff
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
                    tempPartlySold.setSale(found.getSale());
                    tempPartlySold.setDate(found.getDate());
                    tempPartlySold.setQty(found.getQty());
                    tempPartlySold.setDeedLink(found.getDeedLink());
                    tempPartlySold.setDeedId(deed.getDeedId());
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
            }
        } else {
            oldPartlySoldData.forEach(part-> {
                part.setModified_type("DELETED");
                part.setDeleted_on(ldt);
                part.setDeleted_by(username);
                finalPartlySoldData.add(part);
            });
        }

        if(!finalMortgagedData.isEmpty())
            mortgagedRepository.saveAll(finalMortgagedData);

        if(!finalMortFileChanges.isEmpty())
            fileUploadRepository.saveAll(finalMortFileChanges);

        if(!finalPartlySoldData.isEmpty())
            partlySoldRepository.saveAll(finalPartlySoldData);

        deedRepository.save(existingDeed);
        deedRepository.save(deed);

        return deedResMaker(deed, deed_id);
    }

    @Override
    @Transactional
    public boolean deleteDeed(String deed_id, String username) {
        LocalDateTime ldt = LocalDateTime.now();

        Optional<Deed> optionalDeed = deedRepository.findByDeedId(deed_id);

        if (optionalDeed.isPresent()) {
            Deed deed = optionalDeed.get();
            deed.setDeleted_by(username);
            deed.setDeleted_on(ldt);
            deed.setModified_type("DELETED");

            deedRepository.save(deed);

            List<Mortgaged> finalMort = new ArrayList<>();
            List<FileUpload> finalFile = new ArrayList<>();
            List<PartlySold> finalPart = new ArrayList<>();

            if (deed.getMortgaged()) {
                mortgagedRepository.findAllActive(deed_id).forEach(mortgaged -> {
                    fileUploadRepository.findAllFilesByMortId(mortgaged.getMortId()).forEach(file -> {
                        file.setModified_type("DELETED");
                        file.setDeleted_on(ldt);
                        file.setDeleted_by(username);
                        finalFile.add(file);
                    });
                    mortgaged.setModified_type("DELETED");
                    mortgaged.setDeleted_on(ldt);
                    mortgaged.setDeleted_by(username);
                    finalMort.add(mortgaged);
                });
            }

            if (deed.getPartlySold()) {
                partlySoldRepository.findAllActive(deed_id).forEach(partlySold -> {
                    partlySold.setModified_type("DELETED");
                    partlySold.setDeleted_on(ldt);
                    partlySold.setDeleted_by(username);
                    finalPart.add(partlySold);
                });
            }

            fileUploadRepository.findFilesByDeedId(deed_id).forEach(file -> {
                file.setModified_type("DELETED");
                file.setDeleted_on(ldt);
                file.setDeleted_by(username);
                finalFile.add(file);
            });

            if (!finalMort.isEmpty())
                mortgagedRepository.saveAll(finalMort);

            if (!finalPart.isEmpty())
                partlySoldRepository.saveAll(finalPart);

            if (!finalFile.isEmpty())
                fileUploadRepository.saveAll(finalFile);


            return true;
        } else {
            return false;
        }

    }

    private Deed basicDataFromReqDTO(Deed old, DeedReq src) {
        Deed res = new Deed();

        String sellerType = src.getSellerType();
        List<String> sellers = new ArrayList<>();
        if(Objects.equals(sellerType, "within-group")) {
            sellers.add(src.getSellers().getFirst());
        } else
            sellers = src.getSellers();

        res.setModified_type("INSERTED");

        res.setDeedNo(src.getDeedNo());
        res.setDeedDate(src.getDeedDate());
        res.setGroupId(src.getGroupId());
        res.setCompanyId(src.getCompanyId());
        res.setSellerType(sellerType);
        res.setSellers(sellers);
        res.setTotalQty(src.getTotalQty());
        res.setPurQty(src.getPurQty());
        res.setMutedQty(src.getMutedQty());
        res.setUnMutedQty(src.getUnMutedQty());
        res.setLandStatus(src.getLandStatus());
        res.setConversionLandStatus(src.getConversionLandStatus());
        res.setDeedLoc(src.getDeedLoc());
        res.setPhotoLoc(src.getPhotoLoc());
        res.setGovtRec(src.getGovtRec());
        res.setKhazanaStatus(src.getKhazanaStatus());
        res.setTaxDueDate(src.getTaxDueDate());
        res.setLastUpDate(src.getLastUpDate());
        res.setLegalMatters(src.getLegalMatters());
        res.setLedueDate(src.getLedueDate());
        res.setLeDescription(src.getLeDescription());
        res.setMortgaged(src.getMortgaged());
        res.setPartlySold(src.getPartlySold());
        res.setTax(src.getTax());
        res.setLelastDate(src.getLelastDate());
        res.setRemarks(src.getRemarks());

        if(old!=null) {
            res.setDeedId(old.getDeedId());
            res.setInserted_by(old.getInserted_by());
            res.setInserted_on(old.getInserted_on());
            res.setUpdated_by(old.getUpdated_by());
            res.setUpdated_on(old.getUpdated_on());
            res.setDeleted_by(old.getDeleted_by());
            res.setDeleted_on(old.getDeleted_on());
        }

        return res;
    }

    private DeedRes basicDataToResDTO(Deed src) {
        DeedRes dest = new DeedRes();

        dest.setDeedNo(src.getDeedNo());
        dest.setDeedDate(src.getDeedDate());
        dest.setGroupId(src.getGroupId());
        dest.setCompanyId(src.getCompanyId());
        dest.setSellerType(src.getSellerType());
        dest.setSellers(src.getSellers());
        dest.setTotalQty(src.getTotalQty());
        dest.setPurQty(src.getPurQty());
        dest.setMutedQty(src.getMutedQty());
        dest.setUnMutedQty(src.getUnMutedQty());
        dest.setLandStatus(src.getLandStatus());
        dest.setConversionLandStatus(src.getConversionLandStatus());
        dest.setDeedLoc(src.getDeedLoc());
        dest.setPhotoLoc(src.getPhotoLoc());
        dest.setGovtRec(src.getGovtRec());
        dest.setKhazanaStatus(src.getKhazanaStatus());
        dest.setTaxDueDate(src.getTaxDueDate());
        dest.setLastUpDate(src.getLastUpDate());
        dest.setLegalMatters(src.getLegalMatters());
        dest.setLedueDate(src.getLedueDate());
        dest.setLeDescription(src.getLeDescription());
        dest.setMortgaged(src.getMortgaged());
        dest.setPartlySold(src.getPartlySold());
        dest.setTax(src.getTax());
        dest.setLelastDate(src.getLelastDate());
        dest.setRemarks(src.getRemarks());
        dest.setDeedId(src.getDeedId());

        return dest;
    }

    private DeedRes deedResMaker(Deed deed, String deedId) {
        DeedRes res = basicDataToResDTO(deed);

        Set<Mortgaged> mortSet = mortgagedRepository.findAllActive(deedId);
        Set<MortgagedRes> mortResSet = new HashSet<>();

        mortSet.forEach((mort)->{
            MortgagedRes mortRes = new MortgagedRes();
            mortRes.setMortId(mort.getMortId());
            mortRes.setMortDate(mort.getMortDate());
            mortRes.setParty(mort.getParty());
            mortRes.setMortQty(mort.getMortQty());
            fileUploadRepository.findFileByMortId(mort.getMortId())
                    .ifPresent(file -> mortRes.setMortDocFile(file.getFileName()));
            mortResSet.add(mortRes);
        });

        res.setMortgagedData(mortResSet);
        res.setPartlySoldData(partlySoldRepository.findAllActive(deedId));

        DeedMouzaCollection mouzaRec = null;
        if(deed.getMouzaRefId() != null)
            mouzaRec = dmcRepo.findById(deed.getMouzaRefId()).orElse(null);

        if (mouzaRec == null)
            res.setMouza(new ArrayList<>());
        else
            res.setMouza(mouzaRec.getMouza());

        res.setConversionFile(fileUploadListToNameList(
                fileUploadRepository.findFilesByIdNFieldName(deedId, "conversionFile")
        ));
        res.setAreaMapFile(fileUploadListToNameList(
                fileUploadRepository.findFilesByIdNFieldName(deedId, "areaMapFile")
        ));
        res.setDocumentFile(fileUploadListToNameList(
                fileUploadRepository.findFilesByIdNFieldName(deedId, "documentFile")
        ));
        res.setHcdocumentFile(fileUploadListToNameList(
                fileUploadRepository.findFilesByIdNFieldName(deedId, "hcdocumentFile")
        ));
        res.setScanCopyFile(fileUploadListToNameList(
                fileUploadRepository.findFilesByIdNFieldName(deedId, "scanCopyFile")
        ));
        res.setVestedFile(fileUploadListToNameList(
                fileUploadRepository.findFilesByIdNFieldName(deedId, "vestedFile")
        ));
        res.setParchaFile(fileUploadListToNameList(
                fileUploadRepository.findFilesByIdNFieldName(deedId, "parchaFile")
        ));
        res.setMutationFile(fileUploadListToNameList(
                fileUploadRepository.findFilesByIdNFieldName(deedId, "mutationFile")
        ));

        return res;
    }

    private List<String> fileUploadListToNameList(List<FileUpload> files) {
        List<String> res = new ArrayList<>();
        files.forEach((fileUpload) -> res.add(fileUpload.getFileName()));
        return res;
    }

    private PartlySold copyPartlySold(PartlySold src) {
        PartlySold res = new PartlySold();

        res.setId(src.getId());
        res.setPartId(src.getPartId());
        res.setDeedId(src.getDeedId());
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
        res.setDeedId(src.getDeedId());
        res.setMortId(src.getMortId());
        res.setParty(src.getParty());
        res.setMortDate(src.getMortDate());
        res.setMortQty(src.getMortQty());

        res.setModified_type(src.getModified_type());
        res.setInserted_by(src.getInserted_by());
        res.setInserted_on(src.getInserted_on());
        res.setUpdated_by(src.getUpdated_by());
        res.setUpdated_on(src.getUpdated_on());
        res.setDeleted_by(src.getDeleted_by());
        res.setDeleted_on(src.getDeleted_on());

        return res;
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

        Deed deed = deedRepository.findByDeedId(id).orElse(null);
        if (deed != null) {
            String fileName = originalFileName + "-" + UUID.randomUUID().toString().substring(0, 5) + id + '.' + ext;

            FileUpload fileUpload = new FileUpload();

            fileUpload.setDeedId(id);
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
                case "vestedFile":
                case "parchaFile":
                    fileUploadRepository.save(fileUpload);
                    break;
                case "mortDocFile":
                    Optional<Mortgaged> mort = mortgagedRepository.findActiveByMortId(insideId);
                    if (insideId == null || (mort.isEmpty()))
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

}
