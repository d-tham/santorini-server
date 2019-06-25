package ch.uzh.ifi.seal.soprafs19.entity;

import javax.persistence.*;
import java.io.Serializable;

@Entity
public abstract class Action implements Serializable {
    private static final long serialVersionUID = 1L;

    // Attributes
    @Id
    @GeneratedValue
    @Column(name = "action_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "turn_id")
    private Turn turn;

    @Column(nullable = false)
    private Integer workerId;
}
