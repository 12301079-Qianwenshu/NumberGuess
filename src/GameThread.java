//author：qianwenshu-0316

import java.net.Socket;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Random;

public class GameThread extends Thread{
	
	Socket socket = null;
    InputStream is = null;
    OutputStream os = null;
    
    //public 说明属性公开的;static是说此常量是静态的;final是最终的，不能修改
    public final static String NEXT = "NEXT";
    public final static String EQUALS = "E" ;
    public final static String GREATER = "G";
    public final static String LESSER = "L";

	public GameThread(Socket socket) throws IOException{
		// TODO Auto-generated constructor stub
		this.socket = socket;
        is = this.socket.getInputStream();
        os = this.socket.getOutputStream();
	}
	
	@Override
	public void run(){
		try{
			while(true){
				//产生一个在[0,50]之间的随机数
                int randomData = -1;
                randomData = createRandom(0, 50);
                //将randomData强制转化为String类型
                String str = String.valueOf(randomData);
                System.out.println("random data:  " + randomData);
                try{
                	//将该随机数发送给client端
                    send(str.getBytes());
                }catch(Exception e){
                	 break;
                }
                while(true){
                	try{
                		//接收来自client端的猜数
                        byte[] b = receive();
                        //通过使用平台的默认字符集解码指定的byte子数组，构造一个新的String，即strReceive来自client端的猜数
                        //bytes：要解码为字符的byte，offset：要解码的第一个byte的索引，length：要解码的byte数的长度
                        String  strReceive = new String(b, 0, b.length);
                        System.out.println("strReceive: " + strReceive);
                       
                        if(isNext(strReceive))
                        	break;
                        //比较谜底和猜数之间的关系并反馈比较结果
                        String checkResult = checkClientData(randomData, strReceive);
                        send(checkResult.getBytes());
                	}catch(IOException e){
                		break;
                	}
                }
			}
		}catch(Exception e){
			try{
				close();
			}catch(Exception e1){
				// TODO Auto-generated catch block
                e1.printStackTrace();
			}
		}
	}

	private void close() throws Exception{
		// TODO Auto-generated method stub
		socket.close();
        is.close();
        os.close();
	}
    
	//检查client传过来的数据与生成的随机数字的关系
	private String checkClientData(int randomData, String strReceive) {
		// TODO Auto-generated method stub
		String checkResult = null;
		int buf = Integer.parseInt(strReceive);
		
		 if(buf == randomData){
			 checkResult =  EQUALS;
		 }else if(buf > randomData){
			 checkResult =  GREATER;
		 }else if(buf < randomData){
			 checkResult =  LESSER;
		 }
		return checkResult;
	}

	private boolean isNext(String strReceive) {
		// TODO Auto-generated method stub
		boolean flag = false;
		 if(null == strReceive)
			 flag = false;
		 else{
			 if(NEXT.equalsIgnoreCase(strReceive)){//将strReceive与指定字符串NEXT比较，不区分大小写，若相同则返回true
				 flag = true;
			 }
			 else{
				 flag = false;
			 }
		 }
		return flag;
	}
	
    //接收来自client端的数据
	private byte[] receive() throws IOException{
		// TODO Auto-generated method stub
		byte[] buf = new byte[10];
		//输入流，也就是client端发送过来的数据
        int len = is.read(buf);
        byte[] receiveData = new byte[len];
        //数组复制，将buf复制到receiveData
        System.arraycopy(buf, 0, receiveData, 0, len);
       
        return receiveData;
	}
	
    //将随机生成的数发送给client端
	private void send(byte[] bytes) throws IOException{
		// TODO Auto-generated method stub
		//输出流，也就是发送给client端的数据
		os.write(bytes);
	}
    
	//生成随机数字方法
	private int createRandom(int start, int end) {
		// TODO Auto-generated method stub
		Random r = new Random();
        return r.nextInt(end - start + 1) + start;
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
