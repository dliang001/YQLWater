package com.yq.adapt;

import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.yq.model.TjNotChaoBiao;
import com.yq.model.YjMoneyBean;
import com.yq.yqwater.R;

import java.util.List;

public class TjYjMoneyListAdapter extends BaseAdapter {
    private Context mContext;

    private List<YjMoneyBean> mBeans;
    private BluetoothDevice device;

    public TjYjMoneyListAdapter(Context context, List<YjMoneyBean> beans) {
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
            convertView = inflater.inflate(R.layout.item_listview_tj_yjmoney,parent,false); // listview中每一项的布局

            viewHold = new ViewHold(convertView);

            convertView.setTag(viewHold);
        }else {
            viewHold = (ViewHold) convertView.getTag();
        }

        viewHold.tvHmph.setText(mBeans.get(position).getHmph());
        viewHold.tvYjMoney.setText(mBeans.get(position).getYjMoney());
        viewHold.tvTime.setText(mBeans.get(position).getTime());


        return convertView;
    }


    class ViewHold{


        public TextView tvHmph;
        public TextView tvYjMoney;
        public TextView tvTime;


        public ViewHold(View view) {

            tvHmph = (TextView) view.findViewById(R.id.tv_Hmph);
            tvYjMoney = (TextView) view.findViewById(R.id.tv_yjMoney);
            tvTime = (TextView) view.findViewById(R.id.tv_time);

        }


    }

}
