package com.example.semyung_old_book_kkm;

import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

public class Adapter extends RecyclerView.Adapter<Adapter.ViewHolder> {

    public interface AdapterCallback {
        void DoSomeThing(String title, String contents, String link,String photo,String seller);
    }

    private ArrayList<String> mDataTitle = null ;
    private ArrayList<String> mDataCondition = null ;
    private ArrayList<String> mDataTrade = null ;
    private ArrayList<String> mDataPhoto = null ;
    private ArrayList<String> mDataSeller = null ;
    private AdapterCallback mAdapterCallback;
    Context mContext;

    public Adapter(ArrayList<String> list0, ArrayList<String> list1, ArrayList<String> list2, ArrayList<String> list3, ArrayList<String> list4, AdapterCallback AdapterCallback, Context context) {
        mDataTitle = list0 ;
        mDataCondition = list1 ;
        mDataTrade = list2 ;
        mDataPhoto = list3 ;
        mDataSeller = list4;
        mAdapterCallback = AdapterCallback;
        mContext=context;
    }

    // 아이템 뷰를 저장하는 뷰홀더 클래스.
    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView tv_title,tv_condition,tv_trade;
        ImageView imageView;

        ConstraintLayout ll_con;

        ViewHolder(View itemView) {
            super(itemView) ;
            imageView =  itemView.findViewById(R.id.iv_image) ;
            tv_title = itemView.findViewById(R.id.tv_title) ;
            tv_condition = itemView.findViewById(R.id.tv_condition);
            tv_trade = itemView.findViewById(R.id.tv_trade) ;
            // 미개봉, 거의 새 것, 사용감 있음
            // 직거래 택배거래

            ll_con = itemView.findViewById(R.id.ll_con) ;
            ll_con.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (mAdapterCallback != null) {
                        mAdapterCallback.DoSomeThing(mDataTitle.get(getLayoutPosition()),mDataCondition.get(getLayoutPosition()),mDataTrade.get(getLayoutPosition()),mDataPhoto.get(getLayoutPosition()),mDataSeller.get(getLayoutPosition()));
                    }
                }
            });
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext() ;
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) ;
        View view = inflater.inflate(R.layout.adapter_item, parent, false) ;
        ViewHolder vh = new ViewHolder(view) ;
        return vh ;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.tv_title.setText(mDataTitle.get(position)) ;

        if(Integer.parseInt(mDataCondition.get(position))==0){
            holder.tv_condition.setText("미개봉");
        }else if(Integer.parseInt(mDataCondition.get(position))==1){
            holder.tv_condition.setText("거의 새 것");
        }else{
            holder.tv_condition.setText("사용감 있음");
        }

        if(Integer.parseInt(mDataTrade.get(position))==0){
            holder.tv_trade.setText("직거래");
        }else{
            holder.tv_trade.setText("택배 거래");
        }


        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageReference = storage.getReference();
        String path;
        path="photo/"+mDataPhoto.get(position);
        StorageReference pathReference = storageReference.child(path);
        pathReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Log.e("HAN","uri: "+uri);
                Log.e("HAN","mContext: "+mContext);

                Glide.with(mContext).load(uri).into(holder.imageView);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });

    }

    @Override
    public int getItemCount() {
        return mDataTitle.size() ;
    }

}