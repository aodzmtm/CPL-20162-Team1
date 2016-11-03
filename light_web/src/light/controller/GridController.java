package light.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.ibatis.session.SqlSession;
import org.json.simple.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import light.dateFormat.MakeDateTimeFormat;
import light.database.MyBatisSessionFactory;
import light.vo.HistoryVo;
import light.vo.LampVo;
import light.webSocket.BroadMainSocket;

@Controller
public class GridController {
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/lampInfoGridJson")
	public @ResponseBody JSONObject getlampInfoGrid(HttpServletRequest request, HttpServletResponse response)
			throws IOException {
		SqlSession session = MyBatisSessionFactory.getSqlSession();
		JSONObject jsonObj = new JSONObject();
		MakeDateTimeFormat makeDateTimeFormat = new MakeDateTimeFormat();

		List<LampVo> list = session.selectList("SqlGridMapper.selectLamp");
		// 날짜 바꾸기
		for (int i = 0; i < list.size(); i++) {
			LampVo lampVo = new LampVo();
			lampVo = list.get(i);
			if (list.get(i).getDate_time() != null) {
				String dateTime = list.get(i).getDate_time().toString();
				String makeDateTime = makeDateTimeFormat.addStrDateTime(dateTime);
				lampVo.setDate_time(makeDateTime);
				list.set(i, lampVo);
			}
		}
		session.close();
		jsonObj.put("rows", list);
		return jsonObj;
	}

	@RequestMapping("/lampInfoGridDelete")
	public @ResponseBody boolean lampInfoGridDelete(HttpServletRequest request, HttpServletResponse response)
			throws IOException {
		SqlSession session = MyBatisSessionFactory.getSqlSession();
		BroadMainSocket bt = new BroadMainSocket();
		LampVo lampVo = new LampVo();
		String idstr = request.getParameter("idstr");
		System.out.println(idstr);
		String[] idstrs = idstr.split("/");
		
		try {
			for (int i = 0; i < idstrs.length; i++) {
				int id = Integer.parseInt(idstrs[i]);
				lampVo.setId(id);
				List<LampVo> list = session.selectList("SqlGridMapper.selectLamp",lampVo);
				session.delete("SqlGridMapper.deleteLamp", lampVo);
				bt.getInstance().onMessage(list.get(0).getLocation()+" 보안등이 삭제 되었습니다.", null);
			}
		} finally {
			session.commit();
			session.close();
			
		}
		return true;
	}

