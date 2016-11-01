package light.webSocket;

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

	public static  BroadMainSocket instance;
	public  BroadMainSocket(){
		instance=this;
	}
	

	public BroadMainSocket getInstance () {
		
		return instance;
	}
	
	private static Set<Session> webClients = Collections 
			 			.synchronizedSet(new HashSet<Session>()); 
	
	private static Set<Session> clients = Collections 
 			.synchronizedSet(new HashSet<Session>()); 

	@OnMessage
	public void onMessage(String message, Session session) throws IOException { 
		 		/*System.out.println(message); 
		 		
		 		*/
		 		if(message.equals("web"))
		 		{
		 			webClients.add(session);

		 		}
		 		else
		 		{
			 		synchronized (webClients) { 
			 			
			 			if(session == null)
				 		{	
					 		for (Session client : webClients) 
					 		{ 
					 			client.getBasicRemote().sendText(message); 	
					 		}
				 		}
			 			else
			 			{
			 				//��پ� �ڵ�
			 				//����� 1��1 ��� ��
			 				//���⼭ ������ ������
			 		 		for (Session client : webClients) 
					 		{ 
					 			client.getBasicRemote().sendText(message); 	
					 		}
			 				
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
	
			webClients.remove(session); 
	 		clients.remove(session);
	 	} 

}
