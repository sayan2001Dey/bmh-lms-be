package com.bmh.lms.service.record;

import com.bmh.lms.dto.record.RecordReq;
import com.bmh.lms.dto.record.RecordRes;
import com.bmh.lms.model.*;
import com.bmh.lms.model.Record;
import com.bmh.lms.repository.*;
import com.bmh.lms.service.utils.CommonUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;

import java.time.LocalDateTime;
import java.util.*;


@Service
public class RecordServiceImpl implements RecordService {

    @Autowired
    private DeedRepository deedRepository;

    @Autowired
    private RecordRepository recordRepository;

    @Autowired
    private RecordCollectionRepository recordCollectionRepository;

    @Autowired
    private HistoryChainRepository historyChainRepository;

    @Autowired
    private Environment env;

    @Autowired
    private CommonUtils commonUtils;

    @Override
    @Transactional
    public RecordRes saveRecord(RecordReq recordReq, String username) {
        LocalDateTime ldt = LocalDateTime.now();

        Record record = basicDataFromReqDTO(null, recordReq);

        record.setRecId(commonUtils.generateUID("Record", "REC"));
        record.setInserted_on(ldt);
        record.setInserted_by(username);
        record.setUpdated_by("NA");
        record.setUpdated_on(null);
        record.setDeleted_by("NA");
        record.setDeleted_on(null);

//        KEEP THIS FOR FUTURE USE
//        if (recordReq.getDeedId() != null) {
//            if (linkDeed(recordReq.getDeedId(), record.getRecId())) {
//                return null;
//            }
//        }

        List<ChainDeedData> finalChainDeedDataList = new ArrayList<>();
        for (ChainDeedData data : recordReq.getChainDeedData()) {
            if (linkDeed(data.getDeedId(), record.getRecId())) {
                finalChainDeedDataList.add(data);
            }
        }

        saveHC(finalChainDeedDataList);

        ChainDeedDataCollection chainDeedDataCollection = new ChainDeedDataCollection();
//        chainDeedDataCollection.setChainDeedData(
//                Objects.equals(recordReq.getDeedType(), "main-deed") ?
//                        new ArrayList<>()
//                        : finalChainDeedDataList
//        );

        chainDeedDataCollection.setChainDeedData(finalChainDeedDataList);


        record.setChainDeedRefId(recordCollectionRepository.save(chainDeedDataCollection).getId());

        Record res = recordRepository.save(record);
        return recordResMaker(res, res.getRecId());
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
        record.setUpdated_on(ldt);

//        KEEP THIS FOR FUTURE USE
//        //compare and unlink deeds
//        if (Objects.equals(recordReq.getDeedId(), existingRecord.getDeedId())) {
//           if(recordReq.getDeedId() == null) {
//               System.out.println("deedId can't be null");
//           } else {
//               if (!linkDeed(recordReq.getDeedId(), existingRecord.getRecId())) return null;
//               unlinkDeed(existingRecord.getDeedId());
//           }
//        }

        ChainDeedDataCollection existingChainDeedData = null;
        if(existingRecord.getChainDeedRefId() != null) {
            existingChainDeedData = recordCollectionRepository.findById(
                    existingRecord.getChainDeedRefId()
            ).orElse(null);
        }

        boolean _condition1 = existingChainDeedData != null && existingChainDeedData.getChainDeedData() != null;
        if(_condition1) {
            existingChainDeedData.getChainDeedData().forEach((data) -> {
                unlinkDeed(data.getDeedId());
            });
        }

        List<ChainDeedData> finalChainDeedDataList = new ArrayList<>();
        for (ChainDeedData data : recordReq.getChainDeedData()) {
            if (linkDeed(data.getDeedId(), existingRecord.getRecId())) {
                finalChainDeedDataList.add(data);
            }
        }

        if(_condition1) {
            updateHC(existingChainDeedData.getChainDeedData(), finalChainDeedDataList);
        } else {
            saveHC(finalChainDeedDataList);
        }

        ChainDeedDataCollection chainDeedDataCollection = new ChainDeedDataCollection();

//        KEEP THIS FOR FUTURE USE
//        chainDeedDataCollection.setChainDeedData(
//                Objects.equals(recordReq.getDeedType(), "main-deed") ?
//                        new ArrayList<>()
//                        : finalChainDeedDataList
//        );

        chainDeedDataCollection.setChainDeedData(finalChainDeedDataList);
        record.setChainDeedRefId(recordCollectionRepository.save(chainDeedDataCollection).getId());

        recordRepository.save(existingRecord);
        recordRepository.save(record);

        return recordResMaker(record, recId);
    }

    @Override
    @Transactional
    public boolean deleteRecord(String id, String username) {
        LocalDateTime ldt = LocalDateTime.now();
        Optional<Record> optionalRecord = recordRepository.findByRecId(id);

        if (optionalRecord.isPresent()) {
            Record record = optionalRecord.get();
            record.setDeleted_by(username);
            record.setDeleted_on(ldt);
            record.setModified_type("DELETED");

//            KEEP THIS FOR FUTURE USE
//            if (record.getDeedId() != null)
//                unlinkDeed(record.getDeedId());

            if (record.getChainDeedRefId() != null) {
                ChainDeedDataCollection data = recordCollectionRepository.findById(
                        record.getChainDeedRefId()
                ).orElse(null);

                if(data != null) {
                    for (ChainDeedData d : data.getChainDeedData())
                        unlinkDeed(d.getDeedId());
                }
            }

            recordRepository.save(record);

            return true;
        } else {
            return false;
        }
    }

