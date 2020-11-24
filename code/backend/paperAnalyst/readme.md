### 项目描述

 [backend/paperAnalyst](https://github.com/Almarduke/NJUSE-CloudComputing/tree/master/code/backend/paperAnalyst) 是一个springboot后端项目，主要为论文分析系统提供后端数据支持。项目加入了关于流和spark的计算，提高了运行效率。

#### 后端技术栈

1.spring boot

2.jpa

3.mysql

4.spark

5.maven

### 快速部署

#### 1.git

安装git客户端，clone项目到本地

```
git clone https://github.com/Almarduke/NJUSE-CloudComputing.git
```

#### 2.mysql

安装mysql，并提前在本地创建一个空数据库，并导入数据库文件，在项目的application.properties中修改相应配置:

```
spring.datasource.url=jdbc:mysql://172.19.241.172:3306/cc_data?characterEncoding=UTF-8
spring.datasource.username=hadoop
spring.datasource.password=123456
```

#### 3.spark

使用intelij安装spark插件，并配置环境变量：

创建`SPARK_HOME：D:\spark-2.2.0-bin-hadoop2.7`

Path添加：`%SPARK_HOME%\bin`

测试是否安装成功：打开`cmd`命令行，输入`spark-shell`

#### 4.scala

安装scala， 其版本应与上面spark/jars/中scala版本一致，2.2.0版本spark对应的scala版本位2.11.8 

可以通过intelij的plugin直接安装

#### 5.JSch

从[官网](http://www.jcraft.com/jsch/ )下载JSch，并添加到项目中：

File->Project Strcture->Libraries->点击加号

### 项目启动

swagger在本地访问方式是访问http://localhost:8080/swagger-ui.html#，可以进行接口测试
