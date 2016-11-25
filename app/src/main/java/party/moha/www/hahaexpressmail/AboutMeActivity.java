package party.moha.www.hahaexpressmail;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

public class AboutMeActivity extends AppCompatActivity {

    LinearLayout emailAccount;
    LinearLayout autoReply;
    LinearLayout emailSigin;
    LinearLayout setting;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_me);

        emailAccount = (LinearLayout) findViewById(R.id.about_me_email);
        autoReply =  (LinearLayout) findViewById(R.id.about_me_auto_reply);
        emailSigin = (LinearLayout) findViewById(R.id.about_me_email_sign);
        setting = (LinearLayout) findViewById(R.id.about_me_setting);

        emailAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AboutMeActivity.this,EmailAccount.class);
                AboutMeActivity.this.startActivity(intent);
            }
        });

    }

}
