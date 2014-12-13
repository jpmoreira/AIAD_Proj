package General;

public class QLearner {


	
	public int currentPrice; //public for testing purposes
	int minPrice;
	int maxPrice;
	
	public int t;
	
	public int[] qMatrix;//public for testing purposes
	double discount_factor;
	double learning_rate;
	
	public QLearner(int initialPrice,int top, int min,double learningRate,double discountFactor, int tMax){
		
		maxPrice=top;
		minPrice=min;
		currentPrice=initialPrice;
		learning_rate=learningRate;
		discount_factor=discountFactor;
		
		//System.out.println("Initial price is "+currentPrice+ " minPrice="+minPrice);
		
		t=tMax;
		
		qMatrix=new int[maxPrice-minPrice+1];//organizing this way may reduce number of page-faults when running algorithm since we should iterate along a line more often with this disposition
		
		
	}

	
	public boolean iterate(double reward,int action){
		
		
		int priceAfterAction=action+currentPrice;
		
		if(priceAfterAction >maxPrice || priceAfterAction<minPrice)return false;
		
		
		//int S_t0=currentPrice-minPrice;//index of state is the difference to the minPrice (so states are: minPrice,minPrice+1,...,minPrice+n,...,topPrice)
		int S_t1=priceAfterAction-minPrice;
		int Q_t1=qMatrix[S_t1];
		
		int max_a_Q_t1=Integer.MIN_VALUE;
		
		for(int q : qMatrix){//find best value for a action in the future
			if(q>max_a_Q_t1)max_a_Q_t1=q;
		}
		
		
		Q_t1+=(int) (learning_rate*(reward+discount_factor*max_a_Q_t1-Q_t1));
		
		
		//System.out.println("curPrice="+currentPrice+" action="+action);
		//System.out.println(" Set Q["+S_t1+"]="+Q_t1);
		
		qMatrix[S_t1]=Q_t1;//save the value (lets hope compiler optimizes this by not using the temporary value)
		
		currentPrice=priceAfterAction;
		
		
		if(t!=1)t--;
		
		return true;
		
		
		
		
		
	}
	
	public int determenisticAction(){
		
		
		int destState=-1;
		int destValue=Integer.MIN_VALUE;
		
		
		for(int i=0;i<qMatrix.length;i++){
			
			int val=qMatrix[i];
			if(val>destValue){
				destState=i;
				destValue=val;
			}
		}
		
		
		return (destState+minPrice)-currentPrice;//return how much to increase
		
	}
	
	
	public int action(){
		
		
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
	
	public int action(double pseudoRandomValue){
		
		
		double sum=0;
		double previous=0;
		
		double[] intervals=new double[qMatrix.length];
		
		for(int i=0;i<intervals.length;i++){
			
			double prob=Math.pow(Math.E, qMatrix[i]/(float)t);
			intervals[i]=prob+previous;
			previous=intervals[i];
			sum+=prob;
		}
		
		
		
		double rand=Math.random();
		if(pseudoRandomValue>=0)rand=pseudoRandomValue;
		
		rand*=sum;
		
		double prev=0;
		int destState=0;
		
		for(int i=0;i<intervals.length;i++){
			
			if(rand>=prev && rand<=intervals[i]){
				destState=i;
				break;
			}
			prev=intervals[i];
		}
		
		return (destState+minPrice)-currentPrice;
		

		
		
	}

}
