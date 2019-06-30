package com.bifan.txtreaderlib.main;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;

/**
 * Created by HP on 2017/11/26.
 */

public class TxtConfig {

    public static final String SAVE_NAME = "TxtConfig";
    public static final String C_TEXT_SIZE = "TEXT_SIZE ";
    public static final String C_TEXT_COLOR = "TEXT_COLOR";
    public static final String C_NOTE_TEXT_COLOR = "TEXT_COLOR";
    public static final String C_SLIDER_COLOR = "SLIDER_COLOR";
    public static final String C_SELECT_TEXT_COLOR = "SELECTED_TEXT_COLOR";
    public static final String C_BACKGROUND_COLOR = "BACKGROUND_COLOR";
    public static final String C_IS_SHOW_NOTE = "IS_SHOW_NOTE";
    public static final String C_CAN_PRESS_SELECT = "CAN_PRESS_SELECT";
    public static final String C_SWITCH_BY_TRANSLATE = "SWITCH_BY_TRANSLATE";
    public static final String C_BOLD = "BOLD ";
    public static final String C_SHOW_SPECIAL_CHAR = "SHOW_SPECIAL_CHAR ";
    public static final String C_CENTER_CLICK_AREA = "CENTER_CLICK_AREA";
    public static final String C_PAGE_SWITCH_DURATION = "PAGE_SWITCH_DURATION";
    public static final String C_PAGE_VERTICAL_MODE = "PAGE_VERTICAL_MODE ";
    public static final String C_PAGE_SWITCH_STYPE_MODE = "PAGE_SWITCH_STYPE_MODE ";

    public static  final int PAGE_SWITCH_MODE_COVER = 1;//in px
    public static  final int PAGE_SWITCH_MODE_SERIAL = 2;//in px
    public static  final int PAGE_SWITCH_MODE_SHEAR = 3;//in px

    public static  int Page_PaddingLeft = 20;//in px
    public static  int Page_PaddingBottom = 20;//in px
    public static  int Page_PaddingTop = 20;//in px
    public static  int Page_PaddingRight = 20;//in px
    public static  int Page_LinePadding = 30;//in px
    public static  int Page_Paragraph_margin = 20;//in px,为0，没有间距

    public   int Page_Switch_Mode = PAGE_SWITCH_MODE_COVER;
    public static  int MAX_TEXT_SIZE = 150;//in px
    public static  int MIN_TEXT_SIZE = 30;//in px
    public static  int DEFAULT_SELECT_TEXT_COLOR = Color.parseColor("#44f6950b");
    public static  int DEFAULT_SLIDER_COLOR = Color.parseColor("#1f4cf5");

    public int textSize = MIN_TEXT_SIZE;//字体大小
    public int textColor = Color.BLACK;//字体颜色
    public int backgroundColor = Color.WHITE;//背景颜色
    public int NoteColor = Color.RED;//笔记颜色
    public int SelectTextColor = DEFAULT_SELECT_TEXT_COLOR;//选中颜色
    public int SliderColor = DEFAULT_SLIDER_COLOR;//滑动条颜色


    public Boolean showNote = true;//是否显示笔记
    public Boolean canPressSelect = true;//是否能长按选中
    //public Boolean SwitchByTranslate = true;//是否平移切换
    public Boolean VerticalPageMode = false;
    public Boolean Bold = false;//是否加粗
    public Boolean ShowSpecialChar = true;//是否显示特殊符号，对于数字、英文，可以显示特定颜色
    public float CenterClickArea = 0.35f;//0~1,中间点击区域占View宽度的百分比，区域为高为宽两倍的矩形，如果为1f，说明点击翻页将不起效果
    public int PageSwitchDuration = 400;//页面滑动时间间隔，毫秒，建议不要低于200

    public static final SharedPreferences getS(Context context) {
        SharedPreferences share = context.getSharedPreferences(SAVE_NAME, Context.MODE_PRIVATE);
        return share;
    }

