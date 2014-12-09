package General;

import Agents.BuyerAgentBDI;
//import Products.Product;

public class Demand extends Bid {

	public Demand(BuyerAgentBDI is, String pr, int pric, int qt, int time) {
		super(is, pr, pric, qt, time);
	}
	
	
	public Demand(BuyerAgentBDI buyer){
		
		super(buyer,buyer.product,buyer.price,buyer.quantity,buyer.deadline);
		
	}

}
