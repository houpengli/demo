2004年spring框架首次发布，然后陆续发布了一些重要的版本：Spring2.0提供XML命名空间和AspectJ的支持；Spring2.5包含了注释驱动配置；Spring3.0在框架基础代码中引入了强大的Java5+，并且提供诸如基于Java的@Configuration模式。

4.0版本是Spring框架最新发布的主版本，并且首次完全支持Java8的功能。你依然可以使用较早的Java版本，但是现在所需的最小的版本已经被提升的Java SE6。同时还利用主版本发布的机会删除了很多废弃的类和方法。

在Spring框架的GitHub Wiki上提供了升级到Spring4.0的指南。

1. 入门体验的改善

新的spring.io网站提供完整的入门指南系列来帮助初学者。在本文档的第一章Spring入门中，你可以读到更多的有关这个指南的信息。这个网站还提供很多在Spring体系下发布的其他一些工程的详细的概要。

如果你是一个Maven用户，那么你可能还要关注与每个Spring框架版本一起发布的资料清单（POM）。

2. 被删除的废弃的包和方法

所有被废弃的包和很多被废弃的类和方法已经从4.0版中删除，如果从Spring之前的发布版本中升级，要确保修正那些对被废弃的内容的调用，以免使用过期的API。

对于全部改变内容，可查阅API差异报告。

注意，那些可选的第三方依赖已经被升级到2010/2011以后的版本（也就是说Spring 4只支持2010年以后释放的版本）：尤其是Hibernate3.6+、EhCache2.1+、Quartz1.8+、Groovy1.8+、和Joda-Time2.0+。一个例外的规则是：目前Spring4要求最近的hibernate验证器4.3+，并且支持的Jackson已经聚焦到2.0+（在Spring3.2中保留的对Jackson1.8/1.9的支持，现在已经过时了）。

3. java 8（包括6和7）

Spring框架4.0提供了对几个Java 8功能的支持。你可以通过Spring的回调接口来使用lambda表达式和方法引用。 首先支持的类是java.time(JSR-310)和几个已经被改造成@Repeatable的既存标注。你还可以使用Java8 的参数名称发现机制（-parameters编译标记）来对调试信息的使用进行选择性编译。

Spring保留了跟Java和JDK较早版本的兼容性：具体的是Java SE6（JDK 6最小的版本级别要升级到18，这是2010年2月释放的版本）及以上的版本依然是完全支持的。但是基于Spring4最新开发的项目，我们推荐使用Java7或8。

4. Java EE 6和7

Spring框架4使用java ee 6或以上的版本来作为基线，同时包含了相关的JPA2.0和Servlet3.0的规范。为了保留跟Google App引擎和旧的应用服务的兼容性，可能要把Spring 4应用程序部署到Servlet 2.5的环境中。但是在安装了Spring的测试和模拟包的开发环境中，强烈推荐使用Servlet3.0+。

提示：如果你是WebSphere 7的用户，要确保安装JPA2.0的补丁包。在WebLogic 10.3.3或更高的版本中带有JAP2.0的补丁包。这样就会把与Spring 4兼容的部署环境带到这两类服务器中。

前面介绍了当前Spring 框架 4.0支持的Java EE 7的应用程序规范级别，包括：JMS 2.0、JTA 1.2、JPA2.1、Bean Validation 1.1和JSR-236 Concurrency Utilities。通常这种支持只是关注这些规范的独立使用，如在Tomcat或独立的环境中。同样当把Spring应用程序部署到Java EE 7服务上时它的工作方式也是一样的。

注意，Hibernate 4.3是JPA2.1的提供器，因此只在Spring 框架4.0中支持。同样Hibernate Validator 5.0作为Bean Validation提供器也适用于这个约束。这两者都没有提供官方的对Spring框架3.2的支持。


5. 使用Groovy的DSL（DomainSpecific Languages）来定义 Bean

从Spring框架4.0开始，可以使用Groovy的 DSL来定义外部的bean配置。这有点类似使用XML Bean定义的概念，但是它允许使用更加简洁的语法。使用Groovy也允许更加容易的把bean定义嵌入到你的应用程序的启动代码中，如：

