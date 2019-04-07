package com.bol.mancala;


import com.bol.mancala.exception.MancalaException;
import com.bol.mancala.model.GameStatus;
import com.bol.mancala.model.PlayRound;
import com.bol.mancala.model.Player;
import com.bol.mancala.service.MancalaEngine;
import com.bol.mancala.view.MancalaView;


import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;


import org.springframework.boot.test.SpringApplicationContextLoader;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import  static org.assertj.core.api.Assertions.assertThat;

import java.util.Arrays;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes={MancalaEngine.class ,MancalaView.class }, loader=SpringApplicationContextLoader.class)
public class MancalaApplicationTests {


    private static MancalaEngine mancalaEngine;
    private static MancalaView mancalaView;




    @BeforeClass
    public static void init() {
        mancalaEngine =  new MancalaEngine();
        mancalaView =  new MancalaView();
        mancalaView.setMancalaEngine(mancalaEngine);
        mancalaView.init();

    }

    @Before
    public  void beforeEachMethod() {
        mancalaView.init();
    }

    @Test()
    public void testGetMancalaException() {
        try {
            mancalaView.play(PlayRound.PLAYER_TWO,0);    
        }catch (MancalaException e){
            assertThat(e).isEqualToComparingFieldByField(new MancalaException("Illegal player", "Current Round is not for this player."));
        }
        
    }
    @Test()
    public void testGetMancalaViewGameOverException() {
        try {
            Arrays.fill(mancalaView.getPlayer1().getPits(), 0);
            mancalaView.play(PlayRound.PLAYER_TWO,0);
        }catch (MancalaException e){
            assertThat(e).isEqualToComparingFieldByField(new MancalaException("Illegal Play", "This game is over."));
        }

    }

    @Test
    public void testIllegalPitException() {
        try {
            mancalaView.getPlayer1().getPits()[0] = 0;
            mancalaView.play(PlayRound.PLAYER_ONE, 0);
        }catch(Exception e){
            assertThat(e).isEqualToComparingFieldByField(new MancalaException("Illegal pit", "Player can't play on pit with 0 stone."));
        }
    }

    @Test(expected = MancalaException.class)
    public void testIllegalPit() {
        Arrays.fill(mancalaView.getPlayer1().getPits(), 0);
        mancalaView.play(PlayRound.PLAYER_ONE,0);
    }
    

    @Test
    public void testRepeatGameStatus() {
        GameStatus  gameStatus = mancalaEngine.GamePlay(mancalaView.getPlayer1(),mancalaView.getPlayer2(),0);
        assertThat(gameStatus).isEqualTo(GameStatus.REPEAT_BY_CURRENT_PLAYER );
    }

    @Test
    public void testTurnRoundStatus() {
        GameStatus  gameStatus = mancalaEngine.GamePlay(mancalaView.getPlayer1(),mancalaView.getPlayer2(),1);
        assertThat(gameStatus).isEqualTo(GameStatus.TURN_ROUND);
    }

    @Test
    public void testGameOver() {
        Arrays.fill(mancalaView.getPlayer1().getPits(), 0);
        GameStatus  gameStatus = mancalaEngine.GamePlay(mancalaView.getPlayer1(),mancalaView.getPlayer2(),1);
        assertThat(gameStatus).isEqualTo(GameStatus.GAME_OVER);
    }

    @Test
    public void testGameOverMethod() {
        Arrays.fill(mancalaView.getPlayer1().getPits(), 0);
        assertThat(mancalaEngine.checkGameOver(mancalaView.getPlayer1(),mancalaView.getPlayer2())).isTrue();
    }


}
