package com.udemy.game;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

import com.udemy.game.ma.GameRunner;
import com.udemy.game.ma.GamingConsole;
import com.udemy.game.ma.MarioGame;
import com.udemy.game.ma.PacmanGame;
import com.udemy.game.ma.SuperContraGame;

@SpringBootApplication
public class GameApplication {

	public static void main(String[] args) {
		ConfigurableApplicationContext context = SpringApplication.run(GameApplication.class, args);
		//MarioGame game=new MarioGame();
		//SuperContraGame game=new SuperContraGame();
		//GamingConsole game=new MarioGame();
		//GameRunner runner=new GameRunner(game);
		GameRunner runner = context.getBean(GameRunner.class);
		runner.run();
	}

}
