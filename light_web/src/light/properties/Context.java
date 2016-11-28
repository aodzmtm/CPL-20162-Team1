package light.properties;

import java.io.IOException;
import java.util.Properties;

import light.properties.PropertiesManager.FILE_TYPE;

public class Context {/**
	 * Main Setting XML FILE of Acroem Co,. Ltd. 
	 */
	public static final String MAIN_CONFIG_FILE = "context.xml";
	
	/**
	 * 환경설정 파일 리스트를 가지고 있는 Properties
	 */
	private static Properties prop;
	
	
	/**
	 * 생성자
	 * 
	 * @throws IOException 
	 */
	protected Context(){}
	
	
	public static Properties getProp() throws IOException
	{
		if (prop == null)
		{
			prop = PropertiesManager.readProperties(MAIN_CONFIG_FILE, FILE_TYPE.FILE_TYPE_XML);
		}
		
		return prop;
	}
	
	
	
	/**
	 * 주어진 key에 해당하는 property 값을 되돌린다.
	 * 
	 * @param key 키 값
	 * @return property 값, 없으면 null
	 */
	public static String getPropKeyValue(String key) throws IOException
	{
		return getProp().getProperty(key);
	}
	
}