    public static int getPageSwitchMode(Context context) {
        SharedPreferences share = getS(context);
        int PageSwitchMode =  share.getInt(C_PAGE_SWITCH_STYPE_MODE, PAGE_SWITCH_MODE_COVER);
        if(PageSwitchMode!=PAGE_SWITCH_MODE_COVER
                &&PageSwitchMode!= PAGE_SWITCH_MODE_SERIAL
                &&PageSwitchMode!= PAGE_SWITCH_MODE_SHEAR){
            return PAGE_SWITCH_MODE_COVER;
        }
        return PageSwitchMode;
    }

    /**
     * @param context
     * @param PageSwitchMode PAGE_SWITCH_MODE_COVER、PAGE_SWITCH_MODE_SERIAL、PAGE_SWITCH__MODE_SHEAR
     */
    public static void savePageSwitchMode(Context context,int PageSwitchMode) {
        SharedPreferences share = getS(context);
        SharedPreferences.Editor editor = share.edit();
        editor.putInt(C_PAGE_SWITCH_STYPE_MODE, PageSwitchMode);
        editor.commit();
    }


    /**
     * @param context
     * @param duration 不能低于100，建议200以上
     */
    public static void savePageSwitchDuration(Context context, int duration) {
        duration = duration < 100 ? 100 : duration;
        SharedPreferences share = getS(context);
        SharedPreferences.Editor editor = share.edit();
        editor.putInt(C_PAGE_SWITCH_DURATION, duration);
        editor.commit();
    }

    public static int getPageSwitchDuration(Context context) {
        SharedPreferences share = getS(context);
        return share.getInt(C_PAGE_SWITCH_DURATION, 400);
    }

    public static void saveTextSize(Context context, int textSize) {
        textSize = textSize < MIN_TEXT_SIZE ? MIN_TEXT_SIZE : textSize;
        textSize = textSize > MAX_TEXT_SIZE ? MAX_TEXT_SIZE : textSize;
        SharedPreferences share = getS(context);
        SharedPreferences.Editor editor = share.edit();
        editor.putInt(C_TEXT_SIZE, textSize);
        editor.commit();
    }

    public static int getTextSize(Context context) {
        SharedPreferences share = getS(context);
        return share.getInt(C_TEXT_SIZE, MIN_TEXT_SIZE);
    }

    public static void saveTextColor(Context context, int textColor) {
        SharedPreferences share = getS(context);
        SharedPreferences.Editor editor = share.edit();
        editor.putInt(C_TEXT_COLOR, textColor);
        editor.apply();
        editor.commit();
    }

    public static int getTextColor(Context context) {
        SharedPreferences share = getS(context);
        return share.getInt(C_TEXT_COLOR, Color.BLACK);
    }

    public static void saveNoteTextColor(Context context, int textColor) {
        SharedPreferences share = getS(context);
        SharedPreferences.Editor editor = share.edit();
        editor.putInt(C_NOTE_TEXT_COLOR, textColor);
        editor.apply();
        editor.commit();
    }

    public static int getNoteTextColor(Context context) {
        SharedPreferences share = getS(context);
        return share.getInt(C_NOTE_TEXT_COLOR, Color.BLACK);
    }

    public static void saveSelsetTextColor(Context context, int textColor) {
        SharedPreferences share = getS(context);
        SharedPreferences.Editor editor = share.edit();
        editor.putInt(C_SELECT_TEXT_COLOR, textColor);
        editor.apply();
        editor.commit();
    }

    public static int getSelectTextColor(Context context) {
        SharedPreferences share = getS(context);
        return share.getInt(C_SELECT_TEXT_COLOR, DEFAULT_SELECT_TEXT_COLOR);
    }


    public static void saveBackgroundColor(Context context, int BackgroundColor) {
        SharedPreferences share = getS(context);
        SharedPreferences.Editor editor = share.edit();
        editor.putInt(C_BACKGROUND_COLOR, BackgroundColor);
        editor.apply();
        editor.commit();
    }

