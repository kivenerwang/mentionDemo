package com.xxyp.mentiondemo.mention;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.xxyp.mentiondemo.R;

public class MentionFragment extends Fragment implements View.OnClickListener ,  MentionDialog.DialogFragmentDataCallback  {

    private TextView mentionEditText;
    private TextView mSendView;
    private View rootView;
    public static MentionFragment newInstance() {

        Bundle args = new Bundle();

        MentionFragment fragment = new MentionFragment();
        fragment.setArguments(args);
        return fragment;
    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_mention, container,false);
        mentionEditText = rootView.findViewById(R.id.et_comment);
        mentionEditText.setOnClickListener(this);
        mSendView = rootView.findViewById(R.id.tv_send);
        mSendView.setOnClickListener(this);
        return rootView;
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.tv_send) {
            sendText();
        } else if (id == R.id.et_comment) {
            showDialog();
        }
    }
    private void sendText() {
//        ISpan[] dataSpans = mentionEditText.getText().getSpans(0, mentionEditText.length(), ISpan.class);
//
//        StringBuilder stringBuilder = new StringBuilder();
//        stringBuilder.append("完整的信息:");
//        stringBuilder.append(mentionEditText.getText().toString());
//        stringBuilder.append("\n数据信息:");
//        for (ISpan dataSpan : dataSpans) {
//            stringBuilder.append(dataSpan.toString())
//                    .append("\n");
//        }
//        Toast.makeText(getContext(), stringBuilder, Toast.LENGTH_SHORT).show();
    }

    private void showDialog() {
        MentionDialog dialog = new MentionDialog();
        dialog.bind(this);
        dialog.show(getActivity().getSupportFragmentManager(), "aaaa");
    }



    @Override
    public Editable getCommentText() {
        return (Editable) mentionEditText.getText();
    }

    @Override
    public void setCommentText(Editable commentEditable) {
        mentionEditText.setText(commentEditable);
    }
}
