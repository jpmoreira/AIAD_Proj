package Products;

public abstract class Product {
	
	
	public static Product product(){
		
		return null;
	}
	
	@Override
	public boolean equals(Object obj) {
		
		if(obj==null)return false;
		
		return this.getClass()==obj.getClass();
	}

}
