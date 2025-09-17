package ro.mpp2025.domain;

import jakarta.persistence.*;
import jakarta.persistence.Entity;

@Entity
@Table(name = "Programmers")
public class Programmer extends User{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_programmer")
    private Long id;
    public Programmer() {}
    public Programmer(String username, String password, String email, String name) {
        super(username, password, email, name);
    }
    @Override
    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public Long getId() {
        return id;
    }
}
