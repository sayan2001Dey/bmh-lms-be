package com.bmh.lms.service.deed;

import com.bmh.lms.model.Deed;

import java.util.List;
import java.util.Optional;

public interface DeedService {

    Deed createDeed(Deed deed,String username);

    List<Deed> getAllDeed();

    Optional<Deed> getDeedById(String deed_id);

    Deed updateDeed(String deed_id, Deed updatedDeed,String username);

    Boolean deleteDeed(String deed_id, String username);

}
