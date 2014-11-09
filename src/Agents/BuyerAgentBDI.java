package Agents;
import java.util.ArrayList;
import java.util.TimerTask;

import General.Demand;
import General.Proposal;
import General.Utilities;
import Products.Product;
import Services.NegociationService;
import jadex.bdiv3.annotation.Belief;
import jadex.bdiv3.annotation.Plan;
import jadex.micro.annotation.Binding;
import jadex.bdiv3.annotation.Trigger;
import jadex.commons.future.DefaultResultListener;
import jadex.commons.future.IFuture;
import jadex.micro.MicroAgent;
import jadex.micro.annotation.Agent;
import jadex.micro.annotation.AgentBody;
import jadex.micro.annotation.Argument;
import jadex.micro.annotation.Arguments;
import jadex.micro.annotation.Description;
import jadex.micro.annotation.RequiredService;
import jadex.micro.annotation.RequiredServices;
import jadex.bridge.service.RequiredServiceInfo;

@Agent
@Description("A buyer agent")
@RequiredServices(@RequiredService(name="NegociationService", type=NegociationService.class,
binding=@Binding(scope=RequiredServiceInfo.SCOPE_PLATFORM)))
public class BuyerAgentBDI extends MarketAgentBDI {

	
	@Belief
	int quantity=-1;
	
	@Belief
	int deadline;
	
	
	
	@Agent
	MicroAgent agent;
	


	DefaultResultListener<ArrayList<Proposal> > proposalListener=new DefaultResultListener<ArrayList<Proposal>>() {

		@Override
		public void resultAvailable(ArrayList<Proposal> arg0) {
			System.out.println("Recieved proposals");
			
		}
	};


	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}




	public void setDeadline(int deadline) {
		this.deadline = deadline;
	}




	public int getQuantity() {
		return quantity;
	}




	public int getDeadline() {
		return deadline;
	}




	@AgentBody
	public synchronized void agentBody() {
		periodicTask=new TimerTask() {
			
			@Override
			public void run() {
				
				if(quantity<0){
					
					if(Utilities.tossCoin(0.1f)){
						
						System.out.println("I have a need now");
						
						quantity=Utilities.randInt(1, 30);
						deadline=Utilities.randInt(1, 50);
						price=Utilities.randInt(1,100);
						
					}
					
				}
				else{
					
					deadline--;
					
				}
				
			}
		};

		super.agentBody();
	}

	
	@Plan(trigger=@Trigger(factchangeds="deadline"))
	synchronized void changePricesAccordingly(){
		
	
		setPrice((int)(price*1.1));
		
	}
	
	
	@Plan(trigger=@Trigger(factchangeds="price"))
	synchronized void askForPrices(){
		
		
		System.out.println("Asking For prices");
		final Demand d=new Demand(this);
		
		System.out.println("Demand set");
		
		IFuture<NegociationService> futNegociation=agent.getServiceContainer().getRequiredService("NegociationService");
		
		
		System.out.println("got services");
		
		futNegociation.addResultListener(new DefaultResultListener<NegociationService>(){

			@Override
			public void resultAvailable(NegociationService negociator) {
				System.out.println("Negociator fetched " +negociator);
				IFuture<ArrayList<Proposal> > proposalsFuture=negociator.startNegociation(d);
				System.out.println("Asked to start negociating");
				proposalsFuture.addResultListener(proposalListener);
				
			}
			
			
		});
		
		
		
	}
	



}
