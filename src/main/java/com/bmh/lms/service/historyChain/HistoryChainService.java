package com.bmh.lms.service.historyChain;

import com.bmh.lms.model.HistoryChain;

import java.util.Optional;
import java.util.Set;

public interface HistoryChainService {
    Set<HistoryChain> getFullGraphData(String recId);
}
