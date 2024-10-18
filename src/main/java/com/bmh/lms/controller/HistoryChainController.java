package com.bmh.lms.controller;

import com.bmh.lms.model.HistoryChain;
import com.bmh.lms.service.auth.AuthService;
import com.bmh.lms.service.historyChain.HistoryChainService;
import com.google.common.net.HttpHeaders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@RestController
@CrossOrigin()
@RequestMapping("api/hc")
public class HistoryChainController {
    @Autowired
    private HistoryChainService historyChainService;

    @Autowired
    private AuthService authService;

    @PostMapping()
    public ResponseEntity<HistoryChain> saveHc(
            @RequestHeader(value = HttpHeaders.AUTHORIZATION, required = false) String token,
            @RequestBody HistoryChain historyChain
    ){
//        Object[] authData = authService.verifyToken(token);
//        if(authData == null) {
//            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
//        }
        return new ResponseEntity<>(historyChainService.saveHc(historyChain, ""), HttpStatus.OK);

    }

    @GetMapping("{id}")
    public ResponseEntity<Set<HistoryChain>> getHcById(
            @RequestHeader(value = HttpHeaders.AUTHORIZATION, required = false) String token,
            @PathVariable String id
    ) {
//        Object[] authData = authService.verifyToken(token);
//        if(authData == null) {
//            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
//        }

        Set<HistoryChain> res = historyChainService.getFullGraphData(id);
        if (res != null) {
            return new ResponseEntity<>(res, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping("/{recId}")
    public ResponseEntity<HistoryChain> updateHc(@PathVariable String recId,
                                                 @RequestBody HistoryChain updatedHistoryChain) {
        HistoryChain updated = historyChainService.updateHc(recId, updatedHistoryChain);
        if (updated != null) {
            return new ResponseEntity<>(updated, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/{recId}")
    public ResponseEntity<Void> deleteHc(@PathVariable String recId) {
        try {
            historyChainService.deleteHc(recId);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT); // 204 No Content for successful deletion
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND); // 404 Not Found if the entity does not exist
        }
    }

}
