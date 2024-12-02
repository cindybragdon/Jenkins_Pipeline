package edu.mv.mv.db.models;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;

@Data
@Table(name = "rocket")
@Entity
public class Rocket {

    @Id
    private int id;
    private String name;
    private String sorte;

}
