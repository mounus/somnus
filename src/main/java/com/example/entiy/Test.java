package com.example.entiy;


import lombok.Data;

import java.io.Serializable;

import javax.persistence.Id;
import javax.persistence.Table;

@Data
@Table(name="test")
public class Test  implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    private Integer id;
    private String name;
    private Integer age;
    private String time;
    private String zodiac;
    private String constellation;
    private  String things;
    private String img;
    private String period;


}
