package General;
import Agents.BuyerAgentBDI;
import Agents.SellerAgentBDI;
//import Products.Product;

abstract public class Bid {
	
	
	Object issuer;
	String product;
	int price;
	int quantity;
	int timeRemaining;
	
	
	 Bid(BuyerAgentBDI is,String pr,int pric,int qt,int time){
		 
		 issuer=is;
		 product=pr;
		 price=pric;
		 quantity=qt;
		 timeRemaining=time;
	 }
	 
	 Bid(SellerAgentBDI is,String pr,int pric,int qt,int time){
		 
		 issuer=is;
		 product=pr;
		 price=pric;
		 quantity=qt;
		 timeRemaining=time;
	 }


	 
	 public Object getIssuer() {
		return issuer;
	}
	public void setIssuer(Object issuer) {
		this.issuer = issuer;
	}
	public String getProduct() {
		return product;
	}
	public void setProduct(String product) {
		this.product = product;
	}
	public int getPrice() {
		return price;
	}
	public void setPrice(int price) {
		this.price = price;
	}
	public int getQuantity() {
		return quantity;
	}
	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}
	public int getTimeRemaining() {
		return timeRemaining;
	}
	public void setTimeRemaining(int timeRemaining) {
		this.timeRemaining = timeRemaining;
	}
	
	 
	 
	
	

}
