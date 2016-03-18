package org.springframework.samples.mvc.simple;

import java.math.BigDecimal;
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

import redis.clients.jedis.Jedis;
import redis.clients.jedis.Response;
import redis.clients.spatial.model.Img;

@Controller
public class DLController {

	// JedisPool jhdPool = new JedisPool(new GenericObjectPoolConfig(), "172.19.114.204", 19000, 20000, "a1234");
	PlamosaicPool jhdPool = new PlamosaicPool("172.19.114.204", 19000, "a1234");

	// String stringUrlPrefix = "http://175.126.56.112:15003/mosaic_request_handler?url=";

	final int shardNumber = 20;

	String[] items = { "6:1356058666", "6:1238644826", "6:1324210846", "6:1268597626", "6:1168155726", "6:1033685646", "6:1135260186",
			"6:1310023266", "6:1317709706", "6:1357380726", "6:1246028586", "6:1228676906", "6:1336180826", "6:1267035106", "6:1284120106",
			"6:1284089486", "6:1339558346", "6:1354034426", "6:1086388586", "6:1165533606", "6:1365048606", "6:1047017306", "6:1264711726",
			"6:1097431626", "6:1249377666", "6:1215089086", "6:1372467046", "6:1280178486", "6:1156098146", "6:1338187046",
			"6:1096762086" };

	@PreDestroy
	public void release() {
		try {
			// jhdPool.destroy();
			jhdPool.release();
		} finally {
		}
	}

