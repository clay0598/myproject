package com.lenovo.nfv.bp;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;

final public class XnbFileCreate{
	
	final static String crlf = System.getProperty("line.separator");				//跨平台的换行符
	final static String now = new SimpleDateFormat("yyyyMM").format(new Date());
	
	/**
	 * @author 王冠
	 * @param file：上传文件
	 * @param area：行政区域
	 * @return 回盘文件名
	 * @throws IOException 
	 */
	public static String getFileName(String file, String area) throws IOException {
		/*初始化变量*/
		FileReader fr = null;												//读取上传文件字符流
		BufferedReader br = null;											//缓冲从上传文件字符流读取的文本
		String list = null;													//遍历过程中，上传文件的行数据
		StringBuffer sb = new StringBuffer(area + "城居保险发放回盘" + now + "-");//存储要写入目标文件的数据
		int count = 0;
		try {
			fr = new FileReader(file);
			br = new BufferedReader(fr);
			/*遍历上传文件，将准备写入的数据存入sb*/
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
	 * @author 王冠
	 * @param file：上传文件
	 * @param destinationFile：目标文件
	 * @throws IOException 
	 */
	public static void copyFile(String file, String destinationPath, String destinationFile) throws IOException {
		/*目标文件夹不存在，则生成*/
		if(!new File(destinationPath).exists()){
			new File(destinationPath).mkdirs();
		}
		
		/*初始化变量*/
		FileReader fr = new FileReader(file);								//读取上传文件字符流
		BufferedReader br = new BufferedReader(fr);							//缓冲从上传文件字符流读取的文本
		FileWriter fw = new FileWriter(destinationPath + destinationFile);	//写入目标文件字符流
		BufferedWriter bw = new BufferedWriter(fw);							//缓冲写入目标文件字符流的文本 
		String list = null;													//遍历过程中，上传文件的行数据
		StringBuffer sb = new StringBuffer("");								//存储要写入目标文件的数据
		
		try {
			/*遍历上传文件，将准备写入的数据存入sb*/
			while ((list = br.readLine()) != null) {
				sb.append(list + crlf);
			}
			
			/*将暂存在sb中的数据一次性写入文件*/
			bw.write(sb.toString());
			
		} catch (IOException e) {
			e.printStackTrace();
		}finally{
			/*关闭流*/
			bw.close();
			br.close();
			fw.close();
			fr.close();
		}
	}
	
	/**
	 * @author 王冠
	 * @param file：报盘文件
	 * @param bankInterfaceFile：银行接口文件
	 * @return message：错误信息
	 * @throws IOException 
	 */
	public static String bankInterfaceFile(String file, String bankInterfaceFile) throws ArrayIndexOutOfBoundsException, IOException {
		/*初始化变量*/
		String message = "";												//错误信息
		FileReader fr = new FileReader(file);								//读取报盘文件字符流
		BufferedReader br = new BufferedReader(fr);							//缓冲从报盘文件字符流读取的文本
		FileWriter fw = new FileWriter(bankInterfaceFile);					//写入银行接口文件字符流
		BufferedWriter bw = new BufferedWriter(fw);							//缓冲写入银行接口文件字符流的文本 
		String list = null;													//遍历过程中，报盘文件的行数据
		String array[];														//遍历过程中，将报盘文件行数据按tab拆分成数组
		int person = 0;	
		StringBuffer sb = new StringBuffer("");								//存储要写入银行接口文件的数据
		try {
			/*遍历报盘文件，将准备写入的数据存入sb*/
			while ((list = br.readLine()) != null) {
				if(person == 0){
					person++;
					continue;
				}
				/*追加当前记录*/
				array = list.trim().split("\t");
				sb.append("\t" + array[6] + "\t" + array[7] + "\t" + array[8] + crlf);
			}
			/*将暂存在sb中的数据一次性写入银行接口文件*/
			bw.write(sb.toString());
		} catch (ArrayIndexOutOfBoundsException e) {
			String item = "";
			switch(Integer.valueOf(e.getMessage())){
				case 6: item = "姓名、银行账户、金额";break;
				case 7: item = "银行账户、金额";break;
				case 8: item = "金额";break;
			}
			message = "报盘文件的" + item + "项不存在！";
		} catch(IOException e) {
			message = e.getMessage();
		} finally{
			/*关闭流*/
			bw.close();
			br.close();
			fw.close();
			fr.close();
		}
		return message;
	}
	
	/**
	 * @author 王冠
	 * @param file：报盘文件
	 * @param resultFile：结果清单
	 * @param receiveFile：回盘文件
	 * @throws IOException 
	 */
	public static String receiveFile(String file, String resultFile, String receiveFile) throws IOException {
		String message = "";
		/*初始化变量——报盘文件*/
		FileReader frFile = new FileReader(file);							//读取报盘文件字符流
		BufferedReader brFile = new BufferedReader(frFile);					//缓冲从报盘文件字符流读取的文本
		String fileList = null;												//报盘文件行数据
		String[] arrayFile;													//将报盘文件行数据按tab拆分成数组
		int person = 0;														//人数
		//BigDecimal salary = new BigDecimal("0.00");						//工资总额
		StringBuffer sbFile = new StringBuffer("");							//存储要写入回盘文件的数据
		/*初始化变量——结果清单*/	
		FileReader frResult = new FileReader(resultFile);					//读取结果清单字符流
		BufferedReader brResult = new BufferedReader(frResult);				//缓冲从结果清单字符流读取的文本
		String resultList = null;											//结果清单行数据
		String[] arrayResult;												//将结果清单行按crlf拆分成数组
		String[] arrayLine;													//将结果清单行数据按tab拆分成数组
		String wrongmessage = null;											//错误信息
		int code = 0;														//错误码
		StringBuffer sbResult = new StringBuffer("");						//存储结果清单的数据
		/*初始化变量——回盘清单*/
		FileWriter fw = new FileWriter(receiveFile);						//写入回盘文件字符流
		BufferedWriter bw = new BufferedWriter(fw);							//缓冲写入回盘文件字符流的文本 
		try {
			/*将结果清单中的身份证，错误码项存入sbResult中*/
			while ((resultList = brResult.readLine()) != null) {
				arrayLine = resultList.split("\t");
				sbResult.append(arrayLine[6] + "\t" + arrayLine[7] + "\t" + arrayLine[arrayLine.length - 1] + crlf);
			}
			/*关闭流*/
			brResult.close();
			frResult.close();
			/*遍历报盘文件，将对应的结果清单中的对错标记、错误码追加到报盘文件，写入sbFile*/
			while ((fileList = brFile.readLine()) != null) {
				//sbFile.append(fileList.trim() + "\t");
				/*第一行直接写入*/
				if(person == 0){
					sbFile.append(fileList + crlf);
					person++;
					continue;
				}
				/*遍历结果清单，追加与报盘文件当前行对应的对错标记和错误码*/
				arrayResult = sbResult.toString().split(crlf);
				int i = 0;
				for(; i < arrayResult.length; i++){	
					if(arrayResult[i].indexOf(fileList.split("\t")[7]) != -1){
						if(!digitCheck(arrayResult[i].split("\t")[2])){
							wrongmessage = arrayResult[i].split("\t")[2];		
							code = wrongCode(wrongmessage);						
						}
						/*追加当前记录到sbFile*/
						sbFile.append(fileList.trim() + "\t" + ((wrongmessage == null) ? 1 : (0 + "\t" + (code == 0 ? "无对应错误码" : code))) + crlf);
						//salary = salary.add(new BigDecimal(fileList.split("\t")[8]));	
						wrongmessage = null;
						i = -1;
						break;
					}
				}
				/*报盘文件中的记录在结果清单中无法匹配*/
				if(i != -1){
					message = "报盘文件中的银行账户：" + fileList.split("\t")[7] + "所在记录无法在结果清单中找到，请核对报盘文件与结果清单是否对应！";
					return message;
				}
			}
			/*将报盘文件的人数、工资总额插入sbFile的表头并换行*/
			//sbFile.insert(0, (person-1) + "\t" + salary + crlf);					
			/*将暂存在sbFile中的数据一次性写入回盘文件*/
			bw.write(sbFile.toString());									
		} catch (ArrayIndexOutOfBoundsException e) {
			if(Integer.valueOf(e.getMessage()) == 7){
				message = "报盘文件的银行账户项不存在！";
				new File(receiveFile).deleteOnExit();
			}else{
				message = "结果清单的银行账户项不存在！";
			}
		} catch (IOException e) {
			message = e.getMessage();
		} finally{
			/*关闭流*/
			brFile.close();
			frFile.close();
			bw.close();
			fw.close();
		}
		return message;
	}
	
	/**
	 * @author 王冠
	 * @param input：待验证的字符串
	 * @return true（纯数字）/false（含有非数字）
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
	 * @author 王冠
	 * @param message：错误信息
	 * @return 错误代码
	 */
	static int wrongCode(String message){
		if(message.indexOf("余额不足扣款失败") != -1){
			return 101;
		}else if(message.indexOf("人员编号不存在") != -1){
			return 102;
		}else if(message.indexOf("银行账号校验失败") != -1 || message.indexOf("账号不存在") != -1){
			return 103;
		}else if(message.indexOf("公民身份号码校验失败") != -1){
			return 104;
		}else if(message.indexOf("姓名校验失败") != -1 || message.indexOf("账号与户名不匹配") != -1){
			return 105;
		}else if(message.indexOf("批扣账号注销或挂失") != -1){
			return 106;
		}else if(message.indexOf("拨付通知编号错误") != -1){
			return 201;
		}else if(message.indexOf("统筹区错误") != -1){
			return 202;
		}else if(message.indexOf("拨付人员校验失败") != -1){
			return 203;
		}else if(message.indexOf("当事人编号与证件号码不匹配") != -1){
			return 204;
		}else if(message.indexOf("当事人编号与姓名不匹配") != -1){
			return 205;
		}else if(message.indexOf("当事人编号错误") != -1){
			return 206;
		}else if(message.indexOf("当事人不存在拨付记录") != -1){
			return 207;
		}else if(message.indexOf("个人拨付金额与到账金额不相等") != -1){
			return 208;
		}else if(message.indexOf("重复导盘") != -1){
			return 209;
		}else if(message.indexOf("银行拨付失败") != -1){
			return 210;
		}else if(message.indexOf("代发账号注销或挂失") != -1 || message.indexOf("账户未绑定") != -1 || message.indexOf("账户状态不合适") != -1){
			return 211;
		}else{
			return 0;
		}
	}
}