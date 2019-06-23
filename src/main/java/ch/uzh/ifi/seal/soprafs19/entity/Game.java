package ch.uzh.ifi.seal.soprafs19.entity;

import ch.uzh.ifi.seal.soprafs19.constant.GameStatus;
import java.io.Serializable;
import java.util.Arrays;
import java.util.List;
import javax.persistence.*;

@Entity
public class Game implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue
    @Column(name = "game_id")
    private Long id;

    @Column(nullable = false)
    private GameStatus gameStatus;

    @Column(nullable = false)
    private Boolean isGodMode;

    @Column(nullable = false)
    @OneToMany(mappedBy = "game")
    private List<Player> players;

    // Getters & Setters
    public Long getId() {
        return id;
    }

    public GameStatus getGameStatus() {
        return gameStatus;
    }

    public void setGameStatus(GameStatus gameStatus) {
        this.gameStatus = gameStatus;
    }

    public Boolean getIsGodMode() {
        return isGodMode;
    }

    public void setIsGodMode(Boolean isGodMode) {
        this.isGodMode = isGodMode;
    }

    public List<Player> getPlayers() {
        return players;
    }

    public void setPlayers(Player ...players) {
        this.players.addAll(Arrays.asList(players));
    }


    @Override
    public boolean equals(Object o) {
        if (o == this) return true;
        if (!(o instanceof Game)) {
            return false;
        }
        Game game = (Game) o;
        return this.getId().equals(game.getId());
    }
}