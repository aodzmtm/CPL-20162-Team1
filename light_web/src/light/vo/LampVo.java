package light.vo;

public class LampVo {
/*
	  int id` int(11) NOT NULL AUTO_INCREMENT,
	  char beacon_addr` char(12) NOT NULL,
	  `beacon_id` char(6) DEFAULT NULL,
	  `location` varchar(64) DEFAULT NULL,
	  `date_time` varchar(12) DEFAULT NULL,
	  `power_off` char(1) DEFAULT NULL,
	  `abnormal_blink` char(1) DEFAULT NULL,
	  `short_circuit` char(1) DEFAULT NULL,
	  `lamp_failure` char(1) DEFAULT NULL,
	  `lamp_state` char(1) DEFAULT NULL,
	  `illumination` char(1) DEFAULT NULL,
	  `x` varchar(64) DEFAULT NULL,
	  `y` varchar(64) DEFAULT NULL,
	  
	  id; 
	  beacon_addr;
	  beacon_id;
	  location;
	  date_time;
	  power_off;
	  abnormal_blink;
	  short_circuit;
	  lamp_failure;
	  lamp_state;
	  illumination;
	  x;
	  y;
*/
	  int id; 
	  String beacon_addr;
	  String beacon_id;
	  String location;
	  String date_time;
	  String power_off;
	  String abnormal_blink;
	  String short_circuit;
	  String lamp_failure;
	  String lamp_state;
	  String illumination;
	  double x;
	  double y;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getBeacon_addr() {
		return beacon_addr;
	}
	public void setBeacon_addr(String beacon_addr) {
		this.beacon_addr = beacon_addr;
	}
	public String getBeacon_id() {
		return beacon_id;
	}
	public void setBeacon_id(String beacon_id) {
		this.beacon_id = beacon_id;
	}
	public String getLocation() {
		return location;
	}
	public void setLocation(String location) {
		this.location = location;
	}
	public String getDate_time() {
		return date_time;
	}
	public void setDate_time(String date_time) {
		this.date_time = date_time;
	}
	public String getPower_off() {
		return power_off;
	}
	public void setPower_off(String power_off) {
		this.power_off = power_off;
	}
	public String getAbnormal_blink() {
		return abnormal_blink;
	}
	public void setAbnormal_blink(String abnormal_blink) {
		this.abnormal_blink = abnormal_blink;
	}
	public String getShort_circuit() {
		return short_circuit;
	}
	public void setShort_circuit(String short_circuit) {
		this.short_circuit = short_circuit;
	}
	public String getLamp_failure() {
		return lamp_failure;
	}
	public void setLamp_failure(String lamp_failure) {
		this.lamp_failure = lamp_failure;
	}
	public String getLamp_state() {
		return lamp_state;
	}
	public void setLamp_state(String lamp_state) {
		this.lamp_state = lamp_state;
	}
	public String getIllumination() {
		return illumination;
	}
	public void setIllumination(String illumination) {
		this.illumination = illumination;
	}
	public double getX() {
		return x;
	}
	public void setX(double x) {
		this.x = x;
	}
	public double getY() {
		return y;
	}
	public void setY(double y) {
		this.y = y;
	}
	
	  
}
