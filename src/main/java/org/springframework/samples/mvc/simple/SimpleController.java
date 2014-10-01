package org.springframework.samples.mvc.simple;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.skplanet.plandasj.Plandasj;
import com.skplanet.plandasj.PlandasjPool;

@Controller
public class SimpleController {

	@Resource(name="plandasjpool1")
	PlandasjPool pool;
	
	@Resource(name="plandasjpool2")
	PlandasjPool pool2;

	@Autowired
	HelloService helloService;

	@RequestMapping("/simple")
	public @ResponseBody String simple() {
		return "Hello world!";
	}

	@RequestMapping("/plandas")
	public @ResponseBody String plandas() {

		String value = null;
		for (int idx = 0; idx < 1000; idx++) {
			Plandasj client = pool.getClient();
			if(client.exists("hello")){
				System.out.println(client.get("hello"));
			}else{
				client.set("hello", "hello world");
				System.out.println(client.get("hello"));
				
			}
			client.del("hello");
		}
		return value;
	}

	@RequestMapping("/plandas2")
	public @ResponseBody String plandas2() {

		for (int idx = 0; idx < 1000; idx++) {
			// First method execution using key="Josh", not cached
//			System.out.println("message: " + helloService.getMessage("Josh"));

			// Second method execution using key="Josh", still not cached
//			System.out.println("message: " + helloService.getMessage("Josh"));

			// First method execution using key="Joshua", not cached
			System.out.println("message: " + helloService.getMessage("Joshua"));

			// Second method execution using key="Joshua", cached
			System.out.println("message: " + helloService.getMessage("Joshua"));

			System.out.println("Done.");
		}

		return "sucess";
	}

}