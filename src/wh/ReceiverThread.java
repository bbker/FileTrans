package wh;

import java.io.*;
import java.net.*;
import javax.swing.*;
import java.text.DecimalFormat;
import java.util.Date;

public class ReceiverThread extends Thread
{
	Socket socket;
	File localfile;
	JLabel msg,jLabelRate;
	JProgressBar jProgressBarAccept;
	long lengthOfFile;
	
	
	ReceiverThread( Socket socket,File localfile, JLabel msg, JLabel jLabelRate,
	               JProgressBar jProgressBarAccept,long lengthOfFile) 
	{
		this.socket = socket;
		this.localfile = localfile;
		this.msg = msg;
		this.jLabelRate = jLabelRate;
		this.jProgressBarAccept = jProgressBarAccept;
		this.lengthOfFile = lengthOfFile;
	}
	
	private String fileLength( long length )
 	{
 		double klength = length*1.0/1024;
 		DecimalFormat df = new DecimalFormat("0.00");
 		if( klength<1024 )
 		{
 			return ( df.format(klength) + "K" );	
 		}
 		else
 		{
 			return ( df.format( klength/1024 ) + "M" );	
 		}	
 	}	
	
	public void run()
	{
		try{
		PrintWriter out= new PrintWriter( socket.getOutputStream(),true );
		out.println("OK");
		
		//msg.append();
		msg.setText( "<html>Accept to receive <p>the file");
		
		BufferedInputStream remoteFile = 
		                   new BufferedInputStream( socket.getInputStream());
		BufferedOutputStream saveFile = 
		                   new BufferedOutputStream(new FileOutputStream( localfile ));
		byte[] buffer = new byte[1024*128];
		int b;
 		
		
		long cur=0;
		jProgressBarAccept.setVisible( true );
    				
    	long initialTime = new Date().getTime();
    	long preTime = initialTime;
    	long curTime = initialTime;
    	long interspaceAmount=0;
 		
 	
 		msg.setText("<html>开始接受'"+localfile.getName()
 		             +"'("+fileLength( lengthOfFile)+").........");
 		
 		while ((b=remoteFile.read(buffer)) != -1)
      	{
        	saveFile.write(buffer, 0, b);
        
        	cur+=b;
   			interspaceAmount+=b;
      		jProgressBarAccept.setValue( (int)( (cur*1.0/lengthOfFile)*100 )  );
      				
    		curTime = new Date().getTime();
      		if( (curTime-preTime)>500 )
      		{
      			jLabelRate.setText(  
      			    fileLength( interspaceAmount*1000/(curTime-preTime) ) +"/秒");
      			preTime = curTime;
      			interspaceAmount = 0;
      		}
        	
        }
		
		jLabelRate.setText( "用时"+(int)( (curTime-initialTime)*1.0/1000+0.5 )+"秒" );
		msg.setText( "<html>文件'"+localfile.toString()+"'接受成功" );
		jProgressBarAccept.setVisible( false );
		
		remoteFile.close();
		saveFile.close();
		socket.close();
    	}
		catch (Exception e) 
		{ 
			//throw new FileTransferException(e);
			e.printStackTrace(System.err);
		}	
	}
}