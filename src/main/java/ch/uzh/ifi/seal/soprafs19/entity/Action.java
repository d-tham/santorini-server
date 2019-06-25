package ch.uzh.ifi.seal.soprafs19.entity;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@SequenceGenerator(name="action_seq")
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public abstract class Action implements Serializable {
    private static final long serialVersionUID = 1L;

    // Attributes
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "action_seq")
    @Column(name = "action_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "turn_id")
    private Turn turn;

    @Column(nullable = false)
    private Integer workerId;

    // Getters & Setters
    public Long getId() {
        return id;
    }

    public Integer getWorkerId() {
        return workerId;
    }
}
