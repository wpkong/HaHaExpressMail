package party.moha.www.hahaexpressmail.AddNewEmailActivity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import party.moha.www.hahaexpressmail.R;

public class AddNewEmailByAutoActivity extends AppCompatActivity {

    Button manualAdd;
    Button next;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_email_by_auto);

        manualAdd = (Button) findViewById(R.id.add_new_email_by_auto_to_manual);
        next = (Button)findViewById(R.id.add_new_email_by_auto_next);

        manualAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AddNewEmailByAutoActivity.this,AddNewEmailActivity.class);
                startActivity(intent);
                AddNewEmailByAutoActivity.this.finish();
            }
        });
    }
}
