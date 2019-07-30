package com.qy.test;

import java.io.IOException;
import java.util.concurrent.ArrayBlockingQueue;
import com.qy.entity.ShowDTO;
import com.qy.service.ShowService;

public class Test {
	public static void main(String[] args) {
		String[] info =new String[]{"肖众喜请到001号","肖众喜请到002号","肖众喜请到003号"};
		ShowDTO showDTO = new ShowDTO("68.133.228.240", 5005, info);
		ArrayBlockingQueue<String> queue = new ArrayBlockingQueue<String>(7);
		ShowService.queueMap.put(showDTO.getIp(), queue);
		for (String string : showDTO.getInfo()) {
			boolean flag = queue.offer(string);
			if(!flag) {
				queue.poll();
				queue.offer(string);
			}
		}
	    ShowService showService = new ShowService();
		try {
			showService.show(showDTO.getIp(), showDTO.getPort());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
