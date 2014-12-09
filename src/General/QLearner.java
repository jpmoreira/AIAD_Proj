package General;

public class QLearner {
	

	
	public static enum Action {
        Increase(1), Mantain(0), Decrease(-1);
        
        public final int value;//for testing purposes
        public final int index;
        private Action(int value) {
            this.value = value;
            this.index=this.value+1;
        }


    }
	
	public int currentPrice; //public for testing purposes
	int minPrice;
	int maxPrice;
	
	int t;
	
	public int[][] qMatrix;//public for testing purposes
	double discount_factor;
	double learning_rate;
	
	public QLearner(int initialPrice,int top, int min,double learningRate,double discountFactor, int tMax){
		
		maxPrice=top;
		minPrice=min;
		currentPrice=initialPrice;
		learning_rate=learningRate;
		discount_factor=discountFactor;
		
		t=tMax;
		
		qMatrix=new int[maxPrice-minPrice+1][3];//organizing this way may reduce number of page-faults when running algorithm since we should iterate along a line more often with this disposition
		
		
	}

	
	public boolean iterate(double reward,Action ac){
		
		
		if(ac==Action.Increase && currentPrice>=maxPrice)return false;
		else if(ac==Action.Decrease && currentPrice<=minPrice)return false;
		
		int S_t0=currentPrice-minPrice;//index of state is the difference to the minPrice (so states are: minPrice,minPrice+1,...,minPrice+n,...,topPrice)
		int S_t1=S_t0+ac.value;//index for the state we are going to
		
		int Q_t0=qMatrix[S_t0][ac.index];//hold value in temporary variable for readability sake, lets hope compiler is smart enough to discover this and optimize it
		
		int max_a_Q_t1=0;
		
		for(int Q_t1 : qMatrix[S_t1]){//find best value for a action in the future
			if(Q_t1>max_a_Q_t1)max_a_Q_t1=Q_t1;
		}
		
		
		Q_t0+=(int) (learning_rate*(reward+discount_factor*max_a_Q_t1-Q_t0));
		
		qMatrix[S_t0][ac.index]=Q_t0;//save the value (lets hope compiler optimizes this by not using the temporary value)
		
		currentPrice+=ac.value;
		
		
		
		
		return true;
		
		
		
		
		
	}
	
	/**
	 * 
	 * 
	 * @return an array of three positions. At the position Action.Increase.index is placed the Q value for the Increase action, and so on for the other two.
	 */
	
	private int[] QforActions(){
	
		int [] array=new int[3];
		
		array[Action.Increase.index]=qMatrix[currentPrice-minPrice][Action.Increase.index];
		array[Action.Mantain.index]=qMatrix[currentPrice-minPrice][Action.Mantain.index];
		array[Action.Decrease.index]=qMatrix[currentPrice-minPrice][Action.Decrease.index];
		
		return array;
		
		
		
	}
	
	public Action determenisticAction(){
		
		
		int[] qs=QforActions();
		
		//indexes of the qs of the various actions
		int inc_i=Action.Increase.index;
		int dec_i=Action.Decrease.index;
		int man_i=Action.Mantain.index;
		
		
		if(qs[inc_i]>=qs[man_i] && qs[inc_i]>=qs[dec_i])return Action.Increase;
		else if(qs[man_i]>=qs[inc_i] && qs[man_i]>=qs[dec_i])return Action.Mantain;
		else return Action.Decrease;
		
	}
	
	
	public Action action(){
		
		
		return action(-1);//use random "truly random" value.
	}
	
	/**
	 * 
	 * 
	 * A method that tells which action to take based on the current fitness matrix
	 * @param pseudoRandomValue a value to be passed that will override the random number used to compute which transition to take. If value passed is negative then a truly random number is used
	 * Used for testing purposes 
	 * @return the action to take.
	 */
	
	public Action action(double pseudoRandomValue){
		
		
		
		
		
		
		int[] qs_for_actions=QforActions();
		
		int increase=qs_for_actions[Action.Increase.index];
		int mantain=qs_for_actions[Action.Mantain.index];
		int decrease=qs_for_actions[Action.Decrease.index];
		
		
		
		//probability of using action increase
		double p_increase= Math.pow(Math.E, increase/(float)t);
		double p_mantain= Math.pow(Math.E, mantain/(float)t);
		double p_decrease= Math.pow(Math.E, decrease/(float)t);
		
		double total_p=p_increase+p_mantain+p_decrease;//overall prob
		
		//normalize
		p_increase/=total_p;
		p_mantain/=total_p;
		p_decrease/=total_p;
		
		double randomNumber=Math.random();
		if(pseudoRandomValue>=0.0)randomNumber=pseudoRandomValue;//if bigger than zero then override
		
		
		if(t>=1)t--;//change t
		
		if(randomNumber<=p_increase)return Action.Increase;
		if(randomNumber<=p_increase+p_mantain)return Action.Mantain;
		return Action.Decrease;
		
		
	}

}
