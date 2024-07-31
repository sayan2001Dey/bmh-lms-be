package com.sample.dto.record;

import com.sample.model.Mortgaged;
import lombok.*;

import java.util.List;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RecordRes extends RecordReq {
    Set<Mortgaged> mortgagedData;
    List<String> scanCopyFile;
    List<String> mutationFile;
    List<String> conversionFile;
    List<String> documentFile;
    List<String> areaMapFile;
    List<String> hcdocumentFile;
}
