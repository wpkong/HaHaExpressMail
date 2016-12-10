package party.moha.www.hahaexpressmail;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import party.moha.www.hahaexpressmail.AddNewEmailActivity.AddNewEmailByAutoActivity;

public class EmailAccount extends AppCompatActivity {

    LinearLayout emailAdd;
    ListView emaiAccountList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_email_account);

        emailAdd = (LinearLayout)findViewById(R.id.email_account_add);
        emaiAccountList = (ListView)findViewById(R.id.email_accout_email_list);
        emailAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(EmailAccount.this,AddNewEmailByAutoActivity.class);
                EmailAccount.this.startActivity(intent);
            }
        });
        emaiAccountList.setAdapter(new ArrayAdapter<String>(EmailAccount.this,android.R.layout.simple_expandable_list_item_1,loadAccount()));

        emaiAccountList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {


                return true;
            }
        });
    }

    private List<String> loadAccount(){
        List<String> data = new ArrayList<>();
        try{
            File f=getApplicationContext().getDatabasePath("usermail.db").getParentFile();
            if(!f.exists())
                f.mkdirs();//注意是mkdirs()有个s 这样可以创建多重目录。
            String FullPath=f.getPath()+"/usermail.db";
            SQLiteDatabase sqLiteDatabase = SQLiteDatabase.openOrCreateDatabase(FullPath, null);

            String sql = "select alias,username,receiver_server from EmailAccount";
            Cursor cursor = sqLiteDatabase.rawQuery(sql,new String[]{});
            while (cursor.moveToNext()){
                String alias = cursor.getString(0);
                String username = cursor.getString(1);
                String receiver = cursor.getString(2);
                String host = receiver.substring(receiver.indexOf('.')+1);
                data.add(alias + "   "+username+"@"+host);
            }
        }catch (Exception e){
            System.out.println("bilibili"+e.getMessage());
        }
        return data;
    }
}
