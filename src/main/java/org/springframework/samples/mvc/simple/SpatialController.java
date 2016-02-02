package org.springframework.samples.mvc.simple;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.annotation.PreDestroy;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.jayway.jsonpath.JsonPath;
import com.skplanet.mosaic.Pipeline;
import com.skplanet.mosaic.Plamosaic;
import com.skplanet.mosaic.PlamosaicPool;

import redis.clients.jedis.JedisPool;
import redis.clients.jedis.Protocol.ORDERBY;
import redis.clients.jedis.Protocol.UNITS;
import redis.clients.spatial.model.Img;
import redis.clients.spatial.model.LineString;
import redis.clients.spatial.model.LineStringBuffer;
import redis.clients.spatial.model.Point;
import redis.clients.spatial.model.Polygon;

@Controller
public class SpatialController {

	PlamosaicPool geodisPool = new PlamosaicPool("172.19.114.208:9102", "svc07_01", "svc07_01");

	PlamosaicPool jhdPool = new PlamosaicPool("172.19.114.205", 19000, "a1234");

	JedisPool jhdPool1 = new JedisPool(new GenericObjectPoolConfig(), "172.19.114.205", 19001, 10000, "1234", 11);
	JedisPool jhdPool2 = new JedisPool(new GenericObjectPoolConfig(), "172.19.114.205", 19002, 10000, "1234", 11);
	JedisPool jhdPool3 = new JedisPool(new GenericObjectPoolConfig(), "172.19.114.205", 19003, 10000, "1234", 11);

	JedisPool jhdPool4 = new JedisPool(new GenericObjectPoolConfig(), "172.19.114.205", 19004, 10000);

	final String key = "autocomplete";

	String stringUrlPrefix = "http://175.126.56.112:15003/mosaic_request_handler?url=";

	private List<Object> rs;

	@PreDestroy
	public void release() {
		try {
			geodisPool.release();
		} finally {
		}
		try {
			jhdPool.release();
		} finally {
		}
	}

