package com.bmh.lms.service.utils;

import com.bmh.lms.model.UID;
import com.bmh.lms.repository.UIDRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CommonUtils {
    @Autowired
    private UIDRepository uidRepository;

    public String generateUID(String modelName, String prefix) {
        UID uid = uidRepository.findByModelName(modelName).orElse(null);
        if(uid == null) {
            uid = new UID();
            uid.setModelName(modelName);
            uid.setLastId(0);
        }
        uid.setLastId(uid.getLastId()+1);
        String res = prefix + '-' + uid.getLastId() + String.valueOf(Math.random()).substring(2,5);
        uidRepository.save(uid);
        return res;
    }

    public String generateUID(String modelName, String prefix, Boolean enableSave) {
        if(enableSave) return generateUID(modelName, prefix);
        else {
            UID uid = uidRepository.findByModelName(modelName).orElse(null);
            if (uid == null) {
                uid = new UID();
                uid.setModelName(modelName);
                uid.setLastId(0);
            }
            uid.setLastId(uid.getLastId() + 1);
            return prefix + '-' + uid.getLastId() + String.valueOf(Math.random()).substring(2, 5);
        }
    }
}
