
package wh;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.io.*;
import java.net.*;
import java.text.DecimalFormat;
import java.util.Date;

class FileSender extends JFrame implements ActionListener{
	
	ListenerThread lt; /*监听各种消息的线程*/
	Container container;
	JLabel jLabel,status,fileStatus,listenerStatus,fileSendRate,fileAcceptRate;
	JTextField jTextField;
	JButton connect,send,message,accept,refuse;
	JTextArea jTextArea,jTextArea2;
	JPanel jPanel,jPanelLeft,jPanel2,jPanelRight;
	JProgressBar jProgressBar,jProgressBarAccept;
	String IP;  //用于记录你主动区连接的对方的IP信息
	
	
	Socket talkingSocket;
	PrintWriter output;
	BufferedReader input;
	
	
	public FileSender() {
		super("File Sender by WangHao");
		container = this.getContentPane();
		container.setLayout( new BorderLayout() );
		

		jPanelLeft = new JPanel();/*开始左半部的布局*/
		jPanelLeft.setLayout( new BorderLayout() );
		
		
		jTextArea = new JTextArea();
		jTextArea.setLineWrap( true );
		jTextArea.setEditable( false );
		send = new JButton("发送文件");
		send.addActionListener( new SendHandler(this) );
		jTextArea2 = new JTextArea( );
		jTextArea.setLineWrap( true );
		message = new JButton("发送");
		message.addActionListener(this);
		message.setEnabled( false );
		jPanel2 = new JPanel();
		jPanel2.setLayout( new GridLayout(1,2) );
		jPanel2.add(send);
		jPanel2.add(message);
		jPanel = new JPanel();
		jPanel.setLayout( new BorderLayout() );
		jPanel.setPreferredSize( new Dimension(400,200) );
		jPanel.add( new JScrollPane(jTextArea2), BorderLayout.CENTER);
		jPanel.add(jPanel2,BorderLayout.SOUTH);
		
		
		jPanelLeft.add( new JScrollPane( jTextArea), BorderLayout.CENTER);
		jPanelLeft.add( jPanel, BorderLayout.SOUTH );   /*结束左半部的布局*/
		
		
		
		
		   
	
		jLabel = new JLabel("对方IP");   	/*开始右半部的布局*/
		jTextField = new JTextField();
		jTextField.setPreferredSize( new Dimension( 150,25) );
		connect = new JButton("连接");
		connect.addActionListener( this );
		status = new JLabel();
		status.setPreferredSize( new Dimension(150,40) );
				
		jProgressBar = new JProgressBar();
		jProgressBar.setVisible( false );
		fileSendRate = new JLabel();
		fileStatus = new JLabel();
		fileStatus.setPreferredSize( new Dimension(150,100) );
		//fileStatus.setVisible( false );
		 
		listenerStatus = new JLabel();
		listenerStatus.setPreferredSize(new Dimension( 150,100) );
		accept = new JButton("接收");
		accept.setEnabled( false );
		accept.addActionListener( this );
		refuse = new JButton("拒绝");
		refuse.addActionListener( this );
		refuse.setEnabled( false );
		
		jProgressBarAccept = new JProgressBar();
		jProgressBarAccept.setVisible( false );
		fileAcceptRate = new JLabel();
		fileAcceptRate.setPreferredSize( new Dimension(150,50) );
		
		jPanelRight = new JPanel();
		jPanelRight.setLayout( new FlowLayout() );
		
		
		jPanelRight.add( jLabel );
		jPanelRight.add( jTextField );
		jPanelRight.add( connect );
		jPanelRight.add( status );
		jPanelRight.add( fileStatus );
		jPanelRight.add( jProgressBar );
		jPanelRight.add( fileSendRate );
		jPanelRight.setPreferredSize( new Dimension(150,50) );
		jPanelRight.add( listenerStatus );
		jPanelRight.add( accept );
		jPanelRight.add( refuse );
		jPanelRight.add( jProgressBarAccept );
		jPanelRight.add( fileAcceptRate );
		jPanelRight.setPreferredSize( new Dimension( 150,500) );  /*结束右半部的布局*/
		
		container.add( jPanelLeft, BorderLayout.CENTER );
		container.add( jPanelRight, BorderLayout.EAST );
		
		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				dispose();
				System.exit(0);
			}
		});
		
		lt = new ListenerThread(this);
		lt.start();
	}
	


	public static void main(String args[]) {
		System.out.println("Starting FileSender...");
		FileSender mainFrame = new FileSender();
		mainFrame.setSize(500, 520);
		mainFrame.setVisible(true);
	}
	public void actionPerformed( ActionEvent e)
	{
		if ( e.getSource() ==  message )
		{
			String str = jTextArea2.getText();
			if( str.equals("") )
			{
				JOptionPane.showMessageDialog(this,"发送的内容不能为空",
				               "空消息",JOptionPane.WARNING_MESSAGE);	
			}	
			else
			{
				
				output.println( str );
				jTextArea.append( "[I]"+str+"\n");
				jTextArea2.setText("");	
			}
		}
		if( e.getSource() == connect )
		{
			
			new Thread()
			{		
				public void run()
				{	
					try
					{
						String ip = jTextField.getText();
						if( ip.equals("") )
						{
							JOptionPane.showMessageDialog( jPanelRight,"请先输入对方IP",
					        		       "IP为空",JOptionPane.ERROR_MESSAGE);	
						}
						else
						{	
							
							status.setText( "<html>连接中.......");							
							talkingSocket = new Socket(jTextField.getText() , 9999);
							output = new PrintWriter( talkingSocket.getOutputStream(), true );
							output.println("[TALK]");
							status.setText( "<html>连接成功,可以进行<p>交谈了");			
							input = new BufferedReader( new InputStreamReader
				        		              ( talkingSocket.getInputStream() ) );
							IP = jTextField.getText();	
							message.setEnabled( true );
							connect.setEnabled( true );
							
						}
					}
					catch( IOException ioe)
					{
						System.err.println(ioe.toString());	
						status.setText("<html>连接失败，请检查主机名<p>是否正确");
					}
				}
			}.start();
			//connect.setEnabled( false );
			
			
		}
		if( e.getSource() == accept )//接受文件的请求
		{
			JFileChooser fileChooser = new JFileChooser();
 			fileChooser.setFileSelectionMode( JFileChooser.DIRECTORIES_ONLY );
 		
 			int result = fileChooser.showSaveDialog( this );
 		
 			if( result == JFileChooser.CANCEL_OPTION )
 				return;
 		
 			File filePath = fileChooser.getSelectedFile();
 		
 			if( filePath == null||filePath.getName().equals("") )
 			{
 				JOptionPane.showMessageDialog( this, "Invalid File Name",
 			        	  "Invalid File Name", JOptionPane.ERROR_MESSAGE);
 			}
 			else
 			{
 				File  file = new File( filePath, lt.fileName );
 				ReceiverThread receiver = new ReceiverThread( lt.fileReceiver, file, 
 				    listenerStatus,this.fileAcceptRate,this.jProgressBarAccept,lt.lengthOfFile );
 				receiver.start();
 					
 				accept.setEnabled( false );
 				refuse.setEnabled( false );	
 			}
		}
		if( e.getSource() == refuse )
		{
			try
 			{
 			PrintWriter output= new PrintWriter( lt.fileReceiver.getOutputStream(), true  );
 			output.println( "No" ); 
 
 		
 			refuse.setEnabled(false);
 			accept.setEnabled(false);
 			
 			               /*获得了要求传输文件过来的socket的InetAddress*/
 			InetAddress address = this.lt.fileReceiver.getInetAddress();  
 			
 			this.listenerStatus.setText("<html>您拒绝了来自<p>["+address.toString()
 			                    +"]<p>的文件传输请求");
 			output.close();                    
 			lt.fileReceiver.close();
 			}
 			catch( Exception ee)
 			{
 			ee.printStackTrace( System.err );	
 			}
		}
	}
}

 	
 
 class SendHandler implements ActionListener
 {
 	FileSender myFather;
 	
 	SendHandler( FileSender father )
 	{
 		myFather = father;	
 	}	
 	public void actionPerformed( ActionEvent e )
 	{
 		
		if( myFather.IP == null )
		{
			JOptionPane.showMessageDialog( myFather,"请先连接对方",
			                        "尚未建立连接",JOptionPane.WARNING_MESSAGE);	
		}
		else
		{
		/*清除上次文件传输的内容*/
		myFather.fileSendRate.setText("");
		
		JFileChooser fileChooser = new JFileChooser();
 		fileChooser.setFileSelectionMode( JFileChooser.FILES_ONLY );
 		
 		int result = fileChooser.showSaveDialog( myFather );
 		
 		if( result == JFileChooser.CANCEL_OPTION )
 			return;
 		
 		File localfile = fileChooser.getSelectedFile();
 
 		
 		SenderThread st = new SenderThread( myFather,localfile );
 		st.start();
		}
 	}
 }
 
 
 class SenderThread extends Thread
 {
 		File file;
 		String IP;
 		JLabel fileStatus;
 		JLabel fileSendRate ;
 		JProgressBar jProgressBar;
 		
 		SenderThread( FileSender father,File localfile )
 		{
 			this.file =  localfile;
 			this.IP = father.IP;
 			this.fileStatus = father.fileStatus;
 			this.jProgressBar = father.jProgressBar;
 			this.fileSendRate = father.fileSendRate;
 		}
 		public void run()
 		{
 			request();
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
 		
 		
 		private void request()
 		{
 			try
 			{
 				Socket s = new Socket( IP, 9999);
 			    PrintWriter out = new PrintWriter(s.getOutputStream(),true); 
 			    BufferedReader in = new BufferedReader(
 			    	               new InputStreamReader( s.getInputStream() ) );
 			    
 			    out.println( "[FILE]"+file.getName() );
 			    out.println( file.length() );
 			    fileStatus.setText("<html>request to send file '"+file.getName()
 	 			                      +"'("+ fileLength( file.length() )+") to "+IP);
 			    
 			    String answer = in.readLine();
 			    
 			    if ( answer.equals("OK") )
 			    {
 	    			fileStatus.setText("["+IP+"]"+" OK");
 	    			
 	    			BufferedInputStream localFile = new BufferedInputStream
 			    	             (new FileInputStream( file ));
		       		BufferedOutputStream sendFile = 
		       		         new BufferedOutputStream( s.getOutputStream());
		       		         
		       		     
		       		fileStatus.setText("<html>begin to send <p>"+file.getName()+"<p>( "+ 
		       		              fileLength(file.length() ) +" )");
		       		
		       		long fl = file.length();
		       		long cur=0;
		       	
		       		byte[] buffer = new byte[1024 * 128];
    				int b;
    				
    				jProgressBar.setVisible( true );
    				
    				long initialTime = new Date().getTime();
    				long preTime = initialTime;
    				long curTime = initialTime;
    				long interspaceAmount=0;
   					
   					while ( (b = localFile.read(buffer)) != -1) {
      					sendFile.write(buffer, 0, b);
      					cur+=b;
      					interspaceAmount+=b;
      					jProgressBar.setValue( (int)( (cur*1.0/fl)*100 )  );
      				
      					curTime = new Date().getTime();
      					if( (curTime-preTime)>500 )
      					{
      					fileSendRate.setText(  
      					              fileLength( interspaceAmount*1000/(curTime-preTime) ) +"/秒");
      					preTime = curTime;
      					interspaceAmount = 0;
      					}
      				}
      				
    				fileSendRate.setText( "用时"+(int)( (curTime-initialTime)*1.0/1000+0.5 )+"秒" );
    				        
		       		fileStatus.setText( "<html>Have successfully<p>send the file <p>"
		       		                                                  +file.getName()+"<p>to "+IP);
		       		jProgressBar.setVisible( false );
 			    }
 			    else
 			    {	
 			    	fileStatus.setText("<html>["+IP+"]"+"<p>refuse to accept the file<p>"
 			    	                                 +file.getName());
 			    }
				
				in.close();
				out.close();
				s.close();
				
 			    	   
 			}
 			catch( Exception e)
 			{
 				System.err.println( e.toString() ); 	
 			}       	
 		}
 }

 
 class ListenerThread extends Thread
 {
 	
 	ServerSocket ss;
 	FileSender myFather;
 	JLabel listenerStatus;
 	Socket fileReceiver,listener;
 	String fileName;
 	long lengthOfFile;
 	
 	
 	ListenerThread( FileSender father)
 	{
 		myFather = father;
 		listenerStatus = father.listenerStatus;
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
 		try
 		{	
 			
 			ss= new ServerSocket( 9999 );
 			listenerStatus.setText("监听服务已经启动");
 			while(true)
 			{	
 				Socket Connected = ss.accept();
 			    InetAddress	address = Connected.getInetAddress();
 				listenerStatus.setText( "<html>["+address.toString() + "]<p>connected\n" );
 				
 				BufferedReader input = new BufferedReader( 
 					          new InputStreamReader( Connected.getInputStream() ) );
 				String request = input.readLine();
 				if( request.startsWith("[FILE]") )
 				{
 					fileReceiver = Connected;
 					fileName = request.substring(6);
 					
 					String fLength = input.readLine();
 					lengthOfFile = Long.parseLong( fLength );
 							
 					
 					listenerStatus.setText("<html>["+address.toString()+"]请求传输文件'"
 					        +fileName+"'("+fileLength(lengthOfFile)+"),接受还是拒绝" );
 				myFather.fileAcceptRate.setText("");	
 				myFather.accept.setEnabled( true );
 				myFather.refuse.setEnabled( true );
 				}
 				else if( request.equals("[TALK]") )
 				{
 					listener = Connected;
 					new WaitingMsgThread( myFather.jTextArea,listener ).start();	
 				}
 				else
 					listenerStatus.setText( "<html>"+address.toString()+"<p>非法格式的请求");
 		
 			}
 			
 			
 		}
 		catch( Exception e)
 		{
 			System.err.println(e.toString());	
 		}	
 	}
 }
 
 
 
class WaitingMsgThread extends Thread
{
	JTextArea Msg;
	Socket listener;
	WaitingMsgThread( JTextArea Msg,Socket listener )
	{
		this.Msg = Msg;
		this.listener = listener;	
	}	
	public void run()
	{
		try{
			
			BufferedReader input = new BufferedReader( 
			   new InputStreamReader( listener.getInputStream() ) );
			PrintWriter out = new PrintWriter( listener.getOutputStream(), true );
			String ip ="["+listener.getInetAddress().toString()+"]";
			
		    out.println("OK");
		    
			while(true)
			{
				
				String str=input.readLine();
				if( str!=null  )
				{
					
					Msg.append( ip+str+"\n"); 
			    }
			    sleep(300);
			}
		}
		catch( IOException ioe )
		{
			System.err.println( ioe.toString() );	
		}
		catch(InterruptedException ex)
		{
			System.err.println( ex.toString() );	
		}
	}
}