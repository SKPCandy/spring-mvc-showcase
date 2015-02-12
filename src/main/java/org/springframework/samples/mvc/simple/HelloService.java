/**
 * 
 */
package org.springframework.samples.mvc.simple;


/**
 * @author horanghi
 * 
 */
public interface HelloService {

	public abstract String getMessage(String name);
	
	public abstract String evictMessage(String name);
	
	public abstract void evictAll();

}