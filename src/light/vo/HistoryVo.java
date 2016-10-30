package light.vo;

public class HistoryVo {
	int id;
	String beacon_addr;
	String beacon_id;
	String location;
	String date_time;
	String failure_reason_text;
	String repair;
	String recent;
	String failure_type;
	String repair_date_time;
	int failure_reason_id;
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
	public String getFailure_reason_text() {
		return failure_reason_text;
	}
	public void setFailure_reason_text(String failure_reason_text) {
		this.failure_reason_text = failure_reason_text;
	}
	public String getRepair() {
		return repair;
	}
	public void setRepair(String repair) {
		this.repair = repair;
	}
	public String getRecent() {
		return recent;
	}
	public void setRecent(String recent) {
		this.recent = recent;
	}
	public String getFailure_type() {
		return failure_type;
	}
	public void setFailure_type(String failure_type) {
		this.failure_type = failure_type;
	}
	public String getRepair_date_time() {
		return repair_date_time;
	}
	public void setRepair_date_time(String repair_date_time) {
		this.repair_date_time = repair_date_time;
	}
	public int getFailure_reason_id() {
		return failure_reason_id;
	}
	public void setFailure_reason_id(int failure_reason_id) {
		this.failure_reason_id = failure_reason_id;
	}
	
}
