package com.sample.dto.record;

import com.sample.model.Mortgaged;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MortgagedRes extends Mortgaged {
    private List<String> mortDocFile;
}
