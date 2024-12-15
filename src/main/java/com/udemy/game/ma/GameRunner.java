package com.udemy.game.ma;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class GameRunner {
	
	
	@Autowired
	private GamingConsole game;



	//private MarioGame game;
     // private SuperContraGame game;
	  public GameRunner(GamingConsole game) {
		this.game=game;
	  }

     
	
	public void run() {
		game.up();
		game.down();
		game.right();
		game.left();
		
	}

}
