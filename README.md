# programmer-lives-healthy
一个跑步打卡记录站：“程序员，活久点”，2020年写的小项目，拿来练手用。

制作这个网站的初衷是鼓励我们锻炼，因为当时疫情在家，我们一起在线上做项目，久坐且缺乏锻炼，到了期末大家的身体或多或少都出了些问题。所以我们做了一个这样的站点，希望能每天都跑步打卡，并且规定如果某天漏掉了打卡就要在微信群发5元红包，总红包金额在网站里会显示的（直接示众

我和我的队友们用了好久，现在关站了（虽然到后面变成了四六级背单词打卡。。）

由我和另一位同学合作完成，我负责前端部分。使用SpringBoot+Bootstrap4+Thymeleaf+MySQL，前端使用了部分Start Bootstrap模板。这是一个前后端不分离的项目，能实现基本的登录注册，站内私信，添加群组，加入群组，修改群组，组长可以设置群公告和未打卡应当惩罚金额。

站内私信里有一个我手搓的emoji表情面板，勉强能用，但是写的不太好。

目前貌似还没实现退出群组和解散群组，站内私信时不支持手机端，有想法的同学欢迎pull request（

如何使用？

1. 使用IDEA打开，使用Maven安装相关依赖项

2. 在本地导入数据库表（仓库内已给出SQL文件）

3. 在`src/main/java/com/abo/programmerliveshealthy/util/MyMailUtil.java`里，修改一下邮箱服务器的地址（改成你的QQ号和QQ邮箱即可测试）

   ```java
    private static final String VERIFICATION_PASSWORD = "password";
    private static final String HOST = "smtp.qq.com";
    private static final String USER = "QQ号";
    private static final Integer PORT = 465;
    private static final String FROM = "发邮件的邮箱";
    private static final int CODE_LENGTH = 6;
   ```

4. 如果你打算发布并使用它，在`src/main/resource/application.yml`里修改一下数据库连接信息即可

   ```yaml
    driver-class-name: com.mysql.cj.jdbc.Driver
    #本地测试localhost
    url: jdbc:mysql://localhost:3306/plh?useUnicode=true&characterEncoding=utf8&useSSL=true&serverTimezone=UTC
    #本地测试root 123456
    username: root
    password: 123456
   ```

5. 本地测试时，存储图片文件的文件夹在`D:/img`，如果你想修改这个路径，需要修改`src/main/resource/application.properties`

   ```
   file.upload.path=D://img/
   file.upload.path.relative=/img/**
   ```

   

希望全天下的程序员们都要健健康康，记得按时锻炼，身体最重要~