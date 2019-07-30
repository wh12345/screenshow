package com.qy.service;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;
import java.util.concurrent.ArrayBlockingQueue;

public class ClearScreenService {
	public static Map<String, Integer> portMap = new HashMap<String, Integer>();
	public void clearScreen() {
		Map<String, ArrayBlockingQueue<String>> map = ShowService.queueMap;
		Set<Entry<String, ArrayBlockingQueue<String>>> set = map.entrySet();
		for (Entry<String, ArrayBlockingQueue<String>> entry : set) {			
			ArrayBlockingQueue<String> queue = entry.getValue();
			queue.clear();
			String ip = entry.getKey();
			Integer port = portMap.get(ip);
			System.out.println("清理ip:"+ip+" port:"+port);
			try {
				new ShowService().show(ip, port);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
