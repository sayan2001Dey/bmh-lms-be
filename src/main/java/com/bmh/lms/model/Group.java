package com.bmh.lms.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table( name ="group_master")
public class Group extends CommonProperties {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String groupId;

    private String groupName;

    private String state;


    private String city;


    private Integer pincode;
}
