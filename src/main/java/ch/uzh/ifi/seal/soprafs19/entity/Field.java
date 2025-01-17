package ch.uzh.ifi.seal.soprafs19.entity;

import java.io.Serializable;
import javax.persistence.*;

@Entity
@SequenceGenerator(name="field_seq")
public class Field implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "field_seq")
    @Column(name = "field_id")
    private Long id;

    @Column(nullable = false)
    private Integer posX;

    @Column(nullable = false)
    private Integer posY;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "worker_id", referencedColumnName = "worker_id")
    private Worker worker;

    @Column(nullable = false)
    private Integer towerHeight;

    @Column(nullable = false)
    private Boolean hasDome;

    // Constructor
    public Field(Integer posX, Integer posY) {
        this.posX = posX;
        this.posY = posY;
        towerHeight = 0;
        hasDome = false;
    }

    // Getters & Setters
    public Long getId() {
        return id;
    }

    public Integer getPosX() {
        return posX;
    }

    public Integer getPosY() {
        return posY;
    }

    public Worker getWorker() {
        return worker;
    }

    public void setWorker(Worker worker) {
        this.worker = worker;
    }

    public Integer getTowerHeight() {
        return towerHeight;
    }

    public void addBlock() {
        towerHeight++;
    }

    public Boolean getHasDome() {
        return hasDome;
    }

    public void addDome() {
        hasDome = true;
    }

    public Boolean isOccupied() {
        return (worker != null || hasDome);
    }

    @Override
    public boolean equals(Object o) {
        if (o == this) return true;
        if (!(o instanceof Field)) {
            return false;
        }
        Field field = (Field) o;
        return this.getId().equals(field.getId());
    }
}