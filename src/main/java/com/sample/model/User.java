package com.sample.model;

import lombok.Getter;

import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "User")
@NoArgsConstructor
@Getter
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @Setter
    @Column(columnDefinition = "varchar(20) default 'No name'")
    private String name;

    @Setter
    @Column(columnDefinition = "varchar(60)", unique = true, nullable = false)
    private String email;

    @Setter
    @Column(columnDefinition = "varchar(255)", nullable = false)
    private String password;
} 
