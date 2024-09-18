package com.bmh.lms.controller;


import com.bmh.lms.dto.mouza.MouzaDTO;
import com.bmh.lms.model.Mouza;
import com.bmh.lms.service.auth.AuthService;
import com.bmh.lms.service.mouza.MouzaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/api/mouza")
public class MouzaController {
    @Autowired
    private MouzaService mouzaService;

    @Autowired
    private AuthService authService;

    @PostMapping
    public ResponseEntity<MouzaDTO> createMouza(
            @RequestHeader(value = HttpHeaders.AUTHORIZATION, required = false) String token,
            @RequestBody MouzaDTO mouzaDTO

    ) {
        Object[] authData = authService.verifyToken(token);
        if (authData == null || !((Boolean) authData[1])){
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }

        try {
            MouzaDTO createdMouza = mouzaService.createMouza(mouzaDTO, (String) authData[0]);
            return new ResponseEntity<>(createdMouza, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping
    public ResponseEntity<List<MouzaDTO>> getAllMouza(
            @RequestHeader(value = HttpHeaders.AUTHORIZATION, required = false) String token
    ) {
        Object[] authData = authService.verifyToken(token);
        if (authData == null) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }

        List<MouzaDTO> mouzaList = mouzaService.getAllMouza();
        return new ResponseEntity<>(mouzaList, HttpStatus.OK);
    }

    @GetMapping("/{mouzaId}")
    public ResponseEntity<MouzaDTO> getMouzaById(
            @RequestHeader(value = HttpHeaders.AUTHORIZATION, required = false) String token,
            @PathVariable String mouzaId
    ) {
        Object[] authData = authService.verifyToken(token);
        if (authData == null) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }

        MouzaDTO mouzaDTO = mouzaService.getMouzaById(mouzaId);
        return mouzaDTO != null ?
                new ResponseEntity<>(mouzaDTO, HttpStatus.OK) :
                new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PatchMapping("/{mouzaId}")
    public ResponseEntity<MouzaDTO> updateMouza(
            @RequestHeader(value = HttpHeaders.AUTHORIZATION, required = false) String token,
            @PathVariable String mouzaId,
            @RequestBody MouzaDTO updatedMouzaDTO
    ) {
        Object[] authData = authService.verifyToken(token);
        if (authData == null || !((Boolean) authData[1])) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }

        MouzaDTO updatedMouza = mouzaService.updateMouza(mouzaId, updatedMouzaDTO, (String) authData[0]);
        return updatedMouza != null ?
                new ResponseEntity<>(updatedMouza, HttpStatus.OK) :
                new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @DeleteMapping("/{mouzaId}")
    public ResponseEntity<Void> deleteMouza(
            @RequestHeader(value = HttpHeaders.AUTHORIZATION, required = false) String token,
            @PathVariable String mouzaId
    ) {
        Object[] authData = authService.verifyToken(token);
        if (authData == null || !((Boolean) authData[1]))
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        return mouzaService.deleteMouza(mouzaId, (String) authData[0]) ?
                new ResponseEntity<>(HttpStatus.OK) :
                new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

}

