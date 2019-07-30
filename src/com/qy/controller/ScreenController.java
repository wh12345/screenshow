package com.qy.controller;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ArrayBlockingQueue;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.alibaba.fastjson.JSONObject;
import com.qy.entity.ShowDTO;
import com.qy.service.ClearScreenService;
import com.qy.service.ShowService;

public class ScreenController extends HttpServlet {
	private static ShowService showService = new ShowService();
	
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		response.setContentType("text/html");
		this.doPost(request, response);
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		response.setContentType("text/html");
		response.setCharacterEncoding("UTF-8");
		PrintWriter out = response.getWriter();
		String returnInfo = "{\"code\":\"1\",\"message\":\"正常\"}";
		try {
			InputStreamReader insr = new InputStreamReader(request.getInputStream(),"utf-8");
			String result = "";
			int respInt = insr.read();
			while(respInt!=-1) {
				result +=(char)respInt;
				respInt = insr.read();
			}
			JSONObject jsonStr = JSONObject.parseObject(result);
			ShowDTO showDTO = JSONObject.toJavaObject(jsonStr, ShowDTO.class);
			System.out.println("screen接收到信息"+showDTO);
			ArrayBlockingQueue<String> queue = null;
			if(ShowService.queueMap.containsKey(showDTO.getIp())) {
				queue = ShowService.queueMap.get(showDTO.getIp());
			}else{
				queue = new ArrayBlockingQueue<String>(7);
				ShowService.queueMap.put(showDTO.getIp(), queue);
			}
			for (String string : showDTO.getInfo()) {
				boolean flag = queue.offer(string);
				if(!flag) {
					queue.poll();
					queue.offer(string);
				}
			}
			showService.show(showDTO.getIp(), showDTO.getPort());
			ClearScreenService.portMap.put(showDTO.getIp(), showDTO.getPort());			
		} catch (Exception e) {
			e.printStackTrace();
			returnInfo = "{\"code\":\"-1\",\"message\":\""+e.getMessage()+"\"}";
		}		
		out.write(returnInfo);
	}
}
