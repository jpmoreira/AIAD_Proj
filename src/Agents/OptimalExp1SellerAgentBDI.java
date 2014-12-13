package Agents;

import jadex.micro.annotation.Agent;


@Agent
public class OptimalExp1SellerAgentBDI extends SellerAgentBDI{

	
	
	
	@Override
	synchronized void updatePricingInfo() {
		
		super.updatePricingInfo();
		price=10;
	}
	
}
