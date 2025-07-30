package net.codejava.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
public class ImcHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String username;
    private String name;
    private int age;
    private String gender;
    private float height;
    private float weight;
    private float imc;
    private LocalDateTime date;

    public ImcHistory() {
    }
    public ImcHistory(String username, String name, int age,String gender, float height, float weight, float imc, LocalDateTime date)
    {
        this.username = username;
        this.name = name;
        this.age = age;
        this.gender = gender;
        this.height = height;
        this.weight = weight;
        this.imc = imc;
        this.date = date;
    }
    public Long getId() {
    return id;
}

public void setId(Long id) {
    this.id = id;
}

public String getUsername() {
    return username;
}

public void setUsername(String username) {
    this.username = username;
}

public String getName() {
    return name;
}

public void setName(String name) {
    this.name = name;
}

public int getAge() {
    return age;
}

public void setAge(int age) {
    this.age = age;
}

public String getGender() {
    return gender;
}

public void setGender(String gender) {
    this.gender = gender;
}

public float getHeight() {
    return height;
}

public void setHeight(float height) {
    this.height = height;
}

public float getWeight() {
    return weight;
}

public void setWeight(float weight) {
    this.weight = weight;
}

public float getImc() {
    return imc;
}

public void setImc(float imc) {
    this.imc = imc;
}

public LocalDateTime getDate() {
    return date;
}

public void setDate(LocalDateTime date) {
    this.date = date;
}
}