package org.springframework.samples.mvc.simple;

import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.commons.pool2.impl.GenericObjectPoolConfig;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

public class IMGRemoveDups implements Runnable {
	JedisPool pPool;
	String img_list_name;
	int featureSize = 9216;
	int signatures = 144;
	String ip;
	int port;
	final int index;

	public IMGRemoveDups(String img_list_name, long maxsize, String ip, int port, int i) {
		this.pPool = new JedisPool(new GenericObjectPoolConfig(), ip, port, 200000, "1234", 11);
		this.img_list_name = img_list_name;
		this.ip = ip;
		this.port = port;
		this.index = i;
	}

	public void run() {
		Jedis jedis = this.pPool.getResource();
		try {
			Set<String> keys = jedis.keys(index + ":IMG:*");
			Iterator<String> kk = keys.iterator();
			
			String dupKeys = index +":IMGg:dup";
			String colorKeys = index +":IMGg:color";
			
			String allKeys = index +":IMGg:all";

			while (kk.hasNext()) {
				String kkl = kk.next();
				// System.out.println(kkl);

				List<String> klist = jedis.lrange(kkl, 0, -1);
				
				String[] _duarray = klist.toArray(new String[0]);
				String[] duarray = new String[_duarray.length];
				for(int i = 0 ; i < _duarray.length ; i++){
					duarray[i] = _duarray[i]+"_dup";
				}
				
				jedis.rpush(dupKeys, duarray);
				
				String[] _colors = klist.toArray(new String[0]);
				String[] colors = new String[_colors.length];
				for(int i = 0 ; i < _colors.length ; i++){
					colors[i] = _colors[i]+"_color";
				}
				
				jedis.rpush(colorKeys, colors);
				
				jedis.rpush(allKeys, klist.toArray(new String[0]));
				
				
//				System.out.println(index + ":IMG:ALL " + klist.toArray(new String[0]));
//				jedis.rpush(index + ":IMG:ALL", klist.toArray(new String[0]));
				// for (String sk : klist) {
				// long sizeFeatures = jedis.llenDouble(sk + "_features");
				// long sizeSignatures = jedis.llen(sk + "_signatures");
				// if (featureSize != sizeFeatures || signatures != sizeSignatures) {
				// // System.out.println(sk +", "+ index + ":IMG:" + category);
				// jedis.lrem(index + ":IMG:" + category, 1, sk);
				// jedis.del(sk + "_features", sk + "_signatures", sk);
				// }
				//
				// }

			}
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			pPool.returnResource(jedis);
		}
		pPool.destroy();
	}

	public static void main(String args[]) {
		String[] ips = { "172.19.114.203", "172.19.114.203", "172.19.114.203", "172.19.114.204", "172.19.114.204", "172.19.114.204",
				"172.19.114.205", "172.19.114.205", "172.19.114.205", "172.19.114.206", "172.19.114.206", "172.19.114.206",
				"172.19.114.207", "172.19.114.207", "172.19.114.207", "172.19.114.208", "172.19.114.208", "172.19.114.208" };
		int[] ports = { 19001, 19002, 19003, 19001, 19002, 19003, 19001, 19002, 19003, 19001, 19002, 19003, 19001, 19002, 19003, 19001,
				19002, 19003 };

		ExecutorService es = Executors.newFixedThreadPool(20);
		for (int i = 0; i < 18; i++) {
			es.execute(new IMGRemoveDups("IMG", 1000, ips[i], ports[i], i));
		}

		if (es.isTerminated()) {
			System.out.println("end");
		}
	}
}
