package spring.algorithm;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
//import org.springframework.web.bind.annotation.RestController;
import spring.util.Utils;

/**
 * Servlet implementation class Monitor
 */
@Controller
public class Monitor {
	private Logger log = LoggerFactory.getLogger(getClass());



	@RequestMapping(value = "/monitor", method = RequestMethod.POST)
	public @ResponseBody  String monitor(
			@RequestParam(value = "APPID",required = true)String appId
	)  {
//		String appId = request.getParameter("APPID");
		log.info("appId:"+appId);
		String progress = Utils.getAppStatus(appId);
		log.info("appId:"+appId+",进度："+progress);
		return progress;
	}


}
