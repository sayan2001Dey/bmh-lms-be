package com.bmh.lms.model;

import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;
import java.util.Set;

@Data
@Builder
@Document(value = "deed")
@NoArgsConstructor
@AllArgsConstructor
public class DeedCollection {
    @Id
    private String id;

    private List<DeedMouza> mouza;

    private Set<String> recIds;
}
