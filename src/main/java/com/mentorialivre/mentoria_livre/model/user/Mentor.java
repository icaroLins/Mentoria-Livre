package com.mentorialivre.mentoria_livre.model.user;


import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "mentor_tb")
@Getter
@Setter
public class Mentor extends User {

    private String especialidade;

    public Mentor() {
        super(Role.MENTOR);
    }
}

