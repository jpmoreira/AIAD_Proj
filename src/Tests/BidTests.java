package Tests;

import org.junit.Test;

import Agents.BuyerAgentBDI;
import Agents.SellerAgentBDI;
import General.Demand;
import General.Proposal;
import General.SealedProposal;
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
	
	
	@Test
	public void testProposals(){
		
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


	@Test
	public void testSimulateInvalidTransaction(){
		
		BuyerAgentBDI buyer=new BuyerAgentBDI();
		SellerAgentBDI seller=new SellerAgentBDI();
		
		
		//NO matching product
		buyer.setProduct(Banana.product());
		seller.setProduct(Product.product());
		
		buyer.setQuantity(10);
		Demand d=new Demand(buyer);
		
		
		seller.setNrProducts(10);
		
		Proposal p=seller.proposalForDemand(d);
		
		assertEquals(p,null);
		
		
		/*
		SealedProposal sp=new SealedProposal(d,p);
		
		
		sp.execute();//expected no change since product is different
		
		
		assertEquals(buyer.getQuantity(),10);
		*/
		
		
		
		
		
	}
	
	@Test
	public void testSimulateInvalidTransaction2(){
		
		BuyerAgentBDI buyer=new BuyerAgentBDI();
		SellerAgentBDI seller=new SellerAgentBDI();
		SellerAgentBDI notTheSeller=new SellerAgentBDI();
		
		
		//NO matching product
		buyer.setProduct(Banana.product());
		seller.setProduct(Banana.product());
		notTheSeller.setProduct(Banana.product());
		
		buyer.setQuantity(10);
		Demand d=new Demand(buyer);
		
		
		seller.setNrProducts(10);
		notTheSeller.setNrProducts(10);
		
		Proposal p=seller.proposalForDemand(d);
		
		assertTrue(p!=null);
		
		
		
		SealedProposal sp=new SealedProposal(d,p);
		
		
		assertFalse(sp==null);
		
		sp.execute();//expected no change since product is different
		
		
		assertEquals(buyer.getQuantity(),0);
		assertEquals(seller.getNrProducts(),0);
		
		notTheSeller.executeBid(p);//should not execute it since it wasn't his proposal!
		
		assertEquals(notTheSeller.getNrProducts(),10);
		
		
		
		
		
		
		
		
	}
}
