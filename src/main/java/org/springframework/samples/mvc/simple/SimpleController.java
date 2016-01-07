package org.springframework.samples.mvc.simple;

import java.util.Random;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class SimpleController {
	//
	// @Resource(name = "plandasjpool")
	// PlandasjPool pool;

	@Resource(name = "helloService")
	HelloService hello;

	final Random rd = new Random();

	final String value = "0000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000";

	// @RequestMapping("/plandas2")
	// public @ResponseBody String plandas2() {
	// Plandasj client = pool.getClient();
	// rd.setSeed(100);
	// int idvalue = rd.nextInt();
	//
	// Pipeline pl = client.pipelined();
	// for (int idx = 0; idx < 10; idx++) {
	// pl.set("hello" + idvalue + idx, value);
	// }
	// pl.sync();
	//
	// for (int idx = 0; idx < 10; idx++) {
	// System.out.println(client.get("hello" + idvalue + idx) != null);
	// }
	//
	// pl = client.pipelined();
	// for (int idx = 0; idx < 1000; idx++) {
	// pl.del("hello" + idvalue + idx);
	// }
	// pl.sync();
	// return "OK";
	// }

//	@RequestMapping("/plandas1")
//	public @ResponseBody String plandas1() {
//		Plandasj client = pool.getClient();
//		rd.setSeed(100);
//		int idvalue = rd.nextInt();
//
//		for (int idx = 0; idx < 10; idx++) {
//			client.set("hello" + idvalue + idx, value);
//		}
//
//		for (int idx = 0; idx < 10; idx++) {
//			System.out.println(client.get("hello" + idvalue + idx) != null);
//		}
//
//		for (int idx = 0; idx < 1000; idx++) {
//			client.del("hello" + idvalue + idx);
//		}
//		return "OK";
//	}

	@RequestMapping("/hello")
	public @ResponseBody String hello() {
		hello.getMessage("First");
		return "OK";
	}

	// @RequestMapping("/mosaic")
	// public @ResponseBody String spatial() {
	// Plandasj client = pool.getClient();
	// StringBuffer sb = new StringBuffer();
	// sb.append("{ \"machants\": [");
	// int idx = 0;
	// for (Point point : client.gpradius("geo", 37.39627, 127.10998, 1000, UNITS.M)) {
	// if (idx++ != 0)
	// sb.append(",");
	// sb.append("{ \"latitude\":" + point.getX() + ", \"longitude\":" + point.getY() + "}");
	// }
	// sb.append("]}");
	// return sb.toString();
	// }

}