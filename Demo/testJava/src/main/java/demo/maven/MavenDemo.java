package demo.maven;

/**
 * Apache Maven是一个软件项目管理和综合工具。基于项目对象模型（POM）的概念，Maven可以从一个中心资料片管理项目构建，报告和文件。
 * Maven提供了开发人员构建一个完整的生命周期框架。开发团队可以自动完成项目的基础工具建设，Maven使用标准的目录结构和默认构建生命周期。 http://elim.iteye.com/blog/1827778
 * <p>
 * 
 * 
 */
public interface MavenDemo {
    /**
     * 
     * default生命周期是由以下的阶段组成：
     * <ol>
     * <li>validate – 验证该项目是否正确，所有必要的信息都是可用的
     * <li>compile – 编译工程源码
     * <li>test – 使用一个合适的单元测试框架测试编译的源代码。这些测试的代码不会被打包或部署到项目中
     * <li>package – 将编译的代码打包成它发布的格式，例如JAR
     * <li>integration-test – 如果必要的话，该命令会将工程处理并部署在一个集成测试运行的环境中
     * <li>verify – 运行任何检查以验证该包是否有效，是否符合质量标准
     * <li>install – 将工程打包安装到本地仓库中，以便本地其他项目可以进行依赖
     * <li>deploy – 在集成或发布环境中，将最终工程打包复制到远程仓库中，用于与其他开发人员和项目共享
     * </ol>
     * <p>
     * 这些生命周期阶段（包括那些这里没有展示的其他生命周期阶段）会顺序地执行来完成默认的生命周期。
     * 上述的生命周期阶段，意味着当你使用默认的生命周期时，Maven将首先验证项目，然后将编译源代码，运行那些单元测试，再打包二进制文件（例如：jar），
     * 然后再对包文件进行集成测试，再校验包文件，并将已经校验的包文件安装到本地仓库，然后在指定的环境中部署包。 要做到所有这些，你只需要调用最后一个生命阶段来执行，在这种情况下进行部署：mvn deploy。
     * 这是因为如果你调用了一个生命阶段，它不仅执行指定的构建阶段，而且会执行指定构建阶段之前的每一个阶段。因此，执行 mvn integration-test 将会先执行该阶段之前的每一个阶段（validate, compile,
     * package等）。
     * <p>
     * 
     * <h2>生命周期参考</h2> 下面的列表就是default，clean和site生命周期中的所有构建阶段，他们将会按照他们定义的顺序去执行。
     * <h3>Clean生命周期</h3>
     * <ol>
     * <li>pre-clean 执行一些清理前需要完成的工作
     * <li>clean 清理上一次构建生成的文件
     * <li>post-clean 执行一些清理后需要完成的工作
     * </ol>
     * <h3>Default生命周期</h3>
     * <ol>
     * <li>validate 校验项目是否正确以及所有重要信息是否可用
     * <li>initialize 初始化构建状态，例如：设置属性或者创建目录
     * <li>generate-sources 生成编译中包含的源代码
     * <li>process-sources 处理项目资源文件，例如过滤一些值
     * <li>generate-resources 生成包中包含的资源
     * <li>process-resources 复制并处理资源到目标目录，准备打包
     * <li>compile 编译项目中的源代码
     * <li>process-classes 后处理编译生成的文件，例如：对class文件进行字节码增强
     * <li>generate-test-sources 生成编译中包含的测试源码
     * <li>process-test-sources 处理测试源代码，例如：过滤一些值
     * <li>generate-test-resources 创建测试用的资源文件
     * <li>process-test-resources 复制并处理资源到目标测试目录
     * <li>test-compile 编译测试源码放入测试目标文件夹中
     * <li>process-test-classes 后处理测试编译生成的文件，例如：对class文件进行字节码增强，对Maven 2.0.5及以上有效
     * <li>test 使用单元测试框架运行测试。测试代码不会被打包或者部署。
     * <li>prepare-package 执行必要的操作，在真正打包前准备一个包。这通常会产生一个未打包、处理过版本。（Maven 2.1及以上）
     * <li>package 接受编译好的代码，打包成可发布的格式，如：JAR
     * <li>pre-integration-test 在集成测试执行前进行些必要的操作。这也许会涉及相关的东西，例如安装必须的环境。
     * <li>integration-test 处理并将包文件部署到继承测试运行的环境
     * <li>post-integration-test 继承测试执行之后进行的必要操作。这可能包含了清理环境操作。
     * <li>verify 运行一些校验去验证包是否有效，是否符合质量标准。
     * <li>install 安装包到本地仓库，供本地其他项目依赖使用
     * <li>deploy 将最终的包复制到远程仓库，供其他开发人员和Maven项目使用
     * </ol>
     * <h3>Site生命周期</h3>
     * <ol>
     * <li>pre-site 执行一些在生成站点之前需要完成的工作
     * <li>site 生成项目的站点文档
     * <li>post-site 执行一些在生成站点之后需要完成的工作
     * <li>site-deploy 将生成的站点文件发布到远程服务器上
     * </ol>
     * <p>
     * default生命期更多的关注于构建代码。由于你不能直接执行default生命期，你需要执行其中一个构建阶段或者构建目标。default生命期包含了相当多的构建阶段和目标，这里不会所有都介绍。最常用的构建阶段有：
     * 
     * <li>validate 验证项目的正确性，以及所有必需的信息都是否都存在。同时也会确认项目的依赖是否都下载完毕。
     * <li>compile 编译项目的源代码
     * <li>test 选择合适的单元测试框架，对编译后的源码执行测试；这些测试不需要代码被打包或者部署。
     * <li>package 将编译后的代码以可分配的形式打包，如Jar包。
     * <li>install 将项目打包后安装到本地仓库，可以作为其它项目的本地依赖。
     * <li>deploy 将最终的包复制到远程仓库，与其它开发者和项目共享。
     */
    void 生命周期();

