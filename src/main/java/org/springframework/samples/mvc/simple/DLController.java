package org.springframework.samples.mvc.simple;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

import javax.annotation.PreDestroy;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.jayway.jsonpath.JsonPath;
import com.skplanet.mosaic.Pipeline;
import com.skplanet.mosaic.Plamosaic;
import com.skplanet.mosaic.PlamosaicPool;

import redis.clients.jedis.Response;
import redis.clients.spatial.model.Img;

@Controller
public class DLController {

	PlamosaicPool jhdPool = new PlamosaicPool("172.19.114.205", 19000, "a1234");

	// String stringUrlPrefix = "http://175.126.56.112:15003/mosaic_request_handler?url=";

	@PreDestroy
	public void release() {
		try {
			jhdPool.release();
		} finally {
		}
	}

	@RequestMapping(value = "/queryhd", produces = "application/json; charset=utf8")
	public @ResponseBody String queryImg(@RequestParam(value = "itemkey") String itemkey, @RequestParam(value = "count") int count) {
		Plamosaic jedis = jhdPool.getClient();
		List<String> features;
		features = jedis.lrange("0:" + itemkey + "_signatures", 0, -1);
		if (features.isEmpty()) {
			features = jedis.lrange("1:" + itemkey + "_signatures", 0, -1);
			if (features.isEmpty()) {
				features = jedis.lrange("2:" + itemkey + "_signatures", 0, -1);
			}
		}
		if (features.isEmpty())
			return "nothing";

		String img_url = itemkey + "_temp";

		Pipeline pl = jedis.pipelined();
		for (String fe : features) {
			pl.rpush("0:" + img_url, fe);
			pl.rpush("1:" + img_url, fe);
			pl.rpush("2:" + img_url, fe);
		}
		Response<List<Img>> rs0 = pl.hdist("0:IMG", count, "0:" + img_url);
		Response<List<Img>> rs1 = pl.hdist("1:IMG", count, "1:" + img_url);
		Response<List<Img>> rs2 = pl.hdist("2:IMG", count, "2:" + img_url);

		pl.del("0:" + img_url);
		pl.del("1:" + img_url);
		pl.del("2:" + img_url);

		pl.sync();

		List<Img> rss = new ArrayList<Img>();
		rss.addAll(rs0.get());
		rss.addAll(rs1.get());
		rss.addAll(rs2.get());

		if (rss.size() == 0) {
			return "nothing";
		}

		Collections.sort(rss, new Comparator<Img>() {
			@Override
			public int compare(Img o1, Img o2) {
				if (o1.getValue() < o2.getValue()) {
					return 1;
				} else if (o1.getValue() > o2.getValue()) {
					return -1;
				} else {
					return 0;
				}
			}
		});

		StringBuffer sb = new StringBuffer("{ \"list\" : [");

		int size = (rss.size() > count) ? count : rss.size();

		for (int idx = 0; idx < size;) {
			Img img = (Img) rss.get(idx);
			if (idx++ != 0) {
				sb.append(",");
			}
			try {
				String _url = img.getUrl();
				double value = img.getValue();
				sb.append("{\"url\":\"" + _url + "\", \"value\": " + value + "}");
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
		sb.append("]}");

		return sb.toString();
	}

	@RequestMapping(value = "/queryud", produces = "application/json; charset=utf8")
	public @ResponseBody String queryImgUD(@RequestParam(value = "itemkey") String itemkey, @RequestParam(value = "count") int count) {
		Plamosaic jedis = jhdPool.getClient();
		List<Double> features;
		features = jedis.lrangeDouble("0:" + itemkey + "_features", 0, -1);
		if (features.isEmpty()) {
			features = jedis.lrangeDouble("1:" + itemkey + "_features", 0, -1);
			if (features.isEmpty()) {
				features = jedis.lrangeDouble("2:" + itemkey + "_features", 0, -1);
			}
		}
		if (features.isEmpty())
			return "nothing";

		String img_url = itemkey + "_temp";

		Pipeline pl = jedis.pipelined();
		for (double fe : features) {
			pl.rpushDouble("0:" + img_url, fe);
			pl.rpushDouble("1:" + img_url, fe);
			pl.rpushDouble("2:" + img_url, fe);
		}
		Response<List<Img>> rs0 = pl.udist("0:IMG", count, "0:" + img_url);
		Response<List<Img>> rs1 = pl.udist("1:IMG", count, "1:" + img_url);
		Response<List<Img>> rs2 = pl.udist("2:IMG", count, "2:" + img_url);

		pl.del("0:" + img_url);
		pl.del("1:" + img_url);
		pl.del("2:" + img_url);

		pl.sync();

		List<Img> rss = new ArrayList<Img>();
		rss.addAll(rs0.get());
		rss.addAll(rs1.get());
		rss.addAll(rs2.get());

		if (rss.size() == 0) {
			return "nothing";
		}

		Collections.sort(rss, new Comparator<Img>() {
			@Override
			public int compare(Img o1, Img o2) {
				if (o1.getValue() < o2.getValue()) {
					return -1;
				} else if (o1.getValue() > o2.getValue()) {
					return 1;
				} else {
					return 0;
				}
			}
		});

		StringBuffer sb = new StringBuffer("{ \"list\" : [");

		int size = (rss.size() > count) ? count : rss.size();

		for (int idx = 0; idx < size;) {
			Img img = (Img) rss.get(idx);
			if (idx++ != 0) {
				sb.append(",");
			}
			try {
				String _url = img.getUrl();
				double value = img.getValue();
				sb.append("{\"url\":\"" + _url + "\", \"value\": " + value + "}");
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
		sb.append("]}");

		return sb.toString();
	}

	@RequestMapping(value = "/querycos", produces = "application/json; charset=utf8")
	public @ResponseBody String queryImgCOS(@RequestParam(value = "itemkey") String itemkey, @RequestParam(value = "count") int count) {
		Plamosaic jedis = jhdPool.getClient();
		List<Double> features;
		features = jedis.lrangeDouble("0:" + itemkey + "_features", 0, -1);
		if (features.isEmpty()) {
			features = jedis.lrangeDouble("1:" + itemkey + "_features", 0, -1);
			if (features.isEmpty()) {
				features = jedis.lrangeDouble("2:" + itemkey + "_features", 0, -1);
			}
		}
		if (features.isEmpty())
			return "nothing";

		String img_url = itemkey + "_temp";

		Pipeline pl = jedis.pipelined();
		for (double fe : features) {
			pl.rpushDouble("0:" + img_url, fe);
			pl.rpushDouble("1:" + img_url, fe);
			pl.rpushDouble("2:" + img_url, fe);
		}
		Response<List<Img>> rs0 = pl.csimu("0:IMG", count, "0:" + img_url);
		Response<List<Img>> rs1 = pl.csimu("1:IMG", count, "1:" + img_url);
		Response<List<Img>> rs2 = pl.csimu("2:IMG", count, "2:" + img_url);

		pl.del("0:" + img_url);
		pl.del("1:" + img_url);
		pl.del("2:" + img_url);

		pl.sync();

		List<Img> rss = new ArrayList<Img>();
		rss.addAll(rs0.get());
		rss.addAll(rs1.get());
		rss.addAll(rs2.get());

		if (rss.size() == 0) {
			return "nothing";
		}

		Collections.sort(rss, new Comparator<Img>() {
			@Override
			public int compare(Img o1, Img o2) {
				if (o1.getValue() < o2.getValue()) {
					return 1;
				} else if (o1.getValue() > o2.getValue()) {
					return -1;
				} else {
					return 0;
				}
			}
		});

		StringBuffer sb = new StringBuffer("{ \"list\" : [");

		int size = (rss.size() > count) ? count : rss.size();

		for (int idx = 0; idx < size;) {
			Img img = (Img) rss.get(idx);
			if (idx++ != 0) {
				sb.append(",");
			}
			try {
				String _url = img.getUrl();
				double value = img.getValue();
				sb.append("{\"url\":\"" + _url + "\", \"value\": " + value + "}");
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
		sb.append("]}");

		return sb.toString();
	}

	// public String[] httpRequest(String strUrl) throws IOException {
	//
	// URL url = new URL(stringUrlPrefix + strUrl);
	// URLConnection uc = url.openConnection();
	//
	// InputStreamReader inputStreamReader = null;
	// BufferedReader in = null;
	// try {
	// inputStreamReader = new InputStreamReader(uc.getInputStream(), "UTF8");
	// in = new BufferedReader(inputStreamReader);
	// StringBuilder builder = new StringBuilder();
	// int ch;
	// while ((ch = in.read()) != -1) {
	// builder.append((char) ch);
	// }
	// return parsenSave(builder.toString());
	// } catch (Exception ex) {
	// ex.printStackTrace();
	// } finally {
	// if (in != null)
	// in.close();
	// if (inputStreamReader != null)
	// inputStreamReader.close();
	// }
	//
	// return null;
	//
	// }

	public String[] parsenSave(final String jsonStr) {
		String sub = jsonStr.substring(jsonStr.indexOf("\"signature\": ["));
		String sub2 = StringUtils.replace(sub, ", ", "\",\"");
		String sub3 = StringUtils.replace(sub2, "[", "[\"");
		String sub4 = StringUtils.replace(sub3, "]}", "\"]}");
		net.minidev.json.JSONArray signatures = JsonPath.read("{" + sub4, "$.signature");
		String[] result = new String[signatures.size()];
		Iterator<Object> iter = signatures.iterator();
		int idx = 0;
		while (iter.hasNext()) {
			result[idx++] = (String) iter.next();
		}

		return result;
	}

	// @RequestMapping(value = "/loadImg", produces = "application/json; charset=utf8")
	// public @ResponseBody String loadImgJson(@RequestParam(value = "img_list_name") String img_list_name,
	// @RequestParam(value = "start") long start, @RequestParam(value = "count") long count) {
	// IMGGenerator img = new IMGGenerator(jhdPool, img_list_name, count, Long.MAX_VALUE);
	// img.execute();
	// return "OK";
	// }

	// @RequestMapping(value = "/loadall", produces = "application/json; charset=utf8")
	// public @ResponseBody String loadSearchItem(@RequestParam(value = "img_list_name") String img_list_name) {
	//
	// IMGGenerator img0 = new IMGGenerator(jhdPool, img_list_name, 30000, Long.MAX_VALUE);
	// img0.execute();
	//
	// return "OK";
	// }

}