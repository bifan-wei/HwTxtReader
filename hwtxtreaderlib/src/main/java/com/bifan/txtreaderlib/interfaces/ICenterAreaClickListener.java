package com.bifan.txtreaderlib.interfaces;

/**
 * created by ： bifan-wei
 */

public interface ICenterAreaClickListener {
    /**
     * @param widthPercentInView
     * @return 返回是否处理了这个事件，如果已经处理了，将可能会执行翻页事件
     */
    boolean onCenterClick(float widthPercentInView);

    /**
     * @param widthPercentInView
     * @return  返回是否处理了这个事件，如果已经处理了，将可能会执行翻页事件
     */
    boolean onOutSideCenterClick(float widthPercentInView);
}
