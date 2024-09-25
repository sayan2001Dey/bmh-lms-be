package com.bmh.lms.service.historyChain;

import com.bmh.lms.model.HistoryChain;
import com.bmh.lms.repository.HistoryChainRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class HistoryChainServiceImpl implements HistoryChainService{
    @Autowired
    private HistoryChainRepository historyChainRepository;

    @Override
    public Set<HistoryChain> getFullGraphData(String recId) {
        Set<HistoryChain> res = new HashSet<>();
        prepData(historyChainRepository.findByRecId(recId).orElse(null), res);

        return res;
    }

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

    @Override
   public HistoryChain updateHc(String recId, HistoryChain updatedHistoryChain){
        Optional<HistoryChain> optionalExisting=historyChainRepository.findByRecId(recId);
        if(optionalExisting.isPresent()){
            HistoryChain existingHc= optionalExisting.get();

            existingHc.setName(updatedHistoryChain.getName());
            existingHc.setParents(updatedHistoryChain.getParents());
            existingHc.setChildren(updatedHistoryChain.getChildren());

            return historyChainRepository.save(existingHc);

            } else return null;
        }

    @Override
    public void deleteHc(String recId) {
        try {
            Optional<HistoryChain> optionalExisting = historyChainRepository.findByRecId(recId);
            if (optionalExisting.isPresent()) {
                HistoryChain historyChain = optionalExisting.get();
                historyChainRepository.delete(historyChain);
            } else {

                 throw new EntityNotFoundException("HistoryChain with recId: " + recId + " not found");
            }
        } catch (Exception e) {

            throw new RuntimeException("Error deleting HistoryChain with recId: " + recId, e);
        }
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
