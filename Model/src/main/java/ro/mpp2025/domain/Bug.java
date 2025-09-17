package ro.mpp2025.domain;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@jakarta.persistence.Entity
@Table(name = "Bugs")
public class Bug extends Entity<Long>{
    @Id
    @GeneratedValue
    @Column(name = "id_bug")
    private Long id;
    private String name;
    private String description;

    @ManyToOne
    @JoinColumn(name = "testerID")
    private Tester tester;

    @ManyToOne
    @JoinColumn(name = "programmerID")
    private Programmer programmer;
    @Enumerated(EnumType.STRING)
    private BugStatus status;
    private LocalDateTime date;
    public Bug (String name, String description,Tester tester, Programmer programmer, LocalDateTime date) {
        this.name = name;
        this.description = description;
        this.tester = tester;
        this.programmer = programmer;
        this.status = BugStatus.NEW;
        this.date = date;
    }
    public Bug(){}

    @Override
    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public Long getId() {
        return id;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    public Tester getTester() {
        return tester;
    }
    public void setTester(Tester tester) {
        this.tester = tester;
    }
    public Programmer getProgrammer() {
        return programmer;
    }
    public void setProgrammer(Programmer programmer) {
        this.programmer = programmer;
    }
    public BugStatus getStatus() {
        return status;
    }
    public void setStatus(BugStatus status) {
        this.status = status;
    }
    public LocalDateTime getDate() {
        return date;
    }
    public void setDate(LocalDateTime date) {
        this.date = date;
    }
    @Override
    public String toString() {
        return "Bug{" +
                "name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", reportedBy=" + tester +
                ", assignedTo=" + programmer +
                ", status=" + status +
                ", timestamp=" + date +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Bug)) return false;
        Bug bug = (Bug) o;
        return name.equals(bug.name) &&
                description.equals(bug.description) &&
                tester.equals(bug.tester) &&
                programmer.equals(bug.programmer) &&
                status == bug.status &&
                date.equals(bug.date);
    }

    @Override
    public int hashCode() {
        int result = name.hashCode();
        result = 31 * result + description.hashCode();
        result = 31 * result + tester.hashCode();
        result = 31 * result + programmer.hashCode();
        result = 31 * result + status.hashCode();
        result = 31 * result + date.hashCode();
        return result;
    }
}
