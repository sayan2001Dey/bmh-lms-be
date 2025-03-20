package com.bmh.lms.model;

import jakarta.persistence.*;

import lombok.*;

@NoArgsConstructor
@Data
@Entity
@Table(name="partly_sold")
@EqualsAndHashCode(callSuper = false)
public class PartlySold extends CommonProperties {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

    @Column(name = "part_id")
    private String partId;

    @Column(name="deed_id")
    private String deedId;

    @Column(name="sale_party")
    private String sale;
    
    @Column(name="date_of_sale")
	private String date;

    @Column(name="pl_old_rs_dag")
    private String plOldRsDag;

    @Column(name="pl_new_lr_dag")
    private String plNewLrDag;

    @Column(name="quantity")
	private String qty;
	
    @Column(name="deed_link")
	private String deedLink;
}
