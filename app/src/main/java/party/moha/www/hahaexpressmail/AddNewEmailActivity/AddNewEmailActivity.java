package party.moha.www.hahaexpressmail.AddNewEmailActivity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TabHost;

import party.moha.www.hahaexpressmail.R;

public class AddNewEmailActivity extends AppCompatActivity {
    TabHost tabHost;
    Button imap_autoAdd;
    Button imap_next;
    Button pop_autoAdd;
    Button pop_next;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_email);

        tabHost=(TabHost)findViewById(android.R.id.tabhost);//获取tabHost对象
        tabHost.setup();//初始化TabHost组件



        //选项卡切换
        LayoutInflater inflater=LayoutInflater.from(this);
        inflater.inflate(R.layout.activity_add_new_email_imap, tabHost.getTabContentView());
        inflater.inflate(R.layout.activity_add_new_email_pop, tabHost.getTabContentView());
        tabHost.addTab(tabHost.newTabSpec("tab01")
                .setIndicator("IMAP")
                .setContent(R.id.add_new_email_imap));//添加第一个标签页
        tabHost.addTab(tabHost.newTabSpec("tab02")
                .setIndicator("POP")
                .setContent(R.id.add_new_email_pop));//添加第二个标签页

//        imap_autoAdd = (Button)tabHost.getCurrentTabView().findViewById(R.id.add_new_email_imap_to_auto);
//        imap_next = (Button)tabHost.getCurrentTabView().findViewById(R.id.add_new_email_imap_next);
//        pop_autoAdd = (Button)tabHost.getCurrentTabView().findViewById(R.id.add_new_email_pop_to_auto);
//        pop_next = (Button)tabHost.getCurrentTabView().findViewById(R.id.add_new_email_pop_next);

//        imap_autoAdd.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(AddNewEmailActivity.this,AddNewEmailByAutoActivity.class);
//                AddNewEmailActivity.this.startActivity(intent);
//                AddNewEmailActivity.this.finish();
//            }
//        });
//        pop_autoAdd.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(AddNewEmailActivity.this,AddNewEmailByAutoActivity.class);
//                AddNewEmailActivity.this.startActivity(intent);
//                AddNewEmailActivity.this.finish();
//            }
//        });
    }


}
