package Agents;
import jadex.bdiv3.annotation.Belief;
import jadex.bdiv3.annotation.Plan;
import jadex.bdiv3.annotation.Trigger;
import jadex.bridge.service.RequiredServiceInfo;
import jadex.commons.future.DefaultResultListener;
import jadex.commons.future.Future;
import jadex.commons.future.IFuture;
import jadex.commons.future.IResultListener;
import jadex.micro.MicroAgent;
import jadex.micro.annotation.Agent;
import jadex.micro.annotation.AgentBody;
import jadex.micro.annotation.Binding;
import jadex.micro.annotation.Description;
import jadex.micro.annotation.RequiredService;
import jadex.micro.annotation.RequiredServices;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.TimerTask;

import General.Demand;
import General.Proposal;
import General.Utilities;
import Services.NegociationService;
import Services.SellingService;

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
	synchronized void changePricesAccordingly(){
		
	
		setPrice((int)(price*1.1));
		
	}
	synchronized void askForPrices(){
		
		
		System.out.println("Asking For prices");
		final Demand d=new Demand(this);
		
		System.out.println("Demand set");
		
		
		Future<ArrayList<Proposal> >propsFuture=askForBids(d);
		
		propsFuture.addResultListener(new DefaultResultListener<ArrayList<Proposal>>() {

			@Override
			public void resultAvailable(ArrayList<Proposal> arg0) {
				
			}
		});
		
		
		
	}
	synchronized public Future <ArrayList <Proposal> > askForBids(final Demand demand) {
		
		System.out.println("Started negociation");
		
		final Future <ArrayList<Proposal> > fut=new  Future<ArrayList <Proposal> >(); 
		
		IFuture<Collection<SellingService> > sellers=agent.getServiceContainer().getRequiredServices("SellingService");
		
		
		sellers.addResultListener(new IResultListener<Collection<SellingService>>() {
			
			@Override
			public void resultAvailable(Collection<SellingService> sellers) {
				System.out.println("Found "+sellers.size()+" sellers");
				
				ArrayList<Proposal> proposals=new ArrayList<Proposal>();
				
				Iterator<SellingService> it=sellers.iterator();
				
				while(it.hasNext()){
					
					Proposal p=it.next().proposalForDemand(demand);
					if(p==null)continue;
					proposals.add(p);
					
				}
				
				fut.setResult(proposals);
				
				
			}
			
			@Override
			public void exceptionOccurred(Exception arg0) {
				
				System.out.println("No service provider found");
				
				fut.setResult(new ArrayList <Proposal>());
			}
		});
		
		return fut;

	}
	synchronized void handleProposalsWithAdequatePlan(ArrayList <Proposal> proposals,Demand demand){
		
		
	}
	
	@Plan
	synchronized void chooseCheapestSuitedProposalPlan(ArrayList <Proposal> proposals,Demand demand){
		
		Proposal best=null;
		
		for(int i=0;i<proposals.size();i++){
			Proposal prop=proposals.get(i);
			if(!prop.getProduct().equals(demand.getProduct()))return;
			if(prop.getPrice()>demand.getPrice())return;
			if(prop.getQuantity()<demand.getQuantity())return;
			if(best==null|| best.getPrice()>prop.getPrice())best=prop;
		}
		
		
		if(best!=null){
			
			//TODO continue here
		}
	}
}
