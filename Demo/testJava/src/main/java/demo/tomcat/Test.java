package demo.tomcat;

import java.io.File;

import org.apache.catalina.Context;
import org.apache.catalina.Host;
import org.apache.catalina.core.StandardContext;
import org.apache.catalina.startup.ContextConfig;
import org.apache.catalina.startup.Tomcat;
import org.apache.tomcat.util.buf.ByteChunk;

public class Test {

	public static void main(String[] args) {
		Tomcat tomcat = getTomcatInstance();
		File appDir = new File(getBuildDirectory(), "webapps/examples");
		tomcat.addWebapp(null, "/examples", appDir.getAbsolutePath());
		tomcat.start();
		ByteChunk res = getUrl("http://localhost:" + getPort() + "/examples/servlets/servlet/HelloWorldExample");
		assertTrue(res.toString().indexOf("<h1>Hello World!</h1>") > 0);
	}

	public Context addWebapp(Host host, String url, String path) { 
        silence(url); 
        Context ctx = new StandardContext(); 
        ctx.setPath( url ); 
        ctx.setDocBase(path); 
        if (defaultRealm == null) { 
            initSimpleAuth(); 
        } 
        ctx.setRealm(defaultRealm); 
        ctx.addLifecycleListener(new DefaultWebXmlListener()); 
        ContextConfig ctxCfg = new ContextConfig(); 
        ctx.addLifecycleListener(ctxCfg); 
        ctxCfg.setDefaultWebXml("org/apache/catalin/startup/NO_DEFAULT_XML"); 
        if (host == null) { 
            getHost().addChild(ctx); 
        } else { 
            host.addChild(ctx); 
        } 
        return ctx; 
 }
}
