package com.bmh.lms.service.uom;

import com.bmh.lms.model.Uom;

import java.util.List;
import java.util.Optional;

public interface UomService {
    Uom createUomMaster(Uom uom, String username);

    List<Uom> getAllUomMasters(String classification);

    List<Uom> getAllUomMasters();

    Optional<Uom> getUomMasterById(String uom_id);

    Uom updateUomMaster(String uom_id, Uom updatedUom, String username);

    Boolean deleteUomMaster(String uom_id, String username);
}
