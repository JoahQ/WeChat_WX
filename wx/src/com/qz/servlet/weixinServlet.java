package com.qz.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.dom4j.DocumentException;

import com.qz.po.TextMessage;
import com.qz.util.CheckUtil;
import com.qz.util.MessageUtil;
@WebServlet(name = "weixinServlet", urlPatterns = "/do")
public class weixinServlet extends HttpServlet {

	
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String signature = request.getParameter("signature");
		String timestamp = request.getParameter("timestamp");
		String nonce = request.getParameter("nonce");
		String echostr = request.getParameter("echostr");
		
		PrintWriter out = response.getWriter();
		if(CheckUtil.checkSignature(signature,timestamp,nonce)){
			out.print(echostr);
		}
		
	}

	
	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		request.setCharacterEncoding("UTF-8");
		response.setCharacterEncoding("UTF-8");
		PrintWriter out = response.getWriter();
		out.print("");
		try {
			Map<String, String> map = MessageUtil.xmlToMap(request);
			String fromUserName = map.get("FromUserName");
			String toUserName = map.get("ToUserName");
			
			//String createTime = map.get("CreateTime");
			String msgType = map.get("MsgType");
			String content = map.get("Content");
			//String msgId = map.get("MsgId");
			
			String message=null;
			if(MessageUtil.MESSAGE_TEXT.equals(msgType)){
				if ("1".equals(content)) {
					message = MessageUtil.initText(toUserName, 
							fromUserName, MessageUtil.firstMenu());
				}else if ("2".equals(content)) {
					message = MessageUtil.initText(toUserName, 
							fromUserName, MessageUtil.secondMenu());
				}else if ("?".equals(content)||"？".equals(content)) {
					message = MessageUtil.initText(toUserName, 
							fromUserName, MessageUtil.menuText());
				}else {
					message = MessageUtil.initText(toUserName, 
							fromUserName, MessageUtil.menuText().substring
							(MessageUtil.menuText().indexOf("请")));
				}
				
			
			}else if (MessageUtil.MESSAGE_EVENT.equals(msgType)) {
				String eventType = map.get("Event");
				if (MessageUtil.MESSAGE_SUBSCRIBE.equals(eventType)) {
					message = MessageUtil.initText(toUserName, 
							fromUserName, MessageUtil.menuText());
				}
			}
			System.out.println(message);
			out.print(message);
		} catch (DocumentException e) {
			
			e.printStackTrace();
		}finally{
			out.close();
		}
		
		
		
	}

}
