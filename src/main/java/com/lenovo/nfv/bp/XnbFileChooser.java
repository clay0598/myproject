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
	static String crlf = System.getProperty("line.separator");				//跨平台的换行符
    final int month = Calendar.getInstance().get(Calendar.MONTH) + 1;		//获取当前月份
    
    /*各种文件所在路径*/
    String fileString = "城居保\\报盘文件\\" + month + "月\\";
    String bankString = "城居保\\银行系统导入文件\\" + month + "月\\";
    String resultString = "城居保\\结果清单\\" + month + "月\\";
    String receiveString = "城居保\\回盘文件\\" + month + "月\\";
    
    /*行政区域*/
    Object[] area = new Object[]{"朝阳区", "二道区", "高新技术开发区", "经济技术开发区", "净月开发区", "宽城区", "莲花山", "绿园区", "南关区", "西新经济技术开发区"};
	
	JFrame frame = new JFrame("文件生成系统");
    JTabbedPane tabPane = new JTabbedPane();//选项卡布局
    
    Container conBank = new Container();//布局——银行接口文件
    JComboBox choiceBank = new JComboBox(area);
    JTextField textBank1 = new JTextField();
    JTextField textBank2 = new JTextField();
    JButton buttonBank2 = new JButton("报盘文件");
    JButton buttonBank3 = new JButton("生成银行系统导入文件");
    JFileChooser file = new JFileChooser();//银行接口文件选择器
    
    Container conReceive = new Container();//布局——回盘文件
    JComboBox choiceReceive = new JComboBox(area);
    JTextField textReceive1 = new JTextField();
    JTextField textReceive2 = new JTextField();
    JTextField textReceive3 = new JTextField();
    JButton buttonReceive2 = new JButton("报盘文件");
    JButton buttonReceive3 = new JButton("结果清单");
    JButton buttonReceive4 = new JButton("生成回盘文件");
    JFileChooser resultFile = new JFileChooser();//回盘文件选择器
    
    XnbFileChooser(){
    	/*取得屏幕的高度和宽度*/
        double lx=Toolkit.getDefaultToolkit().getScreenSize().getWidth();
        double ly=Toolkit.getDefaultToolkit().getScreenSize().getHeight();
        frame.setLocation(new Point((int)(lx/2)-150,(int)(ly/2)-150));//设定窗口出现位置
        frame.setSize(400,240);//设定窗口大小
        frame.setContentPane(tabPane);//设置布局
        frame.setVisible(true);//窗口可见
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);//使能关闭窗口，结束程序
        
        /*文件选择默认路径*/
        file.setCurrentDirectory(new File(fileString));
        resultFile.setCurrentDirectory(new File(resultString));
        
        /*银行系统导入文件*/
        choiceBank.addItemListener(this);
        choiceBank.setBounds(15,10,240,30);
        textBank1.setText((String)choiceBank.getSelectedItem());
        textBank1.setBounds(15,10,240,30);
        textBank2.setBounds(15,70,240,30);
        buttonBank2.setBounds(275,70,90,30);
        buttonBank3.setBounds(100,130,170,30);
        buttonBank2.addActionListener(this);//添加事件处理
        buttonBank3.addActionListener(this);//添加事件处理
        conBank.add(choiceBank);
        conBank.add(textBank1);
        conBank.add(textBank2);
        conBank.add(buttonBank2);
        conBank.add(buttonBank3);
        conBank.add(file);
        tabPane.add("银行系统导入文件",conBank);//添加布局1
       
        /*回盘文件*/
        choiceReceive.addItemListener(this);
        choiceReceive.setBounds(15,10,240,30);
        textReceive1.setText((String)choiceReceive.getSelectedItem());
        textReceive1.setBounds(15,10,240,30);
        textReceive2.setBounds(15,50,240,30);
        textReceive3.setBounds(15,90,240,30);
        buttonReceive2.setBounds(275,50,90,30);
        buttonReceive3.setBounds(275,90,90,30);
        buttonReceive4.setBounds(100,130,170,30);
        buttonReceive2.addActionListener(this);//添加事件处理
        buttonReceive3.addActionListener(this);//添加事件处理
        buttonReceive4.addActionListener(this);//添加事件处理
        conReceive.add(choiceReceive);
        conReceive.add(textReceive1);
        conReceive.add(textReceive2);
        conReceive.add(textReceive3);
        conReceive.add(buttonReceive2);
        conReceive.add(buttonReceive3);
        conReceive.add(buttonReceive4);
        conReceive.add(resultFile);
        tabPane.add("回盘文件",conReceive);//添加布局2
    }
    
    /*事件处理*/
    public void actionPerformed(ActionEvent e){
        if(e.getSource().equals(buttonBank2)){
        	file.setFileSelectionMode(0);//设定只能选择到文件
            int state= file.showOpenDialog(null);//此句是打开文件选择器界面的触发语句
            if(state==1){
                return;//撤销则返回
            }else{
                File f=file.getSelectedFile();//f为选择到的文件
                textBank2.setText(f.getAbsolutePath());
            }
        }
        if(e.getSource().equals(buttonBank3)){
        	String message = "", path = "";
        	if(textBank2.getText().equals("")){
        		message = "请选择报盘文件！";
        	}else if(!textBank2.getText().endsWith(".txt")){
				message = "报盘文件格式不正确，请上传txt格式的文件！";
			}else{
        		/*银行系统导入文件目录不存在，则生成*/
        		if(!new File(bankString + textBank1.getText()).exists()){
					new File(bankString + textBank1.getText()).mkdirs();
				}
				try {
					message = XnbFileCreate.bankInterfaceFile(textBank2.getText(), bankString + textBank1.getText() + "\\银行系统导入文件.txt");
					if(message == ""){
						/*银行系统导入文件绝对路径*/
						File f = new File(bankString + textBank1.getText() + "\\银行系统导入文件.txt");
						path = f.getCanonicalPath();
						//复制报盘文件
						XnbFileCreate.copyFile(textBank2.getText(), fileString + textBank1.getText(), "\\报盘文件1.txt");
						new File(fileString + textBank1.getText() + "\\报盘文件.txt").delete();
						new File(fileString + textBank1.getText() + "\\报盘文件1.txt").renameTo(new File(fileString + textBank1.getText() + "\\报盘文件.txt"));
						//打开生成的银行接口文件所在目录
						//java.awt.Desktop.getDesktop().open(new File(destinationPath + "\\银行接口文件.txt"));    
					}
				} catch (Exception e1) {
					message = e1.getMessage();
				} 
        	}
        	JOptionPane.showMessageDialog(conBank, message == "" ? "成功生成银行系统导入文件！存放位置：" + crlf + path : message, message == "" ? "SUCCESS" : "FAILED", message == "" ? 1 : 0);
        }
        
        /*回盘文件*/
        if(e.getSource().equals(buttonReceive2)){
            file.setFileSelectionMode(0);//设定只能选择到文件
            int state=file.showOpenDialog(null);//此句是打开文件选择器界面的触发语句
            if(state==1){
                return;//撤销则返回
            }else{
                File f=file.getSelectedFile();//f为选择到的文件
                textReceive2.setText(f.getAbsolutePath());
            }
        }
        if(e.getSource().equals(buttonReceive3)){
            resultFile.setFileSelectionMode(0);//设定只能选择到文件
            int state=resultFile.showOpenDialog(null);//此句是打开文件选择器界面的触发语句
            if(state==1){
                return;//撤销则返回
            }else{
                File f=resultFile.getSelectedFile();//f为选择到的文件
                textReceive3.setText(f.getAbsolutePath());
            }
        }
        if(e.getSource().equals(buttonReceive4)){
        	String message = "", path = "";
        	if(textReceive2.getText().equals("")){
        		message = "请选择报盘文件！";
        	}else if(textReceive3.getText().equals("")){
        		message = "请选择结果清单！";
        	}else if(!textReceive2.getText().endsWith(".txt")){
				message = "报盘文件格式不正确，请上传txt格式的文件";
			}else if(!textReceive3.getText().endsWith(".txt")){
				message = "结果清单格式不正确，请上传txt格式的文件";
			}else{
				try {
					/*回盘文件目录不存在，则生成*/
					if(!new File(receiveString + textReceive1.getText()).exists()){
						new File(receiveString + textReceive1.getText()).mkdirs();
					}
					message = XnbFileCreate.receiveFile(textReceive2.getText(), textReceive3.getText(), receiveString + textReceive1.getText() + "\\" + XnbFileCreate.getFileName(textReceive2.getText(), textReceive1.getText()));
					if(message == ""){
						/*回盘文件绝对路径*/
						File f = new File(receiveString + textReceive1.getText() + "\\" + XnbFileCreate.getFileName(textReceive2.getText(), textReceive1.getText()));
						path = f.getCanonicalPath();
						//复制结果清单
	        			XnbFileCreate.copyFile(textReceive3.getText(), resultString + textReceive1.getText(), "\\结果清单1.txt");
	        			new File(resultString + textReceive1.getText() + "\\结果清单.txt").delete();
	        			new File(resultString + textReceive1.getText() + "\\结果清单1.txt").renameTo(new File(resultString + textReceive1.getText() + "\\结果清单.txt"));
	        		}
				} catch (IOException e1) {
					message = e1.getMessage();
				}
        	}
        	JOptionPane.showMessageDialog(conReceive, message == "" ? "成功生成回盘文件！存放位置：" + crlf + path : message, message == "" ? "SUCCESS" : "FAILED", message == "" ? 1 : 0);
        }
    }
    
    /*响应下拉框事件*/
    public void itemStateChanged(ItemEvent e) {
    	/*判断哪个下拉框调用了此方法*/
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