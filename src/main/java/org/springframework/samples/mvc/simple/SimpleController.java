package org.springframework.samples.mvc.simple;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.skplanet.plandasj.Plandasj;
import com.skplanet.plandasj.PlandasjPool;

@Controller
public class SimpleController {

	@Autowired
	PlandasjPool pPool;

	final String key = "hello";
	final String value = "Plandas's world";

	@RequestMapping("/simple")
	public @ResponseBody
	String simple() {
		Plandasj client = pPool.getClient();
		client.set(key, value);

		String retValue = client.get(key);

		client.del(key);
		return retValue;
	}

}
