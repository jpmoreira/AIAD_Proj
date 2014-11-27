package General;

import Agents.BuyerAgentBDI;
import Agents.SellerAgentBDI;


public class SealedProposal {

	
	Demand demand;
	Proposal proposal;
	
	
	public SealedProposal(Demand d,Proposal p){
		
		demand=d;
		proposal=p;
	}
	
	
	public synchronized void execute(){
		
		
		if(demand.issuer instanceof BuyerAgentBDI){
			((BuyerAgentBDI)demand.issuer).executeBid(demand);
		}
		else if(demand.issuer instanceof SellerAgentBDI){
			((SellerAgentBDI)demand.issuer).executeBid(demand);
		}
		
		
	}
}
