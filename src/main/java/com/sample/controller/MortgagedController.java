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

import com.sample.model.Mortgaged;
import com.sample.service.mortgaged.MortgagedService;

@RestController
@CrossOrigin
@RequestMapping("/api/mortgaged")
public class MortgagedController {

	
	@Autowired
	private MortgagedService mortgagedService;
	
	// Save Mortgaged
	@PostMapping()
	public Mortgaged saveMortgaged(@RequestBody Mortgaged mort) {
		return mortgagedService.saveMortgaged(mort);
	}
	
	
	// Get all Mortgaged
	@GetMapping()
	public ResponseEntity<List<Mortgaged>> getAllMortgaged(){
		List<Mortgaged> mort = mortgagedService.getAllMortgaged();
		return new ResponseEntity<>(mort, HttpStatus.OK);
	}
	
	
	// Get a single Mortgaged by ID
	@GetMapping("{id}")
	public ResponseEntity<Mortgaged> getMortgagedById(@PathVariable Long id){
		Mortgaged mort = mortgagedService.getMortgagedById(id);
		
		if(mort != null) {
			return new ResponseEntity<>(mort, HttpStatus.OK);
		} else {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}
	
	
	// Update an existing Mortgaged
	@PatchMapping("{id}")
	public ResponseEntity<Mortgaged> updateMortgaged(@PathVariable Long id, @RequestBody Mortgaged updatedMortgage) {
	    // Call service method to update the entity
	    Mortgaged updatedEntity = mortgagedService.updateMortgaged(updatedMortgage, id);
	    
	    if (updatedEntity != null) {
	        return ResponseEntity.ok(updatedEntity); // Return 200 OK with updated entity
	    } else {
	        return ResponseEntity.badRequest().build(); // Return 400 Bad Request if update fails
	    }
	}

	
	
	// Delete a Mortgaged
	@DeleteMapping("{id}")
	public ResponseEntity<Void> deleteMortgaged(@PathVariable Long id){
		Mortgaged mort = mortgagedService.getMortgagedById(id);
		if(mort != null) {
			mortgagedService.deleteMortgaged(id);
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		}else {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}
	

}