    /**
     * <h2>配置外部依赖</h2>示例如下：
     * 
     * <pre>
    <dependency>
      <groupId>groupId</groupId>
      <artifactId>artifactId</artifactId>
      <scope>system</scope>
      <version>1.0</version>
      <systemPath>${basedir}\war\WEB-INF\lib\mydependency.jar</systemPath>
      <optional>true</optional>
    </dependency>
     * </pre>
     * 
     * <li>groupId和artifactId为依赖的名称，即API的名称。
     * <li>scope属性为system。
     * <li>systemPath属性为jar文件的路径。${basedir}为pom文件所在的目录，路径中的其它部分是相对于该目录而言的。
     * <li>optional：假如你的Project A的某个依赖D添加了<optional>true</optional>，当别人通过pom依赖Project A的时候，D不会被传递依赖进来。
     * 当你依赖某各工程很庞大或很可能与其他工程的jar包冲突的时候建议加上该选项，可以节省开销，同时减少依赖冲突
     * <p>
     * <h3>依赖范围</h3>
     * <p>
     * 依赖范围控制哪些依赖在哪些classpath 中可用，哪些依赖包含在一个应用中。让我们详细看一下每一种范围：
     * <li>compile （编译范围） compile是默认的范围；如果没有提供一个范围，那该依赖的范围就是编译范围。编译范围依赖在所有的classpath 中可用，同时它们也会被打包。
     * <li>provided （已提供范围） provided 依赖只有在当JDK 或者一个容器已提供该依赖之后才使用。 例如， 如果你开发了一个web 应用，你可能在编译 classpath 中需要可用的Servlet API
     * 来编译一个servlet，但是你不会想要在打包好的WAR 中包含这个Servlet API； 这个Servlet API JAR 由你的应用服务器或者servlet 容器提供。已提供范围的依赖在编译classpath
     * （不是运行时）可用。它们不是传递性的，也不会被打包。
     * <li>runtime （运行时范围） runtime 依赖在运行和测试系统的时候需要，但在编译的时候不需要。比如，你可能在编译的时候只需要JDBC API JAR，而只有在运行的时候才需要JDBC 驱动实现。
     * <li>test （测试范围） test范围依赖 在一般的编译和运行时都不需要，它们只有在测试编译和测试运行阶段可用。
     * <li>system （系统范围） system范围依赖与provided 类似，但是你必须显式的提供一个对于本地系统中JAR 文件的路径。
     * 这么做是为了允许基于本地对象编译，而这些对象是系统类库的一部分。这样的构件应该是一直可用的，Maven 也不会在仓库中去寻找它。 如果你将一个依赖范围设置成系统范围，你必须同时提供一个 systemPath
     * 元素。注意该范围是不推荐使用的（你应该一直尽量去从公共或定制的 Maven 仓库中引用依赖）。
     * <p>
     * <h3>快照依赖</h3>
     * <p>
     * 快照依赖指的是那些还在开发中的依赖（jar包）。与其经常地更新版本号来获取最新版本，不如你直接依赖项目的快照版本。
     * 快照版本的每一个build版本都会被下载到本地仓库，即使该快照版本已经在本地仓库了。总是下载快照依赖可以确保本地仓库中的每一个build版本都是最新的。
     * 在pom文件的最开头（设置groupId和artifactId的地方），在版本号后追加-SNAPSHOT，则告诉Maven你的项目是一个快照版本。如： <version>1.0-SNAPSHOT</version>
     * 可以看到加到版本号后的-SNAPSHOT。 在配置依赖时，在版本号后追加-SNAPSHOT表明依赖的是一个快照版本。如：
     * 
     * <pre>
    <dependency>
        <groupId>com.jenkov</groupId>
        <artifactId>java-web-crawler</artifactId>
        <version>1.0-SNAPSHOT</version>
    </dependency>
     * </pre>
     * 
     * 追加在version后的-SNAPSHOT告诉Maven这是一个快照版本。
     * 
     * 
     * <h3>dependencies与dependencyManagement的区别</h3>
     * <p>
     * 在我们项目顶层的POM文件中，我们会看到dependencyManagement元素。通过它元素来管理jar包的版本，让子项目中引用一个依赖而不用显示的列出版本号。
     * Maven会沿着父子层次向上走，直到找到一个拥有dependencyManagement元素的项目，然后它就会使用在这个dependencyManagement元素中指定的版本号。
     * <p>
     * 这样做的好处：统一管理项目的版本号，确保应用的各个项目的依赖和版本一致，才能保证测试的和发布的是相同的成果，因此，在顶层pom中定义共同的依赖关系。
     * 同时可以避免在每个使用的子项目中都声明一个版本号，这样想升级或者切换到另一个版本时，只需要在父类容器里更新，不需要任何一个子项目的修改；
     * 如果某个子项目需要另外一个版本号时，只需要在dependencies中声明一个版本号即可。子类就会使用子类声明的版本号，不继承于父类版本号。
     * <p>
     * Dependencies 相对于dependencyManagement，所有生命在dependencies里的依赖都会自动引入，并默认被所有的子项目继承。
     * <p>
     * 区别:
     * <li>dependencies即使在子项目中不写该依赖项，那么子项目仍然会从父项目中继承该依赖项（全部继承）
     * <li>dependencyManagement里只是声明依赖，并不实现引入，因此子项目需要显示的声明需要用的依赖。 如果不在子项目中声明依赖，是不会从父项目中继承下来的；
     * 只有在子项目中写了该依赖项，并且没有指定具体版本，才会从父项目中继承该项，并且version和scope都读取自父pom; 另外如果子项目中指定了版本号，那么会使用子项目中指定的jar版本。
     */
    void dependency();

