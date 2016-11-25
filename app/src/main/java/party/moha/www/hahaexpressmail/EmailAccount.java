package party.moha.www.hahaexpressmail;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import party.moha.www.hahaexpressmail.AddNewEmailActivity.AddNewEmailByAutoActivity;

public class EmailAccount extends AppCompatActivity {

    LinearLayout emailAdd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_email_account);

        emailAdd = (LinearLayout)findViewById(R.id.email_account_add);

        emailAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(EmailAccount.this,AddNewEmailByAutoActivity.class);
                EmailAccount.this.startActivity(intent);
            }
        });
    }
}
