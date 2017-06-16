package com.veryworks.android.musicplayer;

import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.veryworks.android.musicplayer.domain.Music;

import java.util.List;

/**
 * Created by pc on 6/16/2017.
 */

public class DetailAdapter extends PagerAdapter {
    List<Music.Item> datas = null;

    public DetailAdapter(List<Music.Item> datas) {
        this.datas = datas;
    }

    @Override
    public int getCount() {
        return datas.size();
    }

    // RecyclerView 의 onBideViewHolder 의 역할
    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        // return super.instantiateItem(container, position);
        View view = LayoutInflater
                .from(container.getContext())
                .inflate(R.layout.fragment_pager_item, null);
        ImageView imageView = (ImageView) view.findViewById(R.id.imageView);
        TextView textView = (TextView) view.findViewById(R.id.textView);

        Glide.with(container.getContext())
                .load(datas.get(position).albumArt)
                .into(imageView);

        textView.setText(datas.get(position).title);

        // 생성한 뷰를 뷰페이저 container에 담아준다
        container.addView(view);

        return view;
    }

    // 화면에서 사라진 뷰를 메모리에서 제거하는 역할
    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        //super.destroyItem(container, position, object);
        container.removeView((View) object);
    }

    // instantiateItem 에서 리턴한 Object 가 View 가 맞는지를 확인한다.
    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }
}
