package light.controller;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import light.database.MyBatisSessionFactory;
import light.vo.LampVo;
import light.vo.UserVo;
import light.webSocket.BroadMainSocket;

@Controller
public class CommonController {
	@RequestMapping("/logIn")
	public String login(HttpServletRequest request) throws IOException {
		request.setCharacterEncoding("UTF-8");
		String returnURL = "";
		// 웹페이지에서받은 아이디,패스워드 일치시 admin 세션key 생성
		UserVo userVo = new UserVo();
		SqlSession session = MyBatisSessionFactory.getSqlSession();

		String user_id = request.getParameter("user_id").toString();
		String user_password = request.getParameter("user_password").toString();

		userVo.setUser_id(user_id);
		userVo.setUser_password(user_password);
		try {

			userVo = session.selectOne("SqlUserMapper.selectUser", userVo);

		} finally {
			session.commit();
			session.close();
		}

		if (userVo != null) {

			Map<String, Object> map = new HashMap<String, Object>();
			map.put("admin_id", userVo.getUser_id());
			map.put("admin_name", userVo.getUser_name());
			request.getSession().setAttribute("admin", map);
			returnURL = "redirect:/main.do";

		} else {
			returnURL = "redirect:/";
		}
		return returnURL;

	}

	@RequestMapping("/logOut")
	public String logout(HttpSession session) throws IOException {
		session.invalidate();
		return "redirect:/main.do";
	}
	
	

	@RequestMapping("/userIdCheck")
	public @ResponseBody boolean userIdCheck(HttpServletRequest request, HttpServletResponse response)
			throws IOException {
		request.setCharacterEncoding("UTF-8");
		UserVo userVo = new UserVo();
		SqlSession session = MyBatisSessionFactory.getSqlSession();
		String user_id = request.getParameter("user_id").toString();
		userVo.setUser_id(user_id);
		try {
			userVo = session.selectOne("SqlUserMapper.selectUserId", userVo);
		} finally {
			session.commit();
			session.close();
		}

		if (userVo != null) {
			return false;
		}
		return true;
	}

	@RequestMapping("/userContactData")
	public @ResponseBody String userContactData(HttpServletRequest request, HttpServletResponse response)
			throws IOException {
		request.setCharacterEncoding("UTF-8");
		UserVo userVo = new UserVo();
		SqlSession session = MyBatisSessionFactory.getSqlSession();

		String user_id = request.getParameter("user_id").toString();
		userVo.setUser_id(user_id);
		try {
			userVo = session.selectOne("SqlUserMapper.selectUserId", userVo);
		} finally {
			session.commit();
			session.close();
		}

		if (userVo == null) {
			return "";
		}
		return userVo.getUser_contact().toString();
	}

	/*
	 * @RequestMapping("/join") public ModelAndView join(HttpServletRequest
	 * request, HttpServletResponse response) { // db session SqlSession session
	 * = MyBatisSessionFactory.getSqlSession(); String message = "join start";
	 * ModelAndView model = new ModelAndView("join", "message", message);
	 * HashMap modelMap = new HashMap<String, Object>(); //
	 * request.getParameter("name") System.out.println(message); return model; }
	 */

	@RequestMapping("/updateUserContact")
	public @ResponseBody String updateUserContact(HttpServletRequest request, HttpServletResponse response)
			throws IOException {
		request.setCharacterEncoding("UTF-8");
		UserVo userVo = new UserVo();
		String parameter = request.getParameter("parameter").toString();

		String strs[] = parameter.split("::");
		SqlSession session = MyBatisSessionFactory.getSqlSession();
		String user_id = strs[0];
		String user_contact = strs[1];
		userVo.setUser_id(user_id);
		userVo.setUser_contact(user_contact);
		try {
			session.update("SqlUserMapper.updateUserCon", userVo);
		} finally {
			session.commit();
			session.close();
		}
		return userVo.getUser_contact();
	}

	@RequestMapping("/userJoin")
	public ModelAndView userJoin(HttpServletRequest request, HttpServletResponse response) throws IOException {
		// db session
		request.setCharacterEncoding("UTF-8");
		SqlSession session = MyBatisSessionFactory.getSqlSession();
		String message = "userJoin start";
		ModelAndView model = new ModelAndView("userJoin", "message", message);
		HashMap modelMap = new HashMap<String, Object>();
		// request.getParameter("name")
		System.out.println(message);
		return model;
	}

