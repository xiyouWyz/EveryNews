package com.example.wyz.everynews1.mvp.ui.main.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DecodeFormat;
import com.example.wyz.everynews1.MyApp;
import com.example.wyz.everynews1.R;
import com.example.wyz.everynews1.mvp.entity.PhotoGirl;
import com.example.wyz.everynews1.mvp.ui.main.adapter.base.BaseRecyclerViewAdapter;
import com.example.wyz.everynews1.utils.DimenUtil;
import com.example.wyz.everynews1.widget.RatioImageView;
import com.squareup.picasso.Picasso;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Wyz on 2016/11/19.
 */
public class PhotoListAdapter extends BaseRecyclerViewAdapter<PhotoGirl> {

    private  int width=(int)(DimenUtil.getScreenSize()/2);

    private Map<Integer,Integer> mHeight=new HashMap<>();

    @Inject
    public PhotoListAdapter() {
        super(null);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        switch (viewType){
            case TYPE_FOOTER:
                view=getView(parent, R.layout.item_news_footer);
                return  new FooterViewHolder(view);
            case  TYPE_ITEM:
                view=getView(parent,R.layout.item_photo);
                final ItemViewHolder itemViewHolder=new  ItemViewHolder(view);
                setItemOnClickEvent(itemViewHolder);
                return  itemViewHolder;
            default:
                throw new RuntimeException("there is no type that matches the type " +
                        viewType + " + make sure your using types correctly");
        }

    }

    private void setItemOnClickEvent(final RecyclerView.ViewHolder itemViewHolder) {
        if(mOnItemClickListener!=null){
            itemViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mOnItemClickListener.onItemClick(v,itemViewHolder.getLayoutPosition());
                }
            });
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        super.onBindViewHolder(holder, position);
        if(holder instanceof  ItemViewHolder){
            /*Glide.with(MyApp.getAppContext())
                    .load(mList.get(position).getUrl())
                    .asBitmap()
                    .format(DecodeFormat.PREFER_ARGB_8888)
                    .placeholder(R.color.image_place_holder)
                    .error(R.mipmap.ic_load_fail)
                    .into(((ItemViewHolder) holder).mPhotoIv);*/
            Picasso.with(MyApp.getAppContext())
                    .load(mList.get(position).getUrl())
                    .placeholder(R.color.image_place_holder)
                    .error(R.mipmap.ic_load_fail)
                    .into(((ItemViewHolder) holder).mPhotoIv);
        }
    }

    @Override
    public int getItemViewType(int position) {
        if(mIsShowFooter&&isFooterPosition(position)){
            return  TYPE_FOOTER;
        }else{
            return TYPE_ITEM;
        }

    }

     class ItemViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.photo_iv)
        RatioImageView mPhotoIv;
        public ItemViewHolder(View view) {
            super(view);
            ButterKnife.bind(this,view);
        }
    }
}
