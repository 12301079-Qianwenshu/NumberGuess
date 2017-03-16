//author：qianwenshu-0316

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;

public class GameClient {
	//定义通讯端口号，与服务端一致，均为3333
	public static int portNo = 3333;
	
	public final static String EQUALS = "E" ;
    public final static String GREATER = "G";
    public final static String LESSER = "L";
    public final static String NEXT = "NEXT";
    
    static Socket socket = null;
	static BufferedReader br = null;
    static InputStream is = null;
    static OutputStream os = null;

	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		//初始化socket
		init();
		
		System.out.println("Ready Go!");

		byte[] bufResult = new byte[10];
		
		while(true){
			//获取server产生的随机数
            String strDate = new String(receive());
            System.out.println("Please guess a number(0~50)(-----you can enter 'N' to quit the game-----):");
            //从控制板获取输入字符
            String str = br.readLine();
            
            //如果玩家输入“N”则退出游戏
            if(isQuit(str))
            {
                System.out.println("Bye!");
                break;
            }
          //client端控制猜谜次数
            int count = 5;
            while(count > 0){
            	//向 server端发送client 猜的数字
                send(str.getBytes());
              //接收来自server的对比后的反馈
                bufResult = receive();
                String result = new String(bufResult);
                
                if(EQUALS.equalsIgnoreCase(result)){
                	System.out.println("Congratulations! You are rgiht.");
                    send(NEXT.getBytes());
                    break;
                }else if(GREATER.equalsIgnoreCase(result))
                {
                	count --;
                    System.out.println("Greater!");
                    System.out.println("You have " + count + " chances left.");
                }else if(LESSER.equalsIgnoreCase(result))
                {
                	 count --;
                     System.out.println("Lesser!");
                     System.out.println("You have " + count + " chances left.");
                }
                //猜谜次数未到？继续
                if(count > 0)
                {
                    System.out.println("Please enter your guess:");
                    str = br.readLine();
                }
                //猜谜次数已到，还没猜出？打印谜底，同时告诉server开始下一轮猜谜游戏
                if(count == 0)
                {
                    System.out.println("The right answer is: " + strDate);
                    send(NEXT.getBytes());
                }
            }
		}
		
		close();
		
	}

	private static void close() throws IOException{
		// TODO Auto-generated method stub
		socket.close();
        is.close();
        os.close();
	}

	private static void send(byte[] bytes) throws IOException{
		// TODO Auto-generated method stub
		os.write(bytes);
	}
    
	private static boolean isQuit(String str) {
		// TODO Auto-generated method stub
		boolean flag = false;
        if(null == str)
            flag = false;
        else
            {
                if("N".equalsIgnoreCase(str))
                {
                    flag = true;
                }
                else
                {
                    flag = false;
                }
            }
        return flag;
	}
    
	//获得server端发送过来的数据
	private static byte[] receive() throws IOException{
		// TODO Auto-generated method stub
		byte[] buf = new byte[10];
        int len = is.read(buf);
        byte[] receiveData = new byte[len];
       
        System.arraycopy(buf, 0, receiveData, 0, len);
       
        return receiveData;
	}

	//初始化socket对象，用以同服务器端的ServerSocket类型对象交互，共同完成C/S通讯流程
	private static void init() throws IOException {
		// TODO Auto-generated method stub
		//获取键盘输入流，并将其放入缓存区
		br = new BufferedReader(new InputStreamReader(System.in));
		socket = new Socket("localhost", portNo);
		//得到一个输入流，就是从服务器端发回的数据
		is = socket.getInputStream();
		//达到一个输出流，就是发送给服务器端的数据
        os = socket.getOutputStream();
	}

}
