package com.bmh.lms.service.mouza;

import com.bmh.lms.dto.mouza.MouzaDTO;

import java.util.List;

public interface MouzaService {
    MouzaDTO createMouza(MouzaDTO mouzaDTO, String username);

    List<MouzaDTO> getAllMouza();

    MouzaDTO getMouzaById(String mouza_id);

    MouzaDTO updateMouza(String mouza_id,  MouzaDTO updatedMouzaDTO, String username);

    Boolean deleteMouza(String mouza_id, String username);
}
