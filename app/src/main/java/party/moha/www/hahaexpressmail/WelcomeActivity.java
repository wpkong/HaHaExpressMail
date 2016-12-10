package party.moha.www.hahaexpressmail;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class WelcomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        final int WELCOME_PAGE_DISPLAY = 3000;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

//        //延时3s后加载内容

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(WelcomeActivity.this, MainActivity.class);
                WelcomeActivity.this.startActivity(intent);
                WelcomeActivity.this.finish();
            }
        },WELCOME_PAGE_DISPLAY);
    }
}
