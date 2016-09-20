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
	 * MyBatis �������� �� ���
	 */
	private final String MYBATIS_CONFIG_FILE;
	
	/**
	 * MyBatis�� �̿��ؼ� Database session�� ���� �� �ֵ��� �ϴ� 
	 */
	private static SqlSessionFactory sqlSessionFactory;
	
	
	/**
	 * ������ : Singleton
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
	 * MyBatis�� SqlSessionFactory�� ��ȯ�Ѵ�.
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
	 * MyBatis�� SqlSessionFactory���� SqlSession�� ��ȯ�Ѵ�.
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
		
		//vo ��� 
		HashMap map = new HashMap<String, Object>();
		map.put("student", "����");
		List<TestVo> list= session.selectList("SqlSampleMapper.daoTest",map);
		System.out.println(list.get(0).getId());
		System.out.println(list.get(0).getStudent());
		
		/*
		//hashmap ���
		HashMap map = new HashMap();
		map= session.selectList("SqlSampleMapper.hashmap");
		System.out.println(list.get(0).getGroupCodeId());
		System.out.println(list.get(0).getGroupCodeName());
		
		*/
		session.close();
	}
	
	
}