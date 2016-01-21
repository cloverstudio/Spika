package com.clover_studio.spikachatmodule.lazy;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.TextUtils;
import android.widget.ImageView;

import com.clover_studio.spikachatmodule.robospice.spice.CustomSpiceListener;
import com.clover_studio.spikachatmodule.robospice.spice.CustomSpiceRequest;
import com.clover_studio.spikachatmodule.utils.Const;
import com.clover_studio.spikachatmodule.utils.LogCS;
import com.clover_studio.spikachatmodule.utils.Tools;
import com.octo.android.robospice.SpiceManager;
import com.octo.android.robospice.persistence.exception.SpiceException;

import org.apache.commons.io.IOUtils;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.ByteArrayHttpMessageConverter;
import org.springframework.http.converter.ResourceHttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.io.File;
import java.io.FileOutputStream;
import java.util.Collections;
import java.util.Map;
import java.util.WeakHashMap;

public class ImageLoaderSpice {

	private static ImageLoaderSpice instance;

	private Context ctx;

	private FileCache fileCache;
	private MemoryCache memoryCache;
	private Map<ImageView, String> imageViews = Collections.synchronizedMap(new WeakHashMap<ImageView, String>());

	private SpiceManager spiceManager;

	private OnImageDisplayFinishListener onFinishListener;

	public static ImageLoaderSpice getInstance(Context ctx) {

		if (instance == null) {
			instance = new ImageLoaderSpice(ctx);
		}

		return instance;
	}

	private ImageLoaderSpice(Context ctx) {

		this.ctx = ctx;
		fileCache = new FileCache(ctx);
		memoryCache = new MemoryCache();
	}

	/**
	 * display image to ImageView, if image is in memory cache show it from there, else if image is in file cache show it form file cache, else get image from web and save it to file cache
	 * @param imageView
	 * @param fileId url of image
	 * @param defaultImage image to show if failed to download image
	 * @param onFinishListener triggered when image is shown in ImageView
	 */
	public void displayImage(final ImageView imageView, final String fileId, int defaultImage, OnImageDisplayFinishListener onFinishListener) {
		this.onFinishListener = onFinishListener;
		displayImage(imageView, fileId, defaultImage);
	}

	/**
	 * display image to ImageView, if image is in memory cache show it from there, else if image is in file cache show it form file cache, else get image from web and save it to file cache
	 * @param imageView
	 * @param fileId url of image
	 * @param defaultImage image to show if failed to download image
	 */
	public void displayImage(final ImageView imageView, final String fileId, final int defaultImage) {

		if (TextUtils.isEmpty(fileId) || fileId.contains("default")) {
			if (defaultImage != 0) {
				imageView.setImageResource(defaultImage);

				if (onFinishListener != null) {
					onFinishListener.onFinish();
				}
			}
			
			return;
		}

		imageViews.put(imageView, fileId);

		// Get from memmory cache
		Bitmap bitmap = memoryCache.get(fileId);

		if (bitmap != null) {
			imageView.setImageBitmap(bitmap);

			if (onFinishListener != null) {
				onFinishListener.onFinish();
			}

			return;
		}

		// Get from file cache
		File file = fileCache.getFile(fileId);

		if (file != null && file.exists()) {

			if(checkImageForScale(file)){
				bitmap = Tools.scaleBitmap(file.getAbsolutePath(), ctx.getContentResolver(), Const.IntConst.SCALE_IMAGE_IN_LOADER_IN_PIXELS);
			}else{
				bitmap = BitmapFactory.decodeFile(file.getAbsolutePath());
			}

			if (bitmap != null) {

				imageView.setImageBitmap(bitmap);

				if (onFinishListener != null) {
					onFinishListener.onFinish();
				}

				ImageSpiceMemory imageSpiceMemory = new ImageSpiceMemory(fileId, bitmap);
				spiceManager.execute(imageSpiceMemory, null);

				return;
			}
		}

		if (defaultImage != 0) {
			imageView.setImageResource(defaultImage);
		}

		ImageSpiceWeb imageSpice = new ImageSpiceWeb(imageView, fileId);
		spiceManager.execute(imageSpice, new CustomSpiceListener<Bitmap>(ctx) {

			@Override
			public void onRequestFailure(SpiceException ex) {
				super.onRequestFailure(ex);

				if (onFinishListener != null) {
					onFinishListener.onFinish();
				}
			}

			@Override
			public void onRequestSuccess(Bitmap bitmap) {

				if (bitmap != null) {
					imageView.setImageBitmap(bitmap);
				}

				if (onFinishListener != null) {
					onFinishListener.onFinish();
				}

				super.onRequestSuccess(bitmap);
			}
		});
	}

