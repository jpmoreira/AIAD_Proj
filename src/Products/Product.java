package Products;

public abstract class Product {
	
	
	@Override
	public boolean equals(Object obj) {
		
		if(obj==null)return false;
		
		return this.getClass()==obj.getClass();
	}

}
