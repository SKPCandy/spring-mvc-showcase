package org.springframework.samples.mvc.simple;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

import org.apache.commons.pool2.impl.GenericObjectPoolConfig;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.Pipeline;

public class IMGGenerator3 {

	JedisPool pPool;
	Jedis jedis;
	String dir;
	String img_list_name;
	long maxsize;
	int index = 0;
	int shardNum = 30;

	public static void main(String args[]) {
		JedisPool jhdPool = new JedisPool(new GenericObjectPoolConfig(), "172.19.114.204", 19000, 2000000, "a1234");

		IMGGenerator3 ig = new IMGGenerator3(jhdPool, "IMG", 10000);
		ig.execute();

		jhdPool.destroy();
	}

	public IMGGenerator3(JedisPool pPool, String img_list_name, long maxsize) {
		this.pPool = pPool;
		this.dir = "/Users/horanghi/dlimg/1";
		this.img_list_name = img_list_name;
		this.maxsize = maxsize;

	}

	public void execute() {
		loadFiles(this.dir);
	}

	public void loadFiles(String source) {
		File dir = new File(source);
		File[] fileList = dir.listFiles();
		for (int i = 0; i < fileList.length; i++) {
			File file = fileList[i];
			if (file.isFile()) {
				String fpath = file.getAbsolutePath();
				String contents = loadConfigGetFile(fpath);
				if (parsenSave(contents)) {
					break;
				}
			}
		}
	}

	public String loadConfigGetFile(String filePathName) {
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

	public boolean parsenSave(String linesStr) {
		jedis = this.pPool.getResource();
		Pipeline pl = jedis.pipelined();

		String[] lines = linesStr.split("\n");

		for (String line : lines) {

			/*
			 * 
			 * <contentid>\t<categoryid>\t<category name>\t<detected name>\t<local filename>\t<color feature>\t<hamming deep feature>\t<float deep feature>\t<duplicate signature>\t<extra flag>\n
			 *
			 *0 <contentid>: 상품고유번호
			 *1 <categoryid>: 중카 번호
			 *2 <category name>: 중카 이름
			 *3 <detected name>: object detect로 검출된 카테고리
			 *4 <local filename>: 로컬에 저장된 파일명 http://로 변환가능
			 *5 <color feature>: 166개의 콤마(,)로 구분된 특징
			 *6 <hamming deep feature>: binary 특징 벡터(9*1024개), 콤마(,)로 구분
			 *7 <float deep feature>: Float 형식의 특징 벡터(9*1024개), 콤마(,)로 구분
			 *8 <duplicate signature>: 바이트 형식의 벡터(50개), 콤마(,)로 구분
			 *9 <extra flag>: 생략
			 * /userdata/index_11st_20151030/test2/1365124751.jpg  ==> http://175.126.56.112/october_11st/1365124751.jpg
			 */

			String[] cons = line.split("\t");

			String contentid = cons[0];
			int preint = (Integer.valueOf(contentid) % shardNum);
			String category = cons[1];
			String url = "http://175.126.56.112/october_11st" + cons[4].substring(cons[4].lastIndexOf("/"));
			String colors = cons[5];
			String hds = cons[6];
			String features = cons[7];
			String duplicate = cons[8];

			if (colors == null || colors.split(",").length != 166) {
				continue;
			}
			if (hds == null || hds.split(",").length != 144) {
				continue;
			}
			if (features == null || features.split(",").length != 9216) {
				continue;
			}
			if (duplicate == null || duplicate.split(",").length != 50) {
				continue;
			}

			String ALL_LIST = preint + ":" + img_list_name + ":ALL";
			String IMG_LIST = preint + ":" + img_list_name + ":" + category;
			String ID = preint + ":" + contentid;
			String Colors = ID + "_color_features";
			String Features = ID + "_features";
			String HD = ID + "_signatures";
			String Duplicates = ID + "_dup_signatures";

			if (maxsize < index++) {
				pl.sync();
				return true;
			}

			pl.rpush(ALL_LIST, ID);
			pl.rpush(IMG_LIST, ID);
			pl.set(ID, url);
			String[] cols = colors.split(",");
			double[] colls = new double[cols.length];
			for (int i = 0; i < cols.length; i++) {
				colls[i] = Double.valueOf(cols[i]);
			}
			pl.rpushDL(Colors, colls);

			String[] fs = features.split(",");
			double[] ds = new double[fs.length];
			for (int i = 0; i < fs.length; i++) {
				ds[i] = Double.valueOf(fs[i]);
			}
			pl.rpushDL(Features, ds);

			pl.rpush(HD, hds.split(","));
			pl.rpush(Duplicates, duplicate.split(","));

			if (index % 20000 == 0) {
				pl.sync();
				pl = jedis.pipelined();
			}

		}
		pl.sync();

		this.pPool.returnResource(jedis);

		return false;
	}
}
