package light.controller;

import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import light.database.MyBatisSessionFactory;
import light.vo.TestVo;

@Controller
//@SessionAttributes	

public class PageController {

	
 @Autowired
 SqlSession session ;
	@RequestMapping("/main")
	public ModelAndView main(HttpServletRequest request, HttpServletResponse response){
		
// db session

	
		session = MyBatisSessionFactory.getSqlSession();			
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
		System.out.println(request.getParameter("name"));
		
		
// homepage view
		
		
		String message = "Homepage start";
		ModelAndView model = new ModelAndView("main", "message", message);
		HashMap modelMap = new HashMap<String, Object>();
		
		modelMap.put("DataSet", request.getParameter("name"));
		model.addAllObjects(modelMap);
		System.out.println(message);
	


		return model; 

	
	}
	

}
