package com.netease.lib_common_ui.dialog;

import android.annotation.SuppressLint;
import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.lxj.xpopup.core.CenterPopupView;
import com.netease.lib_common_ui.R;

//创建歌单
public class CreatePlayListDialog extends CenterPopupView implements View.OnClickListener {

    private EditText mEtName;
    private TextView mTvTextSize;
    private Button mButtonCancel;
    private Button mButtonConfirm;
    OnConfirmListener listener;

    public CreatePlayListDialog(@NonNull Context context, OnConfirmListener listener) {
        super(context);
        this.listener = listener;
    }

    @Override
    protected int getImplLayoutId() {
        return R.layout.dialog_create_sublist;
    }

    @Override
    protected void onCreate() {
        super.onCreate();
        mEtName = findViewById(R.id.et_create_playlist_name);
        mEtName.setFocusable(true);
        mTvTextSize = findViewById(R.id.tv_create_playlist_text_size);
        mButtonCancel = findViewById(R.id.tv_create_playlist_cancel);
        mButtonConfirm = findViewById(R.id.tv_create_playlist_confirm);
        mButtonCancel.setOnClickListener(this);
        mButtonConfirm.setOnClickListener(this);
        mButtonConfirm.setClickable(false);
        mButtonConfirm.setEnabled(false);
        mEtName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @SuppressLint("SetTextI18n")
            @Override
            public void afterTextChanged(Editable editable) {

                if(editable.toString().length() > 0 && editable.toString().length() < 16){
                    mTvTextSize.setEnabled(true);
                    mButtonConfirm.setClickable(true);
                    mButtonConfirm.setEnabled(true);
                    mTvTextSize.setTextColor(getResources().getColor(R.color.colorPrimary));
                }else if(editable.toString().length() < 16 && editable.toString().length() != 0 ){
                    mButtonConfirm.setClickable(false);
                    mButtonConfirm.setEnabled(false);
                    mTvTextSize.setTextColor(getResources().getColor(R.color.colorAccent));
                }else if(editable.toString().length() == 0){
                    mTvTextSize.setTextColor(getResources().getColor(R.color.color_999999));
                    mTvTextSize.setEnabled(false);
                    mButtonConfirm.setClickable(false);
                    mButtonConfirm.setEnabled(false);
                }
                mTvTextSize.setText(editable.toString().length() + "/16");
            }
        });
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.tv_create_playlist_cancel) {
            dismiss();
        }else if(view.getId() == R.id.tv_create_playlist_confirm){
            listener.onConfirm(mEtName.getText().toString());
            mEtName.setFocusable(false);
            dismiss();
        }
    }

    public interface OnConfirmListener{
        void onConfirm(String name);
    }

}
