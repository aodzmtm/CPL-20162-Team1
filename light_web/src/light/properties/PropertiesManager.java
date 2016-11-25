package light.properties;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import light.properties.PropertiesManager.FILE_TYPE;

public class PropertiesManager
{
	public static enum FILE_TYPE { FILE_TYPE_NONE, FILE_TYPE_PROPERTIES, FILE_TYPE_XML }
	
	private PropertiesManager(){}
	
	
	/**
	 * 파일 타입 확인
	 * 
	 * @param file_name
	 * @return
	 */
	private static FILE_TYPE getFileType(String file_name)
	{
		FILE_TYPE type = FILE_TYPE.FILE_TYPE_NONE;
		
		String[] fnSplit = file_name.split("\\.");
		int aSize = fnSplit.length;
		String extension = null;
		
		if ( aSize > 0 )
		{
			extension = fnSplit[aSize-1].toLowerCase();
			
			if (extension.equals("properties"))
			{
				type = FILE_TYPE.FILE_TYPE_PROPERTIES;
			}
			else if (extension.equals("xml"))
			{
				type = FILE_TYPE.FILE_TYPE_XML;
			}
		}
		
		return type;
	}
	
	
	/**
	 * .properties 파일을 가져오기 위한 메소드.
	 * 확장자 .properties
	 * 
	 * @param file_name : property file name(.properties)
	 * @return Properties object to load() method
	 * @throws IOException
	 */
	public static final Properties readProperties(String file_name) throws IOException
	{
		return readProperties(file_name, getFileType(file_name));
	}
	
	
	/**
	 * .properties or .xml file을 가져오기 위한 메소드.
	 * 
	 * @param file_name : properties file name(.properties or .xml)
	 * @param file_type : enum PropertiesManager.File_TYPE
	 * @return Properties object
	 * 
	 * @throws IOException 
	 */
	public static final Properties readProperties(String file_name, FILE_TYPE file_type) throws IOException
	{
		Properties prop = null;
		InputStream is = null;
		ClassLoader loader = Thread.currentThread().getContextClassLoader();

		
		if (loader != null)
		{
			is = loader.getResourceAsStream(file_name);
		}
		
		if (is == null)
		{
			is = ClassLoader.getSystemResourceAsStream(file_name);
		}
		
		if (is == null)
		{
			//Logger.getLogger("PropertiesManager").error("Not found (" + file_name + ") file.");
			throw new IOException("Not found (" + file_name + ") file.");
		}
		
		prop = new Properties();
		
		if (file_type == FILE_TYPE.FILE_TYPE_PROPERTIES)
		{
			prop.load(is);
		}
		else if (file_type == FILE_TYPE.FILE_TYPE_XML)
		{
			prop.loadFromXML(is);
		}
		else
		{
			prop = null;
		}

		is.close();
		
		return prop;
	}
	
	
	/**
	 * .properties file 에서 특정 Key의 값만을 가져오기 위한 메소드이다.
	 * 
	 * @param file_name : property file name(.properties)
	 * @param key		: properties file에서 가져오고자 하는 값의 키
	 * @return key의 값, 해당 키 또는 값이 없을 경우 null
	 * @throws IOException : Properties 파일을 찾을 수 없거나, 읽을 수 없을 경우에 발생한다.
	 */
	public static final String getPropertyValue(String file_name, String key) throws IOException
	{
		return getPropertyValue(file_name, key, getFileType(file_name));
	}
	
	
	/**
	 * .properties or .xml file 에서 특정 Key의 값만을 가져오기 위한 메소드이다.
	 * 
	 * @param file_name : properties file name(.properties or .xml)
	 * @param key		: properties file에서 가져오고자 하는 값의 키
	 * @param file_type : enum PropertiesManager.File_TYPE
	 * @return key의 값, 해당 키 또는 값이 없을 경우 null
	 * @throws IOException : Properties 파일을 찾을 수 없거나, 읽을 수 없을 경우에 발생한다.
	 */
	public static final String getPropertyValue(String file_name, String key, FILE_TYPE file_type) throws IOException 
	{
		Properties prop = readProperties(file_name, file_type);
		
		return prop.getProperty(key);
	}
	
	
	/**
	 * @param args
	 */
	public static void main(String[] args)
	{
		try	{
			System.out.println(PropertiesManager.getPropertyValue(Context.MAIN_CONFIG_FILE, "properties"));
		} catch (IOException e)	{
			e.printStackTrace();
		}
	}

}
