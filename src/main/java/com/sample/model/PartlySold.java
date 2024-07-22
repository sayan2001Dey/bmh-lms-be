package com.sample.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
@Entity
@Table(name="partly_sold")
public class PartlySold {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	
    @Column(name="sale_party")
    private String sale;
    
    @Column(name="date_of_sale")
	private String date;
	
    @Column(name="quantity")
	private String qty;
	
    @Column(name="deed_link")
	private String deedLink;
    
    @ManyToOne()
    @JoinColumn(name = "record_id")
    private Record record;
}
