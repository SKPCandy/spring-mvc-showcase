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

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.Protocol.ORDERBY;
import redis.clients.jedis.Protocol.UNITS;
import redis.clients.spatial.model.Img;
import redis.clients.spatial.model.LineString;
import redis.clients.spatial.model.LineStringBuffer;
import redis.clients.spatial.model.Point;
import redis.clients.spatial.model.Polygon;

import com.jayway.jsonpath.JsonPath;
import com.skplanet.mosaic.Plamosaic;
import com.skplanet.mosaic.PlamosaicPool;

@Controller
public class SpatialController {

	PlamosaicPool geodisPool = new PlamosaicPool("172.19.114.208:9102", "svc07_01", "svc07_01");

	JedisPool jhdPool = new JedisPool(new GenericObjectPoolConfig(), "172.19.114.205", 19000, 10000);

	final String key = "autocomplete";

	String stringUrlPrefix = "http://175.126.56.112:15003/mosaic_request_handler?url=";

	@PreDestroy
	public void release() {
		try {
			geodisPool.release();
		} finally {
		}
		try {
			jhdPool.destroy();
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

	@RequestMapping(value = "/queryImgSample", produces = "application/json; charset=utf8")
	public @ResponseBody String queryImgSample(@RequestParam(value = "count", defaultValue = "10") int count) {

		String[] _features = { "13291668172922939131", "13240301326391574397", "13433626072218198014", "12627520918390701567",
				"18427601007981604847", "16689160991402884987", "17581906984813199339", "18158441129789325310", "13798367708723083249",
				"17482894996636695422", "18297944691150157805", "15564294896170811383", "13618876338904361727", "1935983781169065919",
				"15811646573752213420", "16069910366060145111", "12789650950872622843", "13814509793503607677", "16045762241047289855",
				"12681573047467900911", "18427581215709048813", "17842118230475276283", "18446741324930482175", "13690379912823664638",
				"9223352640694253489", "17870212952661688255", "18297874046508859341", "13762978469919588223", "17724960868975756287",
				"2017611496899866623", "5434337105023565740", "18241490838593301975", "13078453214793170683", "13763000358165020347",
				"13546783282053470206", "18409580578197077999", "15852334237651550075", "16716798316013158396", "18154005423894560763",
				"15960607528579497723", "8628780728651087739", "17838672361956048315", "13761698364531867389", "18284579198381047807",
				"13635688004969055157", "12867857213505531898", "5939544897224118253", "16076542729594925040", "17978369712460922494",
				"13835055787539430971", "18445573914163885567", "17257784973801127935", "15852650873378307967", "16717326082669280767",
				"9223332179523534841", "9079248048329572347", "7493950472269332287", "17843259515947433919", "18445540933001740029",
				"13816999573224095743", "18269329071579866023", "8257824869139476459", "6111187457476505567", "16101031597960523761",
				"2988127286773939967", "13753992093740302067", "13831671134266834367", "17238503932695508479", "16717077573447706491",
				"7484277238609215487", "17582039715546005499", "7890046147527917470", "12677499336220456510", "17856770172979969341",
				"13614240638382857807", "13780429741119373055", "13623377877086857365", "3663062261107353855", "6587012232755097533",
				"9152249669790616563", "13003440167593963259", "13834772627857342333", "15720328652010813439", "17293250269785473007",
				"18427561425562873711", "7474796566457352063", "17581905060936286207", "13546816683425890302", "13834959485223368625",
				"17581945605431492478", "18297980150379085773", "16063600963330490107", "18301491990560236277", "5402066095164485599",
				"14657532393235496877", "16071071448183336407", "13330082910380748539", "13832524741835751289", "18027900230950707135",
				"17148862952447541231", "18427530604074479597", "17807180011015241727", "18446602511587475455", "18446735273321529278",
				"18446743386426570641", "17870212746503257983", "18302483749025087439", "13763000425810739071", "18302626686037912575",
				"6902891220742172671", "5425326727493025199", "15935118965494640087", "13077327314895765119", "13330654828297187257",
				"18445573912017197054", "18436602176296841215", "15850081284639407999", "16716798865768972285", "17577544929863663579",
				"17978220157346496507", "8070334087094337403", "17554949974885137919", "13754973751407738605", "18428694420832387067",
				"17670913271109804981", "8256171203668078330", "5939566887725109229", "16022218058011836400", "17690139299800809342",
				"13762999293012082363", "13835012693982429183", "18410706478408130543", "15843607390205751163", "16717326081594932735",
				"17870243463031554011", "9043074120067563513", "7493952527545401214", "16699345219244261375", "18338587038929846013",
				"9205322350885257211", "18234426174358708903", "5952017026581130747", "6111205183812120543", "16092024256701067249",
				"2990370155195661438", "12456955366714506995", "18137077191513446335", "17256659073525176062", "17292411325258655611",
				"8069746289679007231", "18446730774740582395", "12213677677302202333", "12677525690139718718", "17577546415222750511",
				"18374686203476697037", "17847184467390496375", "13623377877104683141", "8274764222453344511", "6586748374962220991",
				"9156894041265560563" };

		Jedis jedis = jhdPool.getResource();
		List<Img> result = new ArrayList<Img>();
		try {
			result = jedis.queryImg("img_list", count, _features);
		} catch (Exception ex) {
			jhdPool.returnBrokenResource(jedis);
			ex.printStackTrace();
		} finally {
			if (jhdPool != null) {
				jhdPool.returnResource(jedis);
			}
		}

		StringBuffer sb = new StringBuffer("{ \"list\" : [");

		int idx = 0;
		for (Img img : result) {
			if (idx++ != 0) {
				sb.append(",");
			}
			try {
				String _url = img.getUrl();
				long value = img.getValue();
				sb.append("{\"url\":\"" + _url + "\",  \"value\": " + value + "}");
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
		sb.append("]}");

		return sb.toString();
	}

	@RequestMapping(value = "/queryImg", produces = "application/json; charset=utf8")
	public @ResponseBody String queryImg(@RequestParam(value = "count") int count, @RequestParam(value = "features") String[] features) {

		Jedis jedis = jhdPool.getResource();
		List<Img> result = new ArrayList<Img>();
		long s = System.currentTimeMillis();
		long e = 0;
		try {
			result = jedis.queryImg("img_list", 10, features);
			e = System.currentTimeMillis();
		} catch (Exception ex) {
			jhdPool.returnBrokenResource(jedis);
			ex.printStackTrace();
		} finally {
			if (jhdPool != null) {
				jhdPool.returnResource(jedis);
			}
		}

		StringBuffer sb = new StringBuffer("{ \"time\" : " + (e - s) + ", \"list\" : [");

		int idx = 0;
		for (Img img : result) {
			if (idx++ != 0) {
				sb.append(",");
			}
			try {
				String _url = img.getUrl();
				long value = img.getValue();
				sb.append("{\"url\":\"" + _url + "\",  \"value\": " + value + "}");
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
		sb.append("]}");

		return sb.toString();
	}

	@RequestMapping(value = "/queryImg2", produces = "application/json; charset=utf8")
	public @ResponseBody String queryImg(@RequestParam(value = "img_url") String img_url, @RequestParam(value = "count") int count) {
		String[] fs = null;
		try {
			fs = this.httpRequest(img_url);
		} catch (IOException e) {
			e.printStackTrace();
		}

		Jedis jedis = jhdPool.getResource();
		List<Img> result = new ArrayList<Img>();
		try {
			if (fs != null) {
				result = jedis.queryImg("lh_list", count, fs);
			}

		} catch (Exception ex) {
			jhdPool.returnBrokenResource(jedis);
			ex.printStackTrace();
		} finally {
			if (jhdPool != null) {
				jhdPool.returnResource(jedis);
			}
		}

		StringBuffer sb = new StringBuffer("{ \"list\" : [");

		int idx = 0;
		for (Img img : result) {
			if (idx++ != 0) {
				sb.append(",");
			}
			try {
				String _url = img.getUrl();
				long value = img.getValue();
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
}