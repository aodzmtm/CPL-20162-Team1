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

@Controller
public class GraphController {
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/lampInfoGraphJson")
	public @ResponseBody JSONObject getlampInfoGraph(HttpServletRequest request, HttpServletResponse response)
			throws IOException {
		SqlSession session = MyBatisSessionFactory.getSqlSession();
		List<HistoryVo> list = new ArrayList<HistoryVo>();
		JSONObject jsonObj = new JSONObject();
		MakeDateTimeFormat makeDateTimeFormat = new MakeDateTimeFormat();

		list = session.selectList("SqlGraphMapper.selectHistory");
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
}
