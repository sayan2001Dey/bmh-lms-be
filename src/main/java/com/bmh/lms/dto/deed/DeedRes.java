package com.bmh.lms.dto.deed;

import com.bmh.lms.dto.record.MortgagedRes;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DeedRes extends DeedCommon {
    private Set<MortgagedRes> mortgagedData;
    private List<String> scanCopyFile;
    private List<String> mutationFile;
    private List<String> conversionFile;
    private List<String> documentFile;
    private List<String> areaMapFile;
    private List<String> hcdocumentFile;
    private List<String> vestedFile;
    private List<String> parchaFile;
}
