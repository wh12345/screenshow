package com.qy.entity;

import java.util.Arrays;

public class ShowDTO {
	private String ip;
	private int port;
	private String[] info;
	public ShowDTO(){}
	public ShowDTO(String ip, int port, String[] info) {
		super();
		this.ip = ip;
		this.port = port;
		this.info = info;
	}
	public String getIp() {
		return ip;
	}
	public void setIp(String ip) {
		this.ip = ip;
	}
	public int getPort() {
		return port;
	}
	public void setPort(int port) {
		this.port = port;
	}
	public String[] getInfo() {
		return info;
	}
	public void setInfo(String[] info) {
		this.info = info;
	}
	@Override
	public String toString() {
		return "ShowDTO [ip=" + ip + ", port=" + port + ", info="
				+ Arrays.toString(info) + "]";
	}	
}
