package party.moha.www.hahaexpressmail;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v4.widget.TextViewCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Store;
import javax.mail.internet.InternetAddress;

import party.moha.www.hahaexpressmail.AddNewEmailActivity.AddNewEmailActivity;

public class EmailReading extends AppCompatActivity {

    TextView fromPerson_tv;
    TextView fromEmail_tv;
    WebView content_tv;
    TextView date_tv;
    TextView subject_tv;

    String fromPerson;
    String fromEmail;
    String subject;
    String content;
    String date;

    String reply;

    Intent intent;

    ImageButton replySend_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_email_reading);
        fromPerson_tv = (TextView)findViewById(R.id.email_reading_sender_person);
        fromEmail_tv = (TextView)findViewById(R.id.email_reading_sender_email);
        content_tv = (WebView) findViewById(R.id.email_reading_content);
        date_tv = (TextView)findViewById(R.id.email_reading_sender_date);
        subject_tv = (TextView)findViewById(R.id.email_reading_subject);
        replySend_btn = (ImageButton)findViewById(R.id.email_reading_send);

        intent = new Intent(EmailReading.this,SendEmailActivity.class);

        try{
            //创建ProgressDialog对象
            final ProgressDialog progressDialog = new ProgressDialog(EmailReading.this);
            //设置进度条风格，风格为圆形，旋转的
            progressDialog.setProgressStyle(
                    ProgressDialog.STYLE_SPINNER);
            //设置ProgressDialog 提示信息
            progressDialog.setMessage("正刷新邮件...");
            //设置ProgressDialog 的进度条是否不明确
            progressDialog.setIndeterminate(false);
            //设置ProgressDialog 是否可以按退回按键取消
            progressDialog.setCancelable(false);
            // 让ProgressDialog显示
            progressDialog.show();

            final int position = this.getIntent().getIntExtra("position",-1);
            new Thread(new Runnable() {
                @Override
                public void run() {
                    loadMail(position);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            fromPerson_tv.setText(fromPerson);
                            fromEmail_tv.setText(fromEmail);
                            date_tv.setText(date);
                            subject_tv.setText(subject);
                            content_tv.loadDataWithBaseURL(null, content , "text/html", "utf-8", null);
                            content_tv.getSettings().setJavaScriptEnabled(true);
                            content_tv.setWebChromeClient(new WebChromeClient());
                            progressDialog.setMessage("正在获取回复建议...");
                            new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    reply = sendGet("http://zswd.moha.party:8000/reply/",subject + "," + content);
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            intent.putExtra("replyContent",reply);
                                            progressDialog.dismiss();
                                            Toast.makeText(EmailReading.this,"邮件加载成功！",Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                }
                            }).start();
                        }
                    });
                }
            }).start();
        }catch (Exception e){
            System.out.println("bilibili"+e.getMessage());
        }

        replySend_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent.putExtra("replyRec",fromEmail);
                intent.putExtra("replySub",subject);
                startActivity(intent);
            }
        });

    }
    private boolean loadMail(int i){
        String host = "sina.com";
        final Properties props = new Properties();
        props.setProperty("mail.store.protocol", "imap");
        props.setProperty("mail.imap.host", "imap." + host);
        props.setProperty("mail.imap.port", "143");

        try{
            Session ses = Session.getInstance(props);
            Store store = ses.getStore();
            store.connect("zhangshangwuda","wechat@ziqiang");
            Folder folder = store.getFolder("INBOX");
            folder.open(Folder.READ_ONLY);
            Message messages = folder.getMessage(i + 1);
            subject = messages.getSubject();
            InternetAddress address[] = (InternetAddress[]) messages.getFrom();
            fromPerson = address[0].getPersonal();
            fromEmail = address[0].getAddress();
            content = messages.getContent().toString();
            date = messages.getSentDate().toString();
            folder.close(false);
            store.close();
            return true;
        }catch (Exception e){
            return false;
        }
    }

    private String sendGet(String url, String content) {
        String result = "";
        BufferedReader in = null;
        try {
            String urlNameString = url + "?content=" + content;
            URL realUrl = new URL(urlNameString);
            // 打开和URL之间的连接
            URLConnection connection = realUrl.openConnection();
            // 设置通用的请求属性
            connection.setRequestProperty("accept", "*/*");
            connection.setRequestProperty("connection", "Keep-Alive");
            connection.setRequestProperty("user-agent",
                    "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
            // 建立实际的连接
            connection.connect();
            // 获取所有响应头字段
            // 定义 BufferedReader输入流来读取URL的响应
            in = new BufferedReader(new InputStreamReader(
                    connection.getInputStream()));
            String line;
            while ((line = in.readLine()) != null) {
                result += line;
            }
        } catch (Exception e) {
            System.out.println("发送GET请求出现异常！" + e);
            e.printStackTrace();
        }
        // 使用finally块来关闭输入流
        finally {
            try {
                if (in != null) {
                    in.close();
                }
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }
        return result;
    }
}
