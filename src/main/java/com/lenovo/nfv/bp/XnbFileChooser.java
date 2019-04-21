package com.lenovo.nfv.bp;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.File;
import java.io.IOException;
import java.util.Calendar;

final public class XnbFileChooser extends JFrame implements ActionListener, ItemListener{
	static String crlf = System.getProperty("line.separator");				//��ƽ̨�Ļ��з�
    final int month = Calendar.getInstance().get(Calendar.MONTH) + 1;		//��ȡ��ǰ�·�
    
    /*�����ļ�����·��*/
    String fileString = "�Ǿӱ�\\�����ļ�\\" + month + "��\\";
    String bankString = "�Ǿӱ�\\����ϵͳ�����ļ�\\" + month + "��\\";
    String resultString = "�Ǿӱ�\\����嵥\\" + month + "��\\";
    String receiveString = "�Ǿӱ�\\�����ļ�\\" + month + "��\\";
    
    /*��������*/
    Object[] area = new Object[]{"������", "������", "���¼���������", "���ü���������", "���¿�����", "�����", "����ɽ", "��԰��", "�Ϲ���", "���¾��ü���������"};
	
	JFrame frame = new JFrame("�ļ�����ϵͳ");
    JTabbedPane tabPane = new JTabbedPane();//ѡ�����
    
    Container conBank = new Container();//���֡������нӿ��ļ�
    JComboBox choiceBank = new JComboBox(area);
    JTextField textBank1 = new JTextField();
    JTextField textBank2 = new JTextField();
    JButton buttonBank2 = new JButton("�����ļ�");
    JButton buttonBank3 = new JButton("��������ϵͳ�����ļ�");
    JFileChooser file = new JFileChooser();//���нӿ��ļ�ѡ����
    
    Container conReceive = new Container();//���֡��������ļ�
    JComboBox choiceReceive = new JComboBox(area);
    JTextField textReceive1 = new JTextField();
    JTextField textReceive2 = new JTextField();
    JTextField textReceive3 = new JTextField();
    JButton buttonReceive2 = new JButton("�����ļ�");
    JButton buttonReceive3 = new JButton("����嵥");
    JButton buttonReceive4 = new JButton("���ɻ����ļ�");
    JFileChooser resultFile = new JFileChooser();//�����ļ�ѡ����
    
    XnbFileChooser(){
    	/*ȡ����Ļ�ĸ߶ȺͿ��*/
        double lx=Toolkit.getDefaultToolkit().getScreenSize().getWidth();
        double ly=Toolkit.getDefaultToolkit().getScreenSize().getHeight();
        frame.setLocation(new Point((int)(lx/2)-150,(int)(ly/2)-150));//�趨���ڳ���λ��
        frame.setSize(400,240);//�趨���ڴ�С
        frame.setContentPane(tabPane);//���ò���
        frame.setVisible(true);//���ڿɼ�
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);//ʹ�ܹرմ��ڣ���������
        
        /*�ļ�ѡ��Ĭ��·��*/
        file.setCurrentDirectory(new File(fileString));
        resultFile.setCurrentDirectory(new File(resultString));
        
        /*����ϵͳ�����ļ�*/
        choiceBank.addItemListener(this);
        choiceBank.setBounds(15,10,240,30);
        textBank1.setText((String)choiceBank.getSelectedItem());
        textBank1.setBounds(15,10,240,30);
        textBank2.setBounds(15,70,240,30);
        buttonBank2.setBounds(275,70,90,30);
        buttonBank3.setBounds(100,130,170,30);
        buttonBank2.addActionListener(this);//����¼�����
        buttonBank3.addActionListener(this);//����¼�����
        conBank.add(choiceBank);
        conBank.add(textBank1);
        conBank.add(textBank2);
        conBank.add(buttonBank2);
        conBank.add(buttonBank3);
        conBank.add(file);
        tabPane.add("����ϵͳ�����ļ�",conBank);//��Ӳ���1
       
