package light.controller;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.ibatis.session.SqlSession;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import dateFormat.MakeDateTimeFormat;
import light.common.JsonFactory;
import light.database.MyBatisSessionFactory;
import light.vo.HistoryVo;
import light.vo.LampVo;
import light.webSocket.BroadMainSocket;

@Controller
public class MapController {

	@RequestMapping(value = "/lampData")
	public @ResponseBody List<LampVo> getLampData(HttpServletRequest request, HttpServletResponse response) {
		SqlSession session = MyBatisSessionFactory.getSqlSession();
		// request 받기
		JsonFactory fac = new JsonFactory();
		String json = fac.readJSONStringFromRequestBody(request);
		/*
		 * try { JSONParser jsonParser = new JSONParser(); // JSON데이터를 넣어 JSON
		 * Object 로 만들어 준다. JSONObject jsonObject = (JSONObject)
		 * jsonParser.parse(json); // books의 배열을 추출 JSONArray bookInfoArray =
		 * (JSONArray) jsonObject.get("TestVo");
		 * 
		 * for (int i = 0; i < bookInfoArray.size(); i++) {
		 * 
		 * System.out.println("=BOOK_" + i +
		 * " ===========================================");
		 * 
		 * // 배열 안에 있는것도 JSON형식 이기 때문에 JSON Object 로 추출 JSONObject TestVoObject
		 * = (JSONObject) bookInfoArray.get(i);
		 * 
		 * // JSON name으로 추출 System.out.println("id==>" +
		 * TestVoObject.get("id")); System.out.println("student==>" +
		 * TestVoObject.get("student"));
		 * 
		 * }
		 * 
		 * } catch (ParseException e) { // TODO Auto-generated catch block
		 * e.printStackTrace(); }
		 */
		MakeDateTimeFormat makeDateTimeFormat = new MakeDateTimeFormat();

		HashMap map = new HashMap<String, Object>();
		List<LampVo> list = session.selectList("SqlMapMapper.selectLamp", map);
		for (int i = 0; i < list.size(); i++) {
			LampVo lampVo = new LampVo();
			lampVo = list.get(i);
			String dateTime = list.get(i).getDate_time().toString();
			String makeDateTime = makeDateTimeFormat.addStrDateTime(dateTime);
			lampVo.setDate_time(makeDateTime);
			list.set(i, lampVo);
		}
		session.close();
		return list;
	}

