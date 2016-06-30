package hwtxtreader.pipeline;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import android.graphics.Paint;
import hwtxtreader.bean.CharElement;
import hwtxtreader.bean.LineChar;
import hwtxtreader.bean.LineCharImp;
import hwtxtreader.bean.Page;
import hwtxtreader.bean.Paragraph;
import hwtxtreader.bean.ParagraphCache;
import hwtxtreader.bean.ParagraphCacheImp;
import hwtxtreader.bean.ParagraphImp;
import hwtxtreader.bean.TxtErrorCode;
import hwtxtreader.bean.Txterror;
import hwtxtreader.main.Transformer;

public class TxtPipelineImp implements TxtPipeline {

	private ParagraphCache paragraphCache;

	public TxtPipelineImp() {
		paragraphCache = new ParagraphCacheImp();
	}

	@SuppressWarnings("resource")
	@Override
	public ParagraphCache LoadTxtFile(File txtfile, String txtcode, Transformer t) {

		BufferedReader bufferedReader = null;
		Txterror txterror = new Txterror();

		try {
			try {
				bufferedReader = new BufferedReader(new InputStreamReader(new FileInputStream(txtfile), txtcode));
			} catch (FileNotFoundException e) {
				txterror.txterrorcode = TxtErrorCode.LOAD_BOOK_EXCEPTION;
				txterror.message = "无法找到该书籍文件";
				t.PostError(txterror);
				t.PostResult(false);

				return paragraphCache;
			}
		} catch (UnsupportedEncodingException e) {

			txterror.txterrorcode = TxtErrorCode.LOAD_BOOK_EXCEPTION;
			txterror.message = "读取文件编码失败了";
			t.PostError(txterror);
			t.PostResult(false);
			return paragraphCache;
		}

		String data = "";

		int i = 0;
		try {
			while ((data = bufferedReader.readLine()) != null) {
				Paragraph paragraph = new ParagraphImp();
				paragraph.setParagraphIndex(i);
				paragraph.addStringdata(data);
				i++;
				paragraphCache.addParagraph(paragraph);
			}
		} catch (IOException e) {
			txterror.txterrorcode = TxtErrorCode.LOAD_BOOK_EXCEPTION;
			txterror.message = "加载书籍时出现io异常";
			t.PostError(txterror);
			t.PostResult(false);			
			return paragraphCache;
		}
		t.PostResult(true);

		return paragraphCache;
	}

	/**
	 * 思路就是一段一段的将段落数据转化为行数据，然后根据一页需要多少行进行填充 如果没有数据linesdata将会是空的
	 * 
	 * @param startparagraphindex
	 * @param startcharindex
	 * @param paint
	 * @param lineswidth
	 * @param linesnums
	 * @return 如果传入的开始位置超出了数据源本身，返回空，如果本身页面就没有数据，page不为null，数据为null
	 */
	@Override
	public Page getPageFromPosition(int startparagraphindex, int startcharindex, Paint paint, float lineswidth,
			int linesnums) {

		Page page = new Page();// 将这个一页的开始位置标志
		page.firstElementCharindex = startcharindex;
		page.firstElementParagraphIndex = startparagraphindex;

		int pindex = startparagraphindex;// 将段落游标指向开始位置
		List<LineChar> lines = new ArrayList<>();
		int countlinenums = 0;
		int psize = getParagraphCace().getParagraphSize();
		Paragraph paragraph = getParagraphCace().getParagraphByIndex(startparagraphindex);
		if (paragraph == null)// 开始段落位置超出了数据源
			return null;

		int length = paragraph.getStringdata().length();
		Boolean istheend = startcharindex == length;
		// 如果游标开始的话，开始应该上一个位置的下一个字符，如果是段落的结尾应该换下段落，否则就是下个字符
		if (istheend) {
			startcharindex = 0;
		} else { // 字符串不为空才是下一个字符
			startcharindex++;
		}
		// 先处理掉最开始的段落，因为可能开始字符不是第一个
		List<LineChar> s = getLinesFromParagraph(pindex, startcharindex, paint, lineswidth);
		lines.addAll(s);
		countlinenums = countlinenums + s.size();// 累加添加的行数

		startcharindex = 0;// 再吃标志开始字符为第一个
		pindex++;

		while (countlinenums < linesnums && pindex < psize) {// 一直填充直到超出页面行数
			s = getLinesFromParagraph(pindex, startcharindex, paint, lineswidth);
			lines.addAll(s);
			countlinenums = countlinenums + s.size();// 累加添加的行数
			pindex++;
		}

		if (lines.size() > 0) {
			// 添加的行数超出的话应该把超出的去掉
			if (lines.size() > linesnums) {
				while (lines.size() > linesnums) {
					lines.remove(lines.size() - 1);
				}
			}

			// 获取最后一行，并标志最后字符位置
			LineChar l = lines.get(lines.size() - 1);
			if (l.hasdata()) {// 也有可能最后一行并没有数据，没有数据就标志到下一段的开始位置
				page.lastElementCharindex = l.getLastElement().charindex;
				page.lastElementParagraphIndex = l.getLastElement().paragraphindex;

			} else {
				page.lastElementCharindex = 0;
				page.lastElementParagraphIndex = startparagraphindex + 1;

			}
			page.setLinesdata(lines);

		} else {// 没有数据
			page.lastElementCharindex = 0;
			page.lastElementParagraphIndex = startparagraphindex + 1;

		}

		return page;

	}

	@Override
	public List<LineChar> getLinesFromParagraph(Paragraph p, int startcharindex, Paint paint, float measurewith) {
		List<LineChar> linesdata = new ArrayList<LineChar>();
		Paragraph paragraph = p;
		Paint mPaint = paint;
		String str = paragraph.getStringdata();
		int length = str.length();
		int charindex = startcharindex;
		int pindex = p.getIndex();

		if (length > 0 && length > startcharindex) {
			str = str.substring(startcharindex, length);

			while (str.length() > 0) {
				int nums = mPaint.breakText(str, true, measurewith, null);
				if (nums <= str.length()) {
					LineChar line = getLinesFromString(str.substring(0, nums), pindex, charindex);
					linesdata.add(line);
					str = str.substring(nums, str.length());
					charindex = charindex + nums;
				} else {
					LineChar line = getLinesFromString(str, pindex, charindex);
					linesdata.add(line);
					str = "";
				}

			}

		}

		return linesdata;

	}

	@Override
	public List<LineChar> getLinesFromParagraph(int paragraphindex, int startcharindex, Paint paint,
			float measurewith) {
		return getLinesFromParagraph(paragraphCache.getParagraphByIndex(paragraphindex), startcharindex, paint,
				measurewith);

	}

	@Override
	public LineChar getLinesFromString(String data, int paragraphindex, int startcharindex) {
		LineChar cl = new LineCharImp();
		char[] cs = data.toCharArray();
		int clength = cs.length;
		if (clength > 0) {
			for (int i = 0; i < clength; i++) {
				CharElement charElement = new CharElement();
				charElement.paragraphindex = paragraphindex;
				charElement.data = cs[i];
				charElement.charindex = startcharindex + i;
				cl.addElement(charElement);
			}
		}
		return cl;

	}

	@Override
	public ParagraphCache getParagraphCace() {

		return paragraphCache;
	}

	@Override
	public Boolean HasCaChedata() {

		return paragraphCache.isHasParagraphCache();
	}

}
