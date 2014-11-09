package Tests;

import static org.junit.Assert.*;

import org.junit.Test;

import Products.Banana;
import Products.Product;

public class ProductTests {

	@Test
	public void testEqualProducts() {
	
		Banana a=(Banana) Banana.product();
		Banana b=(Banana) Banana.product();
		
		assertEquals(a, b);
	}
	
	@Test
	public void testDifferentProducts(){
		
		Banana a=(Banana) Banana.product();
		Product p= Product.product();
		
		assertNotEquals(a, p);
		
	}

}
