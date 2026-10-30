package com.mentorialivre.mentoria_livre.model.user;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "mentorado_tb")
@Getter
@Setter
public class Mentorado extends User {

    private String objetivo;

    public Mentorado() {
        super(Role.MENTORADO);
    }
}
