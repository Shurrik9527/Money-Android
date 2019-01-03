package com.moyacs.canary.kchart.listener;

/**
 * Created by fangzhu on 16/7/19.
 * k线图标区域点击的额外的事件处理
 */
public interface OnKChartClickListener {
    /**
     * 单击图标
     * @return
     */
    boolean onSingleClick();

    /**
     * 双击图标
     * @return
     */
    boolean onDoubleClick();

    /**
     * 长按
     * @return
     */
    boolean onLongPress();
}
