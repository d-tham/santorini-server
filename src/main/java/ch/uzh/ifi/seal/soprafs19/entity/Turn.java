package ch.uzh.ifi.seal.soprafs19.entity;

import java.io.Serializable;
import javax.persistence.*;
import java.util.List;

@Entity
@SequenceGenerator(name="turn_seq")
public class Turn implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "turn_seq")
    @Column(name = "turn_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "game_id")
    private Game game;

    @Column(nullable = false)
    private Long playerId;

    @OneToMany(mappedBy = "turn")
    private List<Action> actions;

    @Column(nullable = false)
    private Boolean hasMoved;

    @Column(nullable = false)
    private Boolean hasBuilt;

    @Column(nullable = false)
    private Integer movesLeft;

    @Column(nullable = false)
    private Integer buildsLeft;

    // Getters & Setters
    public Long getId() {
        return id;
    }


    @Override
    public boolean equals(Object o) {
        if (o == this) return true;
        if (!(o instanceof Player)) {
            return false;
        }
        Player player = (Player) o;
        return this.getId().equals(player.getId());
    }
}