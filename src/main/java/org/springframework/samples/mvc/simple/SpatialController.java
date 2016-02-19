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

	final String key = "autocomplete";

	private List<Object> rs;

	@PreDestroy
	public void release() {
		try {
			geodisPool.release();
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

}