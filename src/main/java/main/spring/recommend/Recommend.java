package main.spring.recommend;

import java.lang.reflect.InvocationTargetException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import main.spring.model.Movie;
import org.springframework.stereotype.Controller;
import main.spring.util.Utils;

/**
 * Servlet implementation class Recommend
 */
@Controller
public class Recommend  {

	@Autowired
	private Utils utils;

	@RequestMapping("/recommend")
	public @ResponseBody String recommend(
			@RequestParam(value = "userId",required = true)String uid,
			@RequestParam(value = "flag",required = true)String flag,
			@RequestParam(value = "recommendNum",required = false,defaultValue = "10")String recNum
	){
//		String uid = request.getParameter("userId");
//		String flag = request.getParameter("flag");// flag : recommend/check
//		String recNum =null;
		
		List<Movie> rec =null;
		if("recommend".equals(flag)){
//			recNum = request.getParameter("recommendNum");
			try {
				rec= utils.predict(Integer.parseInt(uid),Integer.parseInt(recNum));
			} catch (NumberFormatException | IllegalAccessException | InstantiationException | InvocationTargetException
					| NoSuchMethodException e) {
				e.printStackTrace();
			}
		}else{
			rec = utils.check(Integer.parseInt(uid));
		}
		StringBuffer buffer = new StringBuffer();
		if("check".equals(flag)){
			buffer.append(rec==null?"0":rec.size()).append("::");
		}
		if(rec!= null && rec.size()>0) {
			for (Movie re : rec) {
				buffer.append("<tr>")
						.append("<td>").append(re.getId()).append("</td>")
						.append("<td>").append(re.getTitle()).append("</td>")
						.append("<td>").append(re.getGenres()).append("</td>")
						.append("<td>").append(re.getRated()).append("</td>")
						.append("</tr>");
			}
		}
		
		// 打印输出
//		PrintWriter out = response.getWriter();
//		if(buffer.length()<=0){
//			out.write("");
//		}else{
//			out.write(buffer.toString());
//		}
//		out.flush();

		return buffer.length()<0 ? "" : buffer.toString();
	}



}
