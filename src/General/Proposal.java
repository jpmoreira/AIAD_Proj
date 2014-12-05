package General;

import Agents.SellerAgentBDI;
//import Products.Product;

public class Proposal extends Bid {

	Proposal(SellerAgentBDI is, String pr, int pric, int qt, int time) {
		super(is, pr, pric, qt, time);
	}
	
	
	public Proposal(SellerAgentBDI seller,int quantity){
		
		super(seller,seller.getProduct(),seller.getPrice(),quantity,0);
		
	}

}
