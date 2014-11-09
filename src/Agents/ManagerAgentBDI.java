package Agents;
import java.util.Collection;

import sun.management.resources.agent;
import jadex.bridge.service.RequiredServiceInfo;
import jadex.commons.future.*;
import Services.SellingService;
import Services.ContractNetNegociationService;
import Services.NegociationService;
import jadex.commons.future.IFuture;
import jadex.micro.MicroAgent;
import jadex.micro.annotation.Agent;
import jadex.micro.annotation.AgentBody;
import jadex.micro.annotation.Binding;
import jadex.micro.annotation.Description;
import jadex.micro.annotation.Implementation;
import jadex.micro.annotation.ProvidedService;
import jadex.micro.annotation.ProvidedServices;
import jadex.micro.annotation.RequiredService;
import jadex.micro.annotation.RequiredServices;


@Agent
@Description("A manager agent")
@ProvidedServices(@ProvidedService(type=NegociationService.class, implementation=@Implementation(ContractNetNegociationService.class)))
@RequiredServices(@RequiredService(name="SellingServices",type=SellingService.class,binding=@Binding(scope=RequiredServiceInfo.SCOPE_PLATFORM)))
public class ManagerAgentBDI extends MarketAgentBDI{

	
	@Agent
	MicroAgent agent;
	


	@AgentBody
	public synchronized void agentBody() {
		System.out.println("Executing Manager");
	}
	
	
	
	public IFuture<Collection<SellingService> > getSellers(){
		
		
		System.out.println("Returning sellers");
		
		
		return agent.getServiceContainer().getRequiredServices("SellingService");
		

		
		 
		
	}
	

}
