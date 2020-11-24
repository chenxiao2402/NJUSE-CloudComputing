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

安装有依赖完毕后，打开`CCProject/ccspider/settings.py` ，可以对 Scrapy 的参数进行设置，如果需要使用 Mysql 作为输出，请取消第 72 行代码的注释，并对 25 - 28 行的关于 Mysql 的配置进行修改。（该步骤可跳过）

上述步骤完成后，进入到 `CCProjectSpider` 目录下，使用以下命令即可启动爬虫

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
    scrapy crawl arxiv -o arxiv.csv
    ```

### 数据预处理

1. 打开 `CCProjectSpider/scripts/aggregate_data.py` 文件，在 `file_list` 中添加爬取到的 `csv` 文件路径。
2. 在命令行运行上述文件。
3. 在`CCProjectSpider/json` 文件夹中可以找到最终的数据文件 `final_data.json` 

### 数据集

我们准备了已经爬取并处理好的数据集  `CCProjectSpider/data.json` 。

## III. 流计算部分

### 配置 Spark 相关参数

1. 打开 `CCStreaming/src/main/scala/cc/Main.scala` 文件，配置以下 Spark 参数
   - `SPARK_MASTER_ADDRESS`：目标 master 地址
   - `PAPER_COUNT_DATA_DIR`：HDFS 放数据的文件夹路径（被一个流消耗）
   - `OTHER_DATA_DIR`：HDFS 放数据的文件夹路径（被另外两个流消耗）
   - `CHECK_POINT_DIR`：Checkpoint 文件夹路径
     - 另外还需要在`Main.scala` 文件的 33 行，38 行以及 48 行指定三个流具体的 `checkpoint` 路径
2. 打开`CCStreaming/src/main/scala/cc/sink/JDBCSink.scala` 文件，找到 `object JDBCSink`，配置 Mysql 参数
   - `JDBC_URL`：数据库地址
   - `USER`：数据库用户
   - `PASSWORD`：数据库密码

### 打包项目

1. 首先需要安装 sbt（可通过 Scala 官网进行安装）。
2. 进入 `CCStreaming` 文件夹下，在命令行输入命令 `sbt` 等待初始化完成。
3. 在 sbt 命令行中，输入命令 `assembly` 即可进行打包。
4. 打包完成后可在 `CCStreaming/target/scala-2.12` 中找到已经打包好的 `jar` 文件。

### 运行
