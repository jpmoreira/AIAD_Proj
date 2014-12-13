package Agents;

import jadex.micro.annotation.Agent;

@Agent
public class OptimalExp2SellerAgentBDI extends SellerAgentBDI {
	
	
	@Override
	synchronized void updatePricingInfo() {
		
		super.updatePricingInfo();
		price=20;
	}

}
