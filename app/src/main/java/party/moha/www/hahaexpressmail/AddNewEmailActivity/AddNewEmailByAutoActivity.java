package party.moha.www.hahaexpressmail.AddNewEmailActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.File;
import java.util.Properties;

import javax.mail.Session;
import javax.mail.Store;
import javax.mail.Transport;

import party.moha.www.hahaexpressmail.R;

public class AddNewEmailByAutoActivity extends AppCompatActivity {

    Button manualAdd;
    Button next;

    EditText email_input;
    EditText password;
    EditText alias;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_email_by_auto);

        manualAdd = (Button) findViewById(R.id.add_new_email_by_auto_to_manual);
        next = (Button) findViewById(R.id.add_new_email_by_auto_next);
        email_input = (EditText) findViewById(R.id.add_new_email_by_auto_email);
        password = (EditText) findViewById(R.id.add_new_email_by_auto_password);
        alias = (EditText)findViewById(R.id.add_new_email_by_auto_alias);

        manualAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AddNewEmailByAutoActivity.this, AddNewEmailActivity.class);
                startActivity(intent);
                AddNewEmailByAutoActivity.this.finish();
            }
        });

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //创建ProgressDialog对象
                final ProgressDialog progressDialog = new ProgressDialog(AddNewEmailByAutoActivity.this);
                //设置进度条风格，风格为圆形，旋转的
                progressDialog.setProgressStyle(
                        ProgressDialog.STYLE_SPINNER);
                //设置ProgressDialog 提示信息
                progressDialog.setMessage("正在验证邮箱");
                //设置ProgressDialog 的进度条是否不明确
                progressDialog.setIndeterminate(false);
                //设置ProgressDialog 是否可以按退回按键取消
                progressDialog.setCancelable(false);
                // 让ProgressDialog显示
                progressDialog.show();
                final String email = email_input.getText().toString();
                final String host = email.split("@")[1];
                final String username = email.split("@")[0];
                final String pwd = password.getText().toString();

                //smtp服务器检测（发信
                final Properties properties = new Properties();
                properties.setProperty("mail.debug", "true");
                properties.setProperty("mail.smtp.auth", "true");
                properties.setProperty("mail.smtp.port","25");
                properties.setProperty("mail.host", "smtp." + host);
                properties.setProperty("mail.transport.protocol", "smtp");

                //imap服务器检测（收信
                final Properties props = new Properties();
                props.setProperty("mail.store.protocol", "imap");
                props.setProperty("mail.imap.host", "imap." + host);
                props.setProperty("mail.imap.port", "143");

                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Session session = Session.getInstance(properties);
                            Transport transport = session.getTransport();
                            transport.connect(username, pwd);

                            Session ses = Session.getInstance(props);
                            Store store = ses.getStore();
                            store.connect(username, pwd);

                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(AddNewEmailByAutoActivity.this, "用户登录成功", Toast.LENGTH_SHORT).show();

                                    File f=getApplicationContext().getDatabasePath("usermail.db").getParentFile();
                                    if(!f.exists())
                                        f.mkdirs();//注意是mkdirs()有个s 这样可以创建多重目录。
                                    String FullPath=f.getPath()+"/usermail.db";
                                    SQLiteDatabase sqLiteDatabase = SQLiteDatabase.openOrCreateDatabase(FullPath, null);
                                    String table_sql = "create table if not exists EmailAccount(alias, username text, password text, receiver_server text, rec_port text, sender_server text, sender_port text, mail_count int)";
                                    sqLiteDatabase.execSQL(table_sql);
                                    String insert = "insert into EmailAccount(alias,username, password, receiver_server , rec_port , sender_server, sender_port,mail_count) values(?,?,?,?,?,?,?,?)";
                                    sqLiteDatabase.execSQL(insert, new Object[]{alias.getText().toString(),username,password, "imap." + host,  "143", "smtp." + host, "25",0});
                                    progressDialog.dismiss();
                                    AddNewEmailByAutoActivity.this.finish();
                                }
                            });
                        } catch (Exception e) {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(AddNewEmailByAutoActivity.this, "用户验证失败,或别名冲突！", Toast.LENGTH_SHORT).show();
                                    progressDialog.dismiss();
                                }
                            });
                        }
                    }
                }).start();
            }
        });
    }
}
