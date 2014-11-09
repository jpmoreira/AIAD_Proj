package Tests;

import jadex.bdiv3.examples.booktrading.seller.SellerBDI;

import org.junit.Test;

import Agents.BuyerAgentBDI;
import Agents.SellerAgentBDI;
import General.Demand;
import General.Proposal;
import Products.Banana;
import Products.Product;
import junit.framework.TestCase;

public class BidTests extends TestCase {
	
	
	
	@Test
	public void testDemandProperties(){
		
		BuyerAgentBDI a=new BuyerAgentBDI();
		a.setPrice(20);
		a.setQuantity(5);
		
		
		Demand d=new Demand(a);
		
		assertTrue(d.getPrice()==a.getPrice());
		assertTrue(d.getIssuer()==a);
		assertTrue(d.getTimeRemaining()==a.getDeadline());
		assertEquals(d.getProduct(),a.getProduct());
		
		
	}
	
	
	
	public void proposalTest(){
		
		SellerAgentBDI s=new SellerAgentBDI();
		s.setNrProducts(50);
		s.setProduct(Banana.product());
		s.setPrice(10);
		s.setProduction(10);
		s.setStockCapacity(500);
		
		Demand bananaDemand=new Demand(null, Banana.product(), 10, 1, 0);
		
		Demand unkProdDemand=new Demand(null,Product.product(),1,1,0);
		
		Proposal p=s.proposalForDemand(bananaDemand);
		Proposal p2=s.proposalForDemand(unkProdDemand);		
		assertFalse(p==null);
		assertTrue(p2==null);
		
		assertEquals(p.getPrice(),s.getPrice());
		assertEquals(p.getProduct(),s.getProduct());
		assertEquals(p.getQuantity(),bananaDemand.getQuantity());
		
		
		
		
	}
}
