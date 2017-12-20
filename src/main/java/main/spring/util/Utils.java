package main.spring.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileStatus;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.yarn.api.records.ApplicationId;

import org.apache.spark.SparkConf;
import org.apache.spark.deploy.yarn.Client;
import org.apache.spark.deploy.yarn.ClientArguments;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.fommil.netlib.BLAS;
import com.google.common.collect.Sets;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import main.spring.algorithm.MonitorThread;
import main.spring.config.HadoopConfig;
import main.spring.config.HadoopConfigUtils;
import main.spring.datastructure.FixSizePriorityQueue;
import main.spring.model.MIdRated;
import main.spring.model.Movie;
import main.util.SpringApplicationContextHolder;

@Controller
public class Utils {
    public static final String DOUBLECOLON = "::";
    public static final String COLON = ":";
    public static final String COMMA = ",";
    private static final String SUBFIX = "part-00000";

    private static final String movies_data_subfix = "/data/movies.dat";
    private static final String ratings_data_subfix = "/data/ratings.dat";

    private static Logger log = LoggerFactory.getLogger(Utils.class);

    private static String userFeaturePath = null;
    private static String productFeaturePath = null;
    public static String RMSEPATH = null;

//	private static Configuration configuration = null;

    @Autowired
    private HadoopConfigUtils hadoopConfigUtils;

    @Autowired
    private HadoopConfig hadoopConfig;

    // 允许多用户提交spark任务 TODO 还应解决模型输出目录问题
    private static Map<String, String> allAppStatus = new HashMap<>();

