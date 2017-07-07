/**
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements. See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership. The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package demo.webservice.cxf.demo1.client;

import org.apache.cxf.frontend.ClientProxyFactoryBean;
import org.apache.cxf.jaxws.JaxWsProxyFactoryBean;

import test.webservice.cxf.demo1.server.HelloWorld;

public final class Client {

    private Client() {
    } 

    public static void main(String args[]) throws Exception {
    	//����WebService�ͻ��˴��?��  
        JaxWsProxyFactoryBean factory = new JaxWsProxyFactoryBean(); 
//        ClientProxyFactoryBean factory = new ClientProxyFactoryBean();
        
        //ע��WebService�ӿ�  
        factory.setServiceClass(HelloWorld.class);  
        //����WebService��ַ  
        factory.setAddress("http://localhost:9000/HelloWorld");
        
        HelloWorld iHelloWorld = (HelloWorld)factory.create();  
        System.out.println("invoke webservice...");  
        System.out.println("message context is:"+iHelloWorld.sayHi(" Stan"));  
        System.exit(0);  
    }

}
