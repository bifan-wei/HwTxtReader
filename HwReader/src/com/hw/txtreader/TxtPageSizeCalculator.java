package com.hw.txtreader;

import java.util.Random;

import com.hw.readermain.IPageSizeCalculator;

import android.graphics.Paint;

/**
 * @author 黄威 2016年10月25日下午4:11:52 主页：http://blog.csdn.net/u014614038
 */
public class TxtPageSizeCalculator implements IPageSizeCalculator {
	private static TxtPageSizeCalculator calculator;
	private static TxtReaderContex readerContex;
	private final String MyTestStr = "话说天下大势，分久必合，合久必分。周末七国分争，并入于秦。及秦灭之后，楚、汉分争，又并入于汉。汉朝自高祖斩白蛇而起义，一统天下，后来光武中兴，传至献帝，遂分为三国。推其致乱之由，殆始于桓、灵二帝。桓帝禁锢善类，崇信宦官。及桓帝崩，灵帝即位，大将军窦武、太傅陈蕃共相辅佐。时有宦官曹节等弄权，窦武、陈蕃谋诛之，机事不密，反为所害，中涓自此愈横。建宁二年四月望日，帝御温德殿。方升座，殿角狂风骤起。只见一条大青蛇，从梁上飞将下来，蟠于椅上。帝惊倒，左右急救入宫，百官俱奔避。须臾，蛇不见了。忽然大雷大雨，加以冰雹，落到半夜方止，坏却房屋无数。建宁四年二月，洛阳地震；又海水泛溢，沿海居民，尽被大浪卷入海中。光和元年，雌鸡化雄。六月朔，黑气十余丈，飞入温德殿中。秋七月，有虹现于玉堂；五原山岸，尽皆崩裂。种种不祥，非止一端。帝下诏问群臣以灾异之由，议郎蔡邕上疏，以为堕鸡化，乃妇寺干政之所致，言颇切直。帝览奏叹息，因起更衣。曹节在后窃视，悉宣告左右；遂以他事陷邕于罪，放归田里。后张让、赵忠、封、段、曹节、侯览、蹇硕、程旷、夏恽、郭胜十人朋比为奸，号为“十常侍”。帝尊信张让，呼为“阿父”。朝政日非，以致天下人心思乱，盗贼蜂起。 时巨鹿郡有兄弟三人，一名张角，一名张宝，一名张梁。那张角本是个不第秀才，因入山采药，遇一老人，碧眼童颜，手执藜杖，唤角至一洞中，以天书三卷授之，曰：“此名《太平要术》，汝得之，当代天宣化，普救世人；若萌异心，必获恶报。”角拜问姓名。老人曰：“吾乃南华老仙也。”言讫，化阵清风而去。角得此书，晓夜攻习，能呼风唤雨，号为“太平道人”。中平元年正月内，疫气流行，张角散施符水，为人治病，自称“大贤良师”。角有徒弟五百余人，云游四方，皆能书符念咒。次后徒众日多，角乃立三十六方，大方万余人，小方六七千，各立渠帅，称为将军；讹言：“苍天已死，黄天当立；岁在甲子，天下大吉。”令人各以白土书“甲子”二字于家中大门上。青、幽、徐、冀、荆、扬、兖、豫八州之人，家家侍奉大贤良师张角名字。角遣其党马元义，暗赍金帛，结交中涓封，以为内应。角与二弟商议曰：“至难得者，民心也。今民心已顺，若不乘势取天下，诚为可惜。”遂一面私造黄旗，约期举事；一面使弟子唐周，驰书报封。唐周乃径赴省中告变。帝召大将军何进调兵擒马元义，斩之；次收封等一干人下狱。张角闻知事露，星夜举兵，自称“天公将军”，张宝称“地公将军”，张梁称“人公将军”。申言于众曰：“今汉运将终，大圣人出。汝等皆宜顺天从正，以乐太平。”四方百姓，裹黄巾从张角反者四五十万。贼势浩大，官军望风而靡。何进奏帝火速降诏，令各处备御，讨贼立功。一面遣中郎将卢植、皇甫嵩、朱，各引精兵、分三路讨之。";
	private float perpagecharwidth = -1;

	public static TxtPageSizeCalculator getInstance(TxtReaderContex readerContex) {
		if (calculator == null) {
			calculator = new TxtPageSizeCalculator();
		}

		TxtPageSizeCalculator.readerContex = readerContex;
		return calculator;
	}

	@Override
	public int getPageNums() {
		long charnums = readerContex.mCacheCenter.getAllCharNums();
		int perpagecharnums = calculateperpagecharnums();

		if (perpagecharnums == 0) {
			return 1;
		}
		
		float nums = (charnums / perpagecharnums);
		
		if (((int) nums * 10) % 10 > 0) {
			return ((int) nums) + 2;
		}
		return (int)nums  + 1;
	}

	@Override
	public int getPageNumsByCharNums(long chartnums) {
		int perpagecharnums = calculateperpagecharnums();
		if (perpagecharnums == 0) {
			return 1;
		}
		float nums = chartnums / perpagecharnums;
		
		if (((int) nums * 10) % 10 > 0) {
			return ((int) nums) + 2;
		}
		return (int) nums+1;
	}

	private int calculateperpagecharnums() {
		int perpagelinenums = readerContex.mPaintContex.getPageLineNums();
		int perlinecharnums = getperlinecharnums();
		return perpagelinenums * perlinecharnums;
	}

	private int getperlinecharnums() {
		float viewwidth = readerContex.mPaintContex.Viewwidth;
		float percharwidth = getpercharwidth();
		if (percharwidth == 0) {
			return 0;
		}
		return (int) (viewwidth / percharwidth);
	}

	private float getpercharwidth() {
		if (perpagecharwidth < 0) {
			char[] samplechars = getSamplechars();
			if (samplechars.length == 0) {
				samplechars = MyTestStr.toCharArray();
			}

			Paint paint = new Paint();
			paint.setTextSize(readerContex.mViewSetting.TextSize);

			float width = 0;
			width = paint.measureText(samplechars, 0, samplechars.length);
			perpagecharwidth = width / samplechars.length;
			

		}
		return perpagecharwidth;
	}

	private char[] getSamplechars() {

		Random random = new Random();
		String str = "";
		long charnums = readerContex.mCacheCenter.getAllCharNums();
		int paragraphnums = readerContex.mCacheCenter.getParagraphSize();

		while (str.length() < charnums && str.length() < 512) {
			int index = random.nextInt(paragraphnums);
			String paragraphstr = readerContex.mCacheCenter.getParagraphString(index);
			str = str + paragraphstr;
		}
		return str.toCharArray();
	}

}
