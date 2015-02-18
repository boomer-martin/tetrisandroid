package edu.angelo.finalmartin;

import com.badlogic.androidgames.framework.Screen;
import com.badlogic.androidgames.framework.impl.AndroidGame;

public class Tetris extends AndroidGame {
    public Screen getStartScreen() {
        return new LoadingScreen(this); 
    }
} 
