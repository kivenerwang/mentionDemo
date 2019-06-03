package com.xxyp.mentiondemo.mention;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.xxyp.mentiondemo.R;
import com.xxyp.mentiondemo.mention.model.UserBean;

import java.util.ArrayList;

class UserAdapter extends RecyclerView.Adapter<UserAdapter.UserHolder> {
    private ArrayList<UserBean> mUsers;
    private Context mContext;
    public UserAdapter(Context context, ArrayList mUserList, ViewClickListener listener) {
        mUsers = mUserList;
        mListener = listener;
        mContext = context;
    }

    @NonNull
    @Override
    public UserHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int position) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_user_info, null, false);
        return new UserHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UserHolder holder, int position) {
        holder.bindData(mUsers.get(position));
    }

    @Override
    public int getItemCount() {
        return mUsers.size();
    }


    class UserHolder extends RecyclerView.ViewHolder {
        private TextView mUserName;
        public UserHolder(@NonNull View itemView) {
            super(itemView);
        }

        public void bindData(final UserBean userBean) {
            mUserName = itemView.findViewById(R.id.user_name);
            mUserName.setText(userBean.userName);
            mUserName.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mListener != null) {
                        mListener.onItemClick(userBean);
                    }
                }
            });
        }
    }
    private ViewClickListener mListener;
    public interface ViewClickListener {
        void onItemClick(UserBean userBean);
    }

}
