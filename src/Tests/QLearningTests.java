package Tests;
import General.QLearner;
import General.QLearner.Action;
import static org.junit.Assert.*;

import org.junit.Test;

public class QLearningTests {

	@Test
	public void test1() {
		
		QLearner q=new QLearner(0,9, 0,1,0, 100);
		
		assertFalse(q.iterate(100, Action.Decrease));//should be unable to decrease
		assertTrue(q.iterate(100, Action.Increase));
		assertEquals(q.currentPrice,1);
		
		for(int stateNr=0;stateNr<q.qMatrix.length;stateNr++){
			for(int f=0;f<q.qMatrix[stateNr].length;f++){
				if(stateNr==0 && f==Action.Increase.index)assertEquals(q.qMatrix[stateNr][f], 100);
				else assertEquals(q.qMatrix[stateNr][f],0);
			}
			
		}
		
		
		assertTrue(q.iterate(50, Action.Decrease));
		assertTrue(q.currentPrice==0);//we are at price 0 now
		
		for(int stateNr=0;stateNr<q.qMatrix.length;stateNr++){
			for(int acIndex=0;acIndex<q.qMatrix[stateNr].length;acIndex++){
				if(stateNr==0 && acIndex==Action.Increase.index)assertEquals(q.qMatrix[stateNr][acIndex], 100);
				else if(stateNr==1 && acIndex==Action.Decrease.index)assertEquals(q.qMatrix[stateNr][acIndex],50);
				else assertEquals(q.qMatrix[stateNr][acIndex],0);
			}
			
		}
		
		assertEquals(q.determenisticAction().value,Action.Increase.value);
		assertEquals(q.action(0.0001).value,Action.Increase.value);
		assertEquals(q.action(0.999999).value,Action.Decrease.value);
		
		//assertEquals(q.action().value, Action.Increase.value);//assert that increasing is the best option now
		
		assertFalse(q.iterate(100, Action.Decrease));
		
	}
	
	@Test
	
	

	
	
	public void test2(){
		
		QLearner q=new QLearner(3,4,1,0.5,0.3,100);
		
		assertEquals(q.qMatrix.length,4);
		assertEquals(q.qMatrix[0].length,3);//nr of actions
		
		
		q.qMatrix[0][0]=0;
		q.qMatrix[0][1]=50;
		q.qMatrix[0][2]=80;
		
		q.qMatrix[1][0]=60;
		q.qMatrix[1][1]=80;
		q.qMatrix[1][2]=100;
		
		q.qMatrix[2][0]=70;
		q.qMatrix[2][1]=100;
		q.qMatrix[2][2]=80;
		
		
		q.qMatrix[3][0]=95;
		q.qMatrix[3][1]=70;
		q.qMatrix[3][2]=0;
		
		q.currentPrice=1;
		assertEquals(q.determenisticAction().value, Action.Increase.value);
		q.currentPrice=2;
		assertEquals(q.determenisticAction().value, Action.Increase.value);
		q.currentPrice=3;
		assertEquals(q.determenisticAction().value, Action.Mantain.value);
		q.currentPrice=4;
		assertEquals(q.determenisticAction().value, Action.Decrease.value);
		
		q.currentPrice=2;
		
		
		/**
		 * 
		 * Initial matrix:
		 * 
		 * 	   |Decr|Mant|Incr|
		 *---------------------
		 *  1  | 0  | 50 | 80 |
		 *---------------------
		 *  2  | 60 | 80 | 100| <---
		 *--------------------- 
		 *  3  | 70 | 100| 80 |
		 *---------------------
		 *  4  | 95 | 70 |  0 |
		 *---------------------
		 * 
		 * 
		 */
		
		
		
		q.iterate(50, Action.Decrease);
		assertEquals(67,q.qMatrix[1][Action.Decrease.index]);
		assertEquals(1,q.currentPrice);
		
		
		/** At this point:
		 * 
		 * 	   |Decr|Mant|Incr|
		 *---------------------
		 *  1  | 0  | 50 | 80 | <---
		 *---------------------
		 *  2  | 97 | 80 | 100|
		 *--------------------- 
		 *  3  | 70 | 100| 80 |
		 *---------------------
		 *  4  | 95 | 70 |  0 |
		 *---------------------
		 */
		
		
		q.iterate(10, Action.Mantain);
		
		assertEquals(1,q.currentPrice);
		assertEquals(q.qMatrix[0][Action.Mantain.index],42);
		
		
		
		
		
	}
	
	@Test
	
	public void test3(){
		
		String str1="Banana";
		String str2="Banana";
		assertTrue(str1.equals(str2));
		
	}

}
