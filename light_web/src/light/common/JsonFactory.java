package light.common;

import java.io.BufferedReader;

import javax.servlet.http.HttpServletRequest;

public class JsonFactory {

	public String readJSONStringFromRequestBody(HttpServletRequest request) {
		StringBuffer json = new StringBuffer();
		String line = null;

		try {

			BufferedReader reader = request.getReader();
			while ((line = reader.readLine()) != null) {
				json.append(line);
			}
		} catch (Exception e) {
			System.out.println("Error reading JSON string:" + e.toString());

		}
		return json.toString();
	}
}
