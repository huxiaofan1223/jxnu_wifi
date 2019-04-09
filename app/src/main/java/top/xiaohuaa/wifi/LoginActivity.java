package top.xiaohuaa.wifi;
import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.Spinner;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;

import android.net.wifi.WifiManager;
import android.net.wifi.WifiInfo;
import android.content.Context;
import android.widget.Button;
import android.widget.EditText;
import android.view.View;

import android.view.View.OnClickListener;
import android.widget.TextView;
import android.widget.Toast;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class LoginActivity extends AppCompatActivity{
    private static int ord(char n){
        return Integer.valueOf(n);
    }
    private static char chr(int n){
        return (char)n;
    }
    private static String password_encrypt(String password){
        String result = "";
        String key="1234567890";
        for(int i=0;i<password.length();i++){
            int ki = ord(password.toCharArray()[i]) ^ ord(key.toCharArray()[(key.length()) - i % key.length() - 1]);
            char _l = chr((ki & 0x0F) + 0x36);
            char _h = chr((ki >> 4 & 0x0F) + 0x63);
            if(i % 2 == 0)
                result += String.valueOf(_l) + String.valueOf(_h);
            else
                result += String.valueOf(_h) + String.valueOf(_l);
        }
        return result;
    }
    public String getmacAddress() {
        WifiManager wifi = (WifiManager)getSystemService(Context.WIFI_SERVICE);
        WifiInfo info = wifi.getConnectionInfo();
        return info.getMacAddress();
    }
    private String getIp(){
        WifiManager wm=(WifiManager)getSystemService(Context.WIFI_SERVICE);
        WifiInfo wi=wm.getConnectionInfo();
        int ipAdd=wi.getIpAddress();
        String ip=intToIp(ipAdd);
        return ip;
    }
    private String intToIp(int i) {
        return (i & 0xFF ) + "." +
                ((i >> 8 ) & 0xFF) + "." +
                ((i >> 16 ) & 0xFF) + "." +
                ( i >> 24 & 0xFF) ;
    }
    private void showProgress(final boolean show) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);
            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mProgressView.animate().setDuration(shortAnimTime).alpha(show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
        }
    }
    public String logout(String username){
        try {
            String spec = "http://172.16.8.6:69/cgi-bin/srun_portal";
            URL url = new URL(spec);
            HttpURLConnection conn = (HttpURLConnection)url.openConnection();
            conn.setRequestMethod("POST");
            conn.setReadTimeout(5000);
            conn.setConnectTimeout(5000);
            String data = "action=" + URLEncoder.encode("logout", "UTF-8")+ "&ac_id=" + URLEncoder.encode("1", "UTF-8")+"&username=" + URLEncoder.encode(username, "UTF-8")+"&mac=" + URLEncoder.encode(getmacAddress(), "UTF-8")+"&type=" + URLEncoder.encode("2", "UTF-8");
            conn.setRequestProperty("Content-Length",String.valueOf(data.getBytes().length));
            conn.setRequestProperty("Accept-Encoding","identity");
            conn.setRequestProperty(" Host","172.16.8.6:69");
            conn.setRequestProperty("Content-Type","application/x-www-form-urlencoded");
            conn.setRequestProperty("Cache-Control","no-cache");
            conn.setRequestProperty(" User-Agent","Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.1 (KHTML, like Gecko) Chrome/21.0.1180.89 Safari/537.1");
            conn.setRequestProperty("SRun-Version","SRun3K Client_XW117S0B60401A-SRun3K.Portal");
            conn.setRequestProperty("SRun-AuthorizationCRC32","55ACF9CC");
            conn.setRequestProperty("DiskDevice","|");
            conn.setRequestProperty("OSName","Microsoft  (build 9200), 64-bit");
            conn.setRequestProperty("SRun-ClientTime",String.valueOf(System.currentTimeMillis()/1000));
            conn.setRequestProperty("SRun-AuthorizationKey","b4ba6270fb2e22795332d72f493ccb3f");
            conn.setRequestProperty("CPUDevice","|");
            conn.setRequestProperty("OSVersion","6.2 Build9200");
            conn.setRequestProperty("Accept","text/plain");
            conn.setDoOutput(true);
            conn.setDoInput(true);
            OutputStream os = conn.getOutputStream();
            os.write(data.getBytes());
            os.flush();
            if (conn.getResponseCode() == 200) {
                InputStream is = conn.getInputStream();
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                int len = 0;
                byte[] buffer = new byte[1024];
                while ((len = is.read(buffer)) != -1) {
                    baos.write(buffer, 0, len);
                }
                is.close();
                baos.close();
                final String result = new String(baos.toByteArray());
                System.out.println(result);
                return result;
            } else {
                System.out.println("链接失败.........");
                return "0";
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "0";
    }
    public String login(String username,String password){
        try {
            System.out.println(username+password);
            String spec = "http://172.16.8.6:69/cgi-bin/srun_portal";
            URL url = new URL(spec);
            HttpURLConnection conn = (HttpURLConnection)url.openConnection();
            conn.setRequestMethod("POST");
            conn.setReadTimeout(5000);
            conn.setConnectTimeout(5000);
            String data = "action=" + URLEncoder.encode("login", "UTF-8")+ "&username=" + URLEncoder.encode(username, "UTF-8")+"&password=" + URLEncoder.encode(password, "UTF-8")+"&drop=" + URLEncoder.encode("0", "UTF-8")+"&pop=" + URLEncoder.encode("0", "UTF-8")+"&type=" + URLEncoder.encode("2", "UTF-8")+"&n=" + URLEncoder.encode("117", "UTF-8")+"&mbytes=" + URLEncoder.encode("0", "UTF-8")+"&minutes=" + URLEncoder.encode("0", "UTF-8")+"&ac_id=" + URLEncoder.encode("1", "UTF-8")+"&mac=" + URLEncoder.encode(getmacAddress(), "UTF-8")+"&ip=" + URLEncoder.encode(getIp(), "UTF-8");
            conn.setRequestProperty("Content-Length",String.valueOf(data.getBytes().length));
            conn.setRequestProperty("Accept-Encoding","identity");
            conn.setRequestProperty(" Host","172.16.8.6:69");
            conn.setRequestProperty("Content-Type","application/x-www-form-urlencoded");
            conn.setRequestProperty("Cache-Control","no-cache");
            conn.setRequestProperty(" User-Agent","Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.1 (KHTML, like Gecko) Chrome/21.0.1180.89 Safari/537.1");
            conn.setRequestProperty("SRun-Version","SRun3K Client_XW117S0B60401A-SRun3K.Portal");
            conn.setRequestProperty("SRun-AuthorizationCRC32","1F29E340");
            conn.setRequestProperty("DiskDevice","|");
            conn.setRequestProperty("OSName","Microsoft  (build 9200), 64-bit");
            conn.setRequestProperty("SRun-ClientTime",String.valueOf(System.currentTimeMillis()/1000));
            conn.setRequestProperty("SRun-AuthorizationKey","b4ba6270fb2e22795332d72f493ccb3f");
            conn.setRequestProperty("CPUDevice","|");
            conn.setRequestProperty("OSVersion","6.2 Build9200");
            conn.setRequestProperty("Accept","text/plain");
            conn.setDoOutput(true);
            conn.setDoInput(true);
            OutputStream os = conn.getOutputStream();
            os.write(data.getBytes());
            os.flush();
            if (conn.getResponseCode() == 200) {
                InputStream is = conn.getInputStream();
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                int len = 0;
                byte[] buffer = new byte[1024];
                while ((len = is.read(buffer)) != -1) {
                    baos.write(buffer, 0, len);
                }
                is.close();
                baos.close();
                final String result = new String(baos.toByteArray());
                System.out.println(result);
                return result;
            } else {
                System.out.println("链接失败.........");
                return "0";
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "0";
    }
    private EditText musername,mpassword,merror;
    private Spinner mtype;
    private String username,password,type;
    private HashMap<String, String> type_map = new HashMap<String, String>();
    private SharedPreferences sp = null; //记住密码
    private CheckBox mAutoLoginSwitch,mRemPassSwitch;
    private View mProgressView;
    private WifiManager mWifiManager;
    private boolean isNetOn;
    private TextView mNetStateView;
    private void map_init(){
        type_map.put("移动校园网络","@cmcc");
        type_map.put("联通校园网络","@cucc");
        type_map.put("电信校园网络","@ctcc");
        type_map.put("校园网络","@ctcc");
    }
    private boolean yz_text(){
//        LoginActivity.this.runOnUiThread(new Runnable() {
//            @Override
//            public void run() {
//
//            }
//        });
        if(username.isEmpty()){
            musername.setError("请输入账号");
            merror=musername;
            return false;
        }
        if(password.isEmpty()) {
            mpassword.setError("请输入密码");
            merror=mpassword;
            return false;
        }
        return true;
    }
    private void remember_pass(){
        boolean rem_pass_switch = mRemPassSwitch.isChecked();
        boolean auto_login_switch = mAutoLoginSwitch.isChecked();
        if (rem_pass_switch) {
            Editor editor = sp.edit();
            editor.putString("username", username.split("@")[0]);
            editor.putString("password", password);
            editor.putInt("type",mtype.getSelectedItemPosition());
            editor.putBoolean("rempass", true);
            editor.putBoolean("autologin",auto_login_switch);
            editor.commit();
        }
        else{
            Editor editor = sp.edit();
            editor.clear();
            editor.putString("username", username.split("@")[0]);
            editor.commit();
        }
    }
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity);
        Button login_button = (Button) findViewById(R.id.login_button);
        Button logout_button = (Button) findViewById(R.id.logout_button);
        mRemPassSwitch = (CheckBox)findViewById(R.id.mRemPassSwitch);
        mAutoLoginSwitch = (CheckBox)findViewById(R.id.mAutoLoginSwitch);
        musername = (EditText) findViewById(R.id.username);
        mpassword= (EditText) findViewById(R.id.password);
        mtype= (Spinner) findViewById(R.id.spinner);
        mProgressView = findViewById(R.id.login_progress);
//        mNetStateView = findViewById(R.id.netstate);
        map_init();
        mAutoLoginSwitch.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean auto_login=mAutoLoginSwitch.isChecked();
                boolean rem_pass = mRemPassSwitch.isChecked();
                mAutoLoginSwitch.setTextColor(getResources().getColor(auto_login?R.color.primary:R.color.mid_black));
                if(auto_login&&!rem_pass)
                    mRemPassSwitch.performClick();
            }
        });
        mRemPassSwitch.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean auto_login=mAutoLoginSwitch.isChecked();
                boolean rem_pass = mRemPassSwitch.isChecked();
                mRemPassSwitch.setTextColor(getResources().getColor(rem_pass?R.color.primary:R.color.mid_black));
                if(auto_login&&!rem_pass)
                    mAutoLoginSwitch.performClick();
            }
        });
        sp = this.getSharedPreferences( "config", MODE_PRIVATE);
        username = sp.getString("username", "");
        password = sp.getString("password", "");
        mtype.setSelection(sp.getInt("type",0));
        boolean rempass = sp.getBoolean("rempass", false);
        boolean autologin = sp.getBoolean("autologin", false);
        musername.setText(username);
        mpassword.setText(password);
        mWifiManager=(WifiManager)getSystemService(Context.WIFI_SERVICE);
        if(!mWifiManager.isWifiEnabled()){
            Toast.makeText(this, "请打开wifi开关", Toast.LENGTH_SHORT).show();
        }
        if(rempass)mRemPassSwitch.performClick();
        login_button.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!mWifiManager.isWifiEnabled()){
                    Toast.makeText(LoginActivity.this, "请打开wifi开关", Toast.LENGTH_SHORT).show();
                    return;
                }
                username=musername.getText().toString();
                password=mpassword.getText().toString();
                type=(String) mtype.getSelectedItem();
                showProgress(true);
                if(!yz_text())
                    merror.requestFocus();
                else
                new Thread(){
                    public void run(){
                        username+=type_map.get(type);
                        final String result=login(username,password_encrypt(password));
                        try{
                            Thread.sleep(2000);
                        }catch(InterruptedException e){
                        }
                        LoginActivity.this.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if(result.contains("login_ok")){
                                    remember_pass();
                                    Toast.makeText(LoginActivity.this,"登录成功", Toast.LENGTH_SHORT).show();
                                }
                                else if(result.contains("账号已在线"))
                                    Toast.makeText(LoginActivity.this,"登录失败,账号在线", Toast.LENGTH_SHORT).show();
                                else if(result.contains("ip_already_online_error"))
                                    Toast.makeText(LoginActivity.this,"登录失败,当前设备在线", Toast.LENGTH_SHORT).show();
                                else
                                    Toast.makeText(LoginActivity.this,"登录失败,账户或者密码错误", Toast.LENGTH_SHORT).show();
                                showProgress(false);
                            }
                        });
                    }
                    }.start();
            }
        });
        logout_button.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                username=musername.getText().toString();
                password=mpassword.getText().toString();
                if(!yz_text())
                    merror.requestFocus();
                else
                new Thread(){
                    public void run(){
                        username+=type_map.get(type);
                        final String result=logout(username);
                        LoginActivity.this.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if(result.contains("logout_ok"))
                                    Toast.makeText(LoginActivity.this,"注销成功", Toast.LENGTH_SHORT).show();
                                else if(result.contains("not online"))
                                    Toast.makeText(LoginActivity.this,"当前设备不在线", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                }.start();
            }
        });
        if(autologin){
            mAutoLoginSwitch.performClick();
            login_button.performClick();
        }
    }
}