    public static int getBackgroundColor(Context context) {
        SharedPreferences share = getS(context);
        return share.getInt(C_BACKGROUND_COLOR, Color.WHITE);
    }

    public static void saveCenterClickArea(Context context, float CenterClickArea) {
        SharedPreferences share = getS(context);
        SharedPreferences.Editor editor = share.edit();
        editor.putFloat(C_CENTER_CLICK_AREA, CenterClickArea);
        editor.apply();
        editor.commit();
    }

    public static float getCenterClickArea(Context context) {
        SharedPreferences share = getS(context);
        return share.getFloat(C_CENTER_CLICK_AREA, 0.3f);
    }

    public static void saveSliderColor(Context context, float CenterClickArea) {
        SharedPreferences share = getS(context);
        SharedPreferences.Editor editor = share.edit();
        editor.putFloat(C_SLIDER_COLOR, CenterClickArea);
        editor.apply();
        editor.commit();
    }

    public static int getSliderColor(Context context) {
        SharedPreferences share = getS(context);
        return share.getInt(C_SLIDER_COLOR, DEFAULT_SLIDER_COLOR);
    }

    public static void saveIsShowNote(Context context, Boolean IsShowNote) {
        SharedPreferences share = getS(context);
        SharedPreferences.Editor editor = share.edit();
        editor.putBoolean(C_IS_SHOW_NOTE, IsShowNote);
        editor.apply();
        editor.commit();
    }

    public static Boolean getIsShowNote(Context context) {
        SharedPreferences share = getS(context);
        return share.getBoolean(C_IS_SHOW_NOTE, true);
    }

    public static void saveCanPressSelect(Context context, Boolean CanPressSelect) {
        SharedPreferences share = getS(context);
        SharedPreferences.Editor editor = share.edit();
        editor.putBoolean(C_CAN_PRESS_SELECT, CanPressSelect);
        editor.apply();
        editor.commit();
    }

    public static Boolean getCanPressSelect(Context context) {
        SharedPreferences share = getS(context);
        return share.getBoolean(C_CAN_PRESS_SELECT, true);
    }

    public static void saveSwitchByTranslate(Context context, Boolean switchByTranslate) {
        SharedPreferences share = getS(context);
        SharedPreferences.Editor editor = share.edit();
        editor.putBoolean(C_SWITCH_BY_TRANSLATE, switchByTranslate);
        editor.apply();
        editor.commit();
    }



    public static Boolean isSwitchByTranslate(Context context) {
        SharedPreferences share = getS(context);
        return share.getBoolean(C_SWITCH_BY_TRANSLATE, true);
    }

    public static void saveIsBold(Context context, Boolean bold) {
        SharedPreferences share = getS(context);
        SharedPreferences.Editor editor = share.edit();
        editor.putBoolean(C_BOLD, bold);
        editor.apply();
        editor.commit();
    }

    public static Boolean isBold(Context context) {
        SharedPreferences share = getS(context);
        return share.getBoolean(C_BOLD, false);
    }

    public static void saveIsShowSpecialChar(Context context, Boolean showSpecialChar) {
        SharedPreferences share = getS(context);
        SharedPreferences.Editor editor = share.edit();
        editor.putBoolean(C_SHOW_SPECIAL_CHAR, showSpecialChar);
        editor.apply();
        editor.commit();
    }

    public static Boolean IsShowSpecialChar(Context context) {
        SharedPreferences share = getS(context);
        return share.getBoolean(C_SHOW_SPECIAL_CHAR, true);
    }

    public static void saveIsOnVerticalPageMode(Context context, Boolean IsOnVerticalPageMode) {
        SharedPreferences share = getS(context);
        SharedPreferences.Editor editor = share.edit();
        editor.putBoolean(C_PAGE_VERTICAL_MODE, IsOnVerticalPageMode);
        editor.apply();
        editor.commit();
    }

    public static Boolean IsOnVerticalPageMode(Context context) {
        SharedPreferences share = getS(context);
        return share.getBoolean(C_PAGE_VERTICAL_MODE, true);
    }
}
