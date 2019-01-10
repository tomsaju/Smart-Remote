package ir.iot.smartremote;

import android.app.Dialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.util.CrashUtils;

public class SetupRemoteActivity extends AppCompatActivity {
    private static final String TAG = "SetupRemoteActivity";
    PrefrenceManager prefrenceManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.remotelayout);
        prefrenceManager = PrefrenceManager.getInstance(this);





    }

 public void onRemotePanelClicked(View view){

        Log.d(TAG, "onRemotePanelClicked: "+((Button)view).getText());
        showEditDialog(((Button)view).getText().toString());
    }

    void showEditDialog(final String buttonLabel){
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.edit_remote_dialog);
        Window window = dialog.getWindow();
        window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        window.setGravity(Gravity.CENTER);

        TextView label = (TextView) dialog.findViewById(R.id.button_label);
        label.setText(buttonLabel);

        final EditText codeEdittext = (EditText) dialog.findViewById(R.id.editText_code);

        String currentCode =  prefrenceManager.getCode(buttonLabel);
        if(currentCode!=null&&!currentCode.isEmpty()){
            codeEdittext.setText(currentCode);
        }else{
            codeEdittext.setText("");
        }

        Button saveBtn = (Button) dialog.findViewById(R.id.save);
        Button cancelBtn = (Button) dialog.findViewById(R.id.cancel);

        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(codeEdittext.getText().toString().trim().isEmpty()){
                    Toast.makeText(SetupRemoteActivity.this, "Value cannot be empty", Toast.LENGTH_SHORT).show();
                    return;
                }

                prefrenceManager.setCode(buttonLabel,codeEdittext.getText().toString());
                dialog.dismiss();
            }
        });

        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();

    }
}
