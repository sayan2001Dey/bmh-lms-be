package com.sample.service.utils;

import com.sample.model.UID;
import com.sample.repository.UIDRepository;
import org.springframework.beans.factory.annotation.Autowired;

public class CommonUtils {
    @Autowired
    private static UIDRepository uidRepository;

    public static String generateUID(String modelName, String prefix) {
        UID uid = uidRepository.findByModelName(modelName).orElse(null);
        if(uid == null) {
            uid = new UID();
            uid.setModelName(modelName);
            uid.setLastId(0);
        }
        String res = prefix + '-' + uid.getLastId() + String.valueOf(Math.random()).substring(2,5);
        uid.setLastId(uid.getLastId()+1);
        uidRepository.save(uid);
        return res;
    }
}
