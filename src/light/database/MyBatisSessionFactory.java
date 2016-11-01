package light.database;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import light.properties.Context;
import light.vo.TestVo;

public class MyBatisSessionFactory
{
	public static final String CONTEXT_KEY = "mybatis";
	
	/**
	 * MyBatis 설정파일 및 경로
	 */
	private final String MYBATIS_CONFIG_FILE;
	
	/**
	 * MyBatis를 이용해서 Database session을 얻어올 수 있도록 하는 
	 */
	private static SqlSessionFactory sqlSessionFactory;
	
	
	/**
	 * 생성자 : Singleton
	 * @throws IOException 
	 */
	private MyBatisSessionFactory() throws IOException
	{
		this.MYBATIS_CONFIG_FILE = Context.getPropKeyValue(CONTEXT_KEY);
		makeSqlSessionFactory();
	}
	
	
	/**
	 * @throws IOException
	 */
	private void makeSqlSessionFactory() throws IOException
	{
		InputStream is = null;
		
		is = Resources.getResourceAsStream(MYBATIS_CONFIG_FILE);
		sqlSessionFactory = new SqlSessionFactoryBuilder().build(is);
			
		is.close();
		
	}
	
	
	/**
	 * MyBatis의 SqlSessionFactory를 반환한다.
	 * 
	 * @return SqlSessionFactory
	 * @throws IOException
	 */
	public static SqlSessionFactory getSqlSessionFactory()
	{
		if (sqlSessionFactory == null)
		{
			try
			{
				new MyBatisSessionFactory();
			} catch (IOException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
			
		
		return sqlSessionFactory;
	}
	
	
	/**
	 * MyBatis의 SqlSessionFactory에서 SqlSession을 반환한다.
	 * 
	 * @return SqlSession
	 */
	public static SqlSession getSqlSession()
	{
		return MyBatisSessionFactory.getSqlSessionFactory().openSession();
	}

	/**
	 * Test
	 * @param args
	 */
	public static void main(String[] args)
	{

	
		SqlSession session = MyBatisSessionFactory.getSqlSession();	
		
		//vo 사용 
		HashMap map = new HashMap<String, Object>();
		map.put("student", "교수");
		List<TestVo> list= session.selectList("SqlSampleMapper.daoTest",map);
		System.out.println(list.get(0).getId());
		System.out.println(list.get(0).getStudent());
		
		/*
		//hashmap 사용
		HashMap map = new HashMap();
		map= session.selectList("SqlSampleMapper.hashmap");
		System.out.println(list.get(0).getGroupCodeId());
		System.out.println(list.get(0).getGroupCodeName());
		
		*/
		session.close();
	}
	
	
}
