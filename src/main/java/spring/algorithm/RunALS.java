package spring.algorithm;

import java.io.IOException;

import javax.servlet.ServletException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
//import org.springframework.web.bind.annotation.RestController;
import spring.config.HadoopConfig;

/**
 * Servlet implementation class RunALS
 */
@Controller
public class RunALS {

	@Autowired
	private HadoopConfig hadoopConfig;

	@Autowired
	private RunSpark runSpark;

	@RequestMapping(value = "/runAls",method = RequestMethod.POST)
	public @ResponseBody String runAls(
			@RequestParam(value = "input",required = true)String input,
			@RequestParam(value = "trainPercent",required = true)String trainPercent,
			@RequestParam(value = "ranks",required = true)String ranks,
			@RequestParam(value = "lambda",required = true)String lambda,
			@RequestParam(value = "iterations",required = true)String iterations

			) throws ServletException, IOException {
//		<input> <output> <train_percent> <ranks> <lambda> <iteration>
		String output = hadoopConfig.getOutputData();

		String appId ="null";
		try{
			// 启动任务, 启动成功后返回任务appId
			appId = runSpark.runALS(input, output, trainPercent, ranks, lambda, iterations);
		}catch(Exception e){
			appId = "null";
			e.printStackTrace();
		}
		// 返回字符串
		return  appId == null ? "null" : appId;
	}


}
