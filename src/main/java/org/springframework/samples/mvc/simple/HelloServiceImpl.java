/**
 * 
 */
package org.springframework.samples.mvc.simple;

import org.springframework.stereotype.Service;

/**
 * @author horanghi
 * 
 */
@Service("helloService")
public class HelloServiceImpl implements HelloService {

//	@Cacheable(value = "messageCache", condition = "'Joshua'.equals(#name)") //,
	public String getMessage(String name) {
		System.out.println("Executing HelloServiceImpl" + ".getHelloMessage(\"" + name + "\")");
		//DB call 
		return "Hello " + name + "!";
	}

//	@Cacheable(value = "messageCache01", key="#name") 
	public String getValuec(String name) {
		System.out.println("Executing HelloServiceImpl" + ".getHelloMessage(\"" + name + "\")");

		return "Hello " + name + "!";
	}
	
//	@Cacheable(value = "messageCache02", key="#name") 
	public String getValuec2(String name) {
		System.out.println("Executing HelloServiceImpl" + ".getHelloMessage(\" horanghi \")");

		return "horanghi";
	}
}
