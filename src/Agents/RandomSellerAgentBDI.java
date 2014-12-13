package Agents;

import jadex.micro.annotation.Agent;
import jadex.micro.annotation.AgentKilled;

@Agent
public class RandomSellerAgentBDI extends SellerAgentBDI{
	
	
	
	int[] hits=new int[25];
	
	
	//__________________ Plans __________________
	
	@Override
	synchronized void updatePricingInfo() {
		
		
		super.updatePricingInfo();
		
		double random=Math.random();
		
		price=(int)(random*(20*basePrice-2*basePrice)+2*basePrice);
		
		hits[price]+=1;

		
		
	}
	
	
	@AgentKilled
	public synchronized void agentKill() {
		
		//super.agentKill();
		
		for(int i=0;i<hits.length;i++){
			
			System.out.println("Hits["+i+"]="+hits[i]);
		}
		
	}

}
