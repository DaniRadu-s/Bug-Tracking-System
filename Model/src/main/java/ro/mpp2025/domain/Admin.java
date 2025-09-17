package ro.mpp2025.domain;

import jakarta.persistence.*;
import ro.mpp2025.domain.Entity;

@jakarta.persistence.Entity
@Table(name = "Admins")
public class Admin extends User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    public Admin() {}
    public Admin(String username, String password, String email, String name) {
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