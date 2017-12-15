package spring.config;

import org.apache.hadoop.conf.Configuration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

/**
 * Created by fanzhe on 2017/12/13.
 */
@Controller
public class HadoopConfigUtils {
    private static Logger logger = LoggerFactory.getLogger(HadoopConfigUtils.class);

    public Configuration getConfiguration() {
        if(configuration == null ){
            logger.info("Hadoop Configuration has been created!");
            configuration = new Configuration();
            configuration.setBoolean("mapreduce.app-submission.cross-platform",
                    hadoopConfig.getMapreduceAppSubmissionCrossPlatform());
            configuration.set("fs.defaultFS",hadoopConfig.getFsDefaultFS());
            configuration.set("mapreduce.framework.name",
                    hadoopConfig.getMapreduceFrameworkName());
            configuration.set("yarn.resourcemanager.address",
                    hadoopConfig.getYarnResourceManagerAddress());
            configuration.set("yarn.resourcemanager.scheduler.address",
                    hadoopConfig.getYarnResourceManagerSchedulerAddress());
            configuration.set("mapreduce.jobhistory.address",
                    hadoopConfig.getMapreduceJobHistoryAddress());
        }
        return configuration;
    }

    public void setConfiguration(Configuration configuration) {
        this.configuration = configuration;
    }

    private Configuration configuration = null ;

    @Autowired
    private HadoopConfig hadoopConfig;

    public HadoopConfigUtils(){
        logger.info("HadoopConfigUtils has been inited!");
    }







}
