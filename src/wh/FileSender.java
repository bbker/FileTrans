
package wh;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.io.*;
import java.net.*;
import java.text.DecimalFormat;
import java.util.Date;

class FileSender extends JFrame implements ActionListener{
	
	ListenerThread lt; /*����������Ϣ���߳�*/
	Container container;
	JLabel jLabel,status,fileStatus,listenerStatus,fileSendRate,fileAcceptRate;
	JTextField jTextField;
	JButton connect,send,message,accept,refuse;
	JTextArea jTextArea,jTextArea2;
	JPanel jPanel,jPanelLeft,jPanel2,jPanelRight;
	JProgressBar jProgressBar,jProgressBarAccept;
	String IP;  //���ڼ�¼�����������ӵĶԷ���IP��Ϣ
	
	
	Socket talkingSocket;
	PrintWriter output;
	BufferedReader input;
	
	
	public FileSender() {
		super("File Sender by WangHao");
		container = this.getContentPane();
		container.setLayout( new BorderLayout() );
		

		jPanelLeft = new JPanel();/*��ʼ��벿�Ĳ���*/
		jPanelLeft.setLayout( new BorderLayout() );
		
		
		jTextArea = new JTextArea();
		jTextArea.setLineWrap( true );
		jTextArea.setEditable( false );
		send = new JButton("�����ļ�");
		send.addActionListener( new SendHandler(this) );
		jTextArea2 = new JTextArea( );
		jTextArea.setLineWrap( true );
		message = new JButton("����");
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
		jPanelLeft.add( jPanel, BorderLayout.SOUTH );   /*������벿�Ĳ���*/
		
		
		
		
		   
	
		jLabel = new JLabel("�Է�IP");   	/*��ʼ�Ұ벿�Ĳ���*/
		jTextField = new JTextField();
		jTextField.setPreferredSize( new Dimension( 150,25) );
		connect = new JButton("����");
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
		accept = new JButton("����");
		accept.setEnabled( false );
		accept.addActionListener( this );
		refuse = new JButton("�ܾ�");
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
		jPanelRight.setPreferredSize( new Dimension( 150,500) );  /*�����Ұ벿�Ĳ���*/
		
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
				JOptionPane.showMessageDialog(this,"���͵����ݲ���Ϊ��",
				               "����Ϣ",JOptionPane.WARNING_MESSAGE);	
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
							JOptionPane.showMessageDialog( jPanelRight,"��������Է�IP",
					        		       "IPΪ��",JOptionPane.ERROR_MESSAGE);	
						}
						else
						{	
							
							status.setText( "<html>������.......");							
							talkingSocket = new Socket(jTextField.getText() , 9999);
							output = new PrintWriter( talkingSocket.getOutputStream(), true );
							output.println("[TALK]");
							status.setText( "<html>���ӳɹ�,���Խ���<p>��̸��");			
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
						status.setText("<html>����ʧ�ܣ�����������<p>�Ƿ���ȷ");
					}
				}
			}.start();
			//connect.setEnabled( false );
			
			
		}
		if( e.getSource() == accept )//�����ļ�������
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
 			
 			               /*�����Ҫ�����ļ�������socket��InetAddress*/
 			InetAddress address = this.lt.fileReceiver.getInetAddress();  
 			
 			this.listenerStatus.setText("<html>���ܾ�������<p>["+address.toString()
 			                    +"]<p>���ļ���������");
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
			JOptionPane.showMessageDialog( myFather,"�������ӶԷ�",
			                        "��δ��������",JOptionPane.WARNING_MESSAGE);	
		}
		else
		{
		/*����ϴ��ļ����������*/
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
      					              fileLength( interspaceAmount*1000/(curTime-preTime) ) +"/��");
      					preTime = curTime;
      					interspaceAmount = 0;
      					}
      				}
      				
    				fileSendRate.setText( "��ʱ"+(int)( (curTime-initialTime)*1.0/1000+0.5 )+"��" );
    				        
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
 			listenerStatus.setText("���������Ѿ�����");
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
 							
 					
 					listenerStatus.setText("<html>["+address.toString()+"]�������ļ�'"
 					        +fileName+"'("+fileLength(lengthOfFile)+"),���ܻ��Ǿܾ�" );
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
 					listenerStatus.setText( "<html>"+address.toString()+"<p>�Ƿ���ʽ������");
 		
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