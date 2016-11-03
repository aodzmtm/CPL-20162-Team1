package light.dateFormat;

public class MakeDateTimeFormat {

	public String addStrDateTime(String str) {
		String dateTime = null;
		try {
			
			if(str.length() == 12 &&str!=null)
			{
			String yy = str.substring(0, 2);
			String mm = str.substring(2, 4);
			String dd = str.substring(4, 6);
			String hh = str.substring(6, 8);
			String ii = str.substring(8, 10);
			String ss = str.substring(10, 12);
			dateTime = yy + "/" + mm + "/" + dd + " " + hh + ":" + ii + ":" + ss;
			}
		} catch (Exception e) {
			System.out.println("DateTimeClass");
		}

		return dateTime;
	}

	public String exceptStrDateTime(String str) {
		String dateTime = null;
		try {
			if(str.length()==17 && str!=null)
			{
			String[] dateWithTime = str.split(" ");
			String[] date = dateWithTime[0].split("/");
			String[] time = dateWithTime[1].split(":");
			
			dateTime = date[0] + date[1] + date[2] + time[0] + time[1] + time[2];
			}
		} catch (Exception e) {
			System.out.println("DateTimeClass");
		}

		return dateTime;
	}

}
