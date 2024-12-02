package edu.mv.mv.db.models;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;

@Table(name = "rocket")
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class Rocket {

    @Id
    private int id;
    private String name;
    private String sorte;

    @Override
    public String toString() {
        return "Rocket{id=" + id + ", name='" + name + "', sorte='" + sorte + "'}";
    }
}
