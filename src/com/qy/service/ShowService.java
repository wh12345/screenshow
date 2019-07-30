package com.qy.service;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ArrayBlockingQueue;

import com.qy.bx.BxArea;
import com.qy.bx.BxAreaDynamic;
import com.qy.bx.BxCmdSendDynamicArea;
import com.qy.bx.BxDataPack;

public class ShowService {
    public static Map<String, ArrayBlockingQueue<String>> queueMap = new HashMap<String, ArrayBlockingQueue<String>>();
    public void show(String ip,int port) throws IOException {
        // 添加到列表
        List<BxArea> areas = new ArrayList<BxArea>();
        // 创建一个区域 1
        byte id = 0x00;
        short x = 0;
        short y = 0;
        short w = 96;
        short h = 112;
        //
        Iterator<String> iterator = queueMap.get(ip).iterator();
        StringBuffer buffer = new StringBuffer();
        while(iterator.hasNext()) {
            String info = URLDecoder.decode(iterator.next(), "UTF-8");
            System.out.println(ip+"接收到信息:"+info);
            //String info = iterator.next();
            String nameStr = info.split(":")[0];
            String ktStr = info.split(":")[1];
            System.out.println(nameStr+" "+ktStr);
            if(nameStr.length() ==2){
                nameStr = nameStr.charAt(0)+"\r\r\r\r"+nameStr.charAt(1);
            }else if(nameStr.length() ==3) {
                nameStr = nameStr.charAt(0)+"\r"+nameStr.charAt(1)+"\r"+nameStr.charAt(2);
            }else if(nameStr.length() >4){
                nameStr = nameStr.substring(0,3)+"*\r";
            }
            if(ktStr.length()==2) {
                ktStr="0"+ktStr;
            }else if(ktStr.length()==1) {
                ktStr="00"+ktStr;
            }
            ktStr+="考台";
            info=nameStr+":"+ktStr;
            if(!iterator.hasNext()) {
                buffer.append(info);
            }else{
                buffer.append(info+"\\n");
            }
            System.out.print("sys"+info);
        }
        String infoStr = buffer.toString();
        System.out.println("sys"+infoStr.length());
        byte [] data = infoStr.getBytes("gb2312");
        BxAreaDynamic area = new BxAreaDynamic(id, x, y, w, h, data);
        // 设置显示方式为 0x03 - 向左移动
        area.setDispMode((byte) 0x01);
        area.setHoldTime((byte) 100000000);
        // 添加到列表
        areas.add(area);
        // 创建一个发送动态区命令
        BxCmdSendDynamicArea cmd = new BxCmdSendDynamicArea(areas);
        cmd.setProcessMode((byte)0x01);

        // 对命令进行封装
        BxDataPack dataPack = new BxDataPack(cmd);

        // 生成命令序列
        byte[] seq = dataPack.pack();

        // 创建 Socket
        Socket client = new Socket();

        // 创建 socket 地址
        SocketAddress address = new InetSocketAddress(ip, port);

        // 建立 TCP 连接
        client.connect(address, 3000);
        // 设置读超时时间
        client.setSoTimeout(3000);
        // 创建输出流
        OutputStream out = client.getOutputStream();
        // 创建输入流
        InputStream in = client.getInputStream();

        // 写入数据
        out.write(seq);
        // @todo
        // 回读返回帧
        // 现在还没有对返回帧进行解析，后续有时间添加
        byte[] resp = new byte[1024];
        in.read(resp);
        //
        out.close();
        in.close();
        client.close();
    }
}
