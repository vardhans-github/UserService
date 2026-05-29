package com.example.UserService.Models;
import jakarta.persistence.Entity;

@Entity
public class Role extends BaseModel{

    String value;

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
