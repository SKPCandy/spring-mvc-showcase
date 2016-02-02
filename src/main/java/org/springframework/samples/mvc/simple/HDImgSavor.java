package org.springframework.samples.mvc.simple;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import com.jayway.jsonpath.JsonPath;

public class HDImgSavor {
	static String stringUrlPrefix = "http://175.126.56.112:15003/mosaic_request_handler?url=";

	public void saveFile() {

		List<String> imgurls = this.readfile("/app/plandas/urls/hdimgurl.txt");// hdimgurl1

		String filename = "/app/plandas/data/";

		int idx = 0;
		int fidx = 1001;
		List<String> rs = new ArrayList<String>();
		for (String imgurl : imgurls) {
			try {
				// request over http
				String jsonStr;
				try {
					jsonStr = this.httpRequest(stringUrlPrefix + imgurl);
				} catch (Exception ex) {
					continue;
				}
				try {
					String org = JsonPath.read(jsonStr, "__org_img_url__");
					// String sub = jsonStr.substring(jsonStr.indexOf("\"signature\": ["));
					// "feature": [
					String sub = jsonStr.substring(jsonStr.indexOf("\"feature\": ["));
					String sub2 = StringUtils.replace(sub, ", ", "\",\"");
					String sub3 = StringUtils.replace(sub2, "[", "[\"");
					String sub4 = StringUtils.replace(sub3, "]", "\"]");
					String sub5 = StringUtils.replace(sub4, "\",\"\"signature", ",\"signature");
					String prefixStr = "{ \"img_name\" : \"" + org + "\", ";
					rs.add(prefixStr + sub5);

					if (idx > 1000) {
						this.writeFile(filename + fidx + ".json", rs);
						System.out.println(filename + fidx + ".json");
						rs = new ArrayList<String>();
						fidx += 1000;
						idx = 0;
						
					}
					idx++;
				} catch (Exception ex) {
					idx++;
					continue;
				}

			} catch (Exception ex) {
				ex.printStackTrace();
				break;
			}
		}

	}

	public String httpRequest(String strUrl) throws IOException {

		URL url = new URL(strUrl);
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
			return builder.toString();
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

	public List<String> readfile(String path) {
		List<String> urlfiles = new ArrayList<String>();
		try {
			BufferedReader in = new BufferedReader(new FileReader(path));
			String s;
			while ((s = in.readLine()) != null) {
				urlfiles.add(s);
			}
			in.close();
		} catch (IOException e) {
			System.err.println(e); // 에러가 있다면 메시지 출력
			System.exit(1);
		}
		return urlfiles;
	}

	public void writeFile(String filename, List<String> contents) throws IOException {
		BufferedWriter out = null;
		try {
			out = new BufferedWriter(new FileWriter(filename));
			for (String con : contents) {
				out.append(con + "\n");
			}

		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (out != null)
				out.close();
		}
	}
}