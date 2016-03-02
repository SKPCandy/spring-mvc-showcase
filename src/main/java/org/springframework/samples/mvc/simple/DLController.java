package org.springframework.samples.mvc.simple;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

import javax.annotation.PreDestroy;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.jayway.jsonpath.JsonPath;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.Pipeline;
import redis.clients.jedis.Response;
import redis.clients.spatial.model.Img;

@Controller
public class DLController {

	JedisPool jhdPool = new JedisPool(new GenericObjectPoolConfig(), "172.19.114.205", 19000, 20000, "a1234");

	// String stringUrlPrefix = "http://175.126.56.112:15003/mosaic_request_handler?url=";

	@PreDestroy
	public void release() {
		try {
			jhdPool.destroy();
		} finally {
		}
	}

	@RequestMapping(value = "/queryhd", produces = "application/json; charset=utf8")
	public @ResponseBody String queryImgHD(@RequestParam(value = "itemkey") String itemkey,
			@RequestParam(value = "category") String category, @RequestParam(value = "count") int count) {
		Jedis jedis = jhdPool.getResource();
		StringBuffer sb = new StringBuffer("{ \"list\" : [");
		try {
			List<String> signatures;
			signatures = jedis.lrange(itemkey + "_signatures", 0, -1);

			if (signatures.isEmpty())
				return "nothing";

			String queryKey = itemkey + "_temp";

			Pipeline pl = jedis.pipelined();
			for (int i = 0; i < 18; i++) {
				pl.rpush(i + ":" + queryKey, signatures.toArray(new String[0]));
			}
			pl.sync();

			pl = jedis.pipelined();
			List<Response<List<Img>>> rssList = new ArrayList<Response<List<Img>>>();
			for (int i = 0; i < 18; i++) {
				rssList.add(pl.hdist(i + ":IMG:" + category, count, i + ":" + queryKey));
			}

			for (int i = 0; i < 18; i++) {
				pl.del(i + ":" + queryKey);
			}

			pl.sync();

			List<Img> rss = new ArrayList<Img>();
			for (int i = 0; i < 18; i++) {
				rss.addAll(rssList.get(i).get());
			}

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

		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			jhdPool.returnResource(jedis);
		}

		return sb.toString();
	}

	@RequestMapping(value = "/queryud", produces = "application/json; charset=utf8")
	public @ResponseBody String queryImgUD(@RequestParam(value = "itemkey") String itemkey,
			@RequestParam(value = "category") String category, @RequestParam(value = "count") int count) {
		Jedis jedis = jhdPool.getResource();
		StringBuffer sb = new StringBuffer("{ \"list\" : [");
		try {
			List<Double> features;
			features = jedis.lrangeDouble(itemkey + "_features", 0, -1);

			if (features.isEmpty())
				return "nothing";

			String queryKey = itemkey + "_temp";

			Pipeline pl = jedis.pipelined();
			for (int i = 0; i < 18; i++) {
				pl.rpushDouble(i + ":" + queryKey, ArrayUtils.toPrimitive(features.toArray(new Double[0])));
			}
			pl.sync();

			pl = jedis.pipelined();
			List<Response<List<Img>>> rssList = new ArrayList<Response<List<Img>>>();
			for (int i = 0; i < 18; i++) {
				rssList.add(pl.udist(i + ":IMG:" + category, count, i + ":" + queryKey));
			}

			for (int i = 0; i < 18; i++) {
				pl.del(i + ":" + queryKey);
			}

			pl.sync();

			List<Img> rss = new ArrayList<Img>();
			for (Response<List<Img>> rs : rssList) {
				rss.addAll(rs.get());
			}

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
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			jhdPool.returnResource(jedis);
		}

		return sb.toString();
	}

	@RequestMapping(value = "/querycos", produces = "application/json; charset=utf8")
	public @ResponseBody String queryImgCOS(@RequestParam(value = "itemkey") String itemkey,
			@RequestParam(value = "category") String category, @RequestParam(value = "count") int count) {
		Jedis jedis = jhdPool.getResource();
		StringBuffer sb = new StringBuffer("{ \"list\" : [");
		try {
			List<Double> features;
			features = jedis.lrangeDouble(itemkey + "_features", 0, -1);

			if (features.isEmpty())
				return "nothing";

			String queryKey = itemkey + "_temp";

			Pipeline pl = jedis.pipelined();
			for (int i = 0; i < 18; i++) {
				pl.rpushDouble(i + ":" + queryKey, ArrayUtils.toPrimitive(features.toArray(new Double[0])));
			}
			pl.sync();

			pl = jedis.pipelined();
			List<Response<List<Img>>> rssList = new ArrayList<Response<List<Img>>>();
			for (int i = 0; i < 18; i++) {
				rssList.add(pl.csimu(i + ":IMG:" + category, count, i + ":" + queryKey));
			}

			for (int i = 0; i < 18; i++) {
				pl.del(i + ":" + queryKey);
			}

			pl.sync();

			List<Img> rss = new ArrayList<Img>();
			for (Response<List<Img>> rs : rssList) {
				rss.addAll(rs.get());
			}

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
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			jhdPool.returnResource(jedis);
		}
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
	// IMGGenerator img = new IMGGenerator(jhdPool, img_list_name, count);
	// img.execute();
	// return "OK";
	// }

	// @RequestMapping(value = "/loadall", produces = "application/json; charset=utf8")
	// public @ResponseBody String loadSearchItem(@RequestParam(value = "img_list_name") String img_list_name) {
	//
	// IMGGenerator img0 = new IMGGenerator(jhdPool, img_list_name, 30000);
	// img0.execute();
	//
	// return "OK";
	// }

}