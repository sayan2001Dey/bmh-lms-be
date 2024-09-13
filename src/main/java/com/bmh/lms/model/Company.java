package com.bmh.lms.model;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table( name = "company")
public class Company extends  CommonProperties{
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String companyId;

    private String companyName;

    private String companyAddress;

    private String panNumber;
}
