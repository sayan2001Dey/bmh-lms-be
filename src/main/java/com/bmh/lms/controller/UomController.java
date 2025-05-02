package com.bmh.lms.controller;

import com.bmh.lms.model.Company;
import com.bmh.lms.model.Uom;
import com.bmh.lms.service.auth.AuthService;
import com.bmh.lms.service.company.CompanyService;
import com.bmh.lms.service.uom.UomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("/api/uom")
public class UomController {
    @Autowired
    private UomService uomMasterService;

    @Autowired
    private AuthService authService;

    @PostMapping
    public ResponseEntity<Uom> createUomMaster(
            @RequestHeader(value = HttpHeaders.AUTHORIZATION, required = false) String token,
            @RequestBody Uom uom
    ) {
        Object[] authData = authService.verifyToken(token);
        if (authData == null || !((Boolean) authData[1]))
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);

        try {
            return new ResponseEntity<>(
                    uomMasterService.createUomMaster(uom, (String) authData[0]),
                    HttpStatus.CREATED
            );
        } catch (Exception e) {
            System.out.println(e);
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping
    public ResponseEntity<List<Uom>> getAllUomMasters(
            @RequestHeader(value = HttpHeaders.AUTHORIZATION, required = false) String token
    ) {
        Object[] authData = authService.verifyToken(token);
        if (authData == null)
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        return new ResponseEntity<>(uomMasterService.getAllUomMasters(), HttpStatus.OK);
    }

    @GetMapping("/{uomId}")
    public ResponseEntity<Uom> getUomMasterById(
            @RequestHeader(value = HttpHeaders.AUTHORIZATION, required = false) String token,
            @PathVariable String uomId
    ) {
        Object[] authData = authService.verifyToken(token);
        if (authData == null)
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        Uom res = uomMasterService.getUomMasterById(uomId).orElse(null);
        return res == null ?
                new ResponseEntity<>(HttpStatus.NOT_FOUND) :
                new ResponseEntity<>(res, HttpStatus.OK);
    }

    @PatchMapping("/{uomId}")
    public ResponseEntity<Uom> updateUomMaster(
            @RequestHeader(value = HttpHeaders.AUTHORIZATION, required = false) String token,
            @PathVariable String uomId,
            @RequestBody Uom uom
    ) {
        Object[] authData = authService.verifyToken(token);
        if (authData == null || !((Boolean) authData[1]))
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        Uom res = uomMasterService.updateUomMaster(uomId, uom, (String) authData[0]);
        return res == null ?
                new ResponseEntity<>(HttpStatus.NOT_FOUND) :
                new ResponseEntity<>(res, HttpStatus.OK);
    }

    @DeleteMapping("/{uomId}")
    public ResponseEntity<Void> deleteUomMaster(
            @RequestHeader(value = HttpHeaders.AUTHORIZATION, required = false) String token,
            @PathVariable String uomId
    ) {
        Object[] authData = authService.verifyToken(token);
        if (authData == null || !((Boolean) authData[1]))
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        return uomMasterService.deleteUomMaster(uomId, (String) authData[0]) ?
                new ResponseEntity<>(HttpStatus.OK) :
                new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
}