	@RequestMapping("/insertLamp")
	public void insertLamp(HttpServletRequest request, HttpServletResponse response) throws IOException {
		SqlSession session = MyBatisSessionFactory.getSqlSession();
		/*
		 * request.getParameter("beacon_addr");
		 * request.getParameter("beacon_id"); request.getParameter("location");
		 * equest.getParameter("date_time"); request.getParameter("power_off");
		 * request.getParameter("abnormal_blink");
		 * request.getParameter("short_circuit");
		 * request.getParameter("lamp_failure");
		 * request.getParameter("lamp_state");
		 * request.getParameter("illumination"); request.getParameter("x");
		 * request.getParameter("y");
		 */
		/*
		 * String beacon_addr = request.getParameter("beacon_addr"); String
		 * beacon_id = request.getParameter("beacon_id"); String location =
		 * request.getParameter("location"); String date_time =
		 * request.getParameter("date_time"); String power_off =
		 * request.getParameter("power_off"); String abnormal_blink =
		 * request.getParameter("abnormal_blink"); String short_circuit =
		 * request.getParameter("short_circuit"); String lamp_failure =
		 * request.getParameter("lamp_failure"); String lamp_state =
		 * request.getParameter("lamp_state"); String illumination =
		 * request.getParameter("illumination"); Double x =
		 * Double.parseDouble(request.getParameter("x")); Double y =
		 * Double.parseDouble(request.getParameter("y"));
		 * 
		 * 
		 * LampVo lampVo = new LampVo(); lampVo.setBeacon_addr(beacon_addr);
		 * lampVo.setBeacon_id(beacon_id); lampVo.setLocation(location);
		 * lampVo.setDate_time(date_time); lampVo.setPower_off(power_off);
		 * lampVo.setAbnormal_blink(abnormal_blink);
		 * lampVo.setShort_circuit(short_circuit);
		 * lampVo.setLamp_failure(lamp_failure);
		 * lampVo.setLamp_state(lamp_state);
		 * lampVo.setIllumination(illumination); lampVo.setX(x); lampVo.setY(y);
		 * 
		 */
		// request 받기
		request.setCharacterEncoding("UTF-8");
		BroadMainSocket bt = new BroadMainSocket();
		MakeDateTimeFormat makeDateTimeFormat = new MakeDateTimeFormat();
		LampVo lampVo = new LampVo();
		HistoryVo historyVo = new HistoryVo();
		JsonFactory fac = new JsonFactory();
		String json = fac.readJSONStringFromRequestBody(request);

		try {
			JSONParser jsonParser = new JSONParser();
			// JSON데이터를 넣어 JSON Object 로 만들어 준다.
			JSONObject jsonObject = (JSONObject) jsonParser.parse(json);
			// books의 배열을 추출
			JSONArray lampInfoArray = (JSONArray) jsonObject.get("LampVo");

			for (int i = 0; i < lampInfoArray.size(); i++) {

				// 배열 안에 있는것도 JSON형식 이기 때문에 JSON Object 로 추출
				JSONObject lampVoObject = (JSONObject) lampInfoArray.get(i);
				// JSON name으로 추출
				lampVo.setBeacon_addr(lampVoObject.get("beacon_addr").toString());
				lampVo.setBeacon_id(lampVoObject.get("beacon_id").toString());
				lampVo.setLocation(lampVoObject.get("location").toString());
				lampVo.setDate_time(makeDateTimeFormat.exceptStrDateTime(lampVoObject.get("date_time").toString()));
				lampVo.setPower_off(lampVoObject.get("power_off").toString());
				lampVo.setAbnormal_blink(lampVoObject.get("abnormal_blink").toString());
				lampVo.setShort_circuit(lampVoObject.get("short_circuit").toString());
				lampVo.setLamp_failure(lampVoObject.get("lamp_failure").toString());
				lampVo.setLamp_state(lampVoObject.get("lamp_state").toString());
				lampVo.setIllumination(lampVoObject.get("illumination").toString());
				lampVo.setX(Double.parseDouble(lampVoObject.get("x").toString()));
				lampVo.setY(Double.parseDouble(lampVoObject.get("y").toString()));

				// history
				historyVo.setBeacon_addr(lampVoObject.get("beacon_addr").toString());
				historyVo.setBeacon_id(lampVoObject.get("beacon_id").toString());
				historyVo.setLocation(lampVoObject.get("location").toString());
				historyVo.setDate_time(makeDateTimeFormat.exceptStrDateTime(lampVoObject.get("date_time").toString()));
			}

		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		try {
			session.insert("SqlMapMapper.insertLamp", lampVo);

			if (lampVo.getPower_off().equals("1")) {
				historyVo.setFailure_reason_id(1);
				historyVo.setFailure_reason_text("정전");
				historyVo.setFailure_type("0");
				historyVo.setRepair("1");
				historyVo.setRecent("1");
				if (session.selectList("SqlMapMapper.selectHistory", historyVo).isEmpty())
					session.insert("SqlMapMapper.insertHistory", historyVo);
			}
			if (lampVo.getAbnormal_blink().equals("1")) {
				historyVo.setFailure_reason_id(2);
				historyVo.setFailure_reason_text("이상점등");
				historyVo.setFailure_type("1");
				historyVo.setRepair("1");
				historyVo.setRecent("1");
				if (session.selectList("SqlMapMapper.selectHistory", historyVo).isEmpty())
					session.insert("SqlMapMapper.insertHistory", historyVo);
			}
			if (lampVo.getAbnormal_blink().equals("2")) {
				historyVo.setFailure_reason_id(3);
				historyVo.setFailure_reason_text("이상소등");
				historyVo.setFailure_type("1");
				historyVo.setRepair("1");
				historyVo.setRecent("1");
				if (session.selectList("SqlMapMapper.selectHistory", historyVo).isEmpty())
					session.insert("SqlMapMapper.insertHistory", historyVo);
			}
			if (lampVo.getShort_circuit().equals("1")) {
				historyVo.setFailure_reason_id(4);
				historyVo.setFailure_reason_text("누전");
				historyVo.setFailure_type("2");
				historyVo.setRepair("1");
				historyVo.setRecent("1");
				if (session.selectList("SqlMapMapper.selectHistory", historyVo).isEmpty())
					session.insert("SqlMapMapper.insertHistory", historyVo);
			}
			if (lampVo.getLamp_failure().equals("1")) {
				historyVo.setFailure_reason_id(5);
				historyVo.setFailure_reason_text("램프고장");
				historyVo.setFailure_type("3");
				historyVo.setRepair("1");
				historyVo.setRecent("1");
				if (session.selectList("SqlMapMapper.selectHistory", historyVo).isEmpty())
					session.insert("SqlMapMapper.insertHistory", historyVo);
			}
			if (lampVo.getLamp_failure().equals("2")) {
				historyVo.setFailure_reason_id(6);
				historyVo.setFailure_reason_text("안정기 고장");
				historyVo.setFailure_type("3");
				historyVo.setRepair("1");
				historyVo.setRecent("1");
				if (session.selectList("SqlMapMapper.selectHistory", historyVo).isEmpty())
					session.insert("SqlMapMapper.insertHistory", historyVo);
			}
			if (lampVo.getLamp_failure().equals("3")) {
				historyVo.setFailure_reason_id(7);
				historyVo.setFailure_reason_text("램프 안정기 고장");
				historyVo.setFailure_type("3");
				historyVo.setRepair("1");
				historyVo.setRecent("1");
				if (session.selectList("SqlMapMapper.selectHistory", historyVo).isEmpty())
					session.insert("SqlMapMapper.insertHistory", historyVo);
			}

			if (lampVo.getLamp_state().equals("2")) {
				historyVo.setFailure_reason_id(8);
				historyVo.setFailure_reason_text("강제소등");
				historyVo.setFailure_type("4");
				historyVo.setRepair("1");
				historyVo.setRecent("1");
				if (session.selectList("SqlMapMapper.selectHistory", historyVo).isEmpty())
					session.insert("SqlMapMapper.insertHistory", historyVo);
			}
			if (lampVo.getLamp_state().equals("3")) {
				historyVo.setFailure_reason_id(9);
				historyVo.setFailure_reason_text("강제점등");
				historyVo.setFailure_type("4");
				historyVo.setRepair("1");
				historyVo.setRecent("1");
				if (session.selectList("SqlMapMapper.selectHistory", historyVo).isEmpty())
					session.insert("SqlMapMapper.insertHistory", historyVo);
			}

		} finally {

			session.commit();
			session.close();
			bt.getInstance().onMessage("보안등이 추가 되었습니다.", null);
		}

	}

	@RequestMapping("/updateLamp")
	public void updateLamp(HttpServletRequest request, HttpServletResponse response) throws IOException {
		SqlSession session = MyBatisSessionFactory.getSqlSession();
		// request 받기
		request.setCharacterEncoding("UTF-8");
		BroadMainSocket bt = new BroadMainSocket();
		MakeDateTimeFormat makeDateTimeFormat = new MakeDateTimeFormat();
		LampVo lampVo = new LampVo();
		HistoryVo historyVo = new HistoryVo();
		JsonFactory fac = new JsonFactory();
		String json = fac.readJSONStringFromRequestBody(request);

		try {
			JSONParser jsonParser = new JSONParser();
			// JSON데이터를 넣어 JSON Object 로 만들어 준다.
			JSONObject jsonObject = (JSONObject) jsonParser.parse(json);
			// books의 배열을 추출
			JSONArray lampInfoArray = (JSONArray) jsonObject.get("LampVo");
			for (int i = 0; i < lampInfoArray.size(); i++) {
				// 배열 안에 있는것도 JSON형식 이기 때문에 JSON Object 로 추출
				JSONObject lampVoObject = (JSONObject) lampInfoArray.get(i);
				// JSON name으로 추출
				lampVo.setId(Integer.parseInt(lampVoObject.get("id").toString()));
				lampVo.setBeacon_addr(lampVoObject.get("beacon_addr").toString());
				lampVo.setBeacon_id(lampVoObject.get("beacon_id").toString());
				lampVo.setLocation(lampVoObject.get("location").toString());
				lampVo.setDate_time(makeDateTimeFormat.exceptStrDateTime(lampVoObject.get("date_time").toString()));
				lampVo.setPower_off(lampVoObject.get("power_off").toString());
				lampVo.setAbnormal_blink(lampVoObject.get("abnormal_blink").toString());
				lampVo.setShort_circuit(lampVoObject.get("short_circuit").toString());
				lampVo.setLamp_failure(lampVoObject.get("lamp_failure").toString());
				lampVo.setLamp_state(lampVoObject.get("lamp_state").toString());
				lampVo.setIllumination(lampVoObject.get("illumination").toString());
				lampVo.setX(Double.parseDouble(lampVoObject.get("x").toString()));
				lampVo.setY(Double.parseDouble(lampVoObject.get("y").toString()));

				// history
				historyVo.setBeacon_addr(lampVoObject.get("beacon_addr").toString());
				historyVo.setBeacon_id(lampVoObject.get("beacon_id").toString());
				historyVo.setLocation(lampVoObject.get("location").toString());
				historyVo.setDate_time(makeDateTimeFormat.exceptStrDateTime(lampVoObject.get("date_time").toString()));
			}
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		try {
			session.update("SqlMapMapper.updateLamp", lampVo);
			if (lampVo.getPower_off().equals("1")) {
				historyVo.setFailure_reason_id(1);
				historyVo.setFailure_reason_text("정전");
				historyVo.setFailure_type("0");
				historyVo.setRepair("1");
				historyVo.setRecent("1");
				if (session.selectList("SqlMapMapper.selectHistory", historyVo).isEmpty())
					session.insert("SqlMapMapper.insertHistory", historyVo);
			}
			if (lampVo.getAbnormal_blink().equals("1")) {
				historyVo.setFailure_reason_id(2);
				historyVo.setFailure_reason_text("이상점등");
				historyVo.setFailure_type("1");
				historyVo.setRepair("1");
				historyVo.setRecent("1");
				if (session.selectList("SqlMapMapper.selectHistory", historyVo).isEmpty())
					session.insert("SqlMapMapper.insertHistory", historyVo);
			}
			if (lampVo.getAbnormal_blink().equals("2")) {
				historyVo.setFailure_reason_id(3);
				historyVo.setFailure_reason_text("이상소등");
				historyVo.setFailure_type("1");
				historyVo.setRepair("1");
				historyVo.setRecent("1");
				if (session.selectList("SqlMapMapper.selectHistory", historyVo).isEmpty())
					session.insert("SqlMapMapper.insertHistory", historyVo);
			}
			if (lampVo.getShort_circuit().equals("1")) {
				historyVo.setFailure_reason_id(4);
				historyVo.setFailure_reason_text("누전");
				historyVo.setFailure_type("2");
				historyVo.setRepair("1");
				historyVo.setRecent("1");
				if (session.selectList("SqlMapMapper.selectHistory", historyVo).isEmpty())
					session.insert("SqlMapMapper.insertHistory", historyVo);
			}
			if (lampVo.getLamp_failure().equals("1")) {
				historyVo.setFailure_reason_id(5);
				historyVo.setFailure_reason_text("램프고장");
				historyVo.setFailure_type("3");
				historyVo.setRepair("1");
				historyVo.setRecent("1");
				if (session.selectList("SqlMapMapper.selectHistory", historyVo).isEmpty())
					session.insert("SqlMapMapper.insertHistory", historyVo);
			}
			if (lampVo.getLamp_failure().equals("2")) {
				historyVo.setFailure_reason_id(6);
				historyVo.setFailure_reason_text("안정기 고장");
				historyVo.setFailure_type("3");
				historyVo.setRepair("1");
				historyVo.setRecent("1");
				if (session.selectList("SqlMapMapper.selectHistory", historyVo).isEmpty())
					session.insert("SqlMapMapper.insertHistory", historyVo);
			}
			if (lampVo.getLamp_failure().equals("3")) {
				historyVo.setFailure_reason_id(7);
				historyVo.setFailure_reason_text("램프 안정기 고장");
				historyVo.setFailure_type("3");
				historyVo.setRepair("1");
				historyVo.setRecent("1");
				if (session.selectList("SqlMapMapper.selectHistory", historyVo).isEmpty())
					session.insert("SqlMapMapper.insertHistory", historyVo);
			}

			if (lampVo.getLamp_state().equals("2")) {
				historyVo.setFailure_reason_id(8);
				historyVo.setFailure_reason_text("강제소등");
				historyVo.setFailure_type("4");
				historyVo.setRepair("1");
				historyVo.setRecent("1");
				if (session.selectList("SqlMapMapper.selectHistory", historyVo).isEmpty())
					session.insert("SqlMapMapper.insertHistory", historyVo);
			}
			if (lampVo.getLamp_state().equals("3")) {
				historyVo.setFailure_reason_id(9);
				historyVo.setFailure_reason_text("강제점등");
				historyVo.setFailure_type("4");
				historyVo.setRepair("1");
				historyVo.setRecent("1");
				if (session.selectList("SqlMapMapper.selectHistory", historyVo).isEmpty())
					session.insert("SqlMapMapper.insertHistory", historyVo);
			}

			if (lampVo.getPower_off().equals("0")) {
				historyVo.setFailure_type("0");
				historyVo.setRepair("1");
				historyVo.setRecent("1");
				if (!session.selectList("SqlMapMapper.checkHistory", historyVo).isEmpty()) {
					List<HistoryVo> list = session.selectList("SqlMapMapper.checkHistory", historyVo);
					for (int i = 0; i < list.size(); i++) {
						historyVo = list.get(i);
						historyVo.setRepair_date_time(lampVo.getDate_time().toString());
						historyVo.setRepair("0");
						historyVo.setRecent("0");
						session.update("SqlMapMapper.updateHistory", historyVo);
					}
				}
			}

			if (lampVo.getAbnormal_blink().equals("0")) {
				historyVo.setFailure_type("1");
				historyVo.setRepair("1");
				historyVo.setRecent("1");
				if (!session.selectList("SqlMapMapper.checkHistory", historyVo).isEmpty()) {
					List<HistoryVo> list = session.selectList("SqlMapMapper.checkHistory", historyVo);
					for (int i = 0; i < list.size(); i++) {
						historyVo = list.get(i);
						historyVo.setRepair_date_time(lampVo.getDate_time().toString());
						historyVo.setRepair("0");
						historyVo.setRecent("0");
						session.update("SqlMapMapper.updateHistory", historyVo);
					}
				}
			}
			if (lampVo.getShort_circuit().equals("0")) {
				historyVo.setFailure_type("2");
				historyVo.setRepair("1");
				historyVo.setRecent("1");
				if (!session.selectList("SqlMapMapper.checkHistory", historyVo).isEmpty()) {
					List<HistoryVo> list = session.selectList("SqlMapMapper.checkHistory", historyVo);
					for (int i = 0; i < list.size(); i++) {
						historyVo = list.get(i);
						historyVo.setRepair_date_time(lampVo.getDate_time().toString());
						historyVo.setRepair("0");
						historyVo.setRecent("0");
						session.update("SqlMapMapper.updateHistory", historyVo);
					}
				}
			}

			if (lampVo.getLamp_failure().equals("0")) {
				historyVo.setFailure_type("3");
				historyVo.setRepair("1");
				historyVo.setRecent("1");
				if (!session.selectList("SqlMapMapper.checkHistory", historyVo).isEmpty()) {
					List<HistoryVo> list = session.selectList("SqlMapMapper.checkHistory", historyVo);
					for (int i = 0; i < list.size(); i++) {
						historyVo = list.get(i);
						historyVo.setRepair_date_time(lampVo.getDate_time().toString());
						historyVo.setRepair("0");
						historyVo.setRecent("0");
						session.update("SqlMapMapper.updateHistory", historyVo);
					}
				}
			}

			if (lampVo.getLamp_state().equals("0")) {
				historyVo.setFailure_type("4");
				historyVo.setRepair("1");
				historyVo.setRecent("1");
				if (!session.selectList("SqlMapMapper.checkHistory", historyVo).isEmpty()) {
					List<HistoryVo> list = session.selectList("SqlMapMapper.checkHistory", historyVo);
					for (int i = 0; i < list.size(); i++) {
						historyVo = list.get(i);
						historyVo.setRepair_date_time(lampVo.getDate_time().toString());
						historyVo.setRepair("0");
						historyVo.setRecent("0");
						session.update("SqlMapMapper.updateHistory", historyVo);
					}
				}
			}

		} finally {
			session.commit();
			session.close();
			bt.getInstance().onMessage("보안등이 수정 되었습니다.", null);
		}

	}

	@RequestMapping("/deleteLamp")
	public void deleteLamp(HttpServletRequest request, HttpServletResponse response) throws IOException {
		SqlSession session = MyBatisSessionFactory.getSqlSession();
		// request 받기
		request.setCharacterEncoding("UTF-8");
		BroadMainSocket bt = new BroadMainSocket();
		LampVo lampVo = new LampVo();
		JsonFactory fac = new JsonFactory();
		String json = fac.readJSONStringFromRequestBody(request);

		try {
			JSONParser jsonParser = new JSONParser();
			// JSON데이터를 넣어 JSON Object 로 만들어 준다.
			JSONObject jsonObject = (JSONObject) jsonParser.parse(json);
			// books의 배열을 추출
			JSONArray lampInfoArray = (JSONArray) jsonObject.get("LampVo");

			for (int i = 0; i < lampInfoArray.size(); i++) {

				// 배열 안에 있는것도 JSON형식 이기 때문에 JSON Object 로 추출
				JSONObject lampVoObject = (JSONObject) lampInfoArray.get(i);
				// JSON name으로 추출
				lampVo.setId(Integer.parseInt(lampVoObject.get("id").toString()));

			}

		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		try {

			session.delete("SqlMapMapper.deleteLamp", lampVo);
			bt.getInstance().onMessage("보안등이 삭제 되었습니다.", null);

		} finally {
			session.commit();
			session.close();
		}

	}
	
	@RequestMapping(value = "/selectNeedLocation")
	public @ResponseBody List<LampVo> getNeedLocation(HttpServletRequest request, HttpServletResponse response) {
		SqlSession session = MyBatisSessionFactory.getSqlSession();
		// request 받기
		JsonFactory fac = new JsonFactory();
		String json = fac.readJSONStringFromRequestBody(request);
		MakeDateTimeFormat makeDateTimeFormat = new MakeDateTimeFormat();

		HashMap map = new HashMap<String, Object>();
		List<LampVo> list = session.selectList("SqlMapMapper.selectNeedLocation", map);
		session.close();
		return list;
	}

	

}
