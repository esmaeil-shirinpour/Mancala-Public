package com.bol.mancala.service;



import com.bol.mancala.model.GameStatus;
import com.bol.mancala.model.Player;

import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;
import java.io.Serializable;

@ManagedBean
@ApplicationScoped
public class MancalaEngine implements Serializable {

    // these constants assume for each player in his turn.

    public static final Integer MAX_PLAYER_PIT_POSITION = 5;
    public static final Integer PLAYER_BIG_PIT_POSITION = 6;
    public static final Integer MIN_OPPONENT_PIT_POSITION = 7;
    public static final Integer MAX_OPPONENT_PIT_POSITION = 12;


    /**
     * the main part of this game engine is this method.
     * checking starting game permissions have been checked in the previous method (in MancalaView Class )
     * @param player 
     * @param opponent
     * @param pitIndex
     * @return
     */
    public GameStatus GamePlay(Player player, Player opponent, Integer pitIndex) {

        Integer stones = player.pickUpStones(pitIndex);

        Integer lastPosition = sowRight(player, opponent, pitIndex, stones);

        captureStones(player, opponent, lastPosition);

        Boolean gameOver = checkGameOver(player, opponent);

        if (gameOver) {
            return GameStatus.GAME_OVER;
        } else {
            return lastPosition == PLAYER_BIG_PIT_POSITION ? GameStatus.REPEAT_BY_CURRENT_PLAYER : GameStatus.TURN_ROUND;
        }
    }



    /**
     * if all small pits in one side are empty so we can say the game is over.
     * @param player1
     * @param player2
     * @return
     */
    public Boolean checkGameOver(Player player1, Player player2) {

        if (player1.allSmallPitsEmpty() || player2.allSmallPitsEmpty()) {
            return true;
        }

        return false;
    }

    

    /**
     * checks if the last stone lands in player's empty pit, the player captures his own stone and all stones in the opposite pit. 
     * @param player
     * @param opponent
     * @param lastPosition
     */
    private void captureStones(Player player, Player opponent, Integer lastPosition) {
        if (lastPosition <= MAX_PLAYER_PIT_POSITION && player.getPits()[lastPosition] == 1) {
            Integer capturedStones = player.pickUpStones(lastPosition) + opponent.pickUpStones(getOppositeIndex(lastPosition));
            player.setBigPit(player.getBigPit() + capturedStones);
        }
    }


    /**
     * returns opposite index of player's pit index.
     * player's pit indexes:     0 1 2 3 4 5
     * opponent's pit indexes:   5 4 3 2 1 0
     * @param position
     * @return
     */
    private Integer getOppositeIndex(Integer position) {
        return MAX_PLAYER_PIT_POSITION - position;
    }



    /**
     * sows stones in next right pits excluding opponent's big bit.
     * indexes from 0 to 5 sets for player's (small) pits, 6 for player's big pit and 7 to 12 for opponent's (small) pits.
     * (we exclude opponents' big pit in play)
     * player's pit indexes:     0  1  2  3   4   5   big pit >> 6
     * opponent's pit indexes:   7  8  9  10  11  12
     * @param player
     * @param opponent
     * @param pitIndex
     * @param stones
     * @return
     */
    private Integer sowRight(Player player, Player opponent, Integer pitIndex, Integer stones) {
        // start point is next right pit.
        Integer nextPosition = pitIndex + 1;

        // a play for one side continues till finish his stones.
        while (stones > 0) {

            if (nextPosition > MAX_OPPONENT_PIT_POSITION) {
                nextPosition = 0;
            }

            if (nextPosition == PLAYER_BIG_PIT_POSITION) {
                player.plusOneBigPit();
            } else if (nextPosition <= MAX_PLAYER_PIT_POSITION) {
                player.plusOneAtPit(nextPosition);
            } else {
                Integer opponentPitIndext = getOpponentPitIndext(nextPosition);
                opponent.plusOneAtPit(opponentPitIndext);
            }
            // after every sow, we should remove one from stones.
            stones--;
            nextPosition++;
        }
        return --nextPosition;
    }


    /***
     * return index of opponent's pit
     * in over game we assign positions from 7 to 12 for opponent's (small) pits.
     * opponent's position :             7  8  9  10  11  12
     * opponent's real  pit indexes:     0  1  2  3   4   5
     * for getting the real position we should reduce 7 from every position.
     * @param position
     * @return
     */
    private Integer getOpponentPitIndext(Integer position) {
        return position - MIN_OPPONENT_PIT_POSITION;
    }

}
