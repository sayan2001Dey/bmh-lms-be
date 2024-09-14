package com.bmh.lms.service.mouza;

import com.bmh.lms.dto.mouza.MouzaDTO;
import com.bmh.lms.model.Mouza;
import com.bmh.lms.model.MouzaCollection;

import java.util.List;
import java.util.Optional;

public interface MouzaService {
    MouzaDTO createMouza(MouzaDTO mouza,String username);

    List<MouzaDTO> getAllMouza();

    MouzaDTO getMouzaById(String mouza_id);

    MouzaDTO updateMouza(String mouza_id, MouzaDTO updatedMouza, String username);

    Boolean deleteMouza(String mouza_id, String username);
}
