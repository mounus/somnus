package com.example.entiy;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
//月嫂表
@Table(name="yx_matorn")
@Data
public class Mechanism {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue
    private Integer id;
    private  Integer uid;
    private  String name;

}
