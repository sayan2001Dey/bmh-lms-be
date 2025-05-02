package com.bmh.lms.service.uom;

import com.bmh.lms.model.Uom;
import com.bmh.lms.repository.UomRepository;
import com.bmh.lms.service.utils.CommonUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

@Service
public class UomServiceImpl implements UomService{
    @Autowired
    private UomRepository uomRepo;

    @Override
    public Uom createUomMaster(Uom uom, String username) {
        LocalDateTime ldt = LocalDateTime.now();

        if(uom.getUomId()==null) {
            throw new RuntimeException("UOM Identifier Can't Be Null");
        }
        uom.setUomName(uom.getUomName().toUpperCase(Locale.ROOT));
        if(uomRepo.findByUomId(uom.getUomId()).isPresent()) {
            throw new RuntimeException("Already Exists");
        }

        if(uom.getMultiplier() == null || uom.getMultiplier().isNaN() || uom.getMultiplier().isInfinite() || uom.getMultiplier() <= 0) {
            throw new RuntimeException("Invalid Multiplier");
        }

        uom.setModified_type("INSERTED");
        uom.setInserted_by(username);
        uom.setInserted_on(ldt);
        uom.setUpdated_by("NA");
        uom.setUpdated_on(null);
        uom.setDeleted_by("NA");
        uom.setDeleted_on(null);

        uom.setBaseUnit(false);
        return uomRepo.save(uom);
    }

    @Override
    public List<Uom> getAllUomMasters() {
        return uomRepo.findAllActive();
    }

    @Override
    public Optional<Uom> getUomMasterById(String uom_id) {
        return uomRepo.findByUomId(uom_id);
    }

    @Override
    public Uom updateUomMaster(String uom_id, Uom updatedUom, String username) {
        LocalDateTime ldt = LocalDateTime.now();

        Uom oldUom = uomRepo.findByUomId(uom_id).orElse(null);

        if (oldUom == null) return null;
        if (oldUom.getBaseUnit()) {
            updatedUom.setClassification(oldUom.getClassification());
            updatedUom.setMultiplier(1.0);
            updatedUom.setBaseUnit(true);
        } else
            updatedUom.setBaseUnit(false);
        if(
                updatedUom.getMultiplier() == null || updatedUom.getMultiplier().isNaN() ||
                updatedUom.getMultiplier().isInfinite() || updatedUom.getMultiplier() <= 0
        ) {
            throw new RuntimeException("Invalid Multiplier");
        }

        updatedUom.setUomId(uom_id);
        updatedUom.setModified_type("INSERTED");
        updatedUom.setInserted_by(oldUom.getInserted_by());
        updatedUom.setInserted_on(oldUom.getInserted_on());
        updatedUom.setUpdated_by(username);
        updatedUom.setUpdated_on(ldt);
        updatedUom.setDeleted_by("NA");
        updatedUom.setDeleted_on(null);

        oldUom.setModified_type("UPDATED");
        oldUom.setUpdated_by(username);
        oldUom.setUpdated_on(ldt);

        uomRepo.save(oldUom);

        return uomRepo.save(updatedUom);
    }

    @Override
    public Boolean deleteUomMaster(String uom_id, String username) {
        LocalDateTime ldt = LocalDateTime.now();
        Uom uom = uomRepo.findByUomId(uom_id).orElse(null);

        if(uom == null || uom.getBaseUnit()) return false;
        uom.setModified_type("DELETED");
        uom.setDeleted_by(username);
        uom.setDeleted_on(ldt);

        uomRepo.save(uom);

        return true;
    }
}
