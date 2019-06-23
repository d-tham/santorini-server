package ch.uzh.ifi.seal.soprafs19.entity;

import java.io.Serializable;
import javax.persistence.*;

@Entity
public class Worker implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue
    @Column(name = "worker_id")
    private Long id;

    @OneToOne(mappedBy = "worker")
    private Field field;

    // Getters & Setters
    public Long getId() {
        return id;
    }

    @Override
    public boolean equals(Object o) {
        if (o == this) return true;
        if (!(o instanceof Worker)) {
            return false;
        }
        Worker worker = (Worker) o;
        return this.getId().equals(worker.getId());
    }
}