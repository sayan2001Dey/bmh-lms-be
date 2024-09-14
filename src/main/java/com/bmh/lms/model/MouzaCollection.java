package com.bmh.lms.model;

import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Data
@Builder
@Document(value = "mouza")
@NoArgsConstructor
@AllArgsConstructor
public class MouzaCollection {

    @Id
    private String id;

    private List<MouzaLandSpecifics> landSpesifics;
}
