package light.common;

import java.io.IOException;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

@ServerEndpoint("/echo.do")
public class BroadMainSocket {

	private static Set<Session> clients = Collections 
			 			.synchronizedSet(new HashSet<Session>()); 
	@OnMessage
	public void onMessage(String message, Session session) throws IOException { 
		 		System.out.println(message); 
		 		synchronized (clients) { 
		 			// Iterate over the connected sessions 
		 			// and broadcast the received message 
		 			//¿©±â¼­ ÆÄ½Ì...........
		 			for (Session client : clients) { 
		 				if (!client.equals(session)) { 
		 					client.getBasicRemote().sendText("parsing::"+message); 
		 				} 
		 			} 
		 		} 
		 	} 
	@OnOpen 
	 	public void onOpen(Session session) { 
	 		// Add session to the connected sessions set 
		
	 		System.out.println("wServer:"+session); 
	 		clients.add(session); 
	 	} 
	 
	@OnClose 
	 	public void onClose(Session session) { 
	 		// Remove session from the connected sessions set 
	 		clients.remove(session); 
	 	} 




}
