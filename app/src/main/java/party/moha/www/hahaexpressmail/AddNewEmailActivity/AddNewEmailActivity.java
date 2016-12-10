package party.moha.www.hahaexpressmail.AddNewEmailActivity;

import android.app.Notification;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageInstaller;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TabHost;
import android.widget.Toast;

import java.io.File;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Store;
import javax.mail.Transport;
import javax.mail.internet.MimeMessage;

import party.moha.www.hahaexpressmail.R;

public class AddNewEmailActivity extends AppCompatActivity {
    Button autoAdd;
    Button next;

    EditText alias_et;
    EditText username_et;
    EditText password_et;
    EditText receiver_host_et;
    EditText receiver_port_et;
    EditText sender_host_et;
    EditText sender_port_et;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_email);

        autoAdd = (Button)findViewById(R.id.add_new_email_to_auto);
        next = (Button)findViewById(R.id.add_new_email_next);

        alias_et = (EditText)findViewById(R.id.add_new_email_alias);
        username_et = (EditText)findViewById(R.id.add_new_email_username);
        password_et = (EditText)findViewById(R.id.add_new_email_password);
        receiver_host_et = (EditText)findViewById(R.id.add_new_email_receiver);
        receiver_port_et= (EditText)findViewById(R.id.add_new_email_receiver_port);
        sender_host_et = (EditText)findViewById(R.id.add_new_email_sender);
        sender_port_et = (EditText)findViewById(R.id.add_new_email_sender_port);


        autoAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AddNewEmailActivity.this,AddNewEmailByAutoActivity.class);
                AddNewEmailActivity.this.startActivity(intent);
                AddNewEmailActivity.this.finish();
            }
        });

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try{
                    //创建ProgressDialog对象
                    final ProgressDialog progressDialog = new ProgressDialog(AddNewEmailActivity.this);
                    //设置进度条风格，风格为圆形，旋转的
                    progressDialog.setProgressStyle(
                            ProgressDialog.STYLE_SPINNER);
                    //设置ProgressDialog 提示信息
                    progressDialog.setMessage("正在验证邮箱...");
                    //设置ProgressDialog 的进度条是否不明确
                    progressDialog.setIndeterminate(false);
                    //设置ProgressDialog 是否可以按退回按键取消
                    progressDialog.setCancelable(false);
                    // 让ProgressDialog显示
                    progressDialog.show();

                    final String alias = alias_et.getText().toString();
                    final String username = username_et.getText().toString();
                    final String password = password_et.getText().toString();

                    final String receiver_host = receiver_host_et.getText().toString();
                    final String receiver_port = receiver_port_et.getText().toString();
                    final String receiver_protocol = receiver_host.split("\\.")[0];
                    final String sender_host = sender_host_et.getText().toString();
                    final String sender_port = sender_port_et.getText().toString();
                    //imap服务器检测（收信
                    final Properties props = new Properties();
                    props.setProperty("mail.store.protocol", receiver_protocol);
                    props.setProperty("mail."+ receiver_protocol +".host", receiver_host);
                    props.setProperty("mail."+ receiver_protocol +".port", receiver_port);

                    //smtp服务器检测（发信
                    final Properties properties = new Properties();
                    properties.setProperty("mail.debug", "true");
                    properties.setProperty("mail.smtp.auth", "true");
                    properties.setProperty("mail.smtp.port",sender_port);
                    properties.setProperty("mail.host", sender_host);
                    properties.setProperty("mail.transport.protocol", "smtp");

                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                Session session = Session.getInstance(properties);
                                Transport transport = session.getTransport();
                                transport.connect(username, password);
                                Session ses = Session.getInstance(props);
                                Store store = ses.getStore();
                                store.connect(username, password);

                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(AddNewEmailActivity.this, "用户登录成功", Toast.LENGTH_SHORT).show();

                                        File f=getApplicationContext().getDatabasePath("usermail.db").getParentFile();
                                        if(!f.exists())
                                            f.mkdirs();//注意是mkdirs()有个s 这样可以创建多重目录。
                                        String FullPath=f.getPath()+"/usermail.db";
                                        SQLiteDatabase sqLiteDatabase = SQLiteDatabase.openOrCreateDatabase(FullPath, null);
                                        String table_sql = "create table if not exists EmailAccount(alias, username text, password text, receiver_server text, rec_port text, sender_server text, sender_port text, mail_count int)";
                                        sqLiteDatabase.execSQL(table_sql);
                                        String insert = "insert into EmailAccount(alias,username, password, receiver_server , rec_port , sender_server, sender_port, mail_count) values(?,?,?,?,?,?,?,?)";
                                        sqLiteDatabase.execSQL(insert, new Object[]{alias,username,password, receiver_host , receiver_port,  sender_host , sender_port,0});
                                        System.out.println("bilibili**4");
                                        progressDialog.dismiss();
                                        AddNewEmailActivity.this.finish();
                                    }
                                });
                            } catch (Exception e) {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(AddNewEmailActivity.this, "用户验证失败，或别名冲突！", Toast.LENGTH_SHORT).show();
                                        progressDialog.dismiss();
                                    }
                                });
                            }
                        }
                    }).start();
                }catch (Exception e){
                    System.out.println("bilibili**"+e.getMessage());
                }

            }
        });
    }


}
