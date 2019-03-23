import static org.junit.Assert.*;
import java.util.ArrayList;
import org.junit.Test;
import java.io.*;

public class LevelDataTest{

	
	@Test
	public void test_getWrappingPoint(){
		LevelData lvl = new LevelData();
		lvl.setPassedWrappingPoint(2);
		lvl.setPassedWrappingPoint(1);
		ArrayList<Boolean> temp = new ArrayList<Boolean>();
		temp.add(false);
		temp.add(true);
		temp.add(true);
		temp.add(false);
		temp.add(false);
		temp.add(false);
		boolean allEqual = true;
		for (int i =0;i<6;i++){
			if(temp.get(i) != lvl.getPassedWrappingPoint().get(i)){
				allEqual = false;
			}
		}
		assertTrue("The returned list and the actual list of passed wrapping points should be the same.", allEqual);
	}
	@Test
	public void test_setWrappingPoint(){
		LevelData lvl = new LevelData();
		lvl.setPassedWrappingPoint(0);
		 assertEquals("Set the first wrapping point to true.", true, lvl.getPassedWrappingPoint().get(0));
	}
	
	@Test
	public void test_setWrappingPoint_IndexBiggerThan5(){
		LevelData lvl = new LevelData();
		lvl.setPassedWrappingPoint(8);
		
	}

	@Test
	public void test_checkIfAllPointsPassed(){
		LevelData lvl = new LevelData();
		lvl.setPassedWrappingPoint(0);
		lvl.setPassedWrappingPoint(1);
		lvl.setPassedWrappingPoint(2);
		lvl.setPassedWrappingPoint(3);
		lvl.setPassedWrappingPoint(4);
		lvl.setPassedWrappingPoint(5);
		assertTrue("The method checkIfAllPointsPassed should return true if all of the values in the list are true", lvl.checkIfAllPointsPassed());
	}
	
}