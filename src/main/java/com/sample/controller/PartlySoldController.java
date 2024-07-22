package com.sample.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sample.model.PartlySold;
import com.sample.service.partlySold.PartlySoldService;

@RestController
@CrossOrigin
@RequestMapping("/api/sold")
public class PartlySoldController {

	@Autowired
	private PartlySoldService partlySoldService;
	
	// Save Sold
	@PostMapping()
	public PartlySold saveSold(@RequestBody PartlySold sold) {
		return partlySoldService.saveSold(sold);
	}
	
	// Get all sold
	@GetMapping()
	public ResponseEntity<List<PartlySold>> getAllSold(){
		List<PartlySold> sold = partlySoldService.getAllSold();
		return new ResponseEntity<>(sold, HttpStatus.OK);
	}
	
	//Get a single Sold ID
	@GetMapping("{id}")
	public ResponseEntity<PartlySold> getSoldById(@PathVariable Long id){
		PartlySold sold = partlySoldService.getPartlySoldById(id);
		
		if(sold != null) {
			return new ResponseEntity<>(sold, HttpStatus.OK);
		}else {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}
	
	
	//Update an existing Sold
	@PatchMapping("{id}")
	public ResponseEntity<PartlySold> updateSold(@PathVariable Long id, @RequestBody PartlySold updateSold){
      	
		PartlySold updateEntity = partlySoldService.updateSold(updateSold, id);
		
		 if (updateEntity != null) {
		        return ResponseEntity.ok(updateEntity); // Return 200 OK with updated entity
		    } else {
		        return ResponseEntity.badRequest().build(); // Return 400 Bad Request if update fails
		    }
		
	}
	
	
	// Delete a Sold
	@DeleteMapping("{id}")
	public ResponseEntity<Void> deleteSold(@PathVariable Long id){
		
		PartlySold sold = partlySoldService.getPartlySoldById(id);
		
		if(sold != null) {
			partlySoldService.deleteSold(id);
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		}else {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}
}
