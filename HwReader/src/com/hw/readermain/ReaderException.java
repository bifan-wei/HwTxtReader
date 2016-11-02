package com.hw.readermain;
/**阅读器相关异常
 * @author 黄威
 * 2016年10月19日下午4:24:44
 * 主页：http://blog.csdn.net/u014614038
 */
public class ReaderException {
	
	public enum Type{
				
		bookcodeunsupport,
		bookfilenofound,
		loadbookioexception,
		sucess
	}

	public static String getExceptionMsg(Type exceptiontype){
		switch (exceptiontype) {
		case bookcodeunsupport:
			return "书籍编码不支持";
		case bookfilenofound:
			return "书籍文件找不到";
		case loadbookioexception:
			return "加载书籍时io异常";
		case sucess:
			return "初始化成功 ";
		default:
			break;
		}
		
		return "未知异常";
	}
	
}
