package com.bol.mancala.view;

import com.bol.mancala.exception.MancalaException;
import com.bol.mancala.model.GameStatus;
import com.bol.mancala.model.PlayRound;
import com.bol.mancala.model.Player;
import com.bol.mancala.service.MancalaEngine;
import com.bol.mancala.util.Message;
import lombok.Data;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import java.io.Serializable;
import java.util.Arrays;


@ManagedBean
@ViewScoped
@Data
public class MancalaView implements Serializable {


    @ManagedProperty(value = "#{mancalaEngine}")
    MancalaEngine mancalaEngine;


    // In this game we have two player(no AI).
    private Player player1 = new Player();
    private Player player2 = new Player();

    //This field shows the current turn for game.
    private PlayRound currentPlayRound = PlayRound.PLAYER_ONE;


    /**
     * Board setup
     */
    @PostConstruct
    public void init() {
        setDefaultValue(player1);
        setDefaultValue(player2);
    }


    /**
     * Setting default values for starting the game
     *
     * @param player
     */
    public void setDefaultValue(Player player) {
        Arrays.fill(player.getPits(), 6);
        player.setBigPit(0);
    }


    /**
     * All game decisions are base on the index of pits.
     *
     * @param playerPlayRound
     * @param pitIndex
     */
    public void play(PlayRound playerPlayRound, Integer pitIndex) {


        // Checks if this round is for player.
        if (canPlay(playerPlayRound)) {

            Player player;
            Player opponent;

            if (currentPlayRound == PlayRound.PLAYER_ONE) {
                player = player1;opponent = player2;
            } else {
                player = player2;opponent = player1;
            }

            // If the player's pit is empty he can't play.
            if (player.getPits()[pitIndex] > 0) {

                GameStatus gameStatus = mancalaEngine.GamePlay(player, opponent, pitIndex);

                switch (gameStatus) {
                    case REPEAT_BY_CURRENT_PLAYER:
                        showRepeatGameMessage();
                        break;
                    case GAME_OVER:
                        showWinMessage();
                        break;
                    default:
                        changeRound();
                }

            } else {
                if (mancalaEngine.checkGameOver(player1, player2)) {
                    //showWinMessage();
                    throw new MancalaException("Illegal Play", "This game is over.");
                } else {
                    throw new MancalaException("Illegal pit", "Player can't play on pit with 0 stone.");
                }

            }

        } else {

            if (mancalaEngine.checkGameOver(player1, player2)) {
                //showWinMessage();
                throw new MancalaException("Illegal Play", "This game is over.");
            } else {
                throw new MancalaException("Illegal player", "Current Round is not for this player.");
            }
        }

    }


    /**
     * Checks if clicked (player's) pits are playable.
     *
     * @param playerPlayRound
     * @return
     */
    private Boolean canPlay(PlayRound playerPlayRound) {
        return playerPlayRound == currentPlayRound;
    }


    /**
     * Creates repeat game message for showing in UI.
     */
    private void showRepeatGameMessage() {
        Message.showMessage("Repeat Game", "Current player gets another turn");
    }


    /**
     * Creates a game finishing message for showing in UI.
     */
    private void showWinMessage() {
        Message.showMessage("Game Finish", "Player " + (player1.getScore() > player2.getScore() ? "1" : "2") + " is the winner of game");
    }


    /**
     * change the round after complete the game for one side.
     */
    private void changeRound() {
        currentPlayRound = (currentPlayRound == PlayRound.PLAYER_ONE) ? PlayRound.PLAYER_TWO : PlayRound.PLAYER_ONE;
    }


}
