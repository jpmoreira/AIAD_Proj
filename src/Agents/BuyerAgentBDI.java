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
import General.Bid;
import General.Demand;
import General.Proposal;
import General.SealedProposal;
import General.Utilities;
import Products.Banana;
import Products.Product;
import Services.SellingService;

@Agent
@Description("A buyer agent")
@RequiredServices(@RequiredService(name="SellingService", type=SellingService.class,
binding=@Binding(scope=RequiredServiceInfo.SCOPE_PLATFORM),multiple=true))
public class BuyerAgentBDI  {

	
	@Belief
	public int quantity=-1;
	
	@Belief
	public int deadline;
	
	
	
	@Agent
	MicroAgent agent;
	
	public Product product;
	
	@Belief
	public int price;
	
	
	@Belief(updaterate=1000)
	public long time=System.currentTimeMillis();
	
	
	
	synchronized public void setProduct(Product product) {
		this.product = product;
	}

	public int getPrice(){
		return price;
	}


	synchronized public Product getProduct() {
		return product;
	}

	
	


	DefaultResultListener<ArrayList<Proposal> > proposalListener=new DefaultResultListener<ArrayList<Proposal>>() {

		@Override
		public void resultAvailable(ArrayList<Proposal> arg0) {
			System.out.println("Recieved proposals");
			
		}
	};

	synchronized public int getQuantity() {
		return quantity;
	}




	synchronized public int getDeadline() {
		return deadline;
	}
	
	@Plan(trigger=@Trigger(factchangeds="time"))
	synchronized void updateStuff(){

		
		if(quantity<0){
			
			if(Utilities.tossCoin(0.1f)){
				
				
				
				quantity=Utilities.randInt(1, 30);
				deadline=Utilities.randInt(1, 50);
				price=Utilities.randInt(1,100);
				System.out.println("I have a need now of "+quantity+" "+product+" at "+price);
				
			}
			
		}
		else{
			
			deadline--;
			
		}
		
	
	}
	
	
	@AgentBody
	synchronized public void agentBody() {
		product=new Banana();
		//super.agentBody();

	}
	synchronized void changePricesAccordingly(){
		
	
		price*=1.1;
		
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
		
		System.out.println("Started negociation for price ="+price+" and quantity ="+quantity);
		
		final Future <ArrayList<Proposal> > fut=new  Future<ArrayList <Proposal> >(); 
		
		IFuture<Collection<SellingService> > sellers=agent.getServiceContainer().getRequiredServices("SellingService");
		
		
		
		fut.addResultListener(new DefaultResultListener<ArrayList<Proposal>>() {

			@Override
			public void resultAvailable(ArrayList<Proposal> proposals) {
				
				
				handleProposalsWithAdequatePlan(proposals, demand);
			}
			
			
		});
		

		
		sellers.addResultListener(new IResultListener<Collection<SellingService>>() {
			
			@Override
			public void resultAvailable(Collection<SellingService> sellers) {
				System.out.println("Found "+sellers.size()+" sellers");
				
				
				ArrayList<Proposal> proposals=new ArrayList<Proposal>();
				
				Iterator<SellingService> it=sellers.iterator();
				
				while(it.hasNext()){
					SellingService s=it.next();
					System.out.println("Found "+s);	
					Proposal p=s.proposalForDemand(demand);
					//System.out.println("Got proposal at price "+p.getPrice());
					if(p==null)continue;
					proposals.add(p);
					
				}
				
				fut.setResult(proposals);
				
				
			}
			
			@Override
			public void exceptionOccurred(Exception arg0) {
				
				System.out.println("No service provider found exception: "+arg0);
				
				fut.setResult(new ArrayList <Proposal>());
			}
		});
		
		
		return fut;

	}
	synchronized void handleProposalsWithAdequatePlan(ArrayList <Proposal> proposals,Demand demand){
		
		chooseCheapestSuitedProposalPlan(proposals, demand);
	}
	
	@Plan
	synchronized void chooseCheapestSuitedProposalPlan(ArrayList <Proposal> proposals,Demand demand){
		
		
		System.out.println("Choosing cheapest one");
		Proposal best=null;
		
		for(int i=0;i<proposals.size();i++){
			Proposal prop=proposals.get(i);
			if(!prop.getProduct().equals(demand.getProduct()))return;
			if(prop.getPrice()>demand.getPrice())return;
			if(prop.getQuantity()<demand.getQuantity())return;
			if(best==null|| best.getPrice()>prop.getPrice())best=prop;
		}
		
		
		if(best!=null){
			
			System.out.println("Needed "+quantity);
			SealedProposal s=new SealedProposal(demand, best);
			s.execute();
			System.out.println("Need "+quantity);
		}
	}



	@Plan(trigger=@Trigger(factchangeds="deadline"))
	synchronized void tryToSatisfyNecessities(){
		
		askForPrices();
		
	}

	synchronized public void executeBid(Bid bid) {
		
		System.out.println("Buyer executing Demand");
		
		if(!bid.getClass().equals(Demand.class))return;
		if(!bid.getProduct().equals(getProduct()))return;
		if(!bid.getIssuer().equals(this))return;
		
		quantity-=bid.getQuantity();
		if(quantity==0)quantity=-1;//if all need satisfied then just set quantity to -1 (so that a new need will be triggered latter)
		
		System.out.println("Executed Demand. Now with necessary quantity "+quantity);
		
		
		
		
		
		
		
		
	}
}
