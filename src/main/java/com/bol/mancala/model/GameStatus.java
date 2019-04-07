package com.bol.mancala.model;

import java.io.Serializable;

/**
 *  this enum used for specifying the next stage of the game.
 */
public enum GameStatus implements Serializable {
    REPEAT_BY_CURRENT_PLAYER, TURN_ROUND, GAME_OVER;
}
