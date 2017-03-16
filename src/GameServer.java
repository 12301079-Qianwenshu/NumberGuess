import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class GameServer {
	
	public static int portNo = 3333;
	static ServerSocket serverSocket = null;

	public static void main(String[] args) throws IOException{
		// TODO Auto-generated method stub
		try{
			//初始化ServerSocke，开始监听
            serverSocket = new ServerSocket(portNo);
            System.out.println("The Server started.");
            
            while(true){
            	Socket socket = serverSocket.accept();
            	new GameThread(socket).start();
            }
		}
		catch(Exception e){
			e.printStackTrace();
		}
		
		 serverSocket.close();

	}

}
