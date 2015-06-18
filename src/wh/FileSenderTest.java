package wh;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.fest.swing.fixture.FrameFixture;

public class FileSenderTest {

	private FrameFixture frame;//����FrameFixture��������fest-swing.jar�У�
	@Before
	public void setUp() throws Exception {
		frame=new FrameFixture(new FileSender());//ʵ����FileSender()��frame
		frame.show();//��ʾ����
	}

	@After
	public void tearDown() throws Exception {
		frame.cleanUp();//������
	}

	@Test
	public void test() 
	{
		connect();//��������
		message();//���Է�����Ϣ
		send();//���Է����ļ�
		accept();//���Խ����ļ�	
	}
	public void connect()//��������
	{
		frame.textBox("ipText").enterText("192.168.0.200");//���ı������Զ�����Ip��ַ
		frame.button("connect").click();//�Զ�������Ӱ�ť
		frame.label("status").requireText(
				"<html>���ӳɹ�,���Խ���<p>��̸��");//�鿴�Ƿ����ӳɹ�
	}
	
	public void message(){//���Է�����Ϣ
		frame.textBox("input").enterText("hello��������Duang~��    ");//����Ҫ���͵���Ϣ
		frame.button("message").click();//���Ͱ�ť
	}
	public void send()//���Է����ļ�
	{
		clear();
		frame.textBox("input").enterText("��Ϣ���ͳɹ��������������ļ����͡�    ");
		frame.button("send").click();//�����ķ����ļ���ť
		frame.fileChooser("open").fileNameTextBox().enterText(//����Ҫ���͵��ļ�Ŀ¼
				"E:\\ѧϰ\\�������\\tools.zip");
		frame.fileChooser("open").approveButton().click();//����ȷ����ť
	}
	public void accept()//���Խ����ļ�
	{
		clear();
		frame.textBox("input").enterText("�յ��ļ��������󣬽����������ļ����ա�    ");
		frame.button("accept").click();//�յ��ļ����󵥻����հ�ť
		frame.fileChooser("save").fileNameTextBox().enterText(//�����ļ�����·��
				"C:\\Users\\Administrator\\Desktop");
		frame.fileChooser("save").approveButton().click();//�������水ť
		clear();
		frame.textBox("input").enterText("�ļ����ճɹ������Ե�����鿴��          ");
		frame.textBox("input").enterText("\n�Զ����Գɹ�  �������˳�......    ");
	
	}
	void clear()//����ı�������
	{
		frame.textBox("input").deleteText();
	}

}
