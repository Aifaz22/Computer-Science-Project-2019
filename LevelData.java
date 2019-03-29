import java.util.ArrayList;
/**
 * Class: LevelData
 * Class Use: Storing All Possible Level Data
 * 
 * 0 = Nothing
 * 1,2,3,4 = Walls, Platforms, Floor
 * 5 = Button
 * 6,7,8 = Spikes
 * 9 = Door
 * 
 */
public class LevelData {
	
	public static final String[] LEVEL1 = new String[] {
		"411111111100001111111111111000011111111114",
		"070000000000000700000000007000000004000070",
		"000000000000000000000000000000000004000000",
		"000000000001100000000000000000111004000000",
		"411110000600000000000000000000000004001114",
		"411111100111111000000000004000000004000002",
		"300000000000700000000000011100000004600002",
		"300000000000000000000000000700000084110002",
		"300000044400000000005000000000000000000014",
		"300008440400000001111111000000000000000002",
		"300000044400000811111111100000000000000062",
		"411000070700000000007000000011100000000114",
		"300000000000040000000000000070000000000002",
		"360000000000110000000000000000000000000062",
		"411110000006000000004000004000000000811114",
		"000000000111000000004000011000000000090000",
		"000000004000000000404000000000004000090000",
		"000000000000000000404000000000011000090000",
		"000006000000000000464664000000001600090000",
		"111111111100001111111111111000011111111111",
		
		
	};
	
	public static final String[] LEVEL3 = new String[] {
		"111111111100001111111111111000011111111111",
		"000007000000000000404004000000001700009000",
		"000000000000000000404007000000011000009000",
		"000000004000000000404000000000004000009000",
		"000000000111000000704000011000000000009000",
		"411110000000600000004000004000000000811114",
		"370000000000110000007000000000004400000062",
		"300000000000040000000000000060000000000002",
		"411000060600000000000000000011100000000114",
		"300000044400000811111111100000000000000062",
		"300008440400000001111111000000000000000002",
		"300000044400000000005000000000000000000014",
		"300000000000000000000000000000000084110002",
		"300000000000600000000000011100000004000002",
		"411111100111111000004000004000000004000002",
		"411110000700000000000000000000000004001114",
		"000000000001100000400000000000111004000000",
		"000000010000000000000000000000000004000000",
		"000000010000000600006000006000000004000000",
		"411111111100001111111111111000011111111114",	
	};
	
	

	public static final String[] Tunnel = new String[] {
		"bbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbb",
		"bbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbb",
		"bbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbb",
		"bbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbb",
		"bbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbb",
		"bbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbb",
		"bbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbb",
		"bbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbb",
		"bbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbb",
		"bbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbb",
		"bbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbb",
		"bbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbb",
		"bbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbb",
		"bbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbb",
		"aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa",
		"000000000000000000000000000000000000000000",
		"000000000000000000000000000000000000000000",
		"000000000000000000000000000000000000000000",
		"000000000000000000000000000000000000000000",
		"111111111111111111111111111111111111111111",
		
	};
	
		
	private ArrayList<Boolean> passedWrappingPoint = new ArrayList<Boolean>();
	
	public LevelData() {
		
		//initializing instance variables
		if(passedWrappingPoint.size()<6) {
		for (int i = 0; i < 6;i++) {
		passedWrappingPoint.add(false);
		}
		}
	}
	
	//getter method
	public ArrayList<Boolean> getPassedWrappingPoint() {
		ArrayList<Boolean> temp = new ArrayList<Boolean>();
		for (boolean i : passedWrappingPoint) {
			temp.add(i);
		}
		return temp;
		}
	
	//sets the ith element of the list of wrapping points to true
	public void setPassedWrappingPoint(int i) {
		if (i>-1&&i<6) {
			passedWrappingPoint.set(i, true);
		}
	}
	
	//checks if all of the wrapping points have been passed
	public boolean checkIfAllPointsPassed() {
		int num = 0;
		for (boolean i : passedWrappingPoint) {
			if (i == true) {
				num++;
			}
		}
		if (num == 6) {
			return true;
		}else {
			return false;
		}
		
	}
}
