package com.mmednet.library.view.edit;

import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;

import java.util.List;

/**
 * Title:IRadioBox
 * <p>
 * Description:单选框
 * </p>
 * Author Jming.L
 * Date 17/9/22 15:46
 */
public class IRadioBox extends IEditBox implements EditView {

    public IRadioBox(Context context) {
        this(context, null);
    }

    public IRadioBox(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void selectItem(TextView view) {
        for (int i = 0; i < mList.size(); i++) {
            TextView textView = mList.get(i);
            if (textView == view) {
                view.setSelected(!view.isSelected());
            } else {
                textView.setSelected(false);
            }
        }
    }

    //单选：可变参数默认只设置第一个参数
    @Override
    public void setTexts(String... text) {
        boolean none = true;    //控制单选
        T:
        for (int i = 0; i < mList.size(); i++) {
            TextView textView = mList.get(i);
            String tView = textView.getText().toString();
            if (none && text != null) {
                for (String t : text) {
                    if (TextUtils.equals(t, tView)) {
                        textView.setSelected(true);
                        none = false;
                        continue T;
                    }
                }
            }
            textView.setSelected(false);
        }
    }

    //单选：默认返回第一个被选择的参数
    @Override
    public List<String> getTexts() {
        texts.clear();
        for (TextView textView : mList) {
            if (textView.isSelected()) {
                String str = textView.getText().toString();
                texts.add(str);
                break;
            }
        }
        return texts;
    }

}
