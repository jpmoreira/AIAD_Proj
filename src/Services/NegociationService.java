package Services;

import java.util.ArrayList;

import jadex.commons.future.IFuture;
import General.Demand;
import General.Proposal;


public interface NegociationService {
	
	
	IFuture<ArrayList<Proposal> > startNegociation(final Demand demand);

}
