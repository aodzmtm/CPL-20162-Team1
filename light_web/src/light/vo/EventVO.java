package light.vo;

public class EventVO {
	
	
	private char sTX;
	private String date_time;
	private char instruction;
	private String beacon_id;
	// 기존 CDMA 점멸기와 호환 되는 데이터 부분으로 기본 값만 입력
	private String accessInterval; // 접속 간격
	private String eventENDIS; // EVENT EN/DIS
	private String districtID; // 지역 ID
	private String lightoutTime; // 심야소등 시간설정
	private String lightoutDate; // 심야소등 기간설정
	private String lightingDeviation; // 점등 편차
	private String lightoutDeviation; // 소등 편차
	// 여기까지는 기본 값만 들어옴.
	private char power_off;
	private char abnormal_blink;
	private char short_circuit;
	private char lamp_failure;
	private char lamp_state;
	private char illumination;
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


	public String getBeacon_id() {
		return beacon_id;
	}


	public void setBeacon_id(String beacon_id) {
		this.beacon_id = beacon_id;
	}


	public String getAccessInterval() {
		return accessInterval;
	}


	public void setAccessInterval(String accessInterval) {
		this.accessInterval = accessInterval;
	}


	public String getEventENDIS() {
		return eventENDIS;
	}


	public void setEventENDIS(String eventENDIS) {
		this.eventENDIS = eventENDIS;
	}


	public String getDistrictID() {
		return districtID;
	}


	public void setDistrictID(String districtID) {
		this.districtID = districtID;
	}


	public String getLightoutTime() {
		return lightoutTime;
	}


	public void setLightoutTime(String lightoutTime) {
		this.lightoutTime = lightoutTime;
	}


	public String getLightoutDate() {
		return lightoutDate;
	}


	public void setLightoutDate(String lightoutDate) {
		this.lightoutDate = lightoutDate;
	}


	public String getLightingDeviation() {
		return lightingDeviation;
	}


	public void setLightingDeviation(String lightingDeviation) {
		this.lightingDeviation = lightingDeviation;
	}


	public String getLightoutDeviation() {
		return lightoutDeviation;
	}


	public void setLightoutDeviation(String lightoutDeviation) {
		this.lightoutDeviation = lightoutDeviation;
	}


	public char getPower_off() {
		return power_off;
	}


	public void setPower_off(char power_off) {
		this.power_off = power_off;
	}


	public char getAbnormal_blink() {
		return abnormal_blink;
	}


	public void setAbnormal_blink(char abnormal_blink) {
		this.abnormal_blink = abnormal_blink;
	}


	public char getShort_circuit() {
		return short_circuit;
	}


	public void setShort_circuit(char short_circuit) {
		this.short_circuit = short_circuit;
	}


	public char getLamp_failure() {
		return lamp_failure;
	}


	public void setLamp_failure(char lamp_failure) {
		this.lamp_failure = lamp_failure;
	}


	public char getLamp_state() {
		return lamp_state;
	}


	public void setLamp_state(char lamp_state) {
		this.lamp_state = lamp_state;
	}


	public char getIllumination() {
		return illumination;
	}


	public void setIllumination(char illumination) {
		this.illumination = illumination;
	}


	public char geteTX() {
		return eTX;
	}


	public void seteTX(char eTX) {
		this.eTX = eTX;
	}

	
}
