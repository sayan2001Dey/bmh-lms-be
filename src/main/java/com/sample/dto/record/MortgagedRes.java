package com.sample.dto.record;

import com.sample.model.Mortgaged;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MortgagedRes {
    private String mortId;
    private String party;
    private String mortDate;
    private List<String> mortDocFile;
}
