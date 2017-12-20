package main.spring.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

/**
 * Created by fanzhe on 2017/12/12.
 */
@Configuration
@ConfigurationProperties()
@PropertySource("classpath:main/config/hadoop.properties")
@Component
public class HadoopConfig {

    public String getSparkYarnJar() {
        return sparkYarnJar;
    }

    public void setSparkYarnJar(String sparkYarnJar) {
        this.sparkYarnJar = sparkYarnJar;
    }

    public String getSparkYarnSchedulerHeartbeatIntervalMs() {
        return sparkYarnSchedulerHeartbeatIntervalMs;
    }

    public void setSparkYarnSchedulerHeartbeatIntervalMs(String sparkYarnSchedulerHeartbeatIntervalMs) {
        this.sparkYarnSchedulerHeartbeatIntervalMs = sparkYarnSchedulerHeartbeatIntervalMs;
    }

    public Boolean getMapreduceAppSubmissionCrossPlatform() {
        return mapreduceAppSubmissionCrossPlatform;
    }

    public void setMapreduceAppSubmissionCrossPlatform(Boolean mapreduceAppSubmissionCrossPlatform) {
        this.mapreduceAppSubmissionCrossPlatform = mapreduceAppSubmissionCrossPlatform;
    }

    public String getFsDefaultFS() {
        return fsDefaultFS;
    }

    public void setFsDefaultFS(String fsDefaultFS) {
        this.fsDefaultFS = fsDefaultFS;
    }

    public String getMapreduceFrameworkName() {
        return mapreduceFrameworkName;
    }

    public void setMapreduceFrameworkName(String mapreduceFrameworkName) {
        this.mapreduceFrameworkName = mapreduceFrameworkName;
    }

    public String getYarnResourceManagerAddress() {
        return yarnResourceManagerAddress;
    }

    public void setYarnResourceManagerAddress(String yarnResourceManagerAddress) {
        this.yarnResourceManagerAddress = yarnResourceManagerAddress;
    }

    public String getYarnResourceManagerSchedulerAddress() {
        return yarnResourceManagerSchedulerAddress;
    }

    public void setYarnResourceManagerSchedulerAddress(String yarnResourceManagerSchedulerAddress) {
        this.yarnResourceManagerSchedulerAddress = yarnResourceManagerSchedulerAddress;
    }

    public String getMapreduceJobHistoryAddress() {
        return mapreduceJobHistoryAddress;
    }

    public void setMapreduceJobHistoryAddress(String mapreduceJobHistoryAddress) {
        this.mapreduceJobHistoryAddress = mapreduceJobHistoryAddress;
    }

    public String getAlsName() {
        return alsName;
    }

    public void setAlsName(String alsName) {
        this.alsName = alsName;
    }

    public String getAlsClass() {
        return alsClass;
    }

    public void setAlsClass(String alsClass) {
        this.alsClass = alsClass;
    }

    public String getAlsDriverMemory() {
        return alsDriverMemory;
    }

    public void setAlsDriverMemory(String alsDriverMemory) {
        this.alsDriverMemory = alsDriverMemory;
    }

    public String getAlsNumExecutors() {
        return alsNumExecutors;
    }

    public void setAlsNumExecutors(String alsNumExecutors) {
        this.alsNumExecutors = alsNumExecutors;
    }

    public String getAlsExecutorMemory() {
        return alsExecutorMemory;
    }

    public void setAlsExecutorMemory(String alsExecutorMemory) {
        this.alsExecutorMemory = alsExecutorMemory;
    }

    public String getAlsJar() {
        return alsJar;
    }

    public void setAlsJar(String alsJar) {
        this.alsJar = alsJar;
    }

    public String getAlsFiles() {
        return alsFiles;
    }

    public void setAlsFiles(String alsFiles) {
        this.alsFiles = alsFiles;
    }

    public String getAlSubmittedProgress() {
        return alSubmittedProgress;
    }

    public void setAlSubmittedProgress(String alSubmittedProgress) {
        this.alSubmittedProgress = alSubmittedProgress;
    }

    public String getAlsAcceptedProgress() {
        return alsAcceptedProgress;
    }

    public void setAlsAcceptedProgress(String alsAcceptedProgress) {
        this.alsAcceptedProgress = alsAcceptedProgress;
    }

    public String getAlsRunningProgress() {
        return alsRunningProgress;
    }

    public void setAlsRunningProgress(String alsRunningProgress) {
        this.alsRunningProgress = alsRunningProgress;
    }

    public String getMoviesData() {
        return moviesData;
    }

    public void setMoviesData(String moviesData) {
        this.moviesData = moviesData;
    }

    public String getRatingsData() {
        return ratingsData;
    }

    public void setRatingsData(String ratingsData) {
        this.ratingsData = ratingsData;
    }

    public String getOutputData() {
        return outputData;
    }

    public void setOutputData(String outputData) {
        this.outputData = outputData;
    }

    @Value("${spark.yarn.jar}")
    private String sparkYarnJar;

    @Value("${spark.yarn.scheduler.heartbeat.interval-ms}")
    private String sparkYarnSchedulerHeartbeatIntervalMs;


    @Value("${mapreduce.app-submission.cross-platform}")
    private Boolean mapreduceAppSubmissionCrossPlatform;
    @Value("${fs.defaultFS}")
    private String fsDefaultFS;
    @Value("${mapreduce.framework.name}")
    private String mapreduceFrameworkName;
    @Value("${yarn.resourcemanager.address}")
    private String yarnResourceManagerAddress;
    @Value("${yarn.resourcemanager.scheduler.address}")
    private String yarnResourceManagerSchedulerAddress;
    @Value("${mapreduce.jobhistory.address}")
    private String mapreduceJobHistoryAddress;

    @Value("${als.name}")
    private String alsName;
    @Value("${als.class}")
    private String alsClass;
    @Value("${als.driver-memory}")
    private String alsDriverMemory;
    @Value("${als.num-executors}")
    private String alsNumExecutors;
    @Value("${als.executor-memory}")
    private String alsExecutorMemory;
    @Value("${als.jar}")
    private String alsJar;
    @Value("${als.files}")
    private String alsFiles;


    @Value("${als.submitted.progress}")
    private String alSubmittedProgress;
    @Value("${als.accepted.progress}")
    private String alsAcceptedProgress;
    @Value("${als.runing.progress}")
    private String alsRunningProgress;

    @Value("${movies.data}")
    private String moviesData;
    @Value("${ratings.data}")
    private String ratingsData;
    @Value("${output.data}")
    private String outputData;

    public Boolean getInitUpdateJar() {
        return initUpdateJar;
    }

    public void setInitUpdateJar(Boolean initUpdateJar) {
        this.initUpdateJar = initUpdateJar;
    }

    public Boolean getInitUpdateMovies() {
        return initUpdateMovies;
    }

    public void setInitUpdateMovies(Boolean initUpdateMovies) {
        this.initUpdateMovies = initUpdateMovies;
    }

    public Boolean getInitUpdateRatings() {
        return initUpdateRatings;
    }

    public void setInitUpdateRatings(Boolean initUpdateRatings) {
        this.initUpdateRatings = initUpdateRatings;
    }

    @Value("${init.update.jar}")
    private Boolean initUpdateJar;
    @Value("${init.update.movies}")
    private Boolean initUpdateMovies;
    @Value("${init.update.ratings}")
    private Boolean initUpdateRatings;



}
