package com.xxyp.mentiondemo.mention;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.xxyp.mentiondemo.R;
import com.xxyp.mentiondemo.mention.model.UserBean;

import java.util.ArrayList;

public class UserInfoActivity extends AppCompatActivity implements UserAdapter.ViewClickListener {
    RecyclerView mRecycleView;
    private ArrayList<UserBean> mUserList = new ArrayList();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_info);
        Toolbar toolbar = findViewById(R.id.toolbar);
        initData();
        mRecycleView = findViewById(R.id.recy_user_list);
        mRecycleView.setLayoutManager(new LinearLayoutManager(this));
        mRecycleView.setAdapter(new UserAdapter(this, mUserList, this));
        setSupportActionBar(toolbar);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

    private void initData() {
        mUserList.add(new UserBean("用户a", 18));
        mUserList.add(new UserBean("用户abbb", 18));
        mUserList.add(new UserBean("用户accc", 18));
        mUserList.add(new UserBean("用户addd", 18));
        mUserList.add(new UserBean("用户aeee", 18));
        mUserList.add(new UserBean("用户afff", 18));
    }

    @Override
    public void onItemClick(UserBean userBean) {
        Intent intent = new Intent();
        intent.putExtra("test",userBean);
        setResult(2,intent );
        finish();
    }
}
