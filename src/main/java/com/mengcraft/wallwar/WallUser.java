package com.mengcraft.wallwar;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.UUID;

/**
 * Created on 16-3-9.
 */
@Entity
public class WallUser {

    @Id
    private UUID id;

    private int joining;
    private int winning;

    private int killed;
    private int dead;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public int getJoining() {
        return joining;
    }

    public void setJoining(int joining) {
        this.joining = joining;
    }

    public int getWinning() {
        return winning;
    }

    public void setWinning(int winning) {
        this.winning = winning;
    }

    public int getKilled() {
        return killed;
    }

    public void setKilled(int killed) {
        this.killed = killed;
    }

    public int getDead() {
        return dead;
    }

    public void setDead(int dead) {
        this.dead = dead;
    }

}
