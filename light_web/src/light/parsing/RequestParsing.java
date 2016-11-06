package light.parsing;

import light.vo.RequestVO;

public class RequestParsing {
	private final int indexSTX = 0;
	private final int startDate = 1;
	private final int endDate = 13;
	private final int indexInstruction = 13;
	private final int startMAC = 14;
	private final int endMAC = 26;
	private final int indexETX = 26;
	
	public RequestVO parsing(String s){
		RequestVO r = new RequestVO();
		
		r.setsTX(s.charAt(indexSTX));
		r.setDate_time(new String(s.substring(startDate,endDate)));
		r.setInstruction(s.charAt(indexInstruction)); 
		r.setmACAddr(new String(s.substring(startMAC, endMAC)));
		r.seteTX(s.charAt(indexETX));
		
		return r;
	}
}