	public void setSpiceManager(SpiceManager spiceManager) {
		this.spiceManager = spiceManager;
	}

	private class ImageSpiceWeb extends CustomSpiceRequest<Bitmap> {

		private ImageView imageView;
		private String fileId;

		public ImageSpiceWeb(ImageView imageView, String fileId) {
			super(Bitmap.class);

			this.imageView = imageView;
			this.fileId = fileId;
		}

		@Override
		public Bitmap loadDataFromNetwork() throws Exception {
			
			// Check if image already downloaded
			if (isImageViewReused(imageView, fileId)) {
				return null;
			}

			// Get from web
			String url = fileId;

			HttpHeaders headers = getHeaders();

			HttpEntity<?> entity = new HttpEntity<>(headers);

            LogCS.e("LOG", "URL: " + url);

            RestTemplate restTemplate = new RestTemplate();
            restTemplate.getMessageConverters().add(new StringHttpMessageConverter());
            restTemplate.getMessageConverters().add(new ByteArrayHttpMessageConverter());
            restTemplate.getMessageConverters().add(new ResourceHttpMessageConverter());

            ResponseEntity<Resource> response = restTemplate.exchange(url, HttpMethod.GET, entity, Resource.class);
			final File file = fileCache.getFile(fileId);

			if (!file.exists()) {
				file.createNewFile();
			}

            Bitmap bitmap = null;

            FileOutputStream fos = new FileOutputStream(file);
            IOUtils.copy(response.getBody().getInputStream(), fos);
            fos.flush();
            fos.close();

			if(checkImageForScale(file)){
				bitmap = Tools.scaleBitmap(file.getAbsolutePath(), ctx.getContentResolver(), Const.IntConst.SCALE_IMAGE_IN_LOADER_IN_PIXELS);
			}else{
				bitmap = BitmapFactory.decodeFile(file.getAbsolutePath());
			}

			if (bitmap != null) {
				if (bitmap != null) {
					memoryCache.put(fileId, bitmap);
				}
			} else {
				return null;
			}

			// Check if image already downloaded
			if (isImageViewReused(imageView, fileId)) {
				return null;
			}

			return bitmap;
		}
	}

	private class ImageSpiceMemory extends CustomSpiceRequest<Bitmap> {

		private String fileId;
		private Bitmap bitmap;

		public ImageSpiceMemory(String fileId, Bitmap bitmap) {
			super(Bitmap.class);
			this.fileId = fileId;
			this.bitmap = bitmap;
		}

		@Override
		public Bitmap loadDataFromNetwork() throws Exception {

			if (bitmap != null) {
				memoryCache.put(fileId, bitmap);
			}

			return null;
		}
	}

	private boolean isImageViewReused(ImageView imageView, String fileId) {

		String tag = imageViews.get(imageView);
		// Check url is already exist in imageViews MAP
		if (tag == null || !tag.equals(fileId)) {
			return true;
		}

		return false;
	}

	public interface OnImageDisplayFinishListener {
		void onFinish();
	}

	private boolean checkImageForScale(File file){
		if(file.length() > Const.IntConst.MAX_MB_FOR_BITMAP_IN_LOADER){
			BitmapFactory.Options options = new BitmapFactory.Options();
			options.inJustDecodeBounds = true;

			BitmapFactory.decodeFile(file.getAbsolutePath(), options);
			int width = options.outWidth;
			int height = options.outHeight;

			if(width * height > Const.IntConst.MAX_PIXELS_FOR_BITMAP_IN_LOADER){
				return true;
			}
		}
		return false;
	}

}