    private Record basicDataFromReqDTO(Record old, RecordReq src) {
        Record res = new Record();

        res.setModified_type("INSERTED");

//        KEEP THIS FOR FUTURE USE
//        res.setDeedId(src.getDeedId());
//        res.setDeedType(src.getDeedType());

        res.setHistoryChain(src.getHistoryChain());
        res.setRemarks(src.getRemarks());

        if(old!=null) {
            res.setRecId(old.getRecId());
            res.setInserted_by(old.getInserted_by());
            res.setInserted_on(old.getInserted_on());
            res.setUpdated_by(old.getUpdated_by());
            res.setUpdated_on(old.getUpdated_on());
            res.setDeleted_by(old.getDeleted_by());
            res.setDeleted_on(old.getDeleted_on());
        }

        return res;
    }

    private RecordRes basicDataToResDTO(Record src) {
        RecordRes dest = new RecordRes();

//        KEEP THIS FOR FUTURE USE
//        dest.setDeedId(src.getDeedId());
//        dest.setDeedType(src.getDeedType());

        dest.setHistoryChain(src.getHistoryChain());
        dest.setRemarks(src.getRemarks());
        dest.setRecId(src.getRecId());

        return dest;
    }

    private RecordRes recordResMaker(Record record, String recId) {
        RecordRes res = basicDataToResDTO(record);

        ChainDeedDataCollection chainDeedDataCollection = null;
        if(record.getChainDeedRefId() != null) {
            chainDeedDataCollection = recordCollectionRepository.findById(record.getChainDeedRefId()).orElse(null);
        }
        List<ChainDeedData> chainDeedData = chainDeedDataCollection == null ? new ArrayList<>() : chainDeedDataCollection.getChainDeedData();

        res.setChainDeedData(chainDeedData);

        return res;
    }

    private boolean linkDeed(String deedId, String recId) {
        Deed deed = deedRepository.findByDeedId(deedId).orElse(null);
//        System.out.println(deed);
//        System.out.println(recId);
//        && deed.getRecId() == null
        if(deed != null) {
            deed.setRecId(recId);
            deedRepository.save(deed);
            return true;
        }
        return false;
    }

    private void unlinkDeed(String deedId) {
        Deed deed = deedRepository.findByDeedId(deedId).orElse(null);
        if(deed != null) {
            deed.setRecId(null);
            deedRepository.save(deed);
        }
    }

    private void saveHC(List<ChainDeedData> chainDeedDataList) {
        List<HistoryChain> hcArr = new ArrayList<>();
        for(ChainDeedData chainDeedData : chainDeedDataList) {
            String deedId = chainDeedData.getDeedId();
            HistoryChain hc = historyChainRepository.findByDeedId(deedId).orElse(null);
            if(hc == null) {
                hc = new HistoryChain();
                hc.setDeedId(deedId);
                hc.setParents(new HashSet<>());
                hc.setChildren(new HashSet<>());
            }
            if(chainDeedData.getParentDeedIds()!=null) {
                Set<String> parents = hc.getParents();
                parents.addAll(chainDeedData.getParentDeedIds());
                hc.setParents(parents);
            }
            if(chainDeedData.getChildDeedIds()!=null) {
                Set<String> children = hc.getChildren();
                children.addAll(chainDeedData.getChildDeedIds());
                hc.setChildren(children);
            }
            hcArr.add(hc);
        }
        historyChainRepository.saveAll(hcArr);
    }
    //TODO: HC on update and delete

    private void deleteHC(List<ChainDeedData> chainDeedDataList) {
        List<HistoryChain> hcArr = new ArrayList<>();
        for(ChainDeedData chainDeedData : chainDeedDataList) {
            String deedId = chainDeedData.getDeedId();
            HistoryChain hc = historyChainRepository.findByDeedId(deedId).orElse(null);
            if(hc != null) {
                hcArr.add(hc);
            }
        }
        historyChainRepository.deleteAll(hcArr);
    }

    private void updateHC(List<ChainDeedData> oldChainDeedDataList, List<ChainDeedData> upadatedChainDeedDataList) {
        Set<HistoryChain> hcArr = new HashSet<>();
        for(ChainDeedData chainDeedData : upadatedChainDeedDataList) {
//            String deedId = chainDeedData.getDeedId();
//            HistoryChain hc = historyChainRepository.findByDeedId(deedId).orElse(null);
//            if(hc != null) {
//                hc.setParents(chainDeedData.getParentDeedIds());
//                hc.setChildren(chainDeedData.getChildDeedIds());
//                hcArr.add(hc);
//            }
        }

        oldChainDeedDataList.forEach(oldChainDeedData -> {

        });
        historyChainRepository.saveAll(hcArr);
    }

}
