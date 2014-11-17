package General;


public class SealedProposal {

	
	Demand demand;
	Proposal proposal;
	
	
	public SealedProposal(Demand d,Proposal p){
		
		demand=d;
		proposal=p;
	}
	
	
	public synchronized void execute(){
		
		demand.issuer.executeBid(demand);
		proposal.issuer.executeBid(proposal);
		
	}
}