    /**
     * <h2>Maven标准的目录结构</h2>
     * <p>
     * Maven有一个标准的目录结构。如果你在项目中遵循Maven的目录结构，就无需在pom文件中指定源代码、测试代码等目录。
     * 
     * <pre>
    - src
      -- main
        --- java
        --- resources
        --- webapp
      -- test
        --- java
        --- resources
    - target
     * </pre>
     * 
     * src目录是源代码和测试代码的根目录。main目录是应用的源代码目录。test目录是测试代码的目录。main和test下的java目录，分别表示应用的java源代码和测试代码。
     * resources目录包含项目的资源文件，比如应用的国际化配置的属性文件等。 如果是一个web项目，则webapp目录为web项目的根目录，其中包含如WEB-INF等子目录。
     * target目录是由Maven创建的，其中包含编译后的类文件、jar文件等。当执行maven的clean目标后，target目录会被清空。
     */
    void maven标准目录结构();

    /**
     * <h2>Maven 的仓库</h2>
     * 
     * <h3>Maven的本地库</h3>找到 {M2_HOME}\conf\setting.xml, 更新 localRepository 到其它路径。
     * <h3>Maven中央存储库</h3>当你建立一个 Maven 的项目，Maven 会检查你的 pom.xml 文件，以确定哪些依赖下载。
     * 首先，Maven将从本地资源库获得Maven的本地资源库依赖资源，如果没有找到，然后把它会从默认的 Maven 中央存储库(http://repo1.maven.org/maven/)查找下载。
     * <h3>远程存储库</h3>在Maven中，当你声明的库不存在于本地存储库中，也没有不存在于Maven中心储存库，该过程将停止并将错误消息输出到 Maven 控制台。 你需要声明远程仓库在 pom.xml 文件
     * <repositories> <repository> <id>java.net</id> <url>https://maven.java.net/content/repositories/public/</url>
     * </repository> </repositories>
     * <p>
     * 现在，Maven的依赖库查询顺序更改为：
     * <ol>
     * <li>在 Maven 本地资源库中搜索，如果没有找到，进入第 2 步，否则退出。
     * <li>在 Maven 中央存储库搜索，如果没有找到，进入第 3 步，否则退出。
     * <li>在java.net Maven的远程存储库搜索，如果没有找到，提示错误信息，否则退出。
     * <ol>
     * <p>
     * <h3>添加远程仓库</h3> 添加Java.net远程仓库的详细信息在“pom.xml”文件。 <repositories> <repository> <id>java.net</id>
     * <url>https://maven.java.net/content/repositories/public/</url> </repository> </repositories>
     * <p>
     * 添加JBoss远程仓库的详细信息在 “pom.xml” 文件中 <repositories> <repository> <id>JBoss repository</id>
     * <url>http://repository.jboss.org/nexus/content/groups/public/</url> </repository> </repositories>
     * <h3>定制库到Maven本地资源库</h3>这里有2个案例，需要手动发出Maven命令包括一个 jar 到 Maven 的本地资源库。
     * <li>要使用的 jar 不存在于 Maven 的中心储存库中。
     * <li>您创建了一个自定义的 jar ，而另一个 Maven 项目需要使用。
     * <p>
     * 例如，kaptcha，它是一个流行的第三方Java库，它被用来生成 “验证码” 的图片，以阻止垃圾邮件，但它不在 Maven 的中央仓库中。
     * 
     * <pre>
     * mvn install:install-file -Dfile=c:\kaptcha-2.3.jar -DgroupId=com.google.code -DartifactId=kaptcha -Dversion=2.3 -Dpackaging=jar
     * </pre>
     * 
     */
    void repository();

