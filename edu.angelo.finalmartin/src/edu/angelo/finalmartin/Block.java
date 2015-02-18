package edu.angelo.finalmartin;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Block {
	/**
	 * Here I created as many static variables to improve performance
	 * If you don't need to access an object's fields, make your method static. 
	 * Invocations will be about 15%-20% faster. 
	 * It's also good practice, because you can tell from the method signature that 
	 * calling the method can't alter the object's state.
	 */
    public static final int LEFT = -1;
    public static final int RIGHT = 1;
    public static final int ADVANCE = 1;
    public static final int ROTATE = 0;
    public static final int SHAPE_SIZE = 7;
    public static final int LINE 	= 0;
    public static final int	SQUARE 	= 1;
    public static final int	LEFTL 	= 2;
    public static final int	RIGHTL	= 3;
    public static final int	TBLOCK 	= 4;
    public static final int	STAIRL 	= 5;
    public static final int	STAIRR 	= 6;
    public static final int	LENGTH 	= 4;
    public List<BlockPart> parts = new ArrayList<BlockPart>();
    public int direction;
    public int shape;
    public boolean lock;
    public boolean wall;
    public World currentGrid;
    Random randomBlock = new Random();
    
    /**
     * constructor to build a random tetris shape
     */
    public Block(World w) {
    	currentGrid = w;
    	shape = randomBlock.nextInt(SHAPE_SIZE);
    	if(shape == LINE)
    	{
    		parts.add(new BlockPart(5, 0));
    		parts.add(new BlockPart(6, 0));
    		parts.add(new BlockPart(4, 0));
    		parts.add(new BlockPart(3, 0));
    	}
    	else if(shape == SQUARE)
    	{
    		parts.add(new BlockPart(5, 0));
    		parts.add(new BlockPart(4, 0));
    		parts.add(new BlockPart(5, 1));
    		parts.add(new BlockPart(4, 1));
    	}
    	else if(shape == LEFTL)
    	{
    		parts.add(new BlockPart(5, 1));
    		parts.add(new BlockPart(6, 0));
    		parts.add(new BlockPart(4, 1));
    		parts.add(new BlockPart(6, 1));
    	}
    	else if(shape == RIGHTL)
    	{
    		parts.add(new BlockPart(5, 1));
    		parts.add(new BlockPart(4, 0));
    		parts.add(new BlockPart(4, 1));
    		parts.add(new BlockPart(6, 1));
    	}
    	else if(shape == TBLOCK)
    	{
    		parts.add(new BlockPart(5, 1));
    		parts.add(new BlockPart(6, 1));
    		parts.add(new BlockPart(5, 0));
    		parts.add(new BlockPart(4, 1));
    	}
    	else if(shape == STAIRR)
    	{
    		parts.add(new BlockPart(5, 0));
    		parts.add(new BlockPart(4, 0));
    		parts.add(new BlockPart(5, 1));
    		parts.add(new BlockPart(6, 1));
    	}
    	else
    	{
    		parts.add(new BlockPart(5, 0));
    		parts.add(new BlockPart(6, 0));
    		parts.add(new BlockPart(5, 1));
    		parts.add(new BlockPart(4, 1));
    	}
    	
    }
    /**
     * When invoked, will rotate a block shape by 90 degrees
     */
    public void rotateBlock(){
    	/*matrix rotation
		  [ 0 -1]
		  [ 1  0]
		  Avoid Using Floating-Point
			As a rule of thumb, floating-point is about 2x slower than integer on Android-powered devices.
			In speed terms, there's no difference between float and double on the more modern hardware. 
			Space-wise, double is 2x larger. As with desktop machines, assuming space isn't an issue, 
			you should prefer double to float.
			Also, even for integers, some processors have hardware multiply but lack hardware divide. 
			In such cases, integer division and modulus operations are performed in software—something to think about 
			if you're designing a hash table or doing lots of math.
		  */
		int originX = parts.get(0).x;
		int originY = parts.get(0).y;
		int X1, Y1, adjust = 0;
		int[] tempX = new int[LENGTH];
		int[] tempY = new int[LENGTH];
			//set the rotation
    	for(int i = 0; i < LENGTH; ++i){
    		X1 = (originY - parts.get(i).y) *  1;
    		Y1 = (originX - parts.get(i).x) * -1;
    		
    		tempX[i] =  X1 + originX;
    		tempY[i] =  Y1 + originY;
  		
    		//check to see if it is bounded
    		while(tempX[i] + adjust < 0)
    			adjust += 1;
    		while(tempX[i] + adjust > 9)
    			adjust -= 1;
    	}
    	
        for(int j = 0; j < LENGTH; ++j){
        	try{
        		if(currentGrid.fields[tempX[j]][tempY[j]])
        			return;
        	}catch(Exception ex){}
        } 
    	
    	for(int i = 0; i < LENGTH; ++i){
    		parts.get(i).x =  tempX[i] + adjust;
    		parts.get(i).y =  tempY[i];
    	}
    }

    /**
     * When invoked, will move a block along the X axis by one unit
     */
    
    public void moveRight() {
        direction = 0;
        wallTest();
        //other blocks
        for(int i = 0; i < LENGTH; ++i){
        	try{
        		if(currentGrid.fields[parts.get(i).x + 1][parts.get(i).y])
        			return;
        	}catch(Exception ex){}
        }
        //the wall
        for(int i = 0; i < LENGTH; ++i){
        	// debug the wall problem
            if(wall && parts.get(i).x < 1)
            	for(int j = 0; j < LENGTH; ++j)	 
            		parts.get(j).x += RIGHT; 
            else if(!wall)
            	parts.get(i).x += RIGHT;
        }
        direction = 0;
        wall = false;
    }
   
    /**
     * When invoked, will move a block along the X axis by one unit
     */    
    public void moveLeft(){
        wallTest();
        for(int i = 0; i < LENGTH; ++i){
        	try{
        		if(currentGrid.fields[parts.get(i).x - 1][parts.get(i).y])
        			return;
        	}catch(Exception ex){}
        }
        for(int i = 0; i < LENGTH; ++i){
        	// debug the wall problem
        	if(wall && parts.get(i).x > 8)            
            	for(int j = 0; j < LENGTH; ++j)	
           		 	parts.get(j).x += LEFT;	
            else if(!wall)
            	parts.get(i).x += LEFT;
        }
        wall = false;
    }

    /**
     * will test to see if any blocks are along the edge and set the wall flag to true
     */
    public void wallTest(){
        for(int i = 0; i < LENGTH; ++i){
            if(parts.get(i).x < 1)
            	wall = true;
            else if(parts.get(i).x > 8)
            	wall = true;
        } 
    }
    
    public void gridTest(){
    	
    }
    
    /**
     * moves the unit down by one every tick
     */
    public void advance() {
        for(int i = 0; i < LENGTH; ++i)
            parts.get(i).y += ADVANCE;
    }     
}

