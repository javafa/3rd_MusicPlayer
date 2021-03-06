package com.veryworks.android.musicplayer;

import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.veryworks.android.musicplayer.ListFragment.OnListFragmentInteractionListener;
import com.veryworks.android.musicplayer.domain.Music;

import java.util.List;

import jp.wasabeef.glide.transformations.CropCircleTransformation;

public class ListAdapter extends RecyclerView.Adapter<ListAdapter.ViewHolder> {
    private final OnListFragmentInteractionListener mListener;

    private Context context = null;
    // 데이터 저장소
    private List<Music.Item> datas;

    public ListAdapter(OnListFragmentInteractionListener listener) {
        mListener = listener;
    }

    public void setDatas(List<Music.Item> datas){
        this.datas = datas;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if(context == null)
            context = parent.getContext();
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        // datas 저장소에 들어가 있는 Music.Item 한개를 꺼낸다.
        //Music.Item item = datas.get(position);

        holder.position = position;
        holder.musicUri = datas.get(position).musicUri;
        holder.mIdView.setText(datas.get(position).id);
        holder.mContentView.setText(datas.get(position).title);

        //holder.imgAlbum.setImageURI(datas.get(position).albumArt);
        Glide
                .with(context)
                .load(datas.get(position).albumArt) // 로드할 대상
                .placeholder(R.mipmap.icon)         // 로드가 안됬을 경우
                .bitmapTransform(new CropCircleTransformation(context))
                .into(holder.imgAlbum);             // 이미지를 출력할 대상

        // puase 버튼의 보임 유무를 결정한다.
        if(datas.get(position).itemClicked){
            holder.btnPause.setVisibility(View.VISIBLE);
        }else{
            holder.btnPause.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return datas.size();
    }

    public void goDetail(int position){
        mListener.goDetailInteraction(position);
    }

    public void setItemClicked(int position){
        for(Music.Item item : datas){
            item.itemClicked = false;
        }
        datas.get(position).itemClicked = true;
        // 리스트뷰 전체를 갱신해준다
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public int position;
        public Uri musicUri;
        public final View mView;
        public final TextView mIdView;
        public final TextView mContentView;
        public final ImageView imgAlbum;
        public final ImageButton btnPause;


        public ViewHolder(View view) {
            super(view);
            mView = view;
            mIdView = (TextView) view.findViewById(R.id.id);
            mContentView = (TextView) view.findViewById(R.id.content);
            imgAlbum = (ImageView) view.findViewById(R.id.imgAlbum);
            btnPause = (ImageButton) view.findViewById(R.id.btnPause);


            // 플레이
            mView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    setItemClicked(position);
                    Player.init(musicUri, mView.getContext(), null);
                    Player.play();
                    btnPause.setImageResource(android.R.drawable.ic_media_pause);
                    // btnPause.setVisibility(View.VISIBLE);
                }
            });

            // 상세보기로 이동 -> 뷰페이저로 이동
            mView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    goDetail(position);
                    return true; // 롱클릭 후 온클릭이 실행되지 않도록 한다.
                }
            });

            // pause 버튼 클릭
            btnPause.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    switch(Player.status){
                        case Const.Player.PLAY:
                            Player.pause();
                            // pause 가 클릭되면 이미지 모양이 play 로 바뀐다.
                            btnPause.setImageResource(android.R.drawable.ic_media_play);
                            break;
                        case Const.Player.PAUSE:
                            Player.replay();
                            btnPause.setImageResource(android.R.drawable.ic_media_pause);
                            break;
                    }
                }
            });
        }
    }
}