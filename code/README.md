#运行说明 

说明文件（Readme），解释如何复现本次作业内容，例如按照何种顺序执行哪些文件，即可通过哪种方式查看计算结果等。

[TOC]

## I. 展示界面

### 1. 本地安装

展示界面使用react开发，需要安装nodejs（javascript运行环境），以及通过nodejs内置的包管理工具npm安装需要的依赖，之后可以编译并执行项目。

从[nodejs官网](https://nodejs.org/en/)下载对应平台的安装程序，执行完毕之后安装nodejs结束。可以在命令行输入命令`node -v`查看nodejs版本信息，如果没有报错说明安装成功。

```
$ node -v
v7.6.0
```

npm是nodejs内置的包管理工具，用来下载开发过程中需要的依赖。可以输入命令`npm -v`下载npm。

```
$ node -v
v4.1.2
```

进入展示界面所在的项目文件夹

```
$ cd sparkproject-frontend
```

运行npm安装所有需要的依赖

```
$ npm install
```

安装完成后用命令`npm start`启动系统，系统运行在`localhost:3000`端口下。

```
$ npm start

Compiled successfully!

You can now view sparkproject in the browser.

  Local:            http://localhost:3000
  On Your Network:  http://192.168.1.103:3000

Note that the development build is not optimized.
To create a production build, use npm run build.
```



### 2. 通过网络访问

我们在云主机上部署了相应的展示界面，可以通过在校园网下通过浏览器访问[172.19.241.172](http://172.19.241.172)直接查看界面。

## II. 爬虫

### 启动爬虫

爬虫部分使用 Python 进行了开发，使用了 Scrapy 爬虫框架，并使用了 Pandas 对爬取的数据进行了预处理。将项目打开后，可以使用 `pip` 或 `conda` 安装以下依赖：

```
python 3.7.5
pandas 0.25.3
scrapy 2.4.0
pymysql 0.10.1 (不是必须，如果要指定 Mysql 数据库作为输出的话则需要安装)
```

安装有依赖完毕后，打开`code/spider/ccspider/settings.py` ，可以对 Scrapy 的参数进行设置，如果需要使用 Mysql 作为输出，请取消第 72 行代码的注释，并对 25 - 28 行的关于 Mysql 的配置进行修改。（该步骤可跳过）

上述步骤完成后，进入到 `code/spider` 目录下，使用以下命令即可启动爬虫（可能需要将当前目录添加到 PYTHON_PATH 环境变量中）

- 启动 ACM 爬虫

    ```shell
    scrapy crawl acm -a start_year=2010 -a end_year=2010 -a before_month=1 -a after_month=1 -o acm_2010.csv
    ```
    
    参数描述
    
    - `start_year`：开始年限
    - `end_year`： 结束年限（包含本年）
    - `before_month`：结束月份（包含本月）
    - `after_month`：开始月份（包含本月）
    - `-o` 指定输出文件的名称和格式，Scrapy 自动支持输出为 `.csv` 文件
    
- 启动 Arxiv 爬虫

    ```shell
    scrapy crawl arxiv -o arxiv.json
    ```

### 数据预处理

数据处理步骤如下所示：

1. 打开 `code/spider/scripts/aggregate_data.py` 文件，在 `file_list` 中添加爬取到的 `csv` 文件路径。
2. 在`code/spider/scripts` 目录下使用命令行运行上述 python 脚本。
3. 在`code/spider/json` 文件夹中可以找到最终的数据文件 `final_data.json` 

### 数据集

- 爬取并处理好的各年份数据源文件
  - code/spider/json/acm_2005.json - acm_2020.json
  - code/spider/json/arxiv.json
- 处理好的最终数据文件
  -   code/spider/json/final_data.json

## III. 流计算部分

### 环境依赖

- Spark  3.0.1 + Hadoop  3.2
  - 总共四台云主机：一台 master + 三台 slaves
- Scala  2.12.12
- Mysql  8.0.22

### 配置 Spark 相关参数

（我们在编写流的过程中总共编写了三个流，为了方便展示，我们将其中一个流分离了出来，与另外两个流分开启动和运行，所以在配置时需要分别配置数据存放的路径）

1. 打开 `code/sparkend/streaming/src/main/scala/cc/Main.scala` 文件，配置以下 Spark 参数
   - `SPARK_MASTER_ADDRESS`：目标 master 地址
   - `PAPER_COUNT_DATA_DIR`：HDFS 放数据的文件夹路径（被一个流消耗）
   - `OTHER_DATA_DIR`：HDFS 放数据的文件夹路径（被另外两个流消耗）
   - `CHECK_POINT_DIR`：Checkpoint 文件夹路径
     - 另外还需要在`Main.scala` 文件的 33 行，38 行以及 48 行指定三个流具体的 `checkpoint` 路径
2. 打开`code/sparkend/streaming/src/main/scala/cc/sink/JDBCSink.scala` 文件，找到 `object JDBCSink`，配置 Mysql 参数
   - `JDBC_URL`：数据库地址
   - `USER`：数据库用户
   - `PASSWORD`：数据库密码
3. 使用 sql scripts 创建数据库表（两份文件均需要用数据库执行）
   - `code/sparkend/streaming/scripts/create_table.sql ` 
   - `code/sparkend/streaming/scripts/create_paper_count_table.sql`

### 打包项目

1. 首先需要安装 sbt（可通过 Scala 官网进行安装）。
2. 进入 `code/sparkend/streaming` 文件夹下，在命令行输入命令 `sbt` 等待初始化完成。
3. 在 sbt 命令行中，输入命令 `assembly` 即可进行打包。
4. 打包完成后可在 `code/sparkend/streaming/target/scala-2.12` 中找到已经打包好的 `jar` 文件。

### 运行

1. 使用命令 `spark-submit --main cc.Main ccstreaming-assembly-0.1.jar streamName` 启动流
   - `ccstreaming-assembly-0.1.jar`：在上一步中打包好的 jar 包路径。
   - `streamName`：流的名称，可选 `paperCount` 和 `otherTwo` ，必须在命令行中输入要启动的流的名称，否则将直接推出 Spark。
2. 流启动后，如果在监听的文件夹下有未处理的数据，则将开始处理，可以打开数据库，通过以下数据表查看结果（可能需要等待几分钟）：
   - `author_citations`
   - `paper_citations`
   - `subject_paper_count`
3. 可以使用我们准备好的数据集 `code/spider/data.json` 以及使用命令 `hdfs dfs -put data.json target_dir` 将数据传递到监听的文件夹下。（如果传递数据时还未启动，则可以直接将数据传递到目标文件夹，**如果流已经启动，则需要先传递到 hdfs 的另外任意一个文件夹，然后再把数据文件移动到监听的文件夹下**，这样做是为了保证数据出现时是原子性的，确保流能够处理数据文件中所有的数据 ）
4. 重复启动流之前，需要先删除 checkpoint 文件夹中的数据，否则流不会重新处理已经处理过的文件。

### 一键启动流

在校园网下，访问 `172.19.241.172` ，然后点击前端页面的启动流按钮即可启动流。（需要等到几分钟，因为脚本文件需要将数据移动到监听文件夹中，并删除 checkpoint 信息后再启动 spark）

- 访问 `172.19.241.172:4040` 查看任务执行状态。

- 访问 `172.19.241.172:8080` ，通过界面操作 kill 流的进程。

- 访问数据库 `172.19.241.172:3306 cc_data` ，查看 `subject_paper_count` 表，即可查看计算结果。

- 在原始代码中我们编写了三个流，对应三张数据库表，但是只准备了一个流供前端一键启动，原因是该流的处理速度较另外两个更快，可以更快看到反馈，更利于展示。

- **请不要重复启动，在重新启动下一个流之前，请务必 kill 上一个正在运行的流。**

- 如果需要启动另外两个流，可以使用 ssh 访问云主机 `hadoop@172.19.241.172`（密码是 123456），并执行以下命令即可启动。同样是在 `172.19.241.172:4040` 查看任务状态，可以在部署在云端的数据库 `cc_data`的  `author_citations`  和`paper_citations` 数据表中查看结果。

  ```bash
  cd ~/scripts
  ./scripts/start-stream-other.sh
  ```

  

## Ⅳ. 图计算部分

### 环境依赖

- Spark  3.0.1 + Hadoop  3.2
  - 总共四台云主机：一台 master + 三台 slaves
- Scala  2.12.12
- Mysql  8.0.22

### 打包项目

1. 打开 `sparkend/graphx/src/main/scala/Utils.scala` 文件，找到 `sparkSession.read.json("/data/paper/arxiv_final.json")`函数，将其中的`/data/paper/arxiv_final.json`改为自己在HDFS上存放的数据集的目录。
2. 打开`sparkend/graphx/src/main/scala/MysqlHelper.scala` 文件，找到 `.option("key", "value")`函数，根据key配置相应的value，为Mysql连接配置参数：
   - `url`：数据库地址
   - `user`：数据库用户
   - `password`：数据库密码
3. 使用 sql scripts 创建MySQL数据库表（用MySQL执行）
   - `sparkend/graphx/scripts/create_table.sql`
4. 在`sparkend/graphx/`目录下运行`sbt assembly`编译项目（需要安装有sbt，我们的sbt版本为1.4.1）。

### 运行离线计算

1. 将数据集放到上一步中设置的HDFS路径上，如`hdfs dfs -put spider/json/final_data.json <HDFS路径>`
2.  `使用命令 `spark-submit --class "OfflineMain" --master <你的master节点url> sparkend/graphx/target/scala-2.12/CCGraphx-assembly-0.1.jar` 启动图的离线计算。

### 运行实时计算

使用命令 `spark-submit --class "RealTimeMain" --master <你的master节点url> sparkend/graphx/target/scala-2.12/CCGraphx-assembly-0.1.jar <startYear> <subject> <author1> <author2>` 启动图的离线计算。

- `<startYear> <subject> <author1> <author2>`是四个命令行参数，分别为起始年份、领域名以及作者1和作者2的名字，详见`sparkend/graphx/src/main/scala/RealTimeMain.scala`的注释。