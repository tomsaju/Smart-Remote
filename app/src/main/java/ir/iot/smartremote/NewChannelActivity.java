package ir.iot.smartremote;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class NewChannelActivity extends AppCompatActivity {
String url = "https://purepng.com/public/uploads/large/purepng.com-old-televisiontvtelecommunicationmonochromeblack-and-whittelevisionoldblack-and-whiteclipart-1421526536100hrgmw.png";
    DatabaseReference mDatabase;
     EditText name ;
     EditText index;
     EditText category ;
     EditText language  ;
     Button button ;
@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_channel);
        mDatabase = FirebaseDatabase.getInstance().getReference();
         name = findViewById(R.id.channel_name);
         index = findViewById(R.id.channel_index);
         category = findViewById(R.id.channel_category);
         language = findViewById(R.id.channel_language);
        final Button button = findViewById(R.id.add_Channel_btn);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Channel channel = new Channel(Integer.parseInt(index.getText().toString()),
                        name.getText().toString(),
                        url,
                        category.getText().toString(),
                        language.getText().toString());

                mDatabase.child("Channels").child(name.getText().toString()).setValue(channel)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        clearfields();
                        Toast.makeText(NewChannelActivity.this, "Done", Toast.LENGTH_SHORT).show();
                    }
                })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(NewChannelActivity.this, "Error writing", Toast.LENGTH_SHORT).show();
                            }
                        });
            }
        });


    }

    private void clearfields() {
        name.setText("");
        index.setText("");
        category.setText("");
        language.setText("");
    }
}
