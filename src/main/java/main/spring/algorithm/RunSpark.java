package main.spring.algorithm;

import java.io.IOException;

import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import main.spring.config.HadoopConfig;
import main.spring.config.HadoopConfigUtils;
import main.spring.util.Utils;
/**
 * 使用单线程运行算法调用，前台可以直接返回
 * @author fansy
 *
 */
@Controller
public class RunSpark {
	private Logger log = LoggerFactory.getLogger(getClass());
	@Autowired
	private HadoopConfig hadoopConfig;
	@Autowired
	private HadoopConfigUtils hadoopConfigUtils;

	@Autowired
	private Utils utils;

//	public static void main(String[] args) throws IllegalArgumentException, IOException {
//		//<input> <output> <train_percent> <ranks> <lambda> <iteration>
//		String[] inputArgs= new String[]{
//				"hdfs://master:8020/user/root/ratings.dat",
//				"hdfs://master:8020/user/fansy/als_output",
//				"0.8",
//				"10",
//				"10.0",
//				"20"
//		};
//		String[] runArgs=new String[]{
//                "--name","ALS Model Train ",
//                "--class","als.ALSModelTrainer",
//                "--driver-memory","512m",
//                "--num-executors", "2",
//                "--executor-memory", "512m",
//                "--jar","hdfs://master:8020/user/root/Spark141-als.jar",//
//                "--files","hdfs://master:8020/user/root/yarn-site.xml",
//                "--arg",inputArgs[0],
//                "--arg",inputArgs[1],
//                "--arg",inputArgs[2],
//                "--arg",inputArgs[3],
//                "--arg",inputArgs[4],
//                "--arg",inputArgs[5]
//        };
//		FileSystem.get(Utils.getConf()).delete(new Path(inputArgs[1]), true);
//		Utils.runSpark(runArgs);
//	}

	//<input> <output> <train_percent> <ranks> <lambda> <iteration>
	public  String runALS(String input,String output,String train_percent,String ranks,String lambda,
			String iteration) throws IllegalArgumentException, IOException{
		String[] runArgs=new String[]{
                "--name",hadoopConfig.getAlsName(),
                "--class",hadoopConfig.getAlsClass(),
                "--driver-memory",hadoopConfig.getAlsDriverMemory(),
                "--num-executors", hadoopConfig.getAlsNumExecutors(),
                "--executor-memory",hadoopConfig.getAlsExecutorMemory(),
                "--jar",hadoopConfig.getAlsJar(),//
                "--files",hadoopConfig.getAlsFiles(),
                "--arg",input,
                "--arg",output,
                "--arg",train_percent,
                "--arg",ranks,
                "--arg",lambda,
                "--arg",iteration,
                "--arg","30"// recommend number
        };
		FileSystem.get(hadoopConfigUtils.getConfiguration()).delete(new Path(output), true);
		return utils.runSpark(runArgs);
	}
}
