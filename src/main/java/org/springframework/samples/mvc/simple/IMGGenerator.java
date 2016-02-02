package org.springframework.samples.mvc.simple;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

import org.apache.commons.lang3.StringUtils;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.Pipeline;

import com.jayway.jsonpath.JsonPath;

public class IMGGenerator {

	JedisPool pPool;
	Jedis jedis;
	String dir;
	String img_list_name;
	long maxsize;
	long limit = 1000000;
	int index = 0;
	String _prefix = "M";

	public IMGGenerator(JedisPool pPool, String prefix, String img_list_name, long maxsize, Long limit) {
		this.pPool = pPool;
		this.dir = "/app/plandas/data/";
		this._prefix = prefix;
		this.img_list_name = img_list_name;
		this.maxsize = maxsize;
		if (limit != null) {
			this.limit = limit;
		}

		jedis = this.pPool.getResource();
	}

	public void execute() {
		loadJsonFormFiles(this.dir);
	}

	public void loadJsonFormFiles(String source) {
		File dir = new File(source);
		File[] fileList = dir.listFiles();
		for (int i = 0; i < fileList.length; i++) {
			File file = fileList[i];
			if (file.isFile()) {
				String fpath = file.getAbsolutePath();
				String contents = loadConfigGetJson(fpath);
				if (parsenSave(contents)) {
					break;
				}
			}
		}
	}

	public String loadConfigGetJson(String filePathName) {
		String jsonStr = null;
		BufferedReader in = null;
		try {
			File jsonfile = new File(filePathName);
			in = new BufferedReader(new InputStreamReader(new FileInputStream(jsonfile), "UTF8"));

			StringBuilder builder = new StringBuilder();
			int ch;
			while ((ch = in.read()) != -1) {
				builder.append((char) ch);
			}

			jsonStr = builder.toString();

		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (in != null) {
				try {
					in.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return jsonStr;

	}

	public boolean parsenSave(String jsonStr) {
		Pipeline pl = jedis.pipelined();
		String[] linejsons = jsonStr.split("\n");

		int idx = 0;
		String prefix = this._prefix + img_list_name;

		for (String jsonstr : linejsons) {
			// { "img_name" : "http://i.011st.com/ak/8/3/6/6/9/7/1267836697_B_V5.jpg", "feature": ["0.28011709451675415",.... ],"signature":
			// ["17870141415412989919", ...]}

			String img_name = JsonPath.read(jsonstr, "$.img_name");
			// String[] features = JsonPath.read(jsonstr, "$.feature");
			// String[] signatures = JsonPath.read(jsonstr, "$.signature");

			String features = jsonstr.substring(jsonstr.indexOf("[") + 1, jsonstr.indexOf("]"));
			String signatures = jsonstr.substring(jsonstr.lastIndexOf("[") + 1, jsonstr.lastIndexOf("]"));

			features = StringUtils.remove(features, "\"");
			features = StringUtils.remove(features, " ");
			signatures = StringUtils.remove(signatures, "\"");
			signatures = StringUtils.remove(signatures, " ");

			String id = prefix + index;
			pl.rpush(img_list_name, id);
			pl.set(id, img_name);

			String features_name = id + "_features";
			String[] fs = features.split(",");
			for (int i = 0; i < fs.length; i++) {
				String feature = fs[i];
				pl.mirpush(features_name.getBytes(), feature.getBytes());
				if (i > limit) {
					break;
				}
			}
			String signature_name = id + "_signatures";
			for (String signature : signatures.split(",")) {
				pl.rpush(signature_name, signature);
			}

			if (index % 1000 == 0) {
				pl.sync();
				pl = jedis.pipelined();
			}

			if (maxsize < index++) {
				pl.sync();
				return true;

			}
		}
		pl.sync();

		return false;
	}
}
