package edu.angelo.finalmartin;

import java.util.Random;

public class World {
    static final int WORLD_WIDTH = 10;
    static final int WORLD_HEIGHT = 13;
    static final int SCORE_INCREMENT = 10;
    static final int LENGTH = 4;
    static final int LINE = 10;
    static final float TICK_INITIAL = 0.75f;
    static final float TICK_DECREMENT = 0.02f;

    public Block brick;
    public LockBlock lockedBricks;
    public boolean gameOver = false;
    public int score = 0;

    boolean fields[][] = new boolean[WORLD_WIDTH][WORLD_HEIGHT];
    Random random = new Random();
    float tickTime = 0;
    float tick = TICK_INITIAL;

    public World() {
        lockedBricks = new LockBlock();
        brick = new Block(this);
    }
    
    public void lockInPlace(){
        for (int i = 0; i < LENGTH; i++) {
            BlockPart part = brick.parts.get(i);
            fields[part.x][part.y] = true;
            lockedBricks.lockParts.add(part);
        }
    }
    
    public void lockTest(){
        for(int i = 0; i < LENGTH; ++i){
        	if(brick.parts.get(i).y == 12)
        		brick.lock = true;
        	else if(fields[brick.parts.get(i).x][brick.parts.get(i).y + 1] == true)
        		brick.lock = true;
        } 
    }
    
    public boolean gameOverTest(){
    	for(int i = 0; i < LENGTH; ++i)
    		if(fields[brick.parts.get(i).x][brick.parts.get(i).y + 1] == true && brick.parts.get(i).y == 0)
    			return true;
    	return false;
    }
    
    public void lineTest(){
    	int test = 0;
    	//test to see if you have a full row
    	for(int j = 0; j < WORLD_HEIGHT; ++j, test = 0){
    		for(int i = 0; i < WORLD_WIDTH; ++i)
    			if(fields[i][j])
    				++test;
    		//delete line the line
    		if(test == LINE){
    			score += 10;
    			deleteLine(j);
    			shiftBoard(j - 1);
    		}
    	}
    }
    
    public void deleteLine(int height){
    	for(int width = 0; width < WORLD_WIDTH; ++width){
    		for(int k = 0; k < lockedBricks.lockParts.size(); ++k){
    			if(lockedBricks.lockParts.get(k).y == height){
    				fields[lockedBricks.lockParts.get(k).x][lockedBricks.lockParts.get(k).y] = false;
    				lockedBricks.lockParts.remove(k);   				
    			}	
    		}
    	}
    }
    
    public void shiftBoard(int i){
    	for(int height = i; height > 0; --height){
    		for(int width = 0; width < WORLD_WIDTH; ++width){
    			for(int k = 0; k < lockedBricks.lockParts.size(); ++k){
    				if(lockedBricks.lockParts.get(k).y == height && lockedBricks.lockParts.get(k).x == width){
    					BlockPart newBlock = new BlockPart(width, height + 1);
    					fields[lockedBricks.lockParts.get(k).x][lockedBricks.lockParts.get(k).y] = false;
    					lockedBricks.lockParts.remove(k);
    					fields[width][height + 1] = true;
    					lockedBricks.lockParts.add(newBlock);
    				}
    			}
    		}
    	}
    }
    
    public void update(float deltaTime) {
        if (gameOver)
            return;

        tickTime += deltaTime;

        while (tickTime > tick) {
            tickTime -= tick;
            //occupiedSpaces();
            brick.advance();
            lockTest(); 
            if(brick.lock) {
            	lockInPlace();
            	brick = new Block(this);
            	gameOver = gameOverTest();
            }
            lineTest();
        }
    }
}

