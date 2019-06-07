package com.reeching.adapter;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.reeching.bean.ExhibitionBean;
import com.reeching.bean.HuaLangShowing;
import com.reeching.bluegrass.R;
import com.reeching.utils.HttpApi;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/3/6.
 */

public class ShowingAdapter extends BaseAdapter{
    private List<ExhibitionBean.InfosBean> lists;
    private Context context;


    public ShowingAdapter(List<ExhibitionBean.InfosBean> lists, Context context) {
        this.lists = lists;
        this.context = context;

    }

    @Override
    public int getCount() {
        return null == lists ? 0 : lists.size();
    }

    @Override
    public Object getItem(int position) {
        return lists.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ExhibitionBean.InfosBean infos = lists.get(position);
        ShowingAdapter.viewholder vh = null;
        if (convertView == null) {
            vh = new ShowingAdapter.viewholder();
            convertView = LayoutInflater.from(context).inflate(
                    R.layout.nowing_item, null);
            vh.tvtheme = (TextView) convertView
                    .findViewById(R.id.nowing_theme);
            vh.tvpeople = (TextView) convertView
                    .findViewById(R.id.nowing_cezhanren);
            vh.tvtimes = (TextView) convertView
                    .findViewById(R.id.nowing_times);
            vh.tvtimee = (TextView) convertView
                    .findViewById(R.id.nowing_timee);
           vh.tvcreattime= (TextView) convertView.findViewById(R.id.nowing_creattime);


            vh.lin = (LinearLayout) convertView.findViewById(R.id.nowing_lin);
            vh.infoHuaLang = (TextView) convertView.findViewById(R.id.nowing_hualang);
            vh.zuoZhe = (TextView) convertView.findViewById(R.id.nowing_zuozhe);
            convertView.setTag(vh);
        } else {
            vh = (ShowingAdapter.viewholder) convertView.getTag();
        }
        vh.tvtheme.setText(infos.getTheme());
        vh.tvpeople.setText(infos.getManager());
        vh.infoHuaLang.setText(infos.getGalleryName());
        vh.zuoZhe.setText(infos.getAuthor());
        String dateBegin = infos.getDateBegin();
        vh.tvtimes.setText(dateBegin);
        String dateEnd = infos.getDateEnd();
        vh.tvtimee.setText(dateEnd);
        vh.tvcreattime.setText(infos.getCreateDate());
        vh.lin.removeAllViews();
        vh.list.clear();
        if (!"".equals(infos.getSmallPhoto())&&null!=infos.getSmallPhoto()) {
            String sourceStr = infos.getSmallPhoto();
            String[] sourceStrArray = sourceStr.split("\\|");
            for (int ii = 1; ii < sourceStrArray.length; ii++) {
                vh.list.add(sourceStrArray[ii]);
            }
        }
        for (int i = 0; i < (vh.list.size()>3?3:vh.list.size()); i++) {
            final ImageView mImageView = new ImageView(context);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams((int)
                    context.getResources().getDimension(R.dimen.img_item), (int)context.getResources().getDimension(R.dimen.img_item));
            params.setMargins(5, 5, 5, 5);
            mImageView.setLayoutParams(params);
            if(vh.list.size()!=0){
                final String url = HttpApi.picip + vh.list.get(i);
                Picasso.with(context)
                        .load(url)
                        .resize(40, 40)
                        .placeholder(R.drawable.downing)              //添加占位图片
                        .error(R.drawable.error)
                        .centerCrop()
                        .into(mImageView);
                vh.lin.addView(mImageView);}
        }
        return convertView;
    }

    static class viewholder {
        TextView tvtheme, tvpeople, tvtimes, tvtimee, infoHuaLang, zuoZhe,tvcreattime;
        LinearLayout lin;
        List<String> list = new ArrayList<String>();
    }

}