	@RequestMapping(value = "/polygon", produces = "application/json; charset=utf8")
	public @ResponseBody String gpregionOfPolygon(@RequestParam(value = "latlon") List<Double> latlon,
			@RequestParam(value = "match", required = false) String match) throws UnsupportedEncodingException {
		Plamosaic jedis = geodisPool.getClient();

		if (match == null) {
			match = "*";
		}
		List<Point<String>> plist = new ArrayList<Point<String>>();
		for (int idx = 0; idx < latlon.size(); idx++) {
			plist.add(new Point<String>(latlon.get(idx++), latlon.get(idx)));
		}

		Polygon<String> polygon = new Polygon<String>(plist);

		List<Point<String>> pois;
		StringBuffer sb = new StringBuffer("[");
		pois = jedis.gpregion(key, polygon, new String(match.getBytes("UTF-8")));

		int idx = 0;
		for (Point<String> poi : pois) {
			if (idx++ != 0) {
				sb.append(",");
			}
			try {
				String vName = poi.getMember();
				double slon = poi.getY();
				double slat = poi.getX();
				sb.append("{\"name\":\"" + vName + "\", \"lat\": " + slat + ", \"lon\":" + slon + "}");

			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
		sb.append("]");
		String result = sb.toString();
		return result;
	}

	@RequestMapping(value = "/polyline", produces = "application/json; charset=utf8")
	public @ResponseBody String gpregionOfLineRange(@RequestParam(value = "latlon") List<Double> latlon,
			@RequestParam(value = "radius") double radius, @RequestParam(value = "match", required = false) String match) {
		Plamosaic jedis = geodisPool.getClient();

		if (match == null) {
			match = "*";
		}
		List<Point<String>> plist = new ArrayList<Point<String>>();
		for (int idx = 0; idx < latlon.size(); idx++) {
			plist.add(new Point<String>(latlon.get(idx++), latlon.get(idx)));
		}

		LineString<String> line = new LineString<String>(plist);

		List<Point<String>> pois;
		StringBuffer sb = new StringBuffer("[");
		pois = jedis.gpregion(key, new LineStringBuffer(line, radius, UNITS.M), match, ORDERBY.DISTANCE_DESC);

		int idx = 0;
		for (Point<String> poi : pois) {
			if (idx++ != 0) {
				sb.append(",");
			}
			try {
				String vName = poi.getMember();
				double slon = poi.getY();
				double slat = poi.getX();
				sb.append("{\"name\":\"" + vName + "\", \"lat\": " + slat + ", \"lon\":" + slon + "}");

			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
		sb.append("]");
		String result = sb.toString();
		return result;
	}

	@RequestMapping(value = "/radius", produces = "application/json; charset=utf8")
	public @ResponseBody String grange(@RequestParam(value = "lat") double lat, @RequestParam(value = "lon") double lon,
			@RequestParam(value = "radius") double radius, @RequestParam(value = "match", required = false) String match) {
		Plamosaic jedis = geodisPool.getClient();

		if (match == null) {
			match = "*";
		}

		List<Point<String>> pois;
		StringBuffer sb = new StringBuffer("[");
		pois = jedis.gpradius(key, lat, lon, radius, UNITS.M, match);

		int idx = 0;
		for (Point<String> poi : pois) {
			if (idx++ != 0) {
				sb.append(",");
			}
			try {
				String vName = poi.getMember();
				double slon = poi.getY();
				double slat = poi.getX();
				double distance = poi.getDistance();
				sb.append("{\"name\":\"" + vName + "\", \"lat\": " + slat + ", \"lon\":" + slon + ", \"distance\":" + distance + "}");

			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
		sb.append("]");
		String result = sb.toString();
		return result;
	}

	@RequestMapping(value = "/nnearest", produces = "application/json; charset=utf8")
	public @ResponseBody String gnn(@RequestParam(value = "lat") double lat, @RequestParam(value = "lon") double lon,
			@RequestParam(value = "count") long count, @RequestParam(value = "match", required = false) String match)
					throws UnsupportedEncodingException {
		Plamosaic jedis = geodisPool.getClient();

		if (match == null) {
			match = "*";
		}

		List<Point<String>> pois;
		pois = jedis.gpnn(key, lat, lon, 0, count, match);

		StringBuffer sb = new StringBuffer("[");

		int idx = 0;
		for (Point<String> poi : pois) {
			if (idx++ != 0) {
				sb.append(",");
			}
			try {
				double slon = poi.getY();
				double slat = poi.getX();
				String vName = poi.getMember();
				double distance = poi.getDistance();
				sb.append("{\"name\":\"" + vName + "\", \"lat\": " + slat + ", \"lon\":" + slon + ", \"distance\":" + distance + "}");
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
		sb.append("]");
		String result = sb.toString();

		return result;
	}

	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/queryhd", produces = "application/json; charset=utf8")
	public @ResponseBody String queryImg(@RequestParam(value = "itemkey") String itemkey, @RequestParam(value = "count") int count) {
		Plamosaic jedis = jhdPool.getClient();

		List<String> features = jedis.lrange(itemkey + "_signatures", 0, -1);

		String img_url = itemkey + "_temp";

		Pipeline pl = jedis.pipelined();
		for (String fe : features) {
			pl.rpush(img_url, fe);
		}
		if (features != null) {
			pl.queryImgHD("S01_img", count, img_url);
		}
		pl.del(img_url);

		rs = pl.syncAndReturnAll();

		if (rs.size() == 0) {
			return "nothing";
		}
		List<Img> rss = (List<Img>) rs.get(rs.size() - 2);
		StringBuffer sb = new StringBuffer("{ \"list\" : [");

		for (int idx = 0; idx < rss.size(); idx++) {
			Img img = (Img) rss.get(idx);
			if (idx++ != 0) {
				sb.append(",");
			}
			try {
				String _url = img.getUrl();
				double value = img.getValue();
				sb.append("{\"url\":\"" + _url + "\",  \"value\": " + value + "}");
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
		sb.append("]}");

		return sb.toString();
	}

	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/queryud", produces = "application/json; charset=utf8")
	public @ResponseBody String queryImgUD(@RequestParam(value = "itemkey") String itemkey, @RequestParam(value = "count") int count) {
		Plamosaic jedis = jhdPool.getClient();

		List<String> features = jedis.milrange(itemkey + "_features", 0, -1);

		String img_url = itemkey + "_temp";

		Pipeline pl = jedis.pipelined();
		for (String fe : features) {
			pl.mirpush(img_url.getBytes(), fe.getBytes());
		}
		if (features != null) {
			pl.queryImgUD("S01_img", count, img_url);
		}
		pl.del(img_url);

		rs = pl.syncAndReturnAll();

		if (rs.size() == 0) {
			return "nothing";
		}
		List<Img> rss = (List<Img>) rs.get(rs.size() - 2);
		StringBuffer sb = new StringBuffer("{ \"list\" : [");

		for (int idx = 0; idx < rss.size(); idx++) {
			Img img = (Img) rss.get(idx);
			if (idx++ != 0) {
				sb.append(",");
			}
			try {
				String _url = img.getUrl();
				double value = img.getValue();
				sb.append("{\"url\":\"" + _url + "\",  \"value\": " + value + "}");
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
		sb.append("]}");

		return sb.toString();
	}

	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/querycos", produces = "application/json; charset=utf8")
	public @ResponseBody String queryImgCOS(@RequestParam(value = "itemkey") String itemkey, @RequestParam(value = "count") int count) {
		Plamosaic jedis = jhdPool.getClient();

		List<String> features = jedis.milrange(itemkey + "_features", 0, -1);

		String img_url = itemkey + "_temp";

		Pipeline pl = jedis.pipelined();
		for (String fe : features) {
			pl.mirpush(img_url.getBytes(), fe.getBytes());
		}
		if (features != null) {
			pl.queryImgCSIMU("S01_img", count, img_url);
		}
		pl.del(img_url);

		rs = pl.syncAndReturnAll();

		if (rs.size() == 0) {
			return "nothing";
		}
		List<Img> rss = (List<Img>) rs.get(rs.size() - 2);
		StringBuffer sb = new StringBuffer("{ \"list\" : [");

		for (int idx = 0; idx < rss.size(); idx++) {
			Img img = (Img) rss.get(idx);
			if (idx++ != 0) {
				sb.append(",");
			}
			try {
				String _url = img.getUrl();
				double value = img.getValue();
				sb.append("{\"url\":\"" + _url + "\",  \"value\": " + value + "}");
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
		sb.append("]}");

		return sb.toString();
	}

	public String[] httpRequest(String strUrl) throws IOException {

		URL url = new URL(stringUrlPrefix + strUrl);
		URLConnection uc = url.openConnection();

		InputStreamReader inputStreamReader = null;
		BufferedReader in = null;
		try {
			inputStreamReader = new InputStreamReader(uc.getInputStream(), "UTF8");
			in = new BufferedReader(inputStreamReader);
			StringBuilder builder = new StringBuilder();
			int ch;
			while ((ch = in.read()) != -1) {
				builder.append((char) ch);
			}
			return parsenSave(builder.toString());
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			if (in != null)
				in.close();
			if (inputStreamReader != null)
				inputStreamReader.close();
		}

		return null;

	}

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

	@RequestMapping("/index")
	public void getmap() {
	}
	
	@RequestMapping("/11st")
	public void getmap2() {
	}

	@RequestMapping(value = "/loadImg", produces = "application/json; charset=utf8")
	public @ResponseBody String loadImgJson(@RequestParam(value = "prefix") String prefix,
			@RequestParam(value = "img_list") String img_list, @RequestParam(value = "count") long count,
			@RequestParam(value = "limit", required = false) long limit) {
		JedisPool jp = null;
		if ("S01".equals(prefix)) {
			jp = jhdPool1;
		} else if ("S02".equals(prefix)) {
			jp = jhdPool2;
		} else if ("S03".equals(prefix)) {
			jp = jhdPool3;
		} else if ("S04".equals(prefix)) {
			jp = jhdPool4;
		}
		IMGGenerator img = new IMGGenerator(jp, prefix, img_list, count, limit);
		img.execute();
		return "OK";
	}

}