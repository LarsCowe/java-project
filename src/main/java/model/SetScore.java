package model;

import java.util.Objects;

public class SetScore {
    private int setNumber;
    private int homeScore;
    private int awayScore;

    public SetScore() {}

    public SetScore(int setNumber, int homeScore, int awayScore) {
        this.setNumber = setNumber;
        this.homeScore = homeScore;
        this.awayScore = awayScore;
    }

    public int getSetNumber() {
        return setNumber;
    }

    public void setSetNumber(int setNumber) {
        this.setNumber = setNumber;
    }

    public int getHomeScore() {
        return homeScore;
    }

    public void setHomeScore(int homeScore) {
        this.homeScore = homeScore;
    }

    public int getAwayScore() {
        return awayScore;
    }

    public void setAwayScore(int awayScore) {
        this.awayScore = awayScore;
    }

    // Nodig voor de scores te vergelijken
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SetScore that = (SetScore) o;
        return setNumber == that.setNumber
                && homeScore == that.homeScore
                && awayScore == that.awayScore;
    }

    @Override
    public int hashCode() {
        return Objects.hash(setNumber, homeScore, awayScore);
    }


}
