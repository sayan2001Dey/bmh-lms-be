package com.bmh.lms.service.historyChain;

import com.bmh.lms.model.HistoryChain;
import com.bmh.lms.repository.HistoryChainRepository;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.ExampleMatcher;
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
        prepData(historyChainRepository.findByRecId(recId), res);

        return res;
    }

    private void prepData(HistoryChain data, Set<HistoryChain> res) {
        if(res.contains(data))
            return;
        res.add(data);

        hcLoopfn(data.getParents(), res);
        hcLoopfn(data.getChildren(), res);
    }

    private void hcLoopfn(List<ObjectId> ids, Set<HistoryChain> res) {
        ids.forEach(id -> {
            HistoryChain temp = historyChainRepository.findById(id).orElse(null);
            if(temp==null)
                return;
            prepData(temp, res);
        });
    }
}
