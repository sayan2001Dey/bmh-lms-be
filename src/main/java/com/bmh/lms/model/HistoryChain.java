package com.bmh.lms.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Data
@Builder
@Document
@NoArgsConstructor
@AllArgsConstructor
public class HistoryChain {
    @Id
    private ObjectId id;

    private String name;

    private String recId;

    private List<ObjectId> parents;

    private List<ObjectId> children;
}