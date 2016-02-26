package com.clover_studio.spikachatmodule.view.roundimage;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;

public class RoundImageView extends RoundedImageView {

	public RoundImageView(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.setOval(true);
		this.setBorderColor(ContextCompat.getColor(context, android.R.color.white));
		this.setBorderWidth(2.0f);
	}
}
