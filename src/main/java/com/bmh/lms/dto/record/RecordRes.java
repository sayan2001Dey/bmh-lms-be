package com.bmh.lms.dto.record;

import lombok.*;

import java.util.List;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RecordRes extends RecordCommon {
    private Set<MortgagedRes> mortgagedData;
    private List<String> scanCopyFile;
    private List<String> mutationFile;
    private List<String> conversionFile;
    private List<String> documentFile;
    private List<String> areaMapFile;
    private List<String> hcdocumentFile;
}
