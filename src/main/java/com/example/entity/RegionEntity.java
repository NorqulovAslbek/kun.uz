package com.example.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "region")
public class RegionEntity extends BaseEntity{
    private Integer orderNumber;
    private String nameUz;
    private String nameRu;
    private String nameEn;
}