	@RequestMapping("/lampInfoGridEdit")
	public void lampInfoGridEdit(HttpServletRequest request, HttpServletResponse response) throws IOException {
		SqlSession session = MyBatisSessionFactory.getSqlSession();
		BroadMainSocket bt = new BroadMainSocket();
		MakeDateTimeFormat makeDateTimeFormat = new MakeDateTimeFormat();
		int id = Integer.parseInt(request.getParameter("id"));
		String beacon_addr = request.getParameter("beacon_addr");
		String beacon_id = request.getParameter("beacon_id");
		String location = request.getParameter("location");
		String date_time = request.getParameter("date_time");
		String power_off = request.getParameter("power_off");
		String abnormal_blink = request.getParameter("abnormal_blink");
		String short_circuit = request.getParameter("short_circuit");
		String lamp_failure = request.getParameter("lamp_failure");
		String lamp_state = request.getParameter("lamp_state");

		LampVo lampVo = new LampVo();
		HistoryVo historyVo = new HistoryVo();
		lampVo.setId(id);

		// history
		try {
			lampVo.setBeacon_addr(beacon_addr);
			lampVo.setBeacon_id(beacon_id);
			lampVo.setLocation(location);
			lampVo.setDate_time(makeDateTimeFormat.exceptStrDateTime(date_time));
			lampVo.setPower_off(power_off);
			lampVo.setAbnormal_blink(abnormal_blink);
			lampVo.setShort_circuit(short_circuit);
			lampVo.setLamp_failure(lamp_failure);
			lampVo.setLamp_state(lamp_state);
			historyVo.setBeacon_addr(lampVo.getBeacon_addr().toString());
			historyVo.setBeacon_id(lampVo.getBeacon_id().toString());
			historyVo.setLocation(lampVo.getLocation().toString());
			historyVo.setDate_time(lampVo.getDate_time().toString());
		} catch (Exception e) {
			e.printStackTrace();
		}

		try {
			if (beacon_id != null) {
				session.update("SqlGridMapper.updateLamp", lampVo);

				/*
				 * if (lampVo.getPower_off().equals("0")) {
				 * historyVo.setFailure_reason_id(0);
				 * historyVo.setFailure_reason_text("정상");
				 * historyVo.setRepair("1"); historyVo.setRecent("1"); if
				 * (session.selectList("SqlGridMapper.selectHistory",
				 * historyVo).isEmpty())
				 * session.insert("SqlGridMapper.insertHistory", historyVo); }
				 */
				if (lampVo.getPower_off().equals("1")) {
					historyVo.setFailure_reason_id(1);
					historyVo.setFailure_reason_text("정전");
					historyVo.setFailure_type("0");
					historyVo.setRepair("1");
					historyVo.setRecent("1");
					if (session.selectList("SqlGridMapper.selectHistory", historyVo).isEmpty())
						session.insert("SqlGridMapper.insertHistory", historyVo);
				}
				if (lampVo.getAbnormal_blink().equals("1")) {
					historyVo.setFailure_reason_id(2);
					historyVo.setFailure_reason_text("이상점등");
					historyVo.setFailure_type("1");
					historyVo.setRepair("1");
					historyVo.setRecent("1");
					if (session.selectList("SqlGridMapper.selectHistory", historyVo).isEmpty())
						session.insert("SqlGridMapper.insertHistory", historyVo);
				}
				if (lampVo.getAbnormal_blink().equals("2")) {
					historyVo.setFailure_reason_id(3);
					historyVo.setFailure_reason_text("이상소등");
					historyVo.setFailure_type("1");
					historyVo.setRepair("1");
					historyVo.setRecent("1");
					if (session.selectList("SqlGridMapper.selectHistory", historyVo).isEmpty())
						session.insert("SqlGridMapper.insertHistory", historyVo);
				}
				if (lampVo.getShort_circuit().equals("1")) {
					historyVo.setFailure_reason_id(4);
					historyVo.setFailure_reason_text("누전");
					historyVo.setFailure_type("2");
					historyVo.setRepair("1");
					historyVo.setRecent("1");
					if (session.selectList("SqlGridMapper.selectHistory", historyVo).isEmpty())
						session.insert("SqlGridMapper.insertHistory", historyVo);
				}
				if (lampVo.getLamp_failure().equals("1")) {
					historyVo.setFailure_reason_id(5);
					historyVo.setFailure_reason_text("램프고장");
					historyVo.setFailure_type("3");
					historyVo.setRepair("1");
					historyVo.setRecent("1");
					if (session.selectList("SqlGridMapper.selectHistory", historyVo).isEmpty())
						session.insert("SqlGridMapper.insertHistory", historyVo);
				}
				if (lampVo.getLamp_failure().equals("2")) {
					historyVo.setFailure_reason_id(6);
					historyVo.setFailure_reason_text("안정기 고장");
					historyVo.setFailure_type("3");
					historyVo.setRepair("1");
					historyVo.setRecent("1");
					if (session.selectList("SqlGridMapper.selectHistory", historyVo).isEmpty())
						session.insert("SqlGridMapper.insertHistory", historyVo);
				}
				if (lampVo.getLamp_failure().equals("3")) {
					historyVo.setFailure_reason_id(7);
					historyVo.setFailure_reason_text("램프 안정기 고장");
					historyVo.setFailure_type("3");
					historyVo.setRepair("1");
					historyVo.setRecent("1");
					if (session.selectList("SqlGridMapper.selectHistory", historyVo).isEmpty())
						session.insert("SqlGridMapper.insertHistory", historyVo);
				}

				if (lampVo.getLamp_state().equals("2")) {
					historyVo.setFailure_reason_id(8);
					historyVo.setFailure_reason_text("강제소등");
					historyVo.setFailure_type("4");
					historyVo.setRepair("1");
					historyVo.setRecent("1");
					if (session.selectList("SqlGridMapper.selectHistory", historyVo).isEmpty())
						session.insert("SqlGridMapper.insertHistory", historyVo);
				}
				if (lampVo.getLamp_state().equals("3")) {
					historyVo.setFailure_reason_id(9);
					historyVo.setFailure_reason_text("강제점등");
					historyVo.setFailure_type("4");
					historyVo.setRepair("1");
					historyVo.setRecent("1");
					if (session.selectList("SqlGridMapper.selectHistory", historyVo).isEmpty())
						session.insert("SqlGridMapper.insertHistory", historyVo);
				}

				if (lampVo.getPower_off().equals("0")) {
					historyVo.setFailure_type("0");
					historyVo.setRepair("1");
					historyVo.setRecent("1");
					if (!session.selectList("SqlGridMapper.checkHistory", historyVo).isEmpty()) {
						List<HistoryVo> list = session.selectList("SqlGridMapper.checkHistory", historyVo);
						for (int i = 0; i < list.size(); i++) {
							historyVo = list.get(i);
							historyVo.setRepair_date_time(lampVo.getDate_time().toString());
							historyVo.setRepair("0");
							historyVo.setRecent("0");
							session.update("SqlGridMapper.updateHistory", historyVo);
						}
					}
				}

				if (lampVo.getAbnormal_blink().equals("0")) {
					historyVo.setFailure_type("1");
					historyVo.setRepair("1");
					historyVo.setRecent("1");
					if (!session.selectList("SqlGridMapper.checkHistory", historyVo).isEmpty()) {
						List<HistoryVo> list = session.selectList("SqlGridMapper.checkHistory", historyVo);
						for (int i = 0; i < list.size(); i++) {
							historyVo = list.get(i);
							historyVo.setRepair_date_time(lampVo.getDate_time().toString());
							historyVo.setRepair("0");
							historyVo.setRecent("0");
							session.update("SqlGridMapper.updateHistory", historyVo);
						}
					}
				}
				if (lampVo.getShort_circuit().equals("0")) {
					historyVo.setFailure_type("2");
					historyVo.setRepair("1");
					historyVo.setRecent("1");
					if (!session.selectList("SqlGridMapper.checkHistory", historyVo).isEmpty()) {
						List<HistoryVo> list = session.selectList("SqlGridMapper.checkHistory", historyVo);
						for (int i = 0; i < list.size(); i++) {
							historyVo = list.get(i);
							historyVo.setRepair_date_time(lampVo.getDate_time().toString());
							historyVo.setRepair("0");
							historyVo.setRecent("0");
							session.update("SqlGridMapper.updateHistory", historyVo);
						}
					}
				}

				if (lampVo.getLamp_failure().equals("0")) {
					historyVo.setFailure_type("3");
					historyVo.setRepair("1");
					historyVo.setRecent("1");
					if (!session.selectList("SqlGridMapper.checkHistory", historyVo).isEmpty()) {
						List<HistoryVo> list = session.selectList("SqlGridMapper.checkHistory", historyVo);
						for (int i = 0; i < list.size(); i++) {
							historyVo = list.get(i);
							historyVo.setRepair_date_time(lampVo.getDate_time().toString());
							historyVo.setRepair("0");
							historyVo.setRecent("0");
							session.update("SqlGridMapper.updateHistory", historyVo);
						}
					}
				}

				if (lampVo.getLamp_state().equals("0")) {
					historyVo.setFailure_type("4");
					historyVo.setRepair("1");
					historyVo.setRecent("1");
					if (!session.selectList("SqlGridMapper.checkHistory", historyVo).isEmpty()) {
						List<HistoryVo> list = session.selectList("SqlGridMapper.checkHistory", historyVo);
						for (int i = 0; i < list.size(); i++) {
							historyVo = list.get(i);
							historyVo.setRepair_date_time(lampVo.getDate_time().toString());
							historyVo.setRepair("0");
							historyVo.setRecent("0");
							session.update("SqlGridMapper.updateHistory", historyVo);
						}
					}
				}

				bt.getInstance().onMessage(lampVo.getLocation()+" 보안등이 수정 되었습니다.", null);
			} else {
				session.delete("SqlGridMapper.deleteLamp", lampVo);
				bt.getInstance().onMessage(lampVo.getLocation()+" 보안등이 삭제 되었습니다.", null);
			}
		} finally {
			session.commit();
			session.close();
		}

	}