	@RequestMapping("/userJoinConf")
	public ModelAndView userJoinConf(HttpServletRequest request, HttpServletResponse response) throws IOException {
		// db session
		request.setCharacterEncoding("UTF-8");
		UserVo userVo = new UserVo();
		String user_id = request.getParameter("user_id");
		String user_name = request.getParameter("user_name");
		String user_contact = request.getParameter("user_contact");
		String user_password = request.getParameter("user_password");
		userVo.setUser_id(user_id);
		userVo.setUser_name(user_name);
		userVo.setUser_password(user_password);
		userVo.setUser_contact(user_contact);

		SqlSession session = MyBatisSessionFactory.getSqlSession();

		try {
			if (session.selectList("SqlUserMapper.selectUserId", userVo).isEmpty())
				session.insert("SqlUserMapper.insertUser", userVo);
		} finally {
			session.commit();
			session.close();
		}

		// request.getSession().setAttribute("admin", map);

		String message = "userJoinConf start";
		ModelAndView model = new ModelAndView("userJoinConf", "message", message);
		HashMap<String, Object> modelMap = new HashMap<String, Object>();
		modelMap.put("userId", userVo.getUser_id().toString());
		model.addAllObjects(modelMap);
		System.out.println(message);

		return model;
	}

	@RequestMapping("/userAway")
	public ModelAndView userAway(HttpServletRequest request, HttpServletResponse response) throws IOException {
		// db session
		request.setCharacterEncoding("UTF-8");
		SqlSession session = MyBatisSessionFactory.getSqlSession();
		String message = "userAway start";
		ModelAndView model = new ModelAndView("userAway", "message", message);
		HashMap modelMap = new HashMap<String, Object>();
		// request.getParameter("name")
		System.out.println(message);
		return model;
	}

	@RequestMapping("/userAwayConf")
	public ModelAndView userAwayConf(HttpServletRequest request, HttpServletResponse response) throws IOException {
		// db session
		request.setCharacterEncoding("UTF-8");
		UserVo userVo = new UserVo();
		String user_id = request.getParameter("user_id");
		String user_password = request.getParameter("user_password");

		userVo.setUser_id(user_id);
		userVo.setUser_password(user_password);

		SqlSession session = MyBatisSessionFactory.getSqlSession();
		try {
			if (!session.selectList("SqlUserMapper.selectUser", userVo).isEmpty())
				session.delete("SqlUserMapper.deleteUser", userVo);
		} finally {
			session.commit();
			session.close();
		}

		String message = "userAwayConf start";
		ModelAndView model = new ModelAndView("userAwayConf", "message", message);
		HashMap modelMap = new HashMap<String, Object>();
		// request.getParameter("name")
		System.out.println(message);
		return model;
	}

	@RequestMapping("/userInfo")
	public ModelAndView userInfo(HttpServletRequest request, HttpServletResponse response) throws IOException {
		// db session
		request.setCharacterEncoding("UTF-8");
		SqlSession session = MyBatisSessionFactory.getSqlSession();
		String message = "userInfo start";
		ModelAndView model = new ModelAndView("userInfo", "message", message);
		HashMap modelMap = new HashMap<String, Object>();
		// request.getParameter("name")
		System.out.println(message);
		return model;
	}

	@RequestMapping("/userPassChange")
	public ModelAndView userPassChange(HttpServletRequest request, HttpServletResponse response) throws IOException {
		// db session
		request.setCharacterEncoding("UTF-8");
		SqlSession session = MyBatisSessionFactory.getSqlSession();
		String message = "userPassChange start";
		ModelAndView model = new ModelAndView("userPassChange", "message", message);
		HashMap modelMap = new HashMap<String, Object>();
		// request.getParameter("name")
		System.out.println(message);
		return model;
	}

	@RequestMapping("/userPassChangeConf")
	public ModelAndView userPassChangeConf(HttpServletRequest request, HttpServletResponse response)
			throws IOException {
		// db session
		request.setCharacterEncoding("UTF-8");
		UserVo userVo = new UserVo();
		String user_id = request.getParameter("user_id");

		String user_password = request.getParameter("user_password");
		userVo.setUser_id(user_id);
		userVo.setUser_password(user_password);

		System.out.println(userVo.getUser_id());
		System.out.println(userVo.getUser_password());
		SqlSession session = MyBatisSessionFactory.getSqlSession();
		try {
			session.update("SqlUserMapper.upadateUserPass", userVo);
		} finally {
			session.commit();
			session.close();
		}
		String message = "userPassChangeConf start";
		ModelAndView model = new ModelAndView("userPassChangeConf", "message", message);
		HashMap modelMap = new HashMap<String, Object>();
		// request.getParameter("name")
		System.out.println(message);
		return model;
	}

}
