package wh;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.fest.swing.fixture.FrameFixture;

public class FileSenderTest {

	private FrameFixture frame;//定义FrameFixture变量（在fest-swing.jar中）
	@Before
	public void setUp() throws Exception {
		frame=new FrameFixture(new FileSender());//实例化FileSender()给frame
		frame.show();//显示窗口
	}

	@After
	public void tearDown() throws Exception {
		frame.cleanUp();//清理窗口
	}

	@Test
	public void test() 
	{
		connect();//测试连接
		message();//测试发送信息
		send();//测试发送文件
		accept();//测试接收文件	
	}
	public void connect()//测试连接
	{
		frame.textBox("ipText").enterText("192.168.0.200");//在文本框中自动输入Ip地址
		frame.button("connect").click();//自动点击连接按钮
		frame.label("status").requireText(
				"<html>连接成功,可以进行<p>交谈了");//查看是否连接成功
	}
	
	public void message(){//测试发送信息
		frame.textBox("input").enterText("hello！我们是Duang~。    ");//输入要发送的信息
		frame.button("message").click();//发送按钮
	}
	public void send()//测试发送文件
	{
		clear();
		frame.textBox("input").enterText("信息发送成功，接下来测试文件发送。    ");
		frame.button("send").click();//单击的发送文件按钮
		frame.fileChooser("open").fileNameTextBox().enterText(//输入要发送的文件目录
				"E:\\学习\\汇编语言\\tools.zip");
		frame.fileChooser("open").approveButton().click();//单击确定按钮
	}
	public void accept()//测试接收文件
	{
		clear();
		frame.textBox("input").enterText("收到文件接收请求，接下来测试文件接收。    ");
		frame.button("accept").click();//收到文件请求单击接收按钮
		frame.fileChooser("save").fileNameTextBox().enterText(//输入文件保存路径
				"C:\\Users\\Administrator\\Desktop");
		frame.fileChooser("save").approveButton().click();//单击保存按钮
		clear();
		frame.textBox("input").enterText("文件接收成功，可以到桌面查看！          ");
		frame.textBox("input").enterText("\n自动测试成功  ，即将退出......    ");
	
	}
	void clear()//清空文本框内容
	{
		frame.textBox("input").deleteText();
	}

}
