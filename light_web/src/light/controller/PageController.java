package light.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.ibatis.session.SqlSession;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import light.dateFormat.MakeDateTimeFormat;
import light.common.JsonFactory;
import light.database.MyBatisSessionFactory;
import light.vo.HistoryVo;
import light.vo.LampVo;
import light.vo.TestVo;
import light.webSocket.BroadMainSocket;

@Controller
// @SessionAttributes

public class PageController {

	@RequestMapping("/main")
	public ModelAndView main(HttpServletRequest request, HttpServletResponse response) {
		// db session
		SqlSession session = MyBatisSessionFactory.getSqlSession();
		String message = "Homepage start";
		ModelAndView model = new ModelAndView("main", "message", message);
		HashMap modelMap = new HashMap<String, Object>();
		// request.getParameter("name")
		System.out.println(message);
		return model;
	}

	
	
	
	
	@RequestMapping("/grid")
	public ModelAndView gridView(HttpServletRequest request, HttpServletResponse response) {
		SqlSession session = MyBatisSessionFactory.getSqlSession();
		String message = "grid start";
		ModelAndView model = new ModelAndView("grid", "message", message);
		// request.getParameter("name")
		System.out.println(message);

		return model;
	}

/*	@RequestMapping("/main")
	public ModelAndView main(HttpServletRequest request, HttpServletResponse response) {

		// db session

		SqlSession session = MyBatisSessionFactory.getSqlSession();
		HashMap map = new HashMap<String, Object>();

		 검색방법 map.put("student", "교수"); 
		List<TestVo> list = session.selectList("SqlSampleMapper.daoTest", map);
		System.out.println(list.get(0).getId());
		System.out.println(list.get(0).getStudent());

		session.close();
		// homepage view
		String message = "Homepage start";
		ModelAndView model = new ModelAndView("main", "message", message);
		HashMap modelMap = new HashMap<String, Object>();
		// request.getParameter("name")

		modelMap.put("DataSet", list);
		model.addAllObjects(modelMap);
		// 검색내용 보여 주기 // html에서 호출 방법 ${DataSet.get(0).getStudent()}
		System.out.println(message);

		return model;
	}*/

	
	
	@RequestMapping(value = "/json")
	public @ResponseBody List<TestVo> getJsonByMap(HttpServletRequest request, HttpServletResponse response) {
		SqlSession session = MyBatisSessionFactory.getSqlSession();
		// request 받기
		JsonFactory fac = new JsonFactory();
		String json = fac.readJSONStringFromRequestBody(request);

		try {
			JSONParser jsonParser = new JSONParser();
			// JSON데이터를 넣어 JSON Object 로 만들어 준다.
			JSONObject jsonObject = (JSONObject) jsonParser.parse(json);
			// books의 배열을 추출
			JSONArray bookInfoArray = (JSONArray) jsonObject.get("TestVo");

			for (int i = 0; i < bookInfoArray.size(); i++) {

				System.out.println("=BOOK_" + i + " ===========================================");

				// 배열 안에 있는것도 JSON형식 이기 때문에 JSON Object 로 추출
				JSONObject TestVoObject = (JSONObject) bookInfoArray.get(i);

				// JSON name으로 추출
				System.out.println("id==>" + TestVoObject.get("id"));
				System.out.println("student==>" + TestVoObject.get("student"));

			}

		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

		HashMap map = new HashMap<String, Object>();
		List<TestVo> list = session.selectList("SqlSampleMapper.daoTest", map);

		session.close();

		return list;
	}

	@RequestMapping(value = "/gridJson")
	public @ResponseBody JSONObject getGridByMap(HttpServletRequest request, HttpServletResponse response)
			throws IOException {
		SqlSession session = MyBatisSessionFactory.getSqlSession();
		JSONObject jsonObj = new JSONObject();
		String parameter = request.getParameter("id");
		String student = request.getParameter("student");

		HashMap map = new HashMap<String, Object>();
		List<TestVo> list = session.selectList("SqlSampleMapper.daoTest", map);
		session.close();
		jsonObj.put("rows", list);
		/*
		 * //현재페이지 jsonObj.put("page", current_page); //총페이지수
		 * jsonObj.put("total", 2); //총글목록수 jsonObj.put("records", 4);
		 */
		System.out.println("::" + parameter);
		System.out.println(student);
		//
		/*
		 * { page
		 * 
		 * rows:{list}
		 * 
		 * }
		 */
		// request 받기
		return jsonObj;
	}

	@RequestMapping("/edit")
	public void edit(HttpServletRequest request, HttpServletResponse response) throws IOException {
		SqlSession session = MyBatisSessionFactory.getSqlSession();
		BroadMainSocket bt = new BroadMainSocket();
		bt.getInstance().onMessage("안녕", null);

		String id = request.getParameter("id");
		String student = request.getParameter("student");
		String param = request.getParameter("OrderDate");

		String message = "grid start";

		HashMap map = new HashMap<String, Object>();
		map.put("id", id);
		map.put("student", student);
		try {
			if (student != null) {
				System.out.println(id + student);
				session.update("SqlSampleMapper.daoupdateTest", map);
			} else
				session.delete("SqlSampleMapper.daodeleteTest", map);

		} finally {
			session.commit();
			session.close();
		}
	}

	////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	
	////////////////////////////////////

	
	
	
}
