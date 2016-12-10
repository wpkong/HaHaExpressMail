package party.moha.www.hahaexpressmail.Service;

import android.app.Notification;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Store;
import javax.mail.internet.InternetAddress;

import party.moha.www.hahaexpressmail.NewEmailNotification;

public class MailListener extends Service {
    public MailListener() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onCreate() {
        super.onCreate();
        new Thread(new Runnable() {
            @Override
            public void run() {
                while(true){
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
                        Message[] messages = folder.getMessages();
                        int mailCounts = messages.length;
                        System.out.println("bilibili******"+mailCounts);
                        int count = mailCounts - 8;
                        if(count>0){
                            NewEmailNotification notification = new NewEmailNotification();
                            notification.notify(getApplicationContext(),count+"",0);
                        }
                    }catch (Exception e){
                        System.out.println("bilibili"+e.getMessage());
                    }
                    try {
                        Thread.sleep(20000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

            }
        }).start();

    }
}
