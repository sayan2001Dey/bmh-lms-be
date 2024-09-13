package com.bmh.lms.service.mouza;

import com.bmh.lms.model.Mouza;
import com.bmh.lms.repository.MouzaRepository;
import com.bmh.lms.service.utils.CommonUtils;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class MouzaServiceImpl implements MouzaService {

    @Autowired
    private MouzaRepository mouzaRepository;

    @Autowired
    private CommonUtils commonUtils;

    @Override
    public Mouza createMouza(Mouza mouza, String username) {
        LocalDateTime ldt = LocalDateTime.now();

        mouza.setMouzaId(commonUtils.generateUID("Mouza", "MZM"));
        mouza.setModified_type("INSERTED");
        mouza.setInserted_by(username);
        mouza.setInserted_on(ldt);
        mouza.setUpdated_by("NA");
        mouza.setUpdated_on(null);
        mouza.setDeleted_by("NA");
        mouza.setDeleted_on(null);

        return mouzaRepository.save(mouza);
    }

    @Override
    public List<Mouza> getAllMouza() {
        return mouzaRepository.findAllActiveByMouzaId();
    }

    @Override
    public Optional<Mouza> getMouzaById(String mouza_id) {

        return mouzaRepository.findByMouzaId(mouza_id);

    }

    @Override
    @Transactional
    public Mouza updateMouza(String mouza_id, Mouza updatedMouza, String username) {
        LocalDateTime ldt = LocalDateTime.now();

        Mouza oldMouza = mouzaRepository.findByMouzaId(mouza_id).orElse(null);

        if(oldMouza == null) return null;

        updatedMouza.setMouzaId(mouza_id);
        updatedMouza.setModified_type("INSERTED");
        updatedMouza.setInserted_by(oldMouza.getInserted_by());
        updatedMouza.setInserted_on(oldMouza.getInserted_on());
        updatedMouza.setUpdated_by(username);
        updatedMouza.setUpdated_on(ldt);
        updatedMouza.setDeleted_by("NA");
        updatedMouza.setDeleted_on(null);

        oldMouza.setModified_type("UPDATED");
        oldMouza.setUpdated_by(username);
        oldMouza.setUpdated_on(ldt);

        mouzaRepository.save(oldMouza);

        return mouzaRepository.save(updatedMouza);
    }


    @Override
    public Boolean deleteMouza(String mouza_id, String username) {
        LocalDateTime ldt = LocalDateTime.now();
        Mouza mouza = mouzaRepository.findByMouzaId(mouza_id).orElse(null);

        if(mouza == null) return false;

        mouza.setModified_type("DELETED");
        mouza.setDeleted_by(username);
        mouza.setDeleted_on(ldt);

        mouzaRepository.save(mouza);

        return true;
    }
}
