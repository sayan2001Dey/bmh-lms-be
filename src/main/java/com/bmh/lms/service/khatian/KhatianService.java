package com.bmh.lms.service.khatian;

import com.bmh.lms.model.Khatian;

import java.util.List;
import java.util.Optional;

public interface KhatianService {

    Khatian createKhatianMaster(Khatian khatian, String username);

    List<Khatian> getAllKhatianMaster();

    Optional<Khatian> getKhatianMasterById(String khatian_id);

    Khatian updateKhatianMaster(String khatian_id, Khatian updatedKhatian, String username);

    Boolean deleteKhatianMaster(String khatian_id, String username);
}
