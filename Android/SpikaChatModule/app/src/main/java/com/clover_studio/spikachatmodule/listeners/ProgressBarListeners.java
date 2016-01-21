package com.clover_studio.spikachatmodule.listeners;

public interface ProgressBarListeners {
	public void onSetMax(long total);
	public void onProgress(long current);
	public void onFinish();
}
