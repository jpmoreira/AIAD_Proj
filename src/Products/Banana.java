package Products;

public class Banana extends Product {

	
	static Banana singleton;
	
	public static Product product() {
		
		if(singleton==null)singleton=new Banana();
		return singleton;
		
	}

}
