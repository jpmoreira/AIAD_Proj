package General;

import Agents.SellerAgentBDI;
import Products.Product;

public class Proposal extends Bid {

	Proposal(SellerAgentBDI is, Product pr, int pric, int qt, int time) {
		super(is, pr, pric, qt, time);
	}
	
	
	public Proposal(SellerAgentBDI seller){
		
		super(seller,seller.getProduct(),seller.getPrice(),seller.getNrProducts(),0);
		
	}

}
