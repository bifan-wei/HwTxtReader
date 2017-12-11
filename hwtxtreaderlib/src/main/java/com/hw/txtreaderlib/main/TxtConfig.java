package com.hw.txtreaderlib.main;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;

/**
 * Created by HP on 2017/11/26.
 */

public class TxtConfig {
    private static final String SAVE_NAME = "TxtConfig";
    public static final String C_TEXT_SIZE = "TEXT_SIZE ";
    public static final String C_TEXT_COLOR = "TEXT_COLOR";
    public static final String C_NOTE_TEXT_COLOR = "TEXT_COLOR";
    public static final String C_SLIDER_COLOR = "SLIDER_COLOR";
    public static final String C_SELECT_TEXT_COLOR = "TEXT_COLOR";
    public static final String C_BACKGROUND_COLOR = "BACKGROUND_COLOR";
    public static final String C_IS_SHOW_NOTE = "IS_SHOW_NOTE";
    public static final String C_CAN_PRESS_SELECT = "CAN_PRESS_SELECT";
    public static final String C_SWITCH_BY_TRANSLATE = "SWITCH_BY_TRANSLATE";
    public static final String C_BOLD = "BOLD ";

    public static final int MAX_TEXT_SIZE = 90;//in px
    public static final int MIN_TEXT_SIZE = 40;//in px
    public  int textSize = MIN_TEXT_SIZE;
    public int textColor = Color.BLACK;
    public int backgroundColor = Color.WHITE;
    public int NoteColor = Color.RED;
    public int SelectTextColor = Color.parseColor("#44ffffff");
    public int SliderColor = Color.parseColor("#1f4cf5");
    public Boolean showNote = true;
    public Boolean canPressSelect = true;
    public Boolean SwitchByTranslate = true;
    public Boolean Bold = false;


    private static final SharedPreferences getS(Context context) {
        SharedPreferences share = context.getSharedPreferences(SAVE_NAME, Context.MODE_PRIVATE);
        return share;
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
        //SharedPreferences share = getS(context);
       // return share.getInt(C_SELECT_TEXT_COLOR,SelectTextColor);
        return Color.parseColor("#44ffffff");
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

    public static void saveSliderColor(Context context, int BackgroundColor) {
        SharedPreferences share = getS(context);
        SharedPreferences.Editor editor = share.edit();
        editor.putInt(C_SLIDER_COLOR, BackgroundColor);
        editor.apply();
        editor.commit();
    }

    public static int getSliderColor(Context context) {
        SharedPreferences share = getS(context);
        return share.getInt(C_SLIDER_COLOR, Color.parseColor("#1f4cf5"));
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
        return share.getBoolean(C_SWITCH_BY_TRANSLATE, false);
    }

    public static void saveIsBold(Context context, Boolean bold) {
        SharedPreferences share = getS(context);
        SharedPreferences.Editor editor = share.edit();
        editor.putBoolean(C_BOLD, bold);
        editor.apply();
        editor.commit();
    }

    public static Boolean IsBold(Context context) {
        SharedPreferences share = getS(context);
        return share.getBoolean(C_BOLD, false);
    }
}
