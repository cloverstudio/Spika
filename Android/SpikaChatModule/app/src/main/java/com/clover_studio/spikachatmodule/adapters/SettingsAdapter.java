package com.clover_studio.spikachatmodule.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.clover_studio.spikachatmodule.R;

import java.util.ArrayList;
import java.util.List;

public class SettingsAdapter extends BaseAdapter {

	Context ctx;
	int chatType = 0;

	List<String> data = new ArrayList<>();

	public SettingsAdapter(Context context) {
		this.ctx = context;
	}

	@Override
	public int getCount() {
		return data.size();
	}

	@Override
	public String getItem(int position) {
		return data.get(position);
	}

	@Override
	public long getItemId(int position) {
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		final ViewHolder holder;
		if (convertView == null) {

			convertView = LayoutInflater.from(ctx).inflate(R.layout.item_chat_settings, parent, false);

			holder = new ViewHolder(convertView);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		String item = getItem(position);

		holder.item.setText(item);

		return convertView;
	}

	/**
	 * set settings item
	 */
	public void setSettings() {
		data.add(ctx.getString(R.string.users));
		notifyDataSetChanged();
	}

	private class ViewHolder {
		public RelativeLayout rootView;
		public TextView item;

		public ViewHolder(View view) {
			item = (TextView) view.findViewById(R.id.settings_item);
			rootView = (RelativeLayout) view.findViewById(R.id.rootView);
		}
	}

}
