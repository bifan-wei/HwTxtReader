package com.bifan.txtreaderlib.utils;

/*
* create by bifan-wei
* 2017-11-13
*/
public class FormatUtil {
    public static Boolean isDigital(char Char) {
        return Character.isDigit(Char);
    }

    public static Boolean isLetter(char Char) {
        return (Char >= 65 && Char <= 90) || (Char >= 97 && Char <= 122);
    }
}
