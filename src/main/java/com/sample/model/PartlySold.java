package com.sample.model;

import javax.persistence.*;

import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
@Entity
@Table(name="partly_sold")
public class PartlySold extends CommonProperties {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

    @Column(name = "part_id")
    private String partId;

    @Column(name="rec_id")
    private String recId;

    @Column(columnDefinition="varchar(50) default 'NA'")
    private String slno;

    @Column(name="sale_party")
    private String sale;
    
    @Column(name="date_of_sale")
	private String date;
	
    @Column(name="quantity")
	private String qty;
	
    @Column(name="deed_link")
	private String deedLink;

    @ManyToOne(cascade= CascadeType.ALL)
    @JoinColumn(name = "record_id")
    private Record record;
}
