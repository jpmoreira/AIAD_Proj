package Services;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import General.Demand;
import General.Proposal;
import jadex.bridge.IInternalAccess;
import jadex.bridge.service.annotation.Service;
import jadex.bridge.service.annotation.ServiceComponent;
import jadex.commons.future.DefaultResultListener;
import jadex.commons.future.Future;
import jadex.commons.future.IFuture;


@Service
public class ContractNetNegociationService implements NegociationService {

	@ServiceComponent
	IInternalAccess ag;
	
	

	
	
	
	
	public IFuture<ArrayList<Proposal> > startNegociation(final Demand demand) {
		
		System.out.println("Started negociation");
		
		final Future<ArrayList<Proposal> > bidsFuture=new Future<ArrayList<Proposal> >();
		
		IFuture<Collection<SellingService> > sellers=ag.getServiceContainer().getRequiredServices("SellingService");
		
		sellers.addResultListener(new DefaultResultListener<Collection<SellingService>>(){
			


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
				
				bidsFuture.setResult(proposals);
				
				
			}
			
			
		});
		
		
		return bidsFuture;
		

	}
	

}
