package com.bmh.lms.controller;

import com.bmh.lms.model.Deed;
import com.bmh.lms.service.auth.AuthService;
import com.bmh.lms.service.deed.DeedService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("/api/deed")
public class DeedController {

    @Autowired
    private DeedService deedService;

    @Autowired
    private AuthService authService;

    @PostMapping
    public ResponseEntity<Deed> createDeed(
            @RequestHeader(value = HttpHeaders.AUTHORIZATION, required = false) String token,
            @RequestBody Deed deed
    ) {
        Object[] authData = authService.verifyToken(token);
        if (authData == null || !((Boolean) authData[1]))
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);

        try {
            return new ResponseEntity<>(
                    deedService.createDeed(deed, (String) authData[0]),
                    HttpStatus.CREATED
            );
        } catch (Exception e) {
            System.out.println(e);
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping
    public ResponseEntity<List<Deed>> getAllDeed(
            @RequestHeader(value = HttpHeaders.AUTHORIZATION, required = false) String token
    ) {
        Object[] authData = authService.verifyToken(token);
        if (authData == null || !((Boolean) authData[1]))
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        return new ResponseEntity<>(deedService.getAllDeed(), HttpStatus.OK);
    }

    @GetMapping("/{deedId}")
    public ResponseEntity<Deed> getDeedById(
            @RequestHeader(value = HttpHeaders.AUTHORIZATION, required = false) String token,
            @PathVariable String deedId
    ) {
        Object[] authData = authService.verifyToken(token);
        if (authData == null || !((Boolean) authData[1]))
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
       Deed res = deedService.getDeedById(deedId).orElse(null);
        return res == null ?
                new ResponseEntity<>(HttpStatus.NOT_FOUND) :
                new ResponseEntity<>(res, HttpStatus.OK);
    }

    @PatchMapping("/{deedId}")
    public ResponseEntity<Deed> updateDeed(
            @RequestHeader(value = HttpHeaders.AUTHORIZATION, required = false) String token,
            @PathVariable String deedId,
            @RequestBody Deed deed
    ) {
        Object[] authData = authService.verifyToken(token);
        if (authData == null || !((Boolean) authData[1]))
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        Deed res = deedService.updateDeed(deedId, deed, (String) authData[0]);
        return res == null ?
                new ResponseEntity<>(HttpStatus.NOT_FOUND) :
                new ResponseEntity<>(res, HttpStatus.OK);
    }

    @DeleteMapping("/{deedId}")
    public ResponseEntity<Void> deleteDeed(
            @RequestHeader(value = HttpHeaders.AUTHORIZATION, required = false) String token,
            @PathVariable String deedId
    ) {
        Object[] authData = authService.verifyToken(token);
        if (authData == null || !((Boolean) authData[1]))
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        return deedService.deleteDeed(deedId, (String) authData[0]) ?
                new ResponseEntity<>(HttpStatus.OK) :
                new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

}
