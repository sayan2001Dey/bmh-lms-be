package com.bmh.lms.service.mouza;

import com.bmh.lms.model.Mouza;

import java.util.List;
import java.util.Optional;

public interface MouzaService {
    Mouza createMouza(Mouza mouza, String username);

    List<Mouza> getAllMouza();

    Optional<Mouza> getMouzaById(String mouza_id);

    Mouza updateMouza(String mouza_id, Mouza updatedMouza, String username);

    Boolean deleteMouza(String mouza_id, String username);
}
