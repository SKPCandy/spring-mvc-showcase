/**
 * 
 */
package org.springframework.samples.mvc.simple;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

/**
 * @author horanghi
 * 
 */
@Service("helloService")
public class HelloServiceImpl implements HelloService {

	@Cacheable(value = "messageCache")
	public String getMessage(String name) {
		System.out.println("===> Not Cache HelloServiceImpl" + ".getMessage(\"" + name + "\")");
		return "Hello " + name + "!";
	}

	@CacheEvict(value = "messageCache")
	public String evictMessage(String name) {
		System.out.println("Executing HelloServiceImpl" + ".evictValuec2(\" horanghi \")");
		return "horanghi";
	}

	@CacheEvict(value = { "messageCache", "messageCache01", "messageCache02" }, allEntries = true)
	public void evictAll() {
	}
}
