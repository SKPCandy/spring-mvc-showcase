package org.springframework.samples.mvc.simple;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import redis.clients.jedis.GPoint;
import redis.clients.jedis.Geodis;

import com.skplanet.plandasj.Plandasj;
import com.skplanet.plandasj.PlandasjPool;

@Controller
public class SimpleController {

	@Resource(name = "plandasjpool1")
	PlandasjPool pool;

	@Resource(name = "plandasjpool2")
	PlandasjPool pool2;

	@Autowired
	HelloService helloService;

	Geodis geodis = new Geodis("172.19.114.201", 19005);

	@RequestMapping("/simple")
	public @ResponseBody String simple() {
		return "Hello world!";
	}

	@RequestMapping("/plandas")
	public @ResponseBody String plandas() {

		String value = null;
		for (int idx = 0; idx < 1000; idx++) {
			Plandasj client = pool.getClient();
			if (client.exists("hello")) {
				System.out.println(client.get("hello"));
			} else {
				client.set("hello", "hello world");
				System.out.println(client.get("hello"));

			}
			client.del("hello");
		}
		return value;
	}

	@RequestMapping("/plandas2")
	public @ResponseBody String plandas2() {

		// First method execution using key="Josh", not cached
		System.out.println("control message: " + helloService.getMessage("Josh"));

		// Second method execution using key="Josh", still not cached
		System.out.println("control message: " + helloService.getMessage("Josh"));

		// First method execution using key="Joshua", not cached
		System.out.println("control message(looking forward to cache): " + helloService.getMessage("Joshua"));

		// Second method execution using key="Joshua", cached
		System.out.println("control message(looking forward to cache): " + helloService.getMessage("Joshua"));

		System.out.println("control Done.");

		return "sucess";
	}

	@RequestMapping("/clean")
	public @ResponseBody String plandasClean() {

		helloService.evictAll();

		return "sucess";
	}

	@RequestMapping("/map")
	public void getmap() {

	}

	@RequestMapping("/spatial")
	public @ResponseBody String spatial() {
		StringBuffer sb = new StringBuffer();
		sb.append("{ \"machants\": [");
		int idx = 0;
		for (GPoint point : geodis.gsearchPoint("geo", 37.39627, 127.10998, 100000)) {
			if (idx++ != 0)
				sb.append(",");
			sb.append("{ \"latitude\":" + point.getX() + ", \"longitude\":" + point.getY() + "}");
		}
		sb.append("]}");
		return sb.toString();
	}
	
	@RequestMapping("/flush")
	public @ResponseBody String flush() {
		geodis.del("geo");
		return "good";
	}

	@RequestMapping("/plus1")
	public @ResponseBody String plus() {
		for (TupleDouble td : tuples1) {
			geodis.gadd("geo", td.getX(), td.getY(), 0, new Double(td.getX()).toString(), "");
		}

		return "good";
	}
	@RequestMapping("/plus2")
	public @ResponseBody String plus2() {
		for (TupleDouble td : tuples2) {
			geodis.gadd("geo", td.getX(), td.getY(), 0, new Double(td.getX()).toString(), "");
		}

		return "good";
	}
	@RequestMapping("/plus3")
	public @ResponseBody String plus3() {
		for (TupleDouble td : tuples3) {
			geodis.gadd("geo", td.getX(), td.getY(), 0, new Double(td.getX()).toString(), "");
		}

		return "good";
	}

	TupleDouble[] tuples1 = new TupleDouble[] { new TupleDouble(37.39627, 127.10998), new TupleDouble(37.39765248, 127.1114133),
			new TupleDouble(37.40128991, 127.1104474), new TupleDouble(37.38556173, 127.1214134),
			new TupleDouble(37.38556173, 127.1214134), new TupleDouble(37.38795193, 127.1242267),
			new TupleDouble(37.38427906, 127.1311763), new TupleDouble(37.38549868, 127.1196292) };

	TupleDouble[] tuples2 = new TupleDouble[] { new TupleDouble(37.37811028, 127.1122831), new TupleDouble(37.38963849, 127.0930039),
			new TupleDouble(37.39653679, 127.1172435), new TupleDouble(37.38571902, 127.1117569),
			new TupleDouble(37.38516429, 127.1114445), new TupleDouble(37.38621671, 127.1114189), new TupleDouble(37.3845421, 127.1312309),
			new TupleDouble(37.39751155, 127.1118061), new TupleDouble(37.38602028, 127.1206057),
			new TupleDouble(37.38611812, 127.1235869), new TupleDouble(37.38382427, 127.1349785) };

	TupleDouble[] tuples3 = new TupleDouble[] { new TupleDouble(37.41084952, 127.1268264), new TupleDouble(37.38599382, 127.1218071),
			new TupleDouble(37.38005566, 127.1168611), new TupleDouble(37.39750402, 127.1121494),
			new TupleDouble(37.39547353, 127.1136009), new TupleDouble(37.38660755, 127.1242787),
			new TupleDouble(37.38486346, 127.1205135), new TupleDouble(37.38425249, 127.111407), new TupleDouble(37.39123694, 127.0995962),
			new TupleDouble(37.40203181, 127.1071234), new TupleDouble(37.38378039, 127.1113611) };
}

class TupleDouble {
	double x = 0;
	double y = 0;

	TupleDouble(double x, double y) {
		this.x = x;
		this.y = y;
	}

	public double getX() {
		return x;
	}

	public double getY() {
		return y;
	}
}