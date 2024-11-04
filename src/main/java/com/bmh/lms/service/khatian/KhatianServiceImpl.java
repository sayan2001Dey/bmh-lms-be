package com.bmh.lms.service.khatian;

import com.bmh.lms.model.Khatian;
import com.bmh.lms.repository.KhatianRepository;
import com.bmh.lms.service.utils.CommonUtils;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public class KhatianServiceImpl implements  KhatianService{

    @Autowired
    private KhatianRepository repository;

    @Autowired
    private CommonUtils commonUtils;


    /**
     * @param khatian
     * @param username
     * @return
     */
    @Override
    public Khatian createKhatianMaster(Khatian khatian, String username) {
        LocalDateTime ldt=LocalDateTime.now();

         khatian.setKhatianId(commonUtils.generateUID("Khatian", "KM"));
         khatian.setInserted_by(username);
         khatian.setModified_type("INSERTED");
         khatian.setInserted_on(ldt);
         khatian.setUpdated_by("NA");
         khatian.setUpdated_on(null);
         khatian.setDeleted_by("NA");
         khatian.setDeleted_on(null);



        return repository.save(khatian);
    }

    /**
     * @return
     */
    @Override
    public List<Khatian> getAllKhatianMaster() {
        return repository.findAllActiveByKhatianId();
    }

    /**
     * @param khatian_id
     * @return
     */
    @Override
    public Optional<Khatian> getKhatianMasterById(String khatian_id) {
        return repository.findByKhatianId(khatian_id);
    }

    /**
     * @param khatian_id
     * @param updatedKhatian
     * @param username
     * @return
     */
    @Override
    @Transactional
    public Khatian updateKhatianMaster(String khatian_id, Khatian updatedKhatian, String username) {
        LocalDateTime ldt = LocalDateTime.now();

        Khatian oldk=repository.findByKhatianId(khatian_id).orElse(null);

        if(oldk == null) return  null;

        updatedKhatian.setKhatianId(khatian_id);
        updatedKhatian.setModified_type("INSERTED");
        updatedKhatian.setInserted_by(oldk.getInserted_by());
        updatedKhatian.setInserted_on(oldk.getInserted_on());
        updatedKhatian.setUpdated_by(username);
        updatedKhatian.setUpdated_on(ldt);
        updatedKhatian.setDeleted_by("NA");
        updatedKhatian.setDeleted_on(null);

        oldk.setModified_type("UPDATED");
        oldk.setUpdated_by(username);
        oldk.setUpdated_on(ldt);

        repository.save(oldk);

        return repository.save(updatedKhatian);
    }

    /**
     * @param khatian_id
     * @param username
     * @return
     */
    @Override
    public Boolean deleteKhatianMaster(String khatian_id, String username) {
        LocalDateTime ldt = LocalDateTime.now();
        Khatian khatian = repository.findByKhatianId(khatian_id).orElse(null);

        if(khatian == null) return false;

        khatian.setModified_type("DELETED");
        khatian.setDeleted_by(username);
        khatian.setDeleted_on(ldt);

        repository.save(khatian);

        return true;
    }
}
