#运行说明 

说明文件（Readme），解释如何复现本次作业内容，例如按照何种顺序执行哪些文件，即可通过哪种方式查看计算结果等。



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

## II. 流计算部分

### 1. 本地安装