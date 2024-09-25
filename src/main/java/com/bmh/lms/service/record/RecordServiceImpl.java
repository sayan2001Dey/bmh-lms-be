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
    private RecordRepository recordRepository;

    @Autowired
    private RecordCollectionRepository recordCollectionRepository;

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

        ChainDeedDataCollection chainDeedDataCollection = new ChainDeedDataCollection();
        chainDeedDataCollection.setChainDeedData(
                recordReq.getChainDeedData() == null || Objects.equals(recordReq.getDeedType(), "chain-deed") ?
                        new ArrayList<>()
                        : recordReq.getChainDeedData()
        );
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

        ChainDeedDataCollection chainDeedDataCollection = new ChainDeedDataCollection();
        chainDeedDataCollection.setChainDeedData(
                recordReq.getChainDeedData() == null || Objects.equals(recordReq.getDeedType(), "chain-deed") ?
                        new ArrayList<>()
                        : recordReq.getChainDeedData()
        );
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

            recordRepository.save(record);

            return true;
        } else {
            return false;
        }
    }

    private Record basicDataFromReqDTO(Record old, RecordReq src) {
        Record res = new Record();

        res.setModified_type("INSERTED");

        res.setDeedId(src.getDeedId());
        res.setDeedType(src.getDeedType());
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

        dest.setDeedId(src.getDeedId());
        dest.setDeedType(src.getDeedType());
        dest.setHistoryChain(src.getHistoryChain());
        dest.setRemarks(src.getRemarks());
        dest.setRecId(src.getRecId());

        return dest;
    }

    private RecordRes recordResMaker(Record record, String recId) {
        RecordRes res = basicDataToResDTO(record);

        ChainDeedDataCollection chainDeedDataCollection = recordCollectionRepository.findById(record.getChainDeedRefId()).orElse(null);
        List<ChainDeedData> chainDeedData = chainDeedDataCollection == null ? new ArrayList<>() : chainDeedDataCollection.getChainDeedData();

        res.setChainDeedData(chainDeedData);

        return res;
    }
}
