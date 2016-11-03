package light.parsing;

import light.vo.EventVO;

public class EventParsing {
	private final int indexSTX = 0;
	private final int startDate = 1;
	private final int endDate = 13;
	private final int indexInstruction = 13;
	private final int startbeaconId = 14;
	private final int endbeaconId = 20;
	private final int startAccessInterval = 20;
	private final int endAccessInterval = 22;
	private final int startEventENDIS = 22;
	private final int endEventENDIS = 28;
	private final int startDistrictID = 28;
	private final int endDistrictID = 30;
	private final int startLightoutTime = 30;
	private final int endLightoutTime = 38;
	private final int startLightoutDate = 38;
	private final int endLightoutDate = 46;
	private final int startLightingDeviation = 46;
	private final int endLightingDeviation = 50;
	private final int startLightoutDeviation = 50;
	private final int endLightoutDeviation = 54;
	private final int indexPower_off = 54;
	private final int indexAbnormal_blink = 55;
	private final int indexShort_circuit = 56;
	private final int indexLamp_failure = 57;
	private final int indexLamp_state = 58;
	private final int indexIllumination = 59;
	private final int indexETX = 26;
	
	public EventVO parsing(String s){
		EventVO e = new EventVO();
		
		e.setsTX(s.charAt(indexSTX));
		e.setDate_time(new String(s.substring(startDate, endDate)));
		e.setInstruction(s.charAt(indexInstruction));
		e.setBeacon_id(new String(s.substring(startbeaconId, endbeaconId)));
		e.setAccessInterval(new String(s.substring(startAccessInterval, endAccessInterval)));
		e.setEventENDIS(new String(s.substring(startEventENDIS, endEventENDIS)));
		e.setDistrictID(new String(s.substring(startDistrictID, endDistrictID)));
		e.setLightoutTime(new String(s.substring(startLightoutTime, endLightoutTime)));
		e.setLightoutDate(new String(s.substring(startLightoutDate, endLightoutDate)));
		e.setLightingDeviation(new String(s.substring(startLightingDeviation, endLightingDeviation)));
		e.setLightoutDeviation(new String(s.substring(startLightoutDeviation, endLightoutDeviation)));
		e.setPower_off(s.charAt(indexPower_off));
		e.setAbnormal_blink(s.charAt(indexAbnormal_blink));
		e.setShort_circuit(s.charAt(indexShort_circuit));
		e.setLamp_failure(s.charAt(indexLamp_failure));
		e.setLamp_state(s.charAt(indexLamp_state));
		e.setIllumination(s.charAt(indexIllumination));
		e.seteTX(s.charAt(indexETX));
		
		return e;
	}
}