	//////////////////////////////
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/historyInfoGridJson")
	public @ResponseBody JSONObject getHistoryInfoGrid(HttpServletRequest request, HttpServletResponse response)
			throws IOException {
		SqlSession session = MyBatisSessionFactory.getSqlSession();
		List<HistoryVo> list = new ArrayList<HistoryVo>();
		JSONObject jsonObj = new JSONObject();
		MakeDateTimeFormat makeDateTimeFormat = new MakeDateTimeFormat();

		list = session.selectList("SqlGridMapper.selectHistory");
		// 날짜 바꾸기
		for (int i = 0; i < list.size(); i++) {
			HistoryVo historyVo = new HistoryVo();
			historyVo = list.get(i);
			String makeRepairDateTime = "";
			String makeDateTime = "";
			String dateTime = "";
			String repairDateTime = "";
			try {
				if (list.get(i).getDate_time() != null) {
					dateTime = list.get(i).getDate_time().toString();

				}

				if (list.get(i).getRepair_date_time() != null) {

					repairDateTime = list.get(i).getRepair_date_time().toString();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			makeRepairDateTime = makeDateTimeFormat.addStrDateTime(repairDateTime);
			makeDateTime = makeDateTimeFormat.addStrDateTime(dateTime);
			historyVo.setRepair_date_time(makeRepairDateTime);
			historyVo.setDate_time(makeDateTime);
			list.set(i, historyVo);
		}
		session.close();
		jsonObj.put("rows", list);
		return jsonObj;
	}

	@RequestMapping("/historyInfoGridDelete")
	public @ResponseBody boolean historyInfoGridDelete(HttpServletRequest request, HttpServletResponse response)
			throws IOException {
		SqlSession session = MyBatisSessionFactory.getSqlSession();
		BroadMainSocket bt = new BroadMainSocket();
		HistoryVo historyVo = new HistoryVo();
		String idstr = request.getParameter("idstr");
		System.out.println(idstr);
		String[] idstrs = idstr.split("/");

		try {
			for (int i = 0; i < idstrs.length; i++) {
				int id = Integer.parseInt(idstrs[i]);
				historyVo.setId(id);
				session.delete("SqlGridMapper.deleteHistory", historyVo);

			}
		} finally {
			session.commit();
			session.close();
			bt.getInstance().onMessage(" 보안등 고장내역 및 수리내역이 삭제 되었습니다.", null);
		}
		return true;
	}

	@RequestMapping("/historyInfoGridEdit")
	public void historyInfoGridEdit(HttpServletRequest request, HttpServletResponse response) throws IOException {
		SqlSession session = MyBatisSessionFactory.getSqlSession();
		BroadMainSocket bt = new BroadMainSocket();
		int id = Integer.parseInt(request.getParameter("id"));
		String repair = request.getParameter("repair");

		HistoryVo historyVo = new HistoryVo();
		historyVo.setId(id);

		// history
		try {
			historyVo.setRepair(repair);
			historyVo.setRecent("0");
		} catch (Exception e) {
			e.printStackTrace();
		}

		try {
			if (repair != null) {
				session.update("SqlGridMapper.updateHistory", historyVo);
				bt.getInstance().onMessage(" 보안등 고장내역 및 수리내역이 수정 되었습니다.", null);
			} else {
				session.delete("SqlGridMapper.deleteHistory", historyVo);
				bt.getInstance().onMessage(" 보안등 고장내역 및 수리내역이 삭제되었습니다.", null);
			}
		} finally {
			session.commit();
			session.close();
		}

	}
}
