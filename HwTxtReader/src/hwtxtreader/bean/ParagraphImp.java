package hwtxtreader.bean;

/**
 * @author huangwei
 * 2016下午5:23:02
 * 主页：http://blog.csdn.net/u014614038
 * 
 */
public class ParagraphImp implements Paragraph {
	private int index;
	private String data;

	@Override
	public void setParagraphIndex(int index) {
		this.index = index;
	}

	@Override
	public void Clear() {
	}

	@Override
	public void addStringdata(String str) {
		data=str;
	}

	@Override
	public int getIndex() {
		
		return index;
	}

	@Override
	public String getStringdata() {
		
		return data;
	}

	
}