	@RequestMapping(value = "/queryhd", produces = "application/json; charset=utf8")
	public @ResponseBody String queryImgHD(@RequestParam(value = "itemkey") String itemkey,
			@RequestParam(value = "category") String category, @RequestParam(value = "count") int count) {
		final Plamosaic jedis = jhdPool.getClient();
		StringBuffer sb = new StringBuffer("{ \"list\" : [");

		int shid = Integer.valueOf(itemkey) % shardNumber;
		List<String> signatures;
		signatures = jedis.lrange(shid + ":" + itemkey + "_signatures", 0, -1);
		final List<Double> orgin = jedis.lrangeDL(shid + ":" + itemkey + "_color_features", 0, -1);
		if (signatures.size() == 0) {
			return "nothing";
		}

		Pipeline pl = jedis.pipelined();

		List<Response<List<Img>>> rssList = new ArrayList<Response<List<Img>>>();
		long s = System.currentTimeMillis();
		for (int i = 0; i < shardNumber; i++) {
			rssList.add(pl.hdistByList(i + ":IMG:" + category, count, signatures.toArray(new String[0])));
		}

		pl.sync();
		long e = System.currentTimeMillis();
		System.out.println("[Con] queryhd :" + (e - s));

		List<Img> rss = new ArrayList<Img>();
		for (int i = 0; i < shardNumber; i++) {
			if (rssList.get(i).get() != null) {
				rss.addAll(rssList.get(i).get());
			}
		}

		if (rss.size() == 0) {
			return "nothing";
		}

		// sort by hamming distance
		Collections.sort(rss, new Comparator<Img>() {
			@Override
			public int compare(Img o1, Img o2) {
				int a1 = (int) (o1.getValue());
				int a2 = (int) (o2.getValue());
				if (a1 < a2) {
					return -1;
				} else if (a1 > a2) {
					return 1;
				} else {
					return 0;
				}
			}
		});

		int size = (rss.size() > count) ? count : rss.size();
		rss = rss.subList(0, size);

		// sort by HD / UD(color)
		Collections.sort(rss, new Comparator<Img>() {
			@Override
			public int compare(Img o1, Img o2) {
				int a1 = (int) (o1.getValue() / 200);
				int a2 = (int) (o2.getValue() / 200);
				if (a1 < a2) {
					return -1;
				} else if (a1 > a2) {
					return 1;
				} else {
					List<Double> c01 = jedis.lrangeDL(o1.getId() + "_color_features", 0, -1);
					List<Double> c02 = jedis.lrangeDL(o2.getId() + "_color_features", 0, -1);
					double res01 = calculateUDistance(orgin.toArray(new Double[0]), c01.toArray(new Double[0]));
					double res02 = calculateUDistance(orgin.toArray(new Double[0]), c02.toArray(new Double[0]));
					if (res01 < res02) {
						return -1;
					} else if (res01 > res02) {
						return 1;
					} else {
						return 0;
					}
				}
			}
		});

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

	public static double calculateUDistance(Double[] array1, Double[] array2) {
		double Sum = 0.0;
		for (int i = 0; i < array1.length; i++) {
			Sum = Sum + Math.pow((array1[i] - array2[i]), 2.0);
		}
		return Math.sqrt(Sum);
	}

	public static int getHammingDistance(String sequence1, String sequence2) {
		char[] s1 = sequence1.toCharArray();
		char[] s2 = sequence2.toCharArray();

		int shorter = Math.min(s1.length, s2.length);
		int longest = Math.max(s1.length, s2.length);

		int result = 0;
		for (int i = 0; i < shorter; i++) {
			if (s1[i] != s2[i])
				result++;
		}

		result += longest - shorter;

		return result;
	}

	@RequestMapping(value = "/queryud", produces = "application/json; charset=utf8")
	public @ResponseBody String queryImgUD(@RequestParam(value = "itemkey") String itemkey,
			@RequestParam(value = "category") String category, @RequestParam(value = "count") int count) {
		Plamosaic jedis = jhdPool.getClient();
		StringBuffer sb = new StringBuffer("{ \"list\" : [");

		int shid = Integer.valueOf(itemkey) % shardNumber;
		List<Double> features;
		features = jedis.lrangeDL(shid + ":" + itemkey + "_features", 0, -1);

		Pipeline pl = jedis.pipelined();
		List<Response<List<Img>>> rssList = new ArrayList<Response<List<Img>>>();

		double[] fslist = new double[features.size()];
		for (int i = 0; i < features.size(); i++) {
			fslist[i] = features.get(i);
		}
		long s = System.currentTimeMillis();
		for (int i = 0; i < shardNumber; i++) {
			rssList.add(pl.udistByList(i + ":IMG:" + category, count, fslist));
		}

		pl.sync();
		long e = System.currentTimeMillis();

		List<Img> rss = new ArrayList<Img>();
		for (Response<List<Img>> rs : rssList) {
			if (rs.get() != null) {
				rss.addAll(rs.get());
			}
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

		return sb.toString();
	}

	double round(double d, int decimalPlace) {
		BigDecimal bd = new BigDecimal(d);
		bd = bd.setScale(decimalPlace, BigDecimal.ROUND_HALF_UP);
		return bd.doubleValue();
	}

	@RequestMapping(value = "/querycos", produces = "application/json; charset=utf8")
	public @ResponseBody String queryImgCOS(@RequestParam(value = "itemkey") String itemkey,
			@RequestParam(value = "category") String category, @RequestParam(value = "count") int count) {
		Plamosaic jedis = jhdPool.getClient();
		StringBuffer sb = new StringBuffer("{ \"list\" : [");

		int shid = Integer.valueOf(itemkey) % shardNumber;
		List<Double> features = new ArrayList<Double>();
		features.addAll(jedis.lrangeDL(shid + ":" + itemkey + "_features", 0, -1));

		Pipeline pl = jedis.pipelined();
		pl = jedis.pipelined();
		List<Response<List<Img>>> rssList = new ArrayList<Response<List<Img>>>();
		long s = System.currentTimeMillis();

		double[] fslist = new double[features.size()];
		for (int i = 0; i < features.size(); i++) {
			fslist[i] = features.get(i);
		}
		for (int i = 0; i < shardNumber; i++) {
			rssList.add(pl.csimuByList(i + ":IMG:" + category, count, fslist));
		}

		pl.sync();
		long e = System.currentTimeMillis();
		System.out.println("[Con] querycos :" + (e - s));

		List<Img> rss = new ArrayList<Img>();
		for (Response<List<Img>> rs : rssList) {
			if (rs != null || rs.get() != null) {
				rss.addAll(rs.get());
			}

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

		return sb.toString();
	}

	@RequestMapping(value = "/removedups", produces = "application/json; charset=utf8")
	public @ResponseBody String removeDups() {
		int[] cats = { 1612, 15993, 15878, 1455, 127682 };

		for (int cidx = 0; cidx < 1; cidx++) {
			long s = System.currentTimeMillis();
			Plamosaic jedis = jhdPool.getClient();
			List<String> cdkeys = new ArrayList<String>();

			for (int i = 0; i < shardNumber; i++) {
				cdkeys.add(i + ":IMG:" + cats[cidx]);
			}

			for (String key : cdkeys.toArray(new String[0])) {
				// key 지정
				// 1.get dup signatures
				List<String> dupsig = jedis.lrange(key + "_dup_signatures", 0, -1);

				Pipeline pl = jedis.pipelined();
				for (int j = 0; j < shardNumber; j++) {
					pl.rpush(j + ":" + key + ":DUP_sig", dupsig.toArray(new String[0]));
				}
				pl.sync();

				pl = jedis.pipelined();
				List<Response<List<Img>>> dupkeys = new ArrayList<Response<List<Img>>>();

				for (int j = 0; j < shardNumber; j++) {
					dupkeys.add((Response<List<Img>>) pl.hdistByMember(j + ":IMGg:dup", 50, j + ":" + key + ":DUP_sig"));
					System.out.println("[Con] hdist " + j + ":IMGg:dup 50 by " + j + ":" + key + ":DUP_sig");
				}
				pl.sync();
				List<Img> imgList = new ArrayList<Img>();
				Iterator<Response<List<Img>>> result = dupkeys.iterator();

				while (result.hasNext()) {
					Response<List<Img>> rs = (Response<List<Img>>) result.next();
					if (rs.get() != null) {
						imgList.addAll((List<Img>) rs.get());
					} else {
						System.out.println("[Con] null");
					}
				}
				// pl = jedis.pipelined();
				// for (int j = 0; j < shardNumber; j++) {
				// pl.del(j + ":" + key + ":DUP_sig");
				// }
				// pl.sync();

				Collections.sort(imgList, new Comparator<Img>() {
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
				int lim = 0;
				for (Img img : imgList.toArray(new Img[0])) {
					if (lim++ > 20) {
						break;
					}
				}
			}

			long e = System.currentTimeMillis();
			System.out.println("[Con] time : " + (e - s));
		}

		return "OK";
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