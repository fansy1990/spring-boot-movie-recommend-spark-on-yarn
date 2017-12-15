package als

import org.apache.spark.mllib.recommendation._
import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}

/**
 * ALS 模型训练
 * Created by fansy on 2016/8/4.
 */
object ALSImplicitModelTrainer {
  def main(args: Array[String]) {
    if(args.length!=8){
      System.err.println("Usage:als.ALSImplicitModelTrainner <input> <output> <train_percent> <ranks> <lambda> " +
        "<iteration> <alpha> <recNum>")
      System.exit(-1)
    }

    val input: String = args(0)
    val output: String = args(1)
    val train_percent: Double = args(2).toDouble
    val ranks: Int = args(3).toInt
    val lambda: Double = args(4).toDouble
    val iteration: Int = args(5).toInt
    val alpha= args(6).toDouble
    val recNum = args(7).toInt
    if(train_percent<=0.5 || train_percent>=1.0){
      System.err.println("train data size is not proper!")
      System.exit(-1)
    }
    // 1: read data and split to train and test
    val conf = new SparkConf().setAppName("train ALS Implicit Model ")
    val sc = new SparkContext(conf)
    val ratings = sc.textFile(input).map{
      line => val fields = line.split("::")
        val rating = Rating(fields(0).toInt,fields(1).toInt,fields(2).toDouble)
        val timestamp = fields(3).toLong %10
        (timestamp,rating)
    }
    // 2. split data to train and test
    // training data
    val training = ratings.filter(x=>x._1<10*train_percent).values.repartition(2).cache()
    // testing data
    val testing = ratings.filter(x=>x._1>=10*train_percent).values.cache()

    // 3. train the als implicit model
    val model = ALS.trainImplicit(training,ranks,iteration,lambda,alpha)

    // 4. calculate the RMSE
    val rmse = computeRMSE(model,testing)

    // 5. compute the recommend
    val recommend: RDD[(Int, Array[Rating])] = model.recommendProductsForUsers(recNum);
    // 6. save result to output
    sc.parallelize(Seq(model), 1).saveAsObjectFile(output+"/model")

    sc.parallelize(List(rmse),1).saveAsTextFile(output + "/rmse")
    recommend.map(x => x._1+","+x._2.map(y => y.product+":"+y.rating).mkString(";")).saveAsTextFile(output+"/recommend")
    // 7. close sc
    sc.stop()
  }

  /**
   * 计算模型误差
   * @param model 模型
   * @param data 测试数据集
   * @return
   */
  def computeRMSE(model:MatrixFactorizationModel, data:RDD[Rating]): Double = {
    val usersProducts = data.map(x=>(x.user,x.product))
    val t = model.predict(usersProducts).map{
      case Rating(user,product,rating)=>((user,product),rating)}
    val ratingsAndPredictions = data.map{
      case Rating(user,product,rating)=>
      ((user,product),rating)}
      .join(
        t
      ).values
      math.sqrt(ratingsAndPredictions.map(x=>(x._1-x._2)*(x._1-x._2)).mean())
  }

}
