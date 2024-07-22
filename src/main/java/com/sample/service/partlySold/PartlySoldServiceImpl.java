package com.sample.service.partlySold;

import java.util.List;
import java.util.Optional;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.sample.model.PartlySold;
import com.sample.repository.PartlySoldRepository;

@Service
public class PartlySoldServiceImpl implements PartlySoldService {
   
	@Autowired
	private PartlySoldRepository partlySoldRepository;

	@Override
	public PartlySold saveSold(PartlySold partlySold) {
	
		return partlySoldRepository.save(partlySold);
	}

	@Override
	public PartlySold getPartlySoldById(Long id) {
		Optional<PartlySold> optionalSold = partlySoldRepository.findById(id);
		return optionalSold.orElse(null);
	}

	@Override
	public List<PartlySold> getAllSold() {
		
		return partlySoldRepository.findAll();
	}

	@Override
	@Transactional
	public PartlySold updateSold(PartlySold updateSold, Long id) {
	    Optional<PartlySold> optionalSold= partlySoldRepository.findById(id);
	    if(optionalSold.isPresent()) {
	    	PartlySold existingSold = optionalSold.get();
	    	
	    	existingSold.setSale(updateSold.getSale());
	    	existingSold.setDate(updateSold.getDate());
	    	existingSold.setDeedLink(updateSold.getDeedLink());
	    	existingSold.setQty(updateSold.getQty());
	    	
	    	return partlySoldRepository.save(existingSold);
	    }
		return null;
	}

	@Override
	public void deleteSold(Long id) {
		
		partlySoldRepository.deleteById(id);
		
	}
}