    /**
     * <h2>Maven 的插件</h2>
     * <p>
     * Maven 是一个执行插件的框架，每一个任务实际上是由插件完成的。 Maven 插件通常用于：创建 jar 文件、创建 war 文件 、编译代码文件、进行代码单元测试、创建项目文档、创建项目报告。
     * 一个插件通常提供了一组目标，可使用以下语法来执行：
     * <li>mvn [plugin-name]:[goal-name]
     * <p>
     * 例如，一个 Java 项目可以使用 Maven 编译器插件来编译目标，通过运行以下命令编译: mvn compiler:compile
     * <p>
     * Maven 提供以下两种类型插件：
     * <li>构建插件： 在生成过程中执行，并在 pom.xml 中的<build/> 元素进行配置
     * <li>报告插件：在网站生成期间执行，在 pom.xml 中的 <reporting/> 元素进行配置
     * <p>
     * 常见的插件列表：
     * <li>clean 编译后的清理目标，删除目标目录
     * <li>compiler 编译 Java 源文件
     * <li>surefile 运行JUnit单元测试，创建测试报告
     * <li>jar 从当前项目构建 JAR 文件
     * <li>war 从当前项目构建 WAR 文件
     * <li>javadoc 产生用于该项目的 Javadoc
     * <li>antrun 从构建所述的任何阶段运行一组 Ant 任务
     * 
     * <h3>关键概念：</h3>
     * <p>
     * <li>插件可在 pom.xml 使用的 plugin 元素来指定；
     * <li>每个插件可以有多个目标；
     * <li>从插件应使用它的相位元素开始处理定义阶段。这里已经使用 clean 阶段；
     * <li>可以通过将它们绑定到插件的目标来执行配置任务。这里已经绑定 echo 任务到 maven-antrun-plugin 的运行目标；
     * <li>就这样，Maven将处理其余部分。如果没有可用的本地存储库，它会下载这个插件；
     * 
     * <p>
     * maven会在当前项目中加载plugins声明的插件；
     * pluginManagement是表示插件声明，即你在项目中的pluginManagement下声明了插件，maven不会加载该插件，pluginManagement声明可以被继承。
     * pluginManagement的一个使用案例是当有父子项目的时候，父项目中可以利用pluginManagement声明子项目中需要用到的插件，
     * 之后，当某个或者某几个子项目需要加载该插件的时候，就可以在子项目中plugins节点只配置 groupId 和 artifactId就可以完成插件的引用。
     * pluginManagement主要是为了统一管理插件，确保所有子项目使用的插件版本保持一致，类似的还是dependencies和dependencyManagement。
     */
    void plugin();

