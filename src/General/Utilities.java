package General;

import java.util.Random;


public class Utilities {
	
	public static int randInt(int min, int max) {

	    // NOTE: Usually this should be a field rather than a method
	    // variable so that it is not re-seeded every call.
	    Random rand = new Random();

	    // nextInt is normally exclusive of the top value,
	    // so add 1 to make it inclusive
	    int randomNum = rand.nextInt((max - min) + 1) + min;

	    return randomNum;
	}
	
	
	public static boolean tossCoin(float probability){
		
		
		int rand=Utilities.randInt(0, 100);
		int boarder=(int) (probability*100);
		if(rand<=boarder)return true;
		return false;
		
	}

}
