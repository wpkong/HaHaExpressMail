package party.moha.www.hahaexpressmail;

import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;
import java.util.Properties;

import javax.mail.Address;
import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class SendEmailActivity extends AppCompatActivity {

    EditText receiver;
    Spinner sender;
    EditText cc;
    EditText bcc;
    EditText subject_et;
    EditText content_et;
    Button sendBtn;
    ArrayList<String> data_list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_email);

        receiver = (EditText)findViewById(R.id.send_email_receiver);
        sender = (Spinner)findViewById(R.id.send_email_sender);
        cc = (EditText)findViewById(R.id.send_email_cc);
        bcc = (EditText)findViewById(R.id.send_email_bcc);
        subject_et = (EditText)findViewById(R.id.send_email_subject);
        content_et = (EditText)findViewById(R.id.send_email_content);
        sendBtn = (Button)findViewById(R.id.send_email_send_button);
        data_list = new ArrayList<>();
        loadOptions(); //加载收信人选项

        //如果从emailReading跳转过来，执行以下代码
        Intent intent = this.getIntent();
        if(intent.getStringExtra("replyRec") != null){
            receiver.setText(intent.getStringExtra("replyRec"));
            subject_et.setText("回复:"+intent.getStringExtra("replySub"));
            content_et.setText(intent.getStringExtra("replyContent"));
        }

        sendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try{
                    final String content = content_et.getText().toString();
                    final String subject = subject_et.getText().toString();

                    //创建ProgressDialog对象
                    final ProgressDialog progressDialog = new ProgressDialog(SendEmailActivity.this);
                    //设置进度条风格，风格为圆形，旋转的
                    progressDialog.setProgressStyle(
                            ProgressDialog.STYLE_SPINNER);
                    //设置ProgressDialog 提示信息
                    progressDialog.setMessage("正在发送消息...");
                    //设置ProgressDialog 的进度条是否不明确
                    progressDialog.setIndeterminate(false);
                    //设置ProgressDialog 是否可以按退回按键取消
                    progressDialog.setCancelable(false);
                    // 让ProgressDialog显示
                    progressDialog.show();

                    String host = "sina.com";

                    final Properties properties = new Properties();
                    properties.setProperty("mail.debug", "true");
                    properties.setProperty("mail.smtp.auth", "true");
                    properties.setProperty("mail.smtp.port","25");
                    properties.setProperty("mail.host", "smtp." + host);
                    properties.setProperty("mail.transport.protocol", "smtp");

                    final Session session = Session.getInstance(properties);
                    final Message msg = new MimeMessage(session);
                    final String rec = receiver.getText().toString();
                    try{
                        msg.setSubject(subject);
                        msg.setText(content);
                        //TODO 大改
                        msg.setFrom(new InternetAddress("zhangshangwuda@sina.com"));
                        System.out.println("bilibili**1");
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                try{
                                    System.out.println("bilibili**2");
                                    Transport transport = session.getTransport();
                                    transport.connect("zhangshangwuda","wechat@ziqiang");
                                    Address[] receiver = new InternetAddress[]{new InternetAddress(rec)};
                                    transport.sendMessage(msg,receiver);
                                    System.out.println("bilibili**3");
                                    transport.close();
                                    System.out.println("bilibili**4");
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            Toast.makeText(SendEmailActivity.this, "发送邮件成功！", Toast.LENGTH_SHORT).show();
                                            progressDialog.dismiss();
                                        }
                                    });

                                }catch (Exception e){
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            Toast.makeText(SendEmailActivity.this, "发送邮件失败", Toast.LENGTH_SHORT).show();
                                            progressDialog.dismiss();
                                        }
                                    });
                                }

                            }
                        }).start();

                    }catch (Exception e){
                        Toast.makeText(SendEmailActivity.this, "发送邮件失败", Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();
                    }
                }catch (Exception e){
                    System.out.println("bilibili"+e.getMessage());
                    Toast.makeText(SendEmailActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }

            }
        });
    }

    private void loadOptions(){
        loadData();
        //适配器
        ArrayAdapter<String> arr_adapter= new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, data_list);
        //设置样式
        arr_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //加载适配器
        sender.setAdapter(arr_adapter);
    }

    private void loadData(){
        try{
            File f=getApplicationContext().getDatabasePath("usermail.db").getParentFile();
            if(!f.exists())
                f.mkdirs();//注意是mkdirs()有个s 这样可以创建多重目录。
            String FullPath=f.getPath()+"/usermail.db";
            SQLiteDatabase sqLiteDatabase = SQLiteDatabase.openOrCreateDatabase(FullPath, null);

            String sql = "select username,sender_server from EmailAccount";
            Cursor cursor = sqLiteDatabase.rawQuery(sql,new String[]{});
            while (cursor.moveToNext()){
                String username = cursor.getString(0);
                String sender = cursor.getString(1);
                String host = sender.substring(sender.indexOf('.')+1);
                data_list.add(username+"@"+host);
            }
        }catch (Exception e){
            System.out.println("bilibili"+e.getMessage());
        }
    }
}
