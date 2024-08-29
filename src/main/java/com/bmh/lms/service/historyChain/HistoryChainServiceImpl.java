package com.bmh.lms.service.historyChain;

import com.bmh.lms.model.HistoryChain;
import com.bmh.lms.repository.HistoryChainRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class HistoryChainServiceImpl implements HistoryChainService{
    @Autowired
    private HistoryChainRepository historyChainRepository;


    /**
     * @param recId
     * @return
     */
    @Override
    public Set<HistoryChain> getFullGraphData(String recId) {
        Set<HistoryChain> res = new HashSet<>();
        prepData(historyChainRepository.findByRecId(recId).orElse(null), res);

        return res;
    }

    /**
     * @param historyChain
     * @param username
     * @return
     */
    @Override
    public HistoryChain saveHc(HistoryChain historyChain, String username) {
        return historyChainRepository.save(historyChain);
    }

    private void prepData(HistoryChain data, Set<HistoryChain> res) {
        if(res.contains(data))
            return;
        res.add(data);

        hcLoopFn(data.getParents(), res);
        hcLoopFn(data.getChildren(), res);
    }

    private void hcLoopFn(List<String> ids, Set<HistoryChain> res) {
        ids.forEach(id -> {
            HistoryChain temp = historyChainRepository.findByRecId(id).orElse(null);
            if(temp==null)
                return;
            prepData(temp, res);
        });
    }
}
