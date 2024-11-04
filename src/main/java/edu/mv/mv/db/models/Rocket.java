package edu.mv.mv.db.models;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Table(name = "rocket")
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Rocket {

    @Id
    private int id;
    private String name;
    private String sorte;

}
