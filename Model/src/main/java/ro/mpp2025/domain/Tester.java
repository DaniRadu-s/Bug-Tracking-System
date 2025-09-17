package ro.mpp2025.domain;

import jakarta.persistence.*;
import jakarta.persistence.Entity;

@Entity
@Table(name = "Testers")
public class Tester extends User{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_tester")
    private Long id;

    public Tester() {}
    public Tester(String username, String password,String email, String name) {
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
