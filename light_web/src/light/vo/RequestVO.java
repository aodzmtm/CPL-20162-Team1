package light.vo;

public class RequestVO {
	private char sTX;
	private String date_time;
	private char instruction;
	private String mACAddr;
	private char eTX;
	
	public char getsTX() {
		return sTX;
	}

	public void setsTX(char sTX) {
		this.sTX = sTX;
	}

	
	public String getDate_time() {
		return date_time;
	}

	public void setDate_time(String date_time) {
		this.date_time = date_time;
	}

	public char getInstruction() {
		return instruction;
	}

	public void setInstruction(char instruction) {
		this.instruction = instruction;
	}

	public String getmACAddr() {
		return mACAddr;
	}

	public void setmACAddr(String mACAddr) {
		this.mACAddr = mACAddr;
	}

	public char geteTX() {
		return eTX;
	}

	public void seteTX(char eTX) {
		this.eTX = eTX;
	}
}
