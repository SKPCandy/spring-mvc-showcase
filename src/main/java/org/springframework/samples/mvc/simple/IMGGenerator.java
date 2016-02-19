package org.springframework.samples.mvc.simple;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

import org.apache.commons.lang3.StringUtils;

import com.jayway.jsonpath.JsonPath;
import com.skplanet.mosaic.Pipeline;
import com.skplanet.mosaic.Plamosaic;
import com.skplanet.mosaic.PlamosaicPool;

public class IMGGenerator {

	PlamosaicPool pPool;
	Plamosaic jedis;
	String dir;
	String img_list_name;
	long maxsize;
	long limit = 1000000;
	int index = 0;

	public IMGGenerator(PlamosaicPool pPool, String img_list_name, long maxsize, Long limit) {
		this.pPool = pPool;
		this.dir = "/app/plandas/data";
		this.img_list_name = img_list_name;
		this.maxsize = maxsize;
		if (limit != null) {
			this.limit = limit;
		}

		jedis = this.pPool.getClient();
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

		for (String jsonstr : linejsons) {

			int preint = (index % 3);

			String prefix = preint + ":" + img_list_name; // 0:img_list_name

			String img_name = JsonPath.read(jsonstr, "$.img_name");

			String features = jsonstr.substring(jsonstr.indexOf("[") + 1, jsonstr.indexOf("]"));
			String signatures = jsonstr.substring(jsonstr.lastIndexOf("[") + 1, jsonstr.lastIndexOf("]"));

			features = StringUtils.remove(features, "\"");
			features = StringUtils.remove(features, " ");
			signatures = StringUtils.remove(signatures, "\"");
			signatures = StringUtils.remove(signatures, " ");

			String id = prefix + index; // 0:img_list_name + 0
			pl.rpush(prefix, id); // 0:img_list_name , 0:img_list_name + 0
			pl.set(id, img_name); // 0:img_list_name + 0 , ...

			String features_name = id + "_features"; // 0:img_list_name + _features
			String[] fs = features.split(",");
			for (int i = 0; i < fs.length; i++) {
				pl.rpushDouble(features_name, Double.valueOf(fs[i])); // 0:img_list_name + _features
			}

			String signature_name = id + "_signatures"; // 0:img_list_name + _signatures
			for (String signature : signatures.split(",")) {
				pl.rpush(signature_name, signature); // 0:img_list_name + _signatures
			}

			if (index % 100 == 0) {
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
