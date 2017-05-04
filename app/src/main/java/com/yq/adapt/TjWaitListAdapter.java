package com.yq.adapt;

import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.yq.model.BluethBean;
import com.yq.model.TjNotChaoBiao;
import com.yq.yqwater.R;

import java.util.List;

public class TjWaitListAdapter extends BaseAdapter {
    private Context mContext;

    private List<TjNotChaoBiao> mBeans;
    private BluetoothDevice device;

    public TjWaitListAdapter(Context context, List<TjNotChaoBiao> beans) {
        mContext = context;
        mBeans = beans;
    }

    @Override
    public int getCount() {
        if (mBeans == null) {
            return 0;
        }
        return mBeans.size();
    }

    @Override
    public Object getItem(int position) {

        return null;
    }

    @Override
    public long getItemId(int position) {

        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHold viewHold;
        if(convertView == null){                // 拿缓存
            // 将 layout 填充成"View"
            LayoutInflater inflater=(LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE );
            convertView = inflater.inflate(R.layout.item_listview_tj_wait,parent,false); // listview中每一项的布局

            viewHold = new ViewHold(convertView);

            convertView.setTag(viewHold);
        }else {
            viewHold = (ViewHold) convertView.getTag();
        }

        viewHold.tvHm.setText(mBeans.get(position).getHm());
        viewHold.tvHmph.setText(mBeans.get(position).getHmph());
        viewHold.tvDzbq.setText(mBeans.get(position).getDzbq());
        viewHold.tvDz.setText(mBeans.get(position).getDz());
        viewHold.tvDh.setText(mBeans.get(position).getDh());

        return convertView;
    }


    class ViewHold{

        public TextView tvHm;
        public TextView tvHmph;
        public TextView tvDzbq;
        public TextView tvDz;
        public TextView tvDh;

        public ViewHold(View view) {

            tvHm = (TextView) view.findViewById(R.id.tv_Hm);
            tvHmph = (TextView) view.findViewById(R.id.tv_Hmph);
            tvDzbq = (TextView) view.findViewById(R.id.tv_Dzbq);
            tvDz = (TextView) view.findViewById(R.id.tv_Dz);
            tvDh = (TextView) view.findViewById(R.id.tv_Dh);
        }


    }

}
