package com.clover_studio.spikachatmodule.dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.clover_studio.spikachatmodule.R;

public class NotifyDialog extends Dialog {

    private Type type;
    private String textStr;
    private String titleStr;

    private OneButtonDialogListener oneButtonListener;
    private TwoButtonDialogListener twoButtonListener;

    public enum Type{
        INFO, CONFIRM
    }

    /**
     * start info dialog with one button
     * @param context
     * @param title dialog title
     * @param text dialog text
     * @return
     */
    public static NotifyDialog startInfo(Context context, String title, String text){
        NotifyDialog dialog = new NotifyDialog(context, title, text, Type.INFO);
        return dialog;
    }

    /**
     * start info dialog with two button
     * @param context
     * @param title dialog title
     * @param text dialog text
     * @return
     */
    public static NotifyDialog startConfirm(Context context, String title, String text){
        NotifyDialog dialog = new NotifyDialog(context, title, text, Type.CONFIRM);
        return dialog;
    }

    public NotifyDialog(Context context, String title, String text, Type type) {
        super(context, R.style.Theme_Dialog);

        setCancelable(false);
        setCanceledOnTouchOutside(false);

        titleStr = title;
        textStr = text;
        this.type = type;

        show();

    }

    /**
     * set one button listener
     * @param listener
     */
    public void setOneButtonListener(OneButtonDialogListener listener){
        oneButtonListener = listener;
    }

    /**
     * set two button listener
     * @param listener
     */
    public void setTwoButtonListener(TwoButtonDialogListener listener){
        twoButtonListener = listener;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(type);
    }

    private void setContentView(Type type) {
        if(type == Type.INFO){
            super.setContentView(R.layout.dialog_custom_one_button);
            setOneButtonOptions();
        }else{
            super.setContentView(R.layout.dialog_custom_two_button);
            setTwoButtonOptions();
        }
    }

    private void setOneButtonOptions() {
        TextView text = (TextView) findViewById(R.id.info_text);
        text.setText(textStr);

        TextView title = (TextView) findViewById(R.id.info_title);
        title.setText(titleStr);

        Button buttonOk = (Button) findViewById(R.id.oneButton);
        buttonOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(oneButtonListener != null){
                    oneButtonListener.onOkClicked(NotifyDialog.this);
                }else{
                    dismiss();
                }
            }
        });

    }

    private void setTwoButtonOptions() {
        TextView text = (TextView) findViewById(R.id.info_text);
        text.setText(textStr);

        TextView title = (TextView) findViewById(R.id.info_title);
        title.setText(titleStr);

        Button buttonOk = (Button) findViewById(R.id.rightButton);
        buttonOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (twoButtonListener != null) {
                    twoButtonListener.onOkClicked(NotifyDialog.this);
                } else {
                    dismiss();
                }
            }
        });

        Button buttonCancel = (Button) findViewById(R.id.leftButton);
        buttonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(twoButtonListener != null){
                    twoButtonListener.onCancelClicked(NotifyDialog.this);
                }else{
                    dismiss();
                }
            }
        });

    }

    /**
     * set buttons text, (for two button dialogs)
     * @param leftButton text for left button
     * @param rightButton text for right button
     */
    public void setButtonsText(String leftButton, String rightButton){
        Button buttonRight = (Button) findViewById(R.id.rightButton);
        buttonRight.setText(rightButton);

        Button buttonLeft = (Button) findViewById(R.id.leftButton);
        buttonLeft.setText(leftButton);
    }

    public interface OneButtonDialogListener{
        public void onOkClicked(NotifyDialog dialog);
    }

    public interface TwoButtonDialogListener{
        public void onOkClicked(NotifyDialog dialog);
        public void onCancelClicked(NotifyDialog dialog);
    }


}