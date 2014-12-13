package Agents;

import jadex.micro.annotation.Agent;


@Agent
public class UpNDownSellerAgentBDI extends SellerAgentBDI {

	
	@Override
	synchronized void updatePricingInfo() {
		
		boolean selled=false;
		if(earned>0)selled=true;
		int previousPrice=price;
		super.updatePricingInfo();
		
		if(selled)price=previousPrice+1;
		else if(!selled)price=previousPrice-1;
		
		if(price<2*basePrice)price=2*basePrice;
		else if(price>10*basePrice)price=10*basePrice;
		
	}
}
