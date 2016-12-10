package party.moha.www.hahaexpressmail;

import android.app.ProgressDialog;
import android.content.Intent;
import android.provider.ContactsContract;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Store;
import javax.mail.internet.InternetAddress;

import party.moha.www.hahaexpressmail.AddNewEmailActivity.AddNewEmailActivity;

public class ReceiverActivity extends AppCompatActivity {

    FloatingActionButton fab;
    ListView receiver_list;
    Message[] messages;

    private List<Map<String,String>> data = new ArrayList<Map<String,String>>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_receiver);
        fab = (FloatingActionButton)findViewById(R.id.receiver_refresh_fab);
        receiver_list = (ListView)findViewById(R.id.receiver_list);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                refresh();
            }
        });

        refresh();
        receiver_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                final Intent intent = new Intent(ReceiverActivity.this,EmailReading.class);
                intent.putExtra("position",position);
                ReceiverActivity.this.startActivity(intent);
            }
        });
    }

    private void refresh(){
        //创建ProgressDialog对象
        final ProgressDialog progressDialog = new ProgressDialog(ReceiverActivity.this);
        //设置进度条风格，风格为圆形，旋转的
        progressDialog.setProgressStyle(
                ProgressDialog.STYLE_SPINNER);
        //设置ProgressDialog 提示信息
        progressDialog.setMessage("正刷新收件箱...");
        //设置ProgressDialog 的进度条是否不明确
        progressDialog.setIndeterminate(false);
        //设置ProgressDialog 是否可以按退回按键取消
        progressDialog.setCancelable(false);
        // 让ProgressDialog显示
        progressDialog.show();

        Toast.makeText(ReceiverActivity.this,"正在刷新收件箱...",Toast.LENGTH_SHORT).show();
        new Thread(new Runnable() {
            @Override
            public void run() {
                final boolean result = loadMail();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        receiver_list.setAdapter(new SimpleAdapter(ReceiverActivity.this,
                                data,R.layout.receiver_list_view_item,
                                new String[]{"fromPerson","fromEmail","subject"},
                                new int[]{R.id.receiver_list_from_person,R.id.receiver_list_from_email,R.id.receiver_list_subject}));
                        if(result){
                            Toast.makeText(ReceiverActivity.this,"刷新成功！",Toast.LENGTH_SHORT).show();
                            progressDialog.dismiss();
                        }else {
                            Toast.makeText(ReceiverActivity.this,"刷新失败！",Toast.LENGTH_SHORT).show();
                            progressDialog.dismiss();
                        }
                    }
                });
            }
        }).start();

    }

    private boolean loadMail(){
        String host = "sina.com";
        final Properties props = new Properties();
        props.setProperty("mail.store.protocol", "imap");
        props.setProperty("mail.imap.host", "imap." + host);
        props.setProperty("mail.imap.port", "143");

        try{
            data.clear();
            Session ses = Session.getInstance(props);
            Store store = ses.getStore();
            store.connect("zhangshangwuda","wechat@ziqiang");
            Folder folder = store.getFolder("INBOX");
            folder.open(Folder.READ_ONLY);
            messages = folder.getMessages();
            System.out.println("bilibili****"+folder.getMessageCount());
            int mailCounts = messages.length;
            for(int i = 0; i < mailCounts; i++) {
                System.out.println("bilibili*"+i);
                String subject = messages[i].getSubject();
                InternetAddress address[] = (InternetAddress[]) messages[i].getFrom();
                String fromPerson = address[0].getPersonal();
                String fromEmail = address[0].getAddress();

                Map<String,String> item = new HashMap<>();
                item.put("subject",subject);
                item.put("fromEmail",fromEmail);
                item.put("fromPerson",fromPerson);
                data.add(item);
            }
            folder.close(false);
            store.close();
            return true;
        }catch (Exception e){
            Log.d("bilibili", "loadMail: "+e.getMessage());
            System.out.println("bilibili"+e.getMessage());
            return false;
        }
    }
}
