package com.bmh.lms.controller;


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
    public ResponseEntity<Mouza> createMouza(
            @RequestHeader(value = HttpHeaders.AUTHORIZATION, required = false) String token,
            @RequestBody Mouza mouza
    ) {
        Object[] authData = authService.verifyToken(token);
        if (authData == null || !((Boolean) authData[1]))
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);

        try {
            return new ResponseEntity<>(
                    mouzaService.createMouza(mouza, (String) authData[0]),
                    HttpStatus.CREATED
            );
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping
    public ResponseEntity<List<Mouza>> getAllMouza(
            @RequestHeader(value = HttpHeaders.AUTHORIZATION, required = false) String token
    ) {
        Object[] authData = authService.verifyToken(token);
        if (authData == null || !((Boolean) authData[1]))
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        return new ResponseEntity<>(mouzaService.getAllMouza(), HttpStatus.OK);
    }

    @GetMapping("/{mouzaId}")
    public ResponseEntity<Mouza> getMouzaById(
            @RequestHeader(value = HttpHeaders.AUTHORIZATION, required = false) String token,
            @PathVariable String mouzaId
    ) {
        Object[] authData = authService.verifyToken(token);
        if (authData == null || !((Boolean) authData[1]))
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        Mouza res = mouzaService.getMouzaById(mouzaId).orElse(null);
        return res == null ?
                new ResponseEntity<>(HttpStatus.NOT_FOUND) :
                new ResponseEntity<>(res, HttpStatus.OK);
    }

    @PatchMapping("/{mouzaId}")
    public ResponseEntity<Mouza> updateMouza(
            @RequestHeader(value = HttpHeaders.AUTHORIZATION, required = false) String token,
            @PathVariable String mouzaId,
            @RequestBody Mouza mouza
    ) {
        Object[] authData = authService.verifyToken(token);
        if (authData == null || !((Boolean) authData[1]))
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        Mouza res = mouzaService.updateMouza(mouzaId, mouza, (String) authData[0]);
        return res == null ?
                new ResponseEntity<>(HttpStatus.NOT_FOUND) :
                new ResponseEntity<>(res, HttpStatus.OK);
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

