package ch.uzh.ifi.seal.soprafs19.entity;

import java.io.Serializable;
import javax.persistence.*;

@Entity
@SequenceGenerator(name="worker_seq")
public class Worker implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "worker_seq")
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