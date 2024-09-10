package com.bmh.lms.service.deed;


import com.bmh.lms.model.Deed;
import com.bmh.lms.repository.DeedRepository;
import com.bmh.lms.service.utils.CommonUtils;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class DeedServiceImpl implements DeedService{

    @Autowired
    private DeedRepository deedRepository;

    @Autowired
    private CommonUtils commonUtils;

    @Override
    public Deed createDeed(Deed deed, String username) {
        LocalDateTime ldt = LocalDateTime.now();

        deed.setDeedId(commonUtils.generateUID("Deed", "DM"));
        deed.setModified_type("INSERTED");
        deed.setInserted_by(username);
        deed.setInserted_on(ldt);
        deed.setUpdated_by("NA");
        deed.setUpdated_on(null);
        deed.setDeleted_by("NA");
        deed.setDeleted_on(null);

        return deedRepository.save(deed);
    }

    @Override
    public List<Deed> getAllDeed() {
        return deedRepository.findAllActiveByDeedId();
    }

    @Override
    public Optional<Deed> getDeedById(String deed_id) {

        return deedRepository.findByDeedId(deed_id);
    }

    @Override
    @Transactional
    public Deed updateDeed(String deed_id, Deed updatedDeed, String username) {
        LocalDateTime ldt = LocalDateTime.now();

        Deed oldDeed = deedRepository.findByDeedId(deed_id).orElse(null);

        if(oldDeed == null) return null;

        updatedDeed.setDeedId(deed_id);
        updatedDeed.setModified_type("INSERTED");
        updatedDeed.setInserted_by(oldDeed.getInserted_by());
        updatedDeed.setInserted_on(oldDeed.getInserted_on());
        updatedDeed.setUpdated_by(username);
        updatedDeed.setUpdated_on(ldt);
        updatedDeed.setDeleted_by("NA");
        updatedDeed.setDeleted_on(null);

        oldDeed.setModified_type("UPDATED");
        oldDeed.setUpdated_by(username);
        oldDeed.setUpdated_on(ldt);

        deedRepository.save(oldDeed);

        return deedRepository.save(updatedDeed);
    }

    @Override
    public Boolean deleteDeed(String deed_id, String username) {
        LocalDateTime ldt = LocalDateTime.now();
        Deed deed = deedRepository.findByDeedId(deed_id).orElse(null);

        if(deed == null) return false;

        deed.setModified_type("DELETED");
        deed.setDeleted_by(username);
        deed.setDeleted_on(ldt);

        deedRepository.save(deed);

        return true;
    }

}
