package Tests;
import General.QLearner;
import static org.junit.Assert.*;

import org.junit.Test;

public class QLearningTests {

	@Test
	public void test1() {
		
		QLearner q=new QLearner(0,9, 0,1,0, 100);
		
		assertFalse(q.iterate(100, -1));//should be unable to decrease
		assertTrue(q.iterate(100, +1));
		assertEquals(q.currentPrice,1);
		
		for(int stateNr=0;stateNr<q.qMatrix.length;stateNr++){
			if(stateNr==1) assertEquals(q.qMatrix[stateNr],100);
			else assertEquals(q.qMatrix[stateNr],0);
			
		}
		
		
		assertTrue(q.iterate(50, -1));
		assertTrue(q.currentPrice==0);//we are at price 0 now
		
		
		
		for(int stateNr=0;stateNr<q.qMatrix.length;stateNr++){
			
			if(stateNr==1)assertEquals(q.qMatrix[stateNr], 100);
			else if(stateNr==0)assertEquals(q.qMatrix[stateNr],50);
			else assertEquals(q.qMatrix[stateNr],0);
			
		}
		
		
		assertEquals(q.determenisticAction(),1);
		
		assertEquals(q.action(0.0001),0);
		assertEquals(q.action(0.999999),9);
		
		
		//assertEquals(q.action(), 1);//assert that increasing is the best option now
		
		assertFalse(q.iterate(100, -50));

		
		
	}
	
	@Test
	
	

	
	
	public void test2(){
		
		
		
		
		
	}
	

}
