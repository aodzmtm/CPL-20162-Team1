package light.controller;

import java.io.IOException;
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

import light.dateFormat.MakeDateTimeFormat;
import light.common.JsonFactory;
import light.database.MyBatisSessionFactory;
import light.vo.HistoryVo;
import light.vo.LampVo;
import light.vo.NeedLocationVo;
import light.webSocket.BroadMainSocket;

@Controller
public class MobileController {
	
//��پ� ������Ʈ �˰��� �Դϴ�. �̰� ����ϸ� �ɰ̴ϴ�.
	
	@RequestMapping("/mobileUpdateLamp")
	public void updateLamp(HttpServletRequest request, HttpServletResponse response) throws IOException {
		SqlSession session = MyBatisSessionFactory.getSqlSession();
		// request �ޱ�
		request.setCharacterEncoding("UTF-8");
		BroadMainSocket bt = new BroadMainSocket();
		MakeDateTimeFormat makeDateTimeFormat = new MakeDateTimeFormat();
		LampVo lampVo = new LampVo();
		HistoryVo historyVo = new HistoryVo();
		JsonFactory fac = new JsonFactory();
		String json = fac.readJSONStringFromRequestBody(request);

		try {
			JSONParser jsonParser = new JSONParser();
			// JSON�����͸� �־� JSON Object �� ����� �ش�.
			JSONObject jsonObject = (JSONObject) jsonParser.parse(json);
			// books�� �迭�� ����
			JSONArray lampInfoArray = (JSONArray) jsonObject.get("LampVo");
			for (int i = 0; i < lampInfoArray.size(); i++) {
				// �迭 �ȿ� �ִ°͵� JSON���� �̱� ������ JSON Object �� ����
				JSONObject lampVoObject = (JSONObject) lampInfoArray.get(i);
				// JSON name���� ����
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
			session.update("SqlMobileMapper.updateLamp", lampVo);
			if (lampVo.getPower_off().equals("1")) {
				historyVo.setFailure_reason_id(1);
				historyVo.setFailure_reason_text("����");
				historyVo.setFailure_type("0");
				historyVo.setRepair("1");
				historyVo.setRecent("1");
				if (session.selectList("SqlMobileMapper.selectHistory", historyVo).isEmpty())
					session.insert("SqlMobileMapper.insertHistory", historyVo);
			}
			if (lampVo.getAbnormal_blink().equals("1")) {
				historyVo.setFailure_reason_id(2);
				historyVo.setFailure_reason_text("�̻�����");
				historyVo.setFailure_type("1");
				historyVo.setRepair("1");
				historyVo.setRecent("1");
				if (session.selectList("SqlMobileMapper.selectHistory", historyVo).isEmpty())
					session.insert("SqlMobileMapper.insertHistory", historyVo);
			}
			if (lampVo.getAbnormal_blink().equals("2")) {
				historyVo.setFailure_reason_id(3);
				historyVo.setFailure_reason_text("�̻�ҵ�");
				historyVo.setFailure_type("1");
				historyVo.setRepair("1");
				historyVo.setRecent("1");
				if (session.selectList("SqlMobileMapper.selectHistory", historyVo).isEmpty())
					session.insert("SqlMobileMapper.insertHistory", historyVo);
			}
			if (lampVo.getShort_circuit().equals("1")) {
				historyVo.setFailure_reason_id(4);
				historyVo.setFailure_reason_text("����");
				historyVo.setFailure_type("2");
				historyVo.setRepair("1");
				historyVo.setRecent("1");
				if (session.selectList("SqlMobileMapper.selectHistory", historyVo).isEmpty())
					session.insert("SqlMobileMapper.insertHistory", historyVo);
			}
			if (lampVo.getLamp_failure().equals("1")) {
				historyVo.setFailure_reason_id(5);
				historyVo.setFailure_reason_text("��������");
				historyVo.setFailure_type("3");
				historyVo.setRepair("1");
				historyVo.setRecent("1");
				if (session.selectList("SqlMobileMapper.selectHistory", historyVo).isEmpty())
					session.insert("SqlMobileMapper.insertHistory", historyVo);
			}
			if (lampVo.getLamp_failure().equals("2")) {
				historyVo.setFailure_reason_id(6);
				historyVo.setFailure_reason_text("������ ����");
				historyVo.setFailure_type("3");
				historyVo.setRepair("1");
				historyVo.setRecent("1");
				if (session.selectList("SqlMobileMapper.selectHistory", historyVo).isEmpty())
					session.insert("SqlMobileMapper.insertHistory", historyVo);
			}
			if (lampVo.getLamp_failure().equals("3")) {
				historyVo.setFailure_reason_id(7);
				historyVo.setFailure_reason_text("���� ������ ����");
				historyVo.setFailure_type("3");
				historyVo.setRepair("1");
				historyVo.setRecent("1");
				if (session.selectList("SqlMobileMapper.selectHistory", historyVo).isEmpty())
					session.insert("SqlMobileMapper.insertHistory", historyVo);
			}

			if (lampVo.getLamp_state().equals("2")) {
				historyVo.setFailure_reason_id(8);
				historyVo.setFailure_reason_text("�����ҵ�");
				historyVo.setFailure_type("4");
				historyVo.setRepair("1");
				historyVo.setRecent("1");
				if (session.selectList("SqlMobileMapper.selectHistory", historyVo).isEmpty())
					session.insert("SqlMobileMapper.insertHistory", historyVo);
			}
			if (lampVo.getLamp_state().equals("3")) {
				historyVo.setFailure_reason_id(9);
				historyVo.setFailure_reason_text("��������");
				historyVo.setFailure_type("4");
				historyVo.setRepair("1");
				historyVo.setRecent("1");
				if (session.selectList("SqlMobileMapper.selectHistory", historyVo).isEmpty())
					session.insert("SqlMobileMapper.insertHistory", historyVo);
			}

			if (lampVo.getPower_off().equals("0")) {
				historyVo.setFailure_type("0");
				historyVo.setRepair("1");
				historyVo.setRecent("1");
				if (!session.selectList("SqlMobileMapper.checkHistory", historyVo).isEmpty()) {
					List<HistoryVo> list = session.selectList("SqlMobileMapper.checkHistory", historyVo);
					for (int i = 0; i < list.size(); i++) {
						historyVo = list.get(i);
						historyVo.setRepair_date_time(lampVo.getDate_time().toString());
						historyVo.setRepair("0");
						historyVo.setRecent("0");
						session.update("SqlMobileMapper.updateHistory", historyVo);
					}
				}
			}

			if (lampVo.getAbnormal_blink().equals("0")) {
				historyVo.setFailure_type("1");
				historyVo.setRepair("1");
				historyVo.setRecent("1");
				if (!session.selectList("SqlMobileMapper.checkHistory", historyVo).isEmpty()) {
					List<HistoryVo> list = session.selectList("SqlMobileMapper.checkHistory", historyVo);
					for (int i = 0; i < list.size(); i++) {
						historyVo = list.get(i);
						historyVo.setRepair_date_time(lampVo.getDate_time().toString());
						historyVo.setRepair("0");
						historyVo.setRecent("0");
						session.update("SqlMobileMapper.updateHistory", historyVo);
					}
				}
			}
			if (lampVo.getShort_circuit().equals("0")) {
				historyVo.setFailure_type("2");
				historyVo.setRepair("1");
				historyVo.setRecent("1");
				if (!session.selectList("SqlMobileMapper.checkHistory", historyVo).isEmpty()) {
					List<HistoryVo> list = session.selectList("SqlMobileMapper.checkHistory", historyVo);
					for (int i = 0; i < list.size(); i++) {
						historyVo = list.get(i);
						historyVo.setRepair_date_time(lampVo.getDate_time().toString());
						historyVo.setRepair("0");
						historyVo.setRecent("0");
						session.update("SqlMobileMapper.updateHistory", historyVo);
					}
				}
			}

			if (lampVo.getLamp_failure().equals("0")) {
				historyVo.setFailure_type("3");
				historyVo.setRepair("1");
				historyVo.setRecent("1");
				if (!session.selectList("SqlMobileMapper.checkHistory", historyVo).isEmpty()) {
					List<HistoryVo> list = session.selectList("SqlMobileMapper.checkHistory", historyVo);
					for (int i = 0; i < list.size(); i++) {
						historyVo = list.get(i);
						historyVo.setRepair_date_time(lampVo.getDate_time().toString());
						historyVo.setRepair("0");
						historyVo.setRecent("0");
						session.update("SqlMobileMapper.updateHistory", historyVo);
					}
				}
			}

			if (lampVo.getLamp_state().equals("0")) {
				historyVo.setFailure_type("4");
				historyVo.setRepair("1");
				historyVo.setRecent("1");
				if (!session.selectList("SqlMobileMapper.checkHistory", historyVo).isEmpty()) {
					List<HistoryVo> list = session.selectList("SqlMobileMapper.checkHistory", historyVo);
					for (int i = 0; i < list.size(); i++) {
						historyVo = list.get(i);
						historyVo.setRepair_date_time(lampVo.getDate_time().toString());
						historyVo.setRepair("0");
						historyVo.setRecent("0");
						session.update("SqlMobileMapper.updateHistory", historyVo);
					}
				}
			}

		} finally {
			session.commit();
			session.close();
			bt.getInstance().onMessage("���ȵ��� ���� �Ǿ����ϴ�.", null);
		}

	}
	
	@RequestMapping("/mobileDanger")
	public void mobileDanger(HttpServletRequest request, HttpServletResponse response) throws IOException{
		SqlSession session = MyBatisSessionFactory.getSqlSession();
		request.setCharacterEncoding("UTF-8");
		System.out.println("1111");
		double x = Double.parseDouble(request.getParameter("x"));
		double y = Double.parseDouble(request.getParameter("y"));
		
		NeedLocationVo needLocationVo = new NeedLocationVo();
		needLocationVo.setX(x);
		needLocationVo.setY(y);
		
		System.out.println(x);
		System.out.println(y);
		
//		try{
//			session.insert("SqlMobileMapper.insertNeedLocation", needLocationVo);
//		}
//		finally {
//			session.commit();
//			session.close();
//		}
	}
	
}
