package Services;

import jadex.bridge.service.annotation.Service;
import jadex.bridge.service.annotation.ServiceComponent;
import Agents.SellerAgentBDI;
import General.Demand;
import General.Proposal;

@Service
public class SimpleSellingService implements SellingService{
	
	
	@ServiceComponent
	SellerAgentBDI agent;
	
	public synchronized Proposal proposalForDemand(Demand d) {
		
		if(d==null || agent==null)return null;
		
		return agent.proposalForDemand(d);
	}

}