    /**
     * 调用Spark 加入监控模块
     *
     * @param args
     * @return Application ID字符串
     */
    public String runSpark(String[] args) {
        StringBuffer buff = new StringBuffer();
        for (String arg : args) {
            buff.append(arg).append(",");
        }
        log.info("runSpark args:" + buff.toString());
        try {
            System.setProperty("SPARK_YARN_MODE", "true");
            SparkConf sparkConf = new SparkConf();
            sparkConf.set("spark.yarn.jar", hadoopConfig.getSparkYarnJar());
            sparkConf.set("spark.yarn.scheduler.heartbeat.interval-ms",
                    hadoopConfig.getSparkYarnSchedulerHeartbeatIntervalMs());

            ClientArguments cArgs = new ClientArguments(args, sparkConf);

            Client client = new Client(cArgs, hadoopConfigUtils.getConfiguration(), sparkConf);
            // client.run(); // 去掉此种调用方式，改为有监控的调用方式

            /**
             * 调用Spark ，含有监控
             */
            ApplicationId appId = null;
            try {
                appId = client.submitApplication();
            } catch (Throwable e) {
                e.printStackTrace();
                //  返回null
                return null;
            }
            // 开启监控线程
            updateAppStatus(appId.toString(),
                    hadoopConfig.getAlSubmittedProgress());// 提交任务完成，返回2%
            log.info(allAppStatus.toString());
            new Thread(new MonitorThread(appId, client)).start();
            return appId.toString();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 参考Spark实现删除相关文件代码
     * <p>
     * TODO Tomcat关闭时，如果还有Spark程序还在运行，那么删除不了文件
     *
     * @param appId
     */
    public void cleanupStagingDir(ApplicationId appId) {
        final String appStagingDir = Client.SPARK_STAGING() + Path.SEPARATOR + appId.toString();

        //删除需要等待10分钟，不然如果任务运行失败，查看日志查看不了
        final Configuration conf = ((HadoopConfigUtils)
                SpringApplicationContextHolder.getSpringBean("hadoopConfigUtils")
        ).getConfiguration();
        new Thread(() -> {
            try {
                Thread.sleep(10 * 60 * 1000);
                Path stagingDirPath = new Path(appStagingDir);
                FileSystem fs = FileSystem.get(conf);
                if (fs.exists(stagingDirPath)) {
                    log.info("Deleting staging directory " + stagingDirPath);
                    fs.delete(stagingDirPath, true);
                }
            } catch (IOException |InterruptedException e) {
                log.warn("Failed to cleanup staging dir " + appStagingDir, e);
            }
        }
        ).start();

    }

    /**
     * 读取文件内容
     *
     * @param outputPath
     * @return
     */
    public String readHDFS(String outputPath) {
        StringBuffer buffer = new StringBuffer();
        try {
            Path path = new Path(outputPath);
            FileSystem fs = FileSystem.get(hadoopConfigUtils.getConfiguration());
            BufferedReader br = new BufferedReader(new InputStreamReader(fs.open(path)));
            try {
                String line;
                while ((line = br.readLine()) != null) {
                    buffer.append(line).append("\n");
                    line = br.readLine();
                }
            } finally {
                br.close();
            }
        } catch (Exception e) {
            return null;
        }
        return buffer.toString();
    }

    /**
     * 预测 如果没有初始化，则进行初始化
     *
     * @param uid
     * @param recNum
     * @return
     * @throws NoSuchMethodException
     * @throws InvocationTargetException
     * @throws InstantiationException
     * @throws IllegalAccessException
     */
    public List<Movie> predict(int uid, int recNum) throws IllegalAccessException, InstantiationException, InvocationTargetException, NoSuchMethodException {
        if (userFeatures.size() <= 0 || productFeatures.size() <= 0) {
            try {
                userFeatures = getModelFeatures(userFeaturePath);
                productFeatures = getModelFeatures(productFeaturePath);
            } catch (IOException e) {
                return null;
            }
            if (userFeatures.size() <= 0 || productFeatures.size() <= 0) {
                System.err.println("模型加载失败!");
                return null;
            }
        }

        // 使用模型进行预测
        // 1. 找到uid没有评价过的movieIds
        Set<Integer> candidates = Sets.difference((Set<Integer>) allMovieIds, userWithRatedMovies.get(uid));

        // 2. 构造推荐排序堆栈
        FixSizePriorityQueue<Movie> recommend = new FixSizePriorityQueue<Movie>(recNum);
        Movie movie = null;
        double[] pFeature = null;
        double[] uFeature = userFeatures.get(uid);
        double score = 0.0;
        BLAS blas = BLAS.getInstance();
        for (int candidate : candidates) {
            movie = movies.get(candidate).deepCopy();
            pFeature = productFeatures.get(candidate);
            if (pFeature == null)
                continue;
            score = blas.ddot(pFeature.length, uFeature, 1, pFeature, 1);
            movie.setRated((float) score);
            recommend.add(movie);
        }

        return recommend.sortedList();
    }

    /**
     * 加载model user/product features
     *
     * @param featurePath
     * @return
     */
    private Map<Integer, double[]> getModelFeatures(String featurePath) throws IOException {
        Map<Integer, double[]> features = new HashMap<>();
        Path path = new Path(featurePath);
        FileSystem fs = FileSystem.get(hadoopConfigUtils.getConfiguration());
        BufferedReader br = null;
        InputStreamReader inputReader = null;
        FileStatus[] files = fs.listStatus(path);
        for (FileStatus file : files) {
            if (file.isDirectory() || file.getLen() <= 0) {
                continue;
            }
            try {
                log.info("加载文件：{}",file.getPath());
                inputReader = new InputStreamReader(fs.open(file.getPath()));
                br = new BufferedReader(inputReader);

                String line;
                String[] words = null;
                int id = -1;
                // id:f1,f2,f3,,,fn
                while ((line = br.readLine()) != null) {
                    words = line.split(COLON);
                    id = Integer.parseInt(words[0]);
                    features.put(id, getDoubleFromString(words[1], COMMA));
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                inputReader.close();
                br.close();
            }
        }
        log.info("features size:{}"+features.size());
        return features;
    }

    /**
     * 从string 转为double数组
     *
     * @param string
     * @param splitter
     * @return
     */
    private static double[] getDoubleFromString(String string, String splitter) {
        String[] strings = string.split(splitter);
        double[] ddArr = new double[strings.length];
        for (int i = strings.length - 1; i >= 0; i--) {
            ddArr[i] = Double.parseDouble(strings[i]);
        }
        return ddArr;
    }

    /**
     * 初始化 movies、ratings数据
     *
     * @throws IOException
     */
    public void init() throws IOException {


        // file path initial
        String output = hadoopConfig.getOutputData();
        String MOVIESDATA = hadoopConfig.getMoviesData();
        String RATINGSDATA = hadoopConfig.getRatingsData();
        userFeaturePath = output + "/userFeatures";
        productFeaturePath = output + "/productFeatures";
        RMSEPATH = output + "/rmse/" + SUBFIX;
        String parent = Utils.class.getResource("/").toString().replace("file:","");
        FileSystem fs = FileSystem.get(hadoopConfigUtils.getConfiguration());


        // // 1. 上传数据到HDFS上
        Path pathM = new Path(MOVIESDATA);
        Path pathR = new Path(RATINGSDATA);


        if(hadoopConfig.getInitUpdateJar()) {
            // 0. 上传jar包
            Path alsJar = new Path("/user/root/als.jar");
            fs.deleteOnExit(alsJar);
            String localAlsJar = parent + "jars/als.jar";
            log.info("处理Jar包：{}", localAlsJar);
            fs.copyFromLocalFile(new Path(localAlsJar), alsJar);
        }
        if(hadoopConfig.getInitUpdateMovies()) {
            log.info("deleting " + pathM + "...");
            fs.deleteOnExit(pathM);
            String localMovieData = parent + movies_data_subfix;
            log.info("copying " + localMovieData + "...");
            fs.copyFromLocalFile(new Path(localMovieData), pathM);
        }
        if(hadoopConfig.getInitUpdateRatings()) {
            log.info("deleting " + pathR + "...");
            fs.deleteOnExit(pathR);
            String localRatingData = parent + ratings_data_subfix;
            log.info("copying " + localRatingData + "...");
            fs.copyFromLocalFile(new Path(localRatingData), pathR);
        }
        // 读取movies数据到：Map<movieId,Movie-descriptions>

        BufferedReader br = null;
        InputStreamReader inputReader = null;
        try {
            inputReader = new InputStreamReader(fs.open(pathM));
            br = new BufferedReader(inputReader);

            String line;
            String[] words = null;
            int id = -1;
            // MovieID::Title::Genres
            while ((line = br.readLine()) != null) {
                words = line.split(DOUBLECOLON);
                id = Integer.parseInt(words[0]);
                movies.put(id, new Movie(id, words[1], words[2]));
            }
            log.info("Movies data size:" + movies.size());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            inputReader.close();
            br.close();
        }

        // 读取ratings数据到Map<userid, ratedMoviesId> (not recommended)
//		path = new Path(RATINGSDATA);
        try {
            inputReader = new InputStreamReader(fs.open(pathR));
            br = new BufferedReader(inputReader);

            String line;
            String[] words = null;
            int uid = -1;
            HashSet<MIdRated> movieIds = null;
            // UserID::MovieID::Rating::Timestamp
            while ((line = br.readLine()) != null) {
                words = line.split(DOUBLECOLON);
                uid = Integer.parseInt(words[0]);
                if (userWithRatedMovies.containsKey(uid)) {
                    userWithRatedMovies.get(uid).add(new MIdRated(words[1], words[2]));

                } else {
                    movieIds = new HashSet<>();
                    movieIds.add(new MIdRated(words[1], words[2]));
                    userWithRatedMovies.put(uid, movieIds);
                }

            }
            log.info("Users data size:" + userWithRatedMovies.size());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            inputReader.close();
            br.close();
        }

        allMovieIds = movies.keySet();
    }

    private static Map<Integer, Movie> movies = new HashMap<>();
    private static Map<Integer, Set<MIdRated>> userWithRatedMovies = new HashMap<>();
    private static Set<Integer> allMovieIds = new HashSet<>();
    private static Map<Integer, double[]> userFeatures = new HashMap<>();
    private static Map<Integer, double[]> productFeatures = new HashMap<>();


    /**
     * 获取appId的状态
     *
     * @param appId
     * @return
     */
    public static String getAppStatus(String appId) {
        return allAppStatus.get(appId);
    }

    /**
     * 更新appId状态
     *
     * @param appId
     * @param appStatus
     */
    public synchronized void updateAppStatus(String appId, String appStatus) {
        // 不管是否已经存在改appId，直接更新即可
        allAppStatus.put(appId, appStatus);
    }

    /**
     * 查找给定用户评价过的电影
     *
     * @param userId
     * @return
     */
    public List<Movie> check(int userId) {
        Set<MIdRated> ratedMovieIds = userWithRatedMovies.get(userId);
        List<Movie> moviesList = new ArrayList<>();
        Movie ratedMovie = null;
        for (MIdRated movie : ratedMovieIds) {
            ratedMovie = movies.get(movie.getMovieId()).deepCopy();
            ratedMovie.setRated(movie.getRated());
            moviesList.add(ratedMovie);
        }

        return moviesList;
    }
}
