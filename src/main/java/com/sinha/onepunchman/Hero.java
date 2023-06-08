package com.sinha.onepunchman;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name="heroes")
public class Hero {
    @Id
    private int id;
    @Column(name="hero_rank")
    private String rank;
    private String name;
    @Column(name="class_name")
    private String className;
    private String powers;
    private String gender;
}
