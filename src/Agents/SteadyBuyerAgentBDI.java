package Agents;

import jadex.bridge.service.RequiredServiceInfo;
import jadex.micro.annotation.Agent;
import jadex.micro.annotation.Argument;
import jadex.micro.annotation.Arguments;
import jadex.micro.annotation.Binding;
import jadex.micro.annotation.Description;
import jadex.micro.annotation.RequiredService;
import jadex.micro.annotation.RequiredServices;
import Services.SellingService;


@Agent
@Description("A buyer that allways mantains its price")
@Arguments({
	@Argument(name="Maximum Buying Price", clazz=Integer.class, defaultvalue="10"),
	@Argument(name="Product", clazz=String.class, defaultvalue="Banana")
	})
@RequiredServices(@RequiredService(name="SellingService", type=SellingService.class,
binding=@Binding(scope=RequiredServiceInfo.SCOPE_PLATFORM),multiple=true))
public class SteadyBuyerAgentBDI extends BuyerAgentBDI {
	
	

}