    /**
     * <h2>maven 命令：</h2>
     * <li>查看版本：mvn -version
     * <li>创建Maven项目: mvn archetype:generate
     * <li>编译源程序: mvn compile。 This will run Maven, telling it to execute the compile goal. When it’s finished, you
     * should find the compiled .class files in the target/classes directory.
     * <li>项目打包: mvn package。 The package goal will compile your Java code, run any tests, and finish by packaging the
     * code up in a JAR file within the target directory. The name of the JAR file will be based on the project’s
     * <artifactId> and <version>. If you’ve changed the value of <packaging> from "jar" to "war", the result will be a
     * WAR file within the target directory instead of a JAR file. Maven also maintains a repository of dependencies on
     * your local machine (usually in a .m2/repository directory in your home directory) for quick access to project
     * dependencies. If you’d like to install your project’s JAR file to that local repository, then you should invoke
     * the install goal: mvn install
     * <li>mvn install。 The install goal will compile, test, and package your project’s code and then copy it into the
     * local dependency repository, ready for another project to reference it as a dependency.
     * <li>编译并测试 mvn test
     * <li>清空生成的文件 mvn clean
     * <li>将maven项目转化为eclipse项目 mvn eclipse:eclipse
     * <li>清除有关eclipse项目的配置信息： mvn -Dwtpversion=1.0 eclipse:clean
     * <li>联合使用 mvn eclipse:clean clean
     * <li>启动tomcat mvn tomcat:run
     * <li>启动jetty mvn jetty:run
     * <li>有效pom。 考虑到pom文件的继承关系，当Maven执行的时候可能很难确定最终的pom文件的内容。 总的pom文件（所有继承关系生效后）被称为有效pom（effective
     * pom）。可以使用以下的命令让Maven打印出当前的有效pom： mvn help:effective-pom。 ，可以看到默认的项目源文件夹结构，输出目录，插件，资料库，报表目录，Maven将使用它们来执行预期的目标。
     * 
     * <li>通过mvn install命令可以将你自己的项目构建并安装到本地仓库中。这样，你的其它项目就可以通过在pom文件将该jar包作为外部依赖来使用。 Maven安装第三方Jar包到本地仓库: mvn
     * install:install-file -Dfile= -DgroupId= -DartifactId= -Dversion= -Dpackaging=。 如：
     * 
     * <pre>
     * mvn install:install-file -Dfile=c:/driver/ojdbc14.jar -DgroupId=com.oracle -DartifactId=ojdbc14 -Dversion=10.2.0 -Dpackaging=jar
     * </pre>
     * 
     * 有时在你的构建过程中，需要将第三方jar包添加到本地仓库中，因为它并不存在于像Maven中央仓库或其它的公共仓库中。为了让Maven能够正确获取到jar包，第三方jar包必须放置到本地仓库的正确位置上。
     * 如果也有pom文件的话，你可以使用下面的命令安装：
     * 
     * <pre>
     * mvn install:install-file -Dfile= -DpomFile=
     * </pre>
     * 
     * 如果jar包是通过Maven构建的，它会在目录META-INF的子文件夹下创建一个pom.xml文件，这个Jar包会被默认读取。在这种情况下，你只需要这么做：
     * 
     * <pre>
     * mvn install:install-file -Dfile=
     * </pre>
     * 
     * <li>多环境打包： 开发环境： filter是在maven的compile阶段执行过虑替换的，所以只要触发了编译动作即可，如果像以前一样正常使用发现没有替换，则手工clean一下工程（eclipse -> Project
     * -> Clean）【这里你应该要安装上maven插件，因为替换是maven做的，不是eclipse做的，所以这里的clean应当是触发了maven的compile】，然后在Tomcat上也右键 ->
     * Clean一下即可，然后你去tomcat目录下会发现你的工程的资源文件里面的${key}被替换为对应的config-xx的值了。 如果上面还不行，那么就使用maven插件或者手工控制台下打maven编译命令吧
     * 因为pom.xml中设置了dev为默认激活的，所以默认会把config-dev拿来进行替换${key}。 测试环境 手工编译，打包：maven clean install -Ptest --
     * 激活id="test"的profile 生产环境 手工编译，打包：maven clean install -Pproduct -- 激活id="product"的profile
     * <li>你可以向mvn命令传入多个参数，执行多个构建周期或阶段，如：mvn clean install 该命令首先执行clean构建周期，删除Maven输出目录中已编译的类文件，然后执行install构建阶段。
     * 也可以执行一个Maven目标（构建阶段的一部分），将构建阶段与目标名以冒号(:)相连，作为参数一起传给Maven命令。例如：mvn dependency:copy-dependencies
     * 该命令执行`dependency`构建阶段中的`copy-dependencies`目标。
     * <li>运行打包部署，在maven项目目录下： mvn tomcat:deploy
     * <li>撤销部署： mvn tomcat:undeploy
     * <li>mvn spring-boot:run
     * <li>启动web应用： mvn tomcat:start
     * <li>停止web应用： mvn tomcat:stop
     * <li>重新部署： mvn tomcat:redeploy
     * <li>部署展开的war文件： mvn war:exploded tomcat:exploded
     * <li>加载缺省依赖包： mvn dependency:tree ,来查看项目实际的依赖
     * <li>mvn clean package -Dmaven.test.skip=true
     */
    void command();

}
