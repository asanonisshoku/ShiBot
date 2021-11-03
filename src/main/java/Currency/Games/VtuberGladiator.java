package Currency.Games;

import java.util.Random;

public class VtuberGladiator {
    private String name;
    private int strength = 1; // 1-10
    private int speed = 1; // 1-10
    private int defence = 1; //1-10
    private int totalWins = 0;

    public VtuberGladiator(String name) {
        this.name = name;
    }

    public VtuberGladiator(String name, int strength, int speed, int defence, int totalWins) {
        this.name = name;
        setStats(strength, speed, defence);
        this.totalWins = totalWins;
    }

    public void setRandomStats(){
        Random random = new Random();
        this.strength = random.nextInt(10) + 1;
        this.speed = random.nextInt(10) + 1;
        this.defence = random.nextInt(10) + 1;
    }

    public void setStats(int strength, int speed, int defence){
        this.strength = strength;
        this.speed = speed;
        this.defence = defence;
    }

    public void setStrength(int strength) {
        this.strength = strength;
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }

    public void setDefence(int defence) {
        this.defence = defence;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getStrength() {
        return strength;
    }

    public int getSpeed() {
        return speed;
    }

    public int getDefence() {
        return defence;
    }

    public int getTotalWins() {
        return totalWins;
    }

    public void setTotalWins(int totalWins) {
        this.totalWins = totalWins;
    }
}