        /*�����ļ�*/
        choiceReceive.addItemListener(this);
        choiceReceive.setBounds(15,10,240,30);
        textReceive1.setText((String)choiceReceive.getSelectedItem());
        textReceive1.setBounds(15,10,240,30);
        textReceive2.setBounds(15,50,240,30);
        textReceive3.setBounds(15,90,240,30);
        buttonReceive2.setBounds(275,50,90,30);
        buttonReceive3.setBounds(275,90,90,30);
        buttonReceive4.setBounds(100,130,170,30);
        buttonReceive2.addActionListener(this);//����¼�����
        buttonReceive3.addActionListener(this);//����¼�����
        buttonReceive4.addActionListener(this);//����¼�����
        conReceive.add(choiceReceive);
        conReceive.add(textReceive1);
        conReceive.add(textReceive2);
        conReceive.add(textReceive3);
        conReceive.add(buttonReceive2);
        conReceive.add(buttonReceive3);
        conReceive.add(buttonReceive4);
        conReceive.add(resultFile);
        tabPane.add("�����ļ�",conReceive);//��Ӳ���2
    }
    
    /*�¼�����*/
    public void actionPerformed(ActionEvent e){
        if(e.getSource().equals(buttonBank2)){
        	file.setFileSelectionMode(0);//�趨ֻ��ѡ���ļ�
            int state= file.showOpenDialog(null);//�˾��Ǵ��ļ�ѡ��������Ĵ������
            if(state==1){
                return;//�����򷵻�
            }else{
                File f=file.getSelectedFile();//fΪѡ�񵽵��ļ�
                textBank2.setText(f.getAbsolutePath());
            }
        }
        if(e.getSource().equals(buttonBank3)){
        	String message = "", path = "";
        	if(textBank2.getText().equals("")){
        		message = "��ѡ�����ļ���";
        	}else if(!textBank2.getText().endsWith(".txt")){
				message = "�����ļ���ʽ����ȷ�����ϴ�txt��ʽ���ļ���";
			}else{
        		/*����ϵͳ�����ļ�Ŀ¼�����ڣ�������*/
        		if(!new File(bankString + textBank1.getText()).exists()){
					new File(bankString + textBank1.getText()).mkdirs();
				}
				try {
					message = XnbFileCreate.bankInterfaceFile(textBank2.getText(), bankString + textBank1.getText() + "\\����ϵͳ�����ļ�.txt");
					if(message == ""){
						/*����ϵͳ�����ļ�����·��*/
						File f = new File(bankString + textBank1.getText() + "\\����ϵͳ�����ļ�.txt");
						path = f.getCanonicalPath();
						//���Ʊ����ļ�
						XnbFileCreate.copyFile(textBank2.getText(), fileString + textBank1.getText(), "\\�����ļ�1.txt");
						new File(fileString + textBank1.getText() + "\\�����ļ�.txt").delete();
						new File(fileString + textBank1.getText() + "\\�����ļ�1.txt").renameTo(new File(fileString + textBank1.getText() + "\\�����ļ�.txt"));
						//�����ɵ����нӿ��ļ�����Ŀ¼
						//java.awt.Desktop.getDesktop().open(new File(destinationPath + "\\���нӿ��ļ�.txt"));    
					}
				} catch (Exception e1) {
					message = e1.getMessage();
				} 
        	}
        	JOptionPane.showMessageDialog(conBank, message == "" ? "�ɹ���������ϵͳ�����ļ������λ�ã�" + crlf + path : message, message == "" ? "SUCCESS" : "FAILED", message == "" ? 1 : 0);
        }
        
        /*�����ļ�*/
        if(e.getSource().equals(buttonReceive2)){
            file.setFileSelectionMode(0);//�趨ֻ��ѡ���ļ�
            int state=file.showOpenDialog(null);//�˾��Ǵ��ļ�ѡ��������Ĵ������
            if(state==1){
                return;//�����򷵻�
            }else{
                File f=file.getSelectedFile();//fΪѡ�񵽵��ļ�
                textReceive2.setText(f.getAbsolutePath());
            }
        }
        if(e.getSource().equals(buttonReceive3)){
            resultFile.setFileSelectionMode(0);//�趨ֻ��ѡ���ļ�
            int state=resultFile.showOpenDialog(null);//�˾��Ǵ��ļ�ѡ��������Ĵ������
            if(state==1){
                return;//�����򷵻�
            }else{
                File f=resultFile.getSelectedFile();//fΪѡ�񵽵��ļ�
                textReceive3.setText(f.getAbsolutePath());
            }
        }
        if(e.getSource().equals(buttonReceive4)){
        	String message = "", path = "";
        	if(textReceive2.getText().equals("")){
        		message = "��ѡ�����ļ���";
        	}else if(textReceive3.getText().equals("")){
        		message = "��ѡ�����嵥��";
        	}else if(!textReceive2.getText().endsWith(".txt")){
				message = "�����ļ���ʽ����ȷ�����ϴ�txt��ʽ���ļ�";
			}else if(!textReceive3.getText().endsWith(".txt")){
				message = "����嵥��ʽ����ȷ�����ϴ�txt��ʽ���ļ�";
			}else{
				try {
					/*�����ļ�Ŀ¼�����ڣ�������*/
					if(!new File(receiveString + textReceive1.getText()).exists()){
						new File(receiveString + textReceive1.getText()).mkdirs();
					}
					message = XnbFileCreate.receiveFile(textReceive2.getText(), textReceive3.getText(), receiveString + textReceive1.getText() + "\\" + XnbFileCreate.getFileName(textReceive2.getText(), textReceive1.getText()));
					if(message == ""){
						/*�����ļ�����·��*/
						File f = new File(receiveString + textReceive1.getText() + "\\" + XnbFileCreate.getFileName(textReceive2.getText(), textReceive1.getText()));
						path = f.getCanonicalPath();
						//���ƽ���嵥
	        			XnbFileCreate.copyFile(textReceive3.getText(), resultString + textReceive1.getText(), "\\����嵥1.txt");
	        			new File(resultString + textReceive1.getText() + "\\����嵥.txt").delete();
	        			new File(resultString + textReceive1.getText() + "\\����嵥1.txt").renameTo(new File(resultString + textReceive1.getText() + "\\����嵥.txt"));
	        		}
				} catch (IOException e1) {
					message = e1.getMessage();
				}
        	}
        	JOptionPane.showMessageDialog(conReceive, message == "" ? "�ɹ����ɻ����ļ������λ�ã�" + crlf + path : message, message == "" ? "SUCCESS" : "FAILED", message == "" ? 1 : 0);
        }
    }
    
    /*��Ӧ�������¼�*/
    public void itemStateChanged(ItemEvent e) {
    	/*�ж��ĸ�����������˴˷���*/
    	ItemSelectable choice = e.getItemSelectable();
    	if(choice == choiceBank){
    		textBank1.setText((String)e.getItem());
    	}else{
    		textReceive1.setText((String)e.getItem());
    	}
    }
    
    public static void main(String[] args) {
        new XnbFileChooser();
    }
}