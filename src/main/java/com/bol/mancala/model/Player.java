package com.bol.mancala.model;

import lombok.Data;

import java.util.Arrays;

@Data
public class Player {


    //contains the score of each player
    private Integer bigPit = 0;
    //each player has 6 pits
    private Integer[] pits = new Integer[6];

    // the score for every player contains count of stones in his small pits and his big pit.
    public Integer getScore() {
        Integer score = Arrays.stream(pits).mapToInt(Integer::intValue).sum();
        score += bigPit;
        return score;
    }

    //returns the count of stones in the wanted pit and then sets 0 for that.
    public  Integer pickUpStones(Integer pitIndex) {
        Integer stones = pits[pitIndex];
        pits[pitIndex] = 0;
        return stones;
    }

    // used for check if game over.
    public Boolean allSmallPitsEmpty(){
        return Arrays.stream(pits).mapToInt(Integer::intValue).sum()== 0 ? true :false;
    }

    // adds one stone in bigPit
    public Integer plusOneBigPit(){
        return ++bigPit;
    }
    // adds one stone in index :pitIndex
    public Integer plusOneAtPit(Integer pitIndex){
        return ++pits[pitIndex];
    }

}
