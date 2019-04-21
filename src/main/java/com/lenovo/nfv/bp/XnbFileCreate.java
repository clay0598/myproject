package com.lenovo.nfv.bp;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;

final public class XnbFileCreate{
	
	final static String crlf = System.getProperty("line.separator");				//��ƽ̨�Ļ��з�
	final static String now = new SimpleDateFormat("yyyyMM").format(new Date());
	
	/**
	 * @author ����
	 * @param file���ϴ��ļ�
	 * @param area����������
	 * @return �����ļ���
	 * @throws IOException 
	 */
	public static String getFileName(String file, String area) throws IOException {
		/*��ʼ������*/
		FileReader fr = null;												//��ȡ�ϴ��ļ��ַ���
		BufferedReader br = null;											//������ϴ��ļ��ַ�����ȡ���ı�
		String list = null;													//���������У��ϴ��ļ���������
		StringBuffer sb = new StringBuffer(area + "�Ǿӱ��շ��Ż���" + now + "-");//�洢Ҫд��Ŀ���ļ�������
		int count = 0;
		try {
			fr = new FileReader(file);
			br = new BufferedReader(fr);
			/*�����ϴ��ļ�����׼��д������ݴ���sb*/
			while ((list = br.readLine()) != null) {
				if(count == 1){
					sb.append(list.split("\t")[0] + ".txt");
					break;
				}
				count++;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}finally{
			if(br != null){
				try {
					br.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			if(fr != null){
				try {
					fr.close();
				} catch (Exception e) {
					e.printStackTrace();
				}	
			}
		}
		return sb.toString();
	}
	
	/**
	 * @author ����
	 * @param file���ϴ��ļ�
	 * @param destinationFile��Ŀ���ļ�
	 * @throws IOException 
	 */
	public static void copyFile(String file, String destinationPath, String destinationFile) throws IOException {
		/*Ŀ���ļ��в����ڣ�������*/
		if(!new File(destinationPath).exists()){
			new File(destinationPath).mkdirs();
		}
		
		/*��ʼ������*/
		FileReader fr = new FileReader(file);								//��ȡ�ϴ��ļ��ַ���
		BufferedReader br = new BufferedReader(fr);							//������ϴ��ļ��ַ�����ȡ���ı�
		FileWriter fw = new FileWriter(destinationPath + destinationFile);	//д��Ŀ���ļ��ַ���
		BufferedWriter bw = new BufferedWriter(fw);							//����д��Ŀ���ļ��ַ������ı� 
		String list = null;													//���������У��ϴ��ļ���������
		StringBuffer sb = new StringBuffer("");								//�洢Ҫд��Ŀ���ļ�������
		
		try {
			/*�����ϴ��ļ�����׼��д������ݴ���sb*/
			while ((list = br.readLine()) != null) {
				sb.append(list + crlf);
			}
			
			/*���ݴ���sb�е�����һ����д���ļ�*/
			bw.write(sb.toString());
			
		} catch (IOException e) {
			e.printStackTrace();
		}finally{
			/*�ر���*/
			bw.close();
			br.close();
			fw.close();
			fr.close();
		}
	}
	
	/**
	 * @author ����
	 * @param file�������ļ�
	 * @param bankInterfaceFile�����нӿ��ļ�
	 * @return message��������Ϣ
	 * @throws IOException 
	 */
	public static String bankInterfaceFile(String file, String bankInterfaceFile) throws ArrayIndexOutOfBoundsException, IOException {
		/*��ʼ������*/
		String message = "";												//������Ϣ
		FileReader fr = new FileReader(file);								//��ȡ�����ļ��ַ���
		BufferedReader br = new BufferedReader(fr);							//����ӱ����ļ��ַ�����ȡ���ı�
		FileWriter fw = new FileWriter(bankInterfaceFile);					//д�����нӿ��ļ��ַ���
		BufferedWriter bw = new BufferedWriter(fw);							//����д�����нӿ��ļ��ַ������ı� 
		String list = null;													//���������У������ļ���������
		String array[];														//���������У��������ļ������ݰ�tab��ֳ�����
		int person = 0;	
		StringBuffer sb = new StringBuffer("");								//�洢Ҫд�����нӿ��ļ�������
		try {
			/*���������ļ�����׼��д������ݴ���sb*/
			while ((list = br.readLine()) != null) {
				if(person == 0){
					person++;
					continue;
				}
				/*׷�ӵ�ǰ��¼*/
				array = list.trim().split("\t");
				sb.append("\t" + array[6] + "\t" + array[7] + "\t" + array[8] + crlf);
			}
			/*���ݴ���sb�е�����һ����д�����нӿ��ļ�*/
			bw.write(sb.toString());
		} catch (ArrayIndexOutOfBoundsException e) {
			String item = "";
			switch(Integer.valueOf(e.getMessage())){
				case 6: item = "�����������˻������";break;
				case 7: item = "�����˻������";break;
				case 8: item = "���";break;
			}
			message = "�����ļ���" + item + "����ڣ�";
		} catch(IOException e) {
			message = e.getMessage();
		} finally{
			/*�ر���*/
			bw.close();
			br.close();
			fw.close();
			fr.close();
		}
		return message;
	}
	
	/**
	 * @author ����
	 * @param file�������ļ�
	 * @param resultFile������嵥
	 * @param receiveFile�������ļ�
	 * @throws IOException 
	 */
	public static String receiveFile(String file, String resultFile, String receiveFile) throws IOException {
		String message = "";
		/*��ʼ���������������ļ�*/
		FileReader frFile = new FileReader(file);							//��ȡ�����ļ��ַ���
		BufferedReader brFile = new BufferedReader(frFile);					//����ӱ����ļ��ַ�����ȡ���ı�
		String fileList = null;												//�����ļ�������
		String[] arrayFile;													//�������ļ������ݰ�tab��ֳ�����
		int person = 0;														//����
		//BigDecimal salary = new BigDecimal("0.00");						//�����ܶ�
		StringBuffer sbFile = new StringBuffer("");							//�洢Ҫд������ļ�������
		/*��ʼ��������������嵥*/	
		FileReader frResult = new FileReader(resultFile);					//��ȡ����嵥�ַ���
		BufferedReader brResult = new BufferedReader(frResult);				//����ӽ���嵥�ַ�����ȡ���ı�
		String resultList = null;											//����嵥������
		String[] arrayResult;												//������嵥�а�crlf��ֳ�����
		String[] arrayLine;													//������嵥�����ݰ�tab��ֳ�����
		String wrongmessage = null;											//������Ϣ
		int code = 0;														//������
		StringBuffer sbResult = new StringBuffer("");						//�洢����嵥������
		/*��ʼ���������������嵥*/
		FileWriter fw = new FileWriter(receiveFile);						//д������ļ��ַ���
		BufferedWriter bw = new BufferedWriter(fw);							//����д������ļ��ַ������ı� 
		try {
			/*������嵥�е����֤�������������sbResult��*/
			while ((resultList = brResult.readLine()) != null) {
				arrayLine = resultList.split("\t");
				sbResult.append(arrayLine[6] + "\t" + arrayLine[7] + "\t" + arrayLine[arrayLine.length - 1] + crlf);
			}
			/*�ر���*/
			brResult.close();
			frResult.close();
			/*���������ļ�������Ӧ�Ľ���嵥�еĶԴ��ǡ�������׷�ӵ������ļ���д��sbFile*/
			while ((fileList = brFile.readLine()) != null) {
				//sbFile.append(fileList.trim() + "\t");
				/*��һ��ֱ��д��*/
				if(person == 0){
					sbFile.append(fileList + crlf);
					person++;
					continue;
				}
				/*��������嵥��׷���뱨���ļ���ǰ�ж�Ӧ�ĶԴ��Ǻʹ�����*/
				arrayResult = sbResult.toString().split(crlf);
				int i = 0;
				for(; i < arrayResult.length; i++){	
					if(arrayResult[i].indexOf(fileList.split("\t")[7]) != -1){
						if(!digitCheck(arrayResult[i].split("\t")[2])){
							wrongmessage = arrayResult[i].split("\t")[2];		
							code = wrongCode(wrongmessage);						
						}
						/*׷�ӵ�ǰ��¼��sbFile*/
						sbFile.append(fileList.trim() + "\t" + ((wrongmessage == null) ? 1 : (0 + "\t" + (code == 0 ? "�޶�Ӧ������" : code))) + crlf);
						//salary = salary.add(new BigDecimal(fileList.split("\t")[8]));	
						wrongmessage = null;
						i = -1;
						break;
					}
				}
				/*�����ļ��еļ�¼�ڽ���嵥���޷�ƥ��*/
				if(i != -1){
					message = "�����ļ��е������˻���" + fileList.split("\t")[7] + "���ڼ�¼�޷��ڽ���嵥���ҵ�����˶Ա����ļ������嵥�Ƿ��Ӧ��";
					return message;
				}
			}
			/*�������ļ��������������ܶ����sbFile�ı�ͷ������*/
			//sbFile.insert(0, (person-1) + "\t" + salary + crlf);					
			/*���ݴ���sbFile�е�����һ����д������ļ�*/
			bw.write(sbFile.toString());									
		} catch (ArrayIndexOutOfBoundsException e) {
			if(Integer.valueOf(e.getMessage()) == 7){
				message = "�����ļ��������˻�����ڣ�";
				new File(receiveFile).deleteOnExit();
			}else{
				message = "����嵥�������˻�����ڣ�";
			}
		} catch (IOException e) {
			message = e.getMessage();
		} finally{
			/*�ر���*/
			brFile.close();
			frFile.close();
			bw.close();
			fw.close();
		}
		return message;
	}
	
	/**
	 * @author ����
	 * @param input������֤���ַ���
	 * @return true�������֣�/false�����з����֣�
	 */
	static boolean digitCheck(String input) {
	    for(int i = 0; i < input.length(); i++) {
	        char c = input.charAt(i);
	        if( (c < '0' || c > '9') ) {
	        return false;
	        }
	    }
	    return true;
	}
	
	/**
	 * @author ����
	 * @param message��������Ϣ
	 * @return �������
	 */
	static int wrongCode(String message){
		if(message.indexOf("����ۿ�ʧ��") != -1){
			return 101;
		}else if(message.indexOf("��Ա��Ų�����") != -1){
			return 102;
		}else if(message.indexOf("�����˺�У��ʧ��") != -1 || message.indexOf("�˺Ų�����") != -1){
			return 103;
		}else if(message.indexOf("������ݺ���У��ʧ��") != -1){
			return 104;
		}else if(message.indexOf("����У��ʧ��") != -1 || message.indexOf("�˺��뻧����ƥ��") != -1){
			return 105;
		}else if(message.indexOf("�����˺�ע�����ʧ") != -1){
			return 106;
		}else if(message.indexOf("����֪ͨ��Ŵ���") != -1){
			return 201;
		}else if(message.indexOf("ͳ��������") != -1){
			return 202;
		}else if(message.indexOf("������ԱУ��ʧ��") != -1){
			return 203;
		}else if(message.indexOf("�����˱����֤�����벻ƥ��") != -1){
			return 204;
		}else if(message.indexOf("�����˱����������ƥ��") != -1){
			return 205;
		}else if(message.indexOf("�����˱�Ŵ���") != -1){
			return 206;
		}else if(message.indexOf("�����˲����ڲ�����¼") != -1){
			return 207;
		}else if(message.indexOf("���˲�������뵽�˽����") != -1){
			return 208;
		}else if(message.indexOf("�ظ�����") != -1){
			return 209;
		}else if(message.indexOf("���в���ʧ��") != -1){
			return 210;
		}else if(message.indexOf("�����˺�ע�����ʧ") != -1 || message.indexOf("�˻�δ��") != -1 || message.indexOf("�˻�״̬������") != -1){
			return 211;
		}else{
			return 0;
		}
	}
}