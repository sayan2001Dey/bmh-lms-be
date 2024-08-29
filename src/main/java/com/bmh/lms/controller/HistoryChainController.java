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
@RequestMapping("api/history")
public class HistoryChainController {
    @Autowired
    private HistoryChainService historyChainService;

    @Autowired
    private AuthService authService;

    @PostMapping()
    public ResponseEntity<Set<HistoryChain>> saveHc(
            @RequestHeader(value = HttpHeaders.AUTHORIZATION, required = false) String token
    ){
        Object[] authData = authService.verifyToken(token);
        if(authData == null) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
        return null;
    }

    @GetMapping("{id}")
    public ResponseEntity<Set<HistoryChain>> getHcById(
            @RequestHeader(value = HttpHeaders.AUTHORIZATION, required = false) String token,
            @PathVariable String id
    ) {
        Object[] authData = authService.verifyToken(token);
        if(authData == null) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }

        Set<HistoryChain> res = historyChainService.getFullGraphData(id);
        if (res != null) {
            return new ResponseEntity<>(res, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

}