def reader = new GroovyBeanDefinitionReader(myApplicationContext)
reader.beans {
    dataSource(BasicDataSource) {
        driverClassName = "org.hsqldb.jdbcDriver"
        url = "jdbc:hsqldb:mem:grailsDB"
        username = "sa"
        password = ""
        settings = [mynew:"setting"]
    }
    sessionFactory(SessionFactory) {
        dataSource = dataSource
    }
    myService(MyService) {
        nestedBean = { AnotherBean bean ->
            dataSource = dataSource
        }
    }
}
更多的信息可以查看GroovyBeanDefinitionReader的javadocs

6. 内核容器方面的改善

以下是对核心容器的几个方面的常规改善：

A. 现在Spring可以在注入Bean的时候处理修饰样式的的泛型。例如，如果要使用Spring的数据资源库（Repository），就可以很容易的注入一个特定的实现：@AutowiredRepository<Customer> customerRepository。

B. 如果使用Spring的元注解支持，那么现在可以开发暴露来自源注解中的特定属性的个性化注解。

C. 现在列表和数组中的bean是可以被排序的，它同时支持@Order注解和ordered接口。

D. 现在在注入点可以使用@Lazy注解，跟@Bean定义一样。

E. 引入了@Description注解，方便开发者使用基于Java的配置。

F. 通过@Conditional注解来作为条件过滤bean的常用模式。这有点类似@Profile支持，但它允许用户给开发的程序定义一些策略。

G. 基于CGLIB的代理类不再需要默认的构造器，它通过被重新包装在内部的objenesis类库来提供支持，这个类库是作为Spring框架的一部分来发布。使用这种策略，不再有用于代理示例调用的构造器了。

H. 通过框架提供管理时区的支持，如LocaleContext。

7. 常用的Web方面的改善

部署到Servlet 2.5依然是一个可选项，但当前的Spring 框架4.0主要关注Servlet3.0+环境。如果你是在使用Spring的MVC测试框架，那么就需要确保在测试的类路径中有与Servlet3.0兼容的JAR包。

另外，稍后会介绍WebSocket，下面是Spring的Web模块常规改善：

A. 你可以在Spring的MVC应用程序中使用新的@RestController注解，不再需要给每个@RequestMapping方法添加@ResponseBody。

B. 添加了AsyncRestTemplate类，它允许在开发REST客户端时支持非阻塞的异步支持。

C. 在开发Spring MVC应用程序时，Spring提供了全面的时区支持。

8. WebSocket、SockJS、和STOMP消息

新的spring-websocket模块提供了全面的基于WebSocket的支持，在Web应用程序的客户端和服务端之间有两种通信方式。它跟JSR-356兼容，用于浏览器的Java的WebSocket API和额外提供的基于SockJS的回退选项（如WebSocket模拟器）依然不支持WebSocket协议（如IE以前的版本）。

新的spring-messaging模块添加了对WebSocket的子协议STOMP的支持，它在应用程序中跟注解编程模式一起用于路由和处理来自WebSocket客户端的STOMP消息。现在一个@Controller就能够包含处理HTTP请求和来自被连接的WebSocket客户端的@RequestMapping和@MessageMapping方法的结果。这个模块还包含了来自Spring集成项目的关键抽象原型，如Message、MessageChannel、MessageHandler以及其他的基于消息的应用的基础服务。

更多的信息，请看第25章---WebSocket支持

9. 测试的改善

Spring框架4.0中删除了spring-test模块中的废弃代码，还引入了几个用于单元和集成测试的新功能：

A. 在spring-test模块中几乎所有的注解（如@ContextConfiguration、@WebAppConfiguration、@ContextHierarchy、@ActiveProfiles等）都可以使用元注解来创建个性化的组合注解并减少跨测试单元的配置成本。

B. 通过简单的编程实现个性化的ActiveProfilesResolver接口，并使用@ActiveProfiles的resolver属性就可以激活bean定义的配置。

C. 在spring-core模块中引入了新的SocketUtils类，它确保你可以扫描到本地主机上的闲置的TCP和UDP服务端口。这个功能不是专门提供给测试的，但是在编写需要使用套接字的集成测试代码是就非常有用，例如，测试内存中启动的SMTP服务、FTP服务、Servlet容器等。

D. 在Spring4.0的org.springframework.mock.web包中，有一组基于Servlet3.0 API的模仿器。此外，还增强了几个Servlet的API模仿器（如MockHttpServletRequest、MockServletContext等）的功能，并改善了可配置性。