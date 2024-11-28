package com.bmh.lms.service.khatian;

import com.bmh.lms.dto.khatian.KhatianDTO;

import java.util.List;
import java.util.Optional;

public interface KhatianService {

    KhatianDTO createKhatianMaster(KhatianDTO khatian, String username);

    List<KhatianDTO> getAllKhatianMaster();

    Optional<KhatianDTO> getKhatianMasterById(String khatian_id);

    KhatianDTO updateKhatianMaster(String khatian_id, KhatianDTO updatedKhatian, String username);

    Boolean deleteKhatianMaster(String khatian_id, String username);
}
