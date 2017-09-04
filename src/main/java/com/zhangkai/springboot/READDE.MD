2013年12月12日，Spring发布了4.0版本。这个本来只是作为Java平台上的控制反转容器的库，经过将近10年的发展已经成为了一个巨无霸产品。不过其依靠良好的分层设计，每个功能模块都能保持较好的独立性，是Java平台不可多得的好用的开源应用程序框架。 Spring的4.0版本可以说是一个重大的更新，其全面支持Java8，并且对Groovy语言也有良好的支持。另外引入了非常多的新项目，比如Spring boot，Spring Cloud，Spring WebSocket等。

Spring由于其繁琐的配置，一度被人成为“配置地狱”，各种XML、Annotation配置，让人眼花缭乱，而且如果出错了也很难找出原因。Spring Boot项目就是为了解决配置繁琐的问题，最大化的实现convention over configuration(约定大于配置)。熟悉Ruby On Rails（ROR框架的程序员都知道，借助于ROR的脚手架工具只需简单的几步即可建立起一个Web应用程序。而Spring Boot就相当于Java平台上的ROR。

Spring Boot的特性有以下几条：

创建独立Spring应用程序
嵌入式Tomcat，Jetty容器，无需部署WAR包
简化Maven及Gradle配置
尽可能的自动化配置Spring
直接植入产品环境下的实用功能，比如度量指标、健康检查及扩展配置等
无需代码生成及XML配置
目前Spring Boot的版本为1.2.3,需要Java7及Spring Framework4.1.5以上的支持。如果想在Java6下使用它，需要一些额外的设置。

如果你想创建一个基于Spring的Web应用，只是简单的在页面中输出一个’Hello World’，按照之前的老方式，你需要创建以下文件：

web.xml : 配置使用Spring servlet，以及web其它配置；
spring-servlet.xml:配置Spring servlet的配置；
HelloController.java: controller。
如果你想运行它的话，需要将生成的WAR包部署到相应的Tomcat或者Jetty容器中才行，这也需要相应的配置。

如果使用Spring Boot的话，我们只需要创建HelloController.java。

HelloController.java
1
2
3
4
5
6
7
8
9
10
11
12
13
14
15
16
17
18
19
20
21
package hello;

import org.springframework.boot.*;
import org.springframework.boot.autoconfigure.*;
import org.springframework.stereotype.*;
import org.springframework.web.bind.annotation.*;

@Controller
@EnableAutoConfiguration
public class HelloController {

    @RequestMapping("/")
    @ResponseBody
    String home() {
        return "Hello World!";
    }

    public static void main(String[] args) throws Exception {
        SpringApplication.run(HelloController.class, args);
    }
}
然后借助于Spring Boot为Maven和Gradle提供的插件来生成Jar包以后直接运行java -jar即可，简单易用。

上面提过Spring Boot对Maven及Gradle等构建工具支持力度非常大。其内置一个’Starter POM’，对项目构建进行了高度封装，最大化简化项目构建的配置。另外对Maven和Gradle都有相应的插件，打包、运行无需编写额外的脚本。

Spring Boot不止对web应用程序做了简化，还提供一系列的依赖包来把其它一些工作做成开箱即用。下面列出了几个经典的Spring Boot依赖库。

spring-boot-starter-web:支持全栈web开发，里面包括了Tomcat和Spring-webmvc。
spring-boot-starter-mail:提供对javax.mail的支持.
spring-boot-starter-ws: 提供对Spring Web Services的支持
spring-boot-starter-test:提供对常用测试框架的支持，包括JUnit，Hamcrest以及Mockito等。
spring-boot-starter-actuator:支持产品环境下的一些功能，比如指标度量及监控等。
spring-boot-starter-jetty:支持jetty容器。
spring-boot-starter-log4j:引入默认的log框架（logback）
Spring Boot提供的starter比这个要多，详情请参阅文档：Starter POMs章节。

如果你不喜欢Maven或Gradle，Spring提供了CLI（Command Line Interface）来开发运行Spring应用程序。你可以使用它来运行Groovy脚本，甚至编写自定义命令。安装Spring CLI有多种方法，具体请看：安装Spring Boot Cli章节。

安装完毕以后可以运行 srping version来查看当前版本。

你可以使用Groovy编写一个Controller。

hello.groovy
1
2
3
4
5
6
7
8
9
@RestController
class WebApplication {

    @RequestMapping("/")
    String home() {
        "Hello World!"
    }

}
然后使用spring run hello.groovy来直接运行它。访问localhost:8080即可看到打印的信息。

Spring Boot提供的功能还有很多，比如对MVC的支持、外部Properties的注入，日志框架的支持等。这里就不详述了。感兴趣的可以查看其文档来获取详细的信息。

如果你想在你的项目中使用Spring，那么最好把Spring Boot设为标配，因为它真的很方面开发，不过你也需要仔细阅读它的文档，避免不知不觉掉入坑中。