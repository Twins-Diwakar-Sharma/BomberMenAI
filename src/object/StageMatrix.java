package object;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Arrays;

public class StageMatrix {

	public static int size = 20;
	
    public static int[][] data = new int[size][size];
    public static double pNotEmpty = 0.2;
    public static double pBreakGivenNotEmpty = 0.4;
    
    
    public StageMatrix() {
    	data = new int[size][size];
    	System.out.println("someBulShit");
    }
    
    
    
   
    public static void reset() {
    	for(int i=0; i<StageMatrix.size; i++) {
    		for(int j=0; j<StageMatrix.size; j++) {
    			data[i][j] = 0;
    		}
    	}
    }
    
  
    
    
    public static void loadEvenMoreHardCoded() {
    	
    	int[][] sata = {
    			{0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
    			{4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4},
    			{4, 0, 0, 0, 4, 0, 0, 4, 0, 4, 4, 0, 0, 0, 0, 0, 0, 0, 0, 4},
    			{4, 0, 1, 0, 3, 0, 0, 4, 0, 4, 4, 0, 0, 0, 0, 4, 3, 4, 0, 4},
    			{4, 3, 4, 4, 4, 0, 0, 4, 0, 4, 4, 0, 0, 0, 0, 4, 0, 4, 0, 4},
    			{4, 0, 0, 0, 0, 0, 0, 0, 0, 4, 4, 0, 0, 0, 0, 4, 3, 4, 0, 4},
    			{4, 0, 0, 0, 0, 0, 0, 3, 0, 3, 3, 0, 0, 0, 0, 4, 4, 4, 0, 4},
    			{4, 0, 0, 0, 0, 0, 0, 0, 0, 3, 3, 0, 0, 0, 0, 0, 0, 0, 0, 4},
    			{4, 0, 0, 0, 0, 0, 0, 0, 0, 3, 3, 0, 0, 0, 0, 0, 0, 3, 0, 4},
    			{4, 0, 0, 0, 0, 0, 0, 0, 0, 4, 4, 0, 0, 0, 0, 0, 0, 0, 0, 4},
    			{4, 0, 0, 4, 4, 4, 0, 0, 0, 4, 4, 0, 0, 4, 4, 4, 4, 0, 0, 4},
    			{4, 0, 0, 4, 3, 4, 0, 0, 0, 4, 4, 0, 0, 4, 0, 0, 0, 0, 0, 4},
    			{4, 0, 0, 4, 0, 4, 0, 0, 0, 4, 4, 0, 0, 0, 0, 0, 0, 0, 0, 4},
    			{4, 0, 0, 4, 3, 4, 0, 0, 0, 4, 4, 0, 0, 0, 0, 0, 0, 0, 0, 4},
    			{4, 0, 0, 0, 0, 0, 0, 0, 0, 3, 3, 0, 0, 0, 0, 0, 0, 0, 0, 4},
    			{4, 0, 0, 0, 0, 0, 0, 0, 0, 3, 3, 0, 0, 4, 0, 0, 4, 3, 4, 4},
    			{4, 0, 0, 0, 3, 0, 0, 0, 0, 4, 4, 0, 0, 0, 0, 0, 4, 0, 0, 4},
    			{4, 0, 0, 0, 0, 0, 0, 0, 0, 4, 4, 0, 3, 0, 0, 0, 4, 0, 0, 4},
    			{4, 0, 4, 0, 0, 0, 0, 0, 0, 4, 4, 0, 0, 0, 0, 0, 3, 0, 2, 4},
    			{4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4},
	};
    	data = sata;
    }
    

    
    public static void loadProbabilisticLevel() {
      int player1Row = (int)(Math.random()*4 + 3 );
      int player2Row = (int)(Math.random()*4 + 3 + 7 + 4);
      int player1Col = (int)(Math.random()*16 + 2);
      int player2Col = (int)(Math.random()*16 + 2);
      
      
  	
  	for(int i=0; i<size; i++) {
  		data[1][i] = 4;
  	}
  	for(int i=0; i<size; i++) {
  		data[size-1][i] = 4;
  	}
  	for(int i=2; i<size-1; i++) {
  		data[i][0] = 4;
  	}
  	for(int i=2; i<size-1; i++) {
  		data[i][size-1] = 4;
  	}
  	
	for(int i=2; i<StageMatrix.size - 1; i++) {
		for(int j=1; j<StageMatrix.size -1; j++) {
			data[i][j] = 0;
			double random = Math.random();
			
			if(random < pNotEmpty) {  // probability of not empty
				random = Math.random(); // conditional probability
				if(random < pBreakGivenNotEmpty) { // probability of breakable given not empty P( B | !E )
					data[i][j] = 3;
				}else {  // probability of unbreakable given not empty P( U | !E )
					data[i][j] = 4;
				}
			}
			
			
		}		
	}
	
	//check around players
	  int fourCtwo = (int)(Math.random()*6);
	  removeTwoObstacles(fourCtwo, player1Row, player1Col);
	  fourCtwo = (int)(Math.random()*6);
	  removeTwoObstacles(fourCtwo, player2Row, player1Col);
	
	//assing players
	  data[player1Row][player1Col] = 1;
	  data[player2Row][player2Col] = 2;
}
    
    
    private static void removeTwoObstacles(int probability, int aroundRow, int aroundCol) {
    switch(probability) {
  	  case 0 : // up right
  		  data[aroundRow-1][aroundCol] = 0; // up
  		  data[aroundRow][aroundCol+1] = 0; // right
  		  break;
  	  case 1: // right down
  		  data[aroundRow][aroundCol+1] = 0; // right
  		  data[aroundRow+1][aroundCol] = 0; //down
  		  break;
  	  case 2: // down left
  		  data[aroundRow+1][aroundCol] = 0; // down
  		  data[aroundRow][aroundCol-1] = 0; // left
  		  break;
  	  case 3: // left up
  		  data[aroundRow][aroundCol-1] = 0; // left
  		  data[aroundRow-1][aroundCol] = 0; // up
  		  break;
  	  case 4: // left right
  		  data[aroundRow][aroundCol-1] = 0; // left
  		  data[aroundRow][aroundCol+1] = 0; // right
  		  break;
  	  case 5: // up down
  		  data[aroundRow-1][aroundCol] = 0; // up
  		  data[aroundRow+1][aroundCol] = 0; // down
  		  break;
  	  }
    
    }
    
    
    
      
}
    

