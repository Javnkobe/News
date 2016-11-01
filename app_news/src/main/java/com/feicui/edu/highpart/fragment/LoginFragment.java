package com.feicui.edu.highpart.fragment;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.feicui.edu.highpart.MainActivity;
import com.feicui.edu.highpart.R;
import com.feicui.edu.highpart.bean.BaseEntity;
import com.feicui.edu.highpart.bean.Register;
import com.feicui.edu.highpart.biz.UserManager;
import com.feicui.edu.highpart.exception.URLErrorException;
import com.feicui.edu.highpart.util.CommonUtil;
import com.feicui.edu.highpart.util.Const;
import com.feicui.edu.highpart.util.SharedPreferenceUtil;
import com.feicui.edu.highpart.util.SystemUtils;
import com.feicui.edu.highpart.util.UrlParameterUtil;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2016/9/13 0013.
 */
public class LoginFragment extends DialogFragment implements View.OnClickListener {

    private Context context;
    private EditText et_username;
    private EditText et_pwd;

  /*  @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setShowsDialog(true);
    }*/

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        context = getContext();
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        //LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.frgment_login, null);
        builder.setView(view);
        view.findViewById(R.id.btn_register).setOnClickListener(this);
        view.findViewById(R.id.btn_login).setOnClickListener(this);
        view.findViewById(R.id.btn_forgetPwd).setOnClickListener(this);
        et_username = (EditText) view.findViewById(R.id.et_username);
        et_pwd = (EditText) view.findViewById(R.id.et_pwd);
        return builder.create();
    }

    @Override
    public void onCancel(DialogInterface dialog) {
        super.onCancel(dialog);
        getActivity().finish();
        startActivity(new Intent(getContext(), MainActivity.class));
    }

   /* @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
    }*/

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            //注册
            case R.id.btn_register:
                new RegisterFragment().show(getFragmentManager(),"");
                break;
            //登录
            case R.id.btn_login:
                //Toast.makeText(context, "ok", Toast.LENGTH_SHORT).show();
                //Log.d("Login","debug");
                String username = et_username.getText().toString();
                String pwd = et_pwd.getText().toString();
                login(
                        username,
                        pwd
                );
                break;
            //忘记密码
            case R.id.btn_forgetPwd:
                new ForgetFragment().show(getFragmentManager(),"");
                break;
            default:
                break;
        }
    }
    private void login(String username, String pwd) {
        Map<String, String> p = new HashMap<>();
        //TODO 对用户名，密码，邮箱进行本地校验
//        * http://118.244.212.82:9094//newsClient/login?uid=admin&pwd=admin&
//        * imei=abc&ver=1&device=1
        p.put("uid", username);
        p.put("pwd", pwd);
        p.put("imei", SystemUtils.getIMEI(context));
        p.put("ver", CommonUtil.getVersionCode(context) + "");
        p.put("device", Const.PHONE);
        String urlPath = UrlParameterUtil.parameter(Const.URL_LOGIN, p);
        new LoginTask(username,pwd).execute(urlPath);
    }

    class LoginTask extends AsyncTask<String, Void, String> {

        private final String username;
        private final String pwd;

        public LoginTask(String username, String pwd) {

            this.username = username;
            this.pwd = pwd;
        }

        @Override
        protected String doInBackground(String... params) {
            UserManager m = new UserManager();
            try {
                return m.register(params[0]);
            } catch (IOException e) {
                e.printStackTrace();
                Toast.makeText(context, "服务器访问失败", Toast.LENGTH_SHORT).show();
            } catch (URLErrorException e) {
                e.printStackTrace();
                Toast.makeText(context, "参数有误", Toast.LENGTH_SHORT).show();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            BaseEntity entity = parseUser(s);
            String status = entity.getStatus();
            //Toast.makeText(context, "99999", Toast.LENGTH_SHORT).show();
            if (status.equals("0")) {
                Log.d("LoginFragment","1221311");
                Register registerInfo = (Register) entity.getData();

                if (registerInfo.getResult().equals("0")) {
                    dismiss();
                    //登入成功
                    SharedPreferenceUtil.saveToken(context,registerInfo.getToken());
                    //保存用户名和密码
                    SharedPreferenceUtil.saveAccount(context,username,pwd);
                    getFragmentManager().beginTransaction()
                            .replace(R.id.container_login, new UserInfoFragment()
                    ).commit();
                } else {
                    //失败
                }
                Toast.makeText(context, registerInfo.getExplain(), Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(context, entity.getMessage(), Toast.LENGTH_SHORT).show();
            }

        }
    }

    public BaseEntity parseUser(String jsonString) {
        Gson g = new Gson();
        Type t = new TypeToken<BaseEntity<Register>>() {
        }.getType();
        return g.fromJson(jsonString, t);
    }
}
