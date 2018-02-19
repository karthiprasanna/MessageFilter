package com.example.administrator.smsfilterapp;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.administrator.smsfilterapp.Db.DbHelper;
import com.example.administrator.smsfilterapp.model.SmsStorageData;

import org.apache.commons.lang3.StringUtils;

import java.security.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.TimeZone;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MainActivity extends AppCompatActivity {
    private static final int MY_PERMISSIONS_REQUEST_SMS_CONTACTS = 99;
    private static final int MY_PERMISSIONS_REQUEST_READ_CONTACTS = 100;
    private DbHelper dbHelper;
    private String msgData = "";
     private DetailListAdapter detailListAdapter;
    private List<SmsStorageData> datasavemovielist;
    private RecyclerView recyclerView;
    List<SmsStorageData> lstSms = new ArrayList<>();
    private static final int SMS_PERMISSION_CODE = 0;


    private static final int REQUEST_CODE_ASK_PERMISSIONS = 0;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

            requestReadAndSendSmsPermission();



        dbHelper = new DbHelper(MainActivity.this);
        recyclerView = (RecyclerView) findViewById(R.id.detail_listview);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        datasavemovielist = new ArrayList<>();
        lstSms=dbHelper.getAllSmsData();
        detailListAdapter= new DetailListAdapter(this,lstSms);
                recyclerView.setAdapter(detailListAdapter);

      /*  SmsReceiver.bindListener(new SmsListener() {
            @Override
            public void messageReceived(String messageText) {


                Log.e("Message",messageText);
                Toast.makeText(MainActivity.this,"Message: "+messageText,Toast.LENGTH_LONG).show();


            }
        });*/
    }


    private void requestReadAndSendSmsPermission() {
        if (ContextCompat.checkSelfPermission(MainActivity.this,
                Manifest.permission.READ_SMS)
                != PackageManager.PERMISSION_GRANTED) {

//            if (ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this,
//                    Manifest.permission.READ_SMS)) {

//                Toast.makeText(MainActivity.this,"permiision deied",Toast.LENGTH_SHORT).show();
//
//            } else {


                ActivityCompat.requestPermissions(MainActivity.this,
                        new String[]{Manifest.permission.READ_SMS},
                        MY_PERMISSIONS_REQUEST_READ_CONTACTS);


//            }
        }
    }



    public List<SmsStorageData> getAllSms() {


        ActivityCompat.requestPermissions(MainActivity.this, new String[]{"android.permission.READ_SMS"}, REQUEST_CODE_ASK_PERMISSIONS);


        Cursor c = getContentResolver().query(Uri.parse("content://sms/inbox"), null, null, null, null);
        int totalSMS = c.getCount();
        if (c.moveToFirst()) {
            for (int i = 0; i < totalSMS; i++) {
                SmsStorageData objSms = new SmsStorageData();
                objSms.set_id(c.getString(c.getColumnIndexOrThrow("_id")));
                objSms.setAddress(c.getString(c.getColumnIndexOrThrow("address")));
                objSms.setDate(c.getString(c.getColumnIndexOrThrow("date")));
                objSms.setBody(c.getString(c.getColumnIndexOrThrow("body")));


                Log.e("checkdata", "..." + c.getString(c.getColumnIndexOrThrow("body")));
                  // if (c.getString(c.getColumnIndexOrThrow("body")).toLowerCase().toString().contains("debited") || (c.getString(c.getColumnIndexOrThrow("body")).toLowerCase().toString().contains("credited")) ) {
                    if (c.getString(c.getColumnIndexOrThrow("body")).toLowerCase().toString().contains("credit card")) {
                lstSms.add(objSms);
                dbHelper.addSmsData(objSms);
                 }
         //   }

                c.moveToNext();
            }
        }
        c.close();
        return lstSms;


    }



    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_READ_CONTACTS: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    lstSms= getAllSms();
                    detailListAdapter.notifyDataSetChanged();


                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }

        }
    }











    private class DetailListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
        private Activity activity;
        Date netDate;
        private List<SmsStorageData>infoList;
        private SimpleDateFormat sdf;

        public DetailListAdapter(Activity activity, List<SmsStorageData> infoList) {

            this.activity=activity;
            this.infoList=infoList;

        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.list_items, parent, false);
            return new MyHolder(itemView);



        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

            MyHolder itemHolder = (MyHolder) holder;
            final SmsStorageData questionInfo=infoList.get(position);
            itemHolder.address.setText(questionInfo.address);


//            String s1 = questionInfo.body;
//            String content = StringUtils.substringBetween(s1, "ending", "at");
//            System.out.println("title "+content);
//            String[] deatils = content.split("on");
            // System.out.println("Card no "+deatils[0].trim());

            // System.out.println("date and time "+deatils[1].trim());


            itemHolder.body.setText(questionInfo.body);








            try{
                sdf = new SimpleDateFormat("yyyyMMddHHmmss");
                netDate = (new Date(Long.parseLong(questionInfo.date)));
                sdf.applyPattern("dd MMM yyyy hh:mm");

            }
            catch(Exception ex){
            }

            itemHolder.date.setText(sdf.format(netDate));
            final int s = position+1;
            itemHolder.position.setText(s+"");

        }

        @Override
        public int getItemCount() {
            return infoList.size();
        }

        private class MyHolder extends RecyclerView.ViewHolder {

            public TextView address,position;
            public TextView date,body;

            public MyHolder(View itemView) {
                super(itemView);


                address= (TextView)itemView.findViewById(R.id.address);
                date= (TextView)itemView.findViewById(R.id.date);
                body= (TextView)itemView.findViewById(R.id.body);
                position= (TextView)itemView.findViewById(R.id.position);



            }
        }





    }



}

