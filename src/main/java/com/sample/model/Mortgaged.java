package com.sample.model;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.ElementCollection;
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

@Data
@NoArgsConstructor
@Entity
@Table(name="mortgaged")
public class Mortgaged {
	
	@Id
	@Column(name = "id")
    @GeneratedValue(strategy = GenerationType.AUTO)
	private long id;
	
	@Column(name="mort_party")
	private String party;
	
	@Column(name="mort_Date")
	private String mortDate;
    
	@Column(name="doc")
	@ElementCollection
	private List<String> docFile;
	
	@ManyToOne()
	@JoinColumn(name = "record_id")
	private Record record;
}
