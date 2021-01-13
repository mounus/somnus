package com.example.entiy;

import com.sun.javafx.beans.IDProperty;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
@Data
public class UsersEntity {
    @Id
    private int id;
    private String userName;
    private String password;
    private String telPhone;







    @Override
    public String toString() {
        return "UsersEntity{" +
                "id=" + id +
                ", userName='" + userName + '\'' +
                ", password='" + password + '\'' +
                ", telPhone='" + telPhone + '\'' +

                '}';
    }
}
