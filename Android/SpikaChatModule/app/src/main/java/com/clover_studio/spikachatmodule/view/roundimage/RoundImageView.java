package com.clover_studio.spikachatmodule.view.roundimage;

import android.content.Context;
import android.util.AttributeSet;

public class RoundImageView extends RoundedImageView {

	public RoundImageView(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.setOval(true);
		this.setBorderColor(getResources().getColor(android.R.color.white));
		this.setBorderWidth(2.0f);
	}
}
