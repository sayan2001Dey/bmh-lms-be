package com.sample.service.partlySold;

import java.util.List;

import com.sample.model.PartlySold;

public interface PartlySoldService {
      
	
	PartlySold saveSold(PartlySold partlySold);
	
	PartlySold getPartlySoldById(Long id);
	
	List<PartlySold> getAllSold();
	
	PartlySold updateSold(PartlySold partlySold,Long id);
	
	void deleteSold(Long id);
}
