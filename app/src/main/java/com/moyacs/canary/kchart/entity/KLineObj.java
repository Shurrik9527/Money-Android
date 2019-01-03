
package com.moyacs.canary.kchart.entity;

import android.graphics.Color;

import java.util.ArrayList;
import java.util.List;

/**
 * 指标线参数
 * @param <T>
 */
public class KLineObj<T> {
	//指标线list
	private List<T> lineData;
	//指标名字
	private String title;
	//指标参数
	private double value;
	//线条颜色
	private int lineColor = Color.LTGRAY;
	//是否显示指标线
	private boolean display = true;

	public KLineObj() {
		super();
	}

	public KLineObj(List<T> lineData, String title, int lineColor) {
		super();
		this.lineData = lineData;
		this.title = title;
		this.lineColor = lineColor;
	}

	/**
	 * @param value
	 */
	public void put(T value) {
		if (null == lineData) {
			lineData = new ArrayList<T>();
		}
		lineData.add(value);
	}


	public List<T> getLineData() {
		return lineData;
	}

	public void setLineData(List<T> lineData) {
		this.lineData = lineData;
	}


	public String getTitle() {
		return title;
	}


	public void setTitle(String title) {
		this.title = title;
	}

	public int getLineColor() {
		return lineColor;
	}


	public void setLineColor(int lineColor) {
		this.lineColor = lineColor;
	}


	public boolean isDisplay() {
		return display;
	}


	public void setDisplay(boolean display) {
		this.display = display;
	}

	public double getValue() {
		return value;
	}

	public void setValue(double value) {
		this.value = value;
	}
}
