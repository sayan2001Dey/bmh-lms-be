package com.bmh.lms.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table( name ="mouza")
public class Mouza extends CommonProperties {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column
    private String mouzaId;

    @Column
    private String groupId;

    @Column
    private String mouza;

    @Column
    private String block;

    @Column
    private Long jlno;

    @Column
    private String landSpecifics;
}
