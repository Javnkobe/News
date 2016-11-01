package com.feicui.edu.highpart.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.FileProvider;

import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.feicui.edu.highpart.Adapter.LoginLogAdapter;
import com.feicui.edu.highpart.Common.OkHttpUtil;;
import com.feicui.edu.highpart.LoginActivity;
import com.feicui.edu.highpart.MainActivity;
import com.feicui.edu.highpart.R;
import com.feicui.edu.highpart.bean.BaseEntity;
import com.feicui.edu.highpart.bean.LoginLog;
import com.feicui.edu.highpart.bean.User;
import com.feicui.edu.highpart.biz.UserManager;
import com.feicui.edu.highpart.exception.URLErrorException;
import com.feicui.edu.highpart.util.CommonUtil;
import com.feicui.edu.highpart.util.Const;
import com.feicui.edu.highpart.util.DensityUtil;
import com.feicui.edu.highpart.util.GetPictureUtil;
import com.feicui.edu.highpart.util.SharedPreferenceUtil;
import com.feicui.edu.highpart.util.SystemUtils;
import com.feicui.edu.highpart.util.UrlParameterUtil;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * 1.下载用户数据信息 user_home?ver=版本号&imei=手机标识符&token =用户令牌
 * 2.解析、封装到一个对象user
 * 把数据设置到view中
 */
public class UserInfoFragment extends Fragment implements View.OnClickListener {

    private Context context;
    private CircleImageView icon;//用户头像
    private TextView useName;//用户昵称
    private TextView integral;//用户积分
    private TextView comment_count;//用户评论次数
    private Button exit;
    private PopupWindow window;
    boolean isShowing = false;//是否显示泡泡窗口
    static final int REQUEST_IMAGE_CAPTURE = 1;
    String mCurrentPhotoPath;
    static final int REQUEST_TAKE_PHOTO = 1;
    private Toolbar toolbar;
    private ListView list;
    private LoadUserTask loadUserTask;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_user_info, null);
        context = getContext();
        icon = (CircleImageView) view.findViewById(R.id.icon);
        useName = (TextView) view.findViewById(R.id.tv_name);
        integral = (TextView) view.findViewById(R.id.tv_integral);
        comment_count = (TextView) view.findViewById(R.id.tv_comment_count);
        list = (ListView) view.findViewById(R.id.list);
        exit = (Button) view.findViewById(R.id.btn_exit);
        toolbar = (Toolbar) view.findViewById(R.id.tb_toolbar);
        exit.setOnClickListener(this);
        icon.setOnClickListener(this);

        //下载用户数据
        loadUserInfo();
        return view;
    }
    @Override
    public void onActivityCreated (@Nullable Bundle ssavedInstanceState) {
        super.onActivityCreated(ssavedInstanceState);
        if (getActivity() instanceof LoginActivity){
            final LoginActivity activity = (LoginActivity) getActivity();
            activity.setSupportActionBar(toolbar);
            activity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    /*activity.getSupportFragmentManager().beginTransaction().
                            replace(R.id.container, new NewsGroupFragment()).commit();*/
                    startActivity(new Intent(context,MainActivity.class));
                    activity.finish();
                }
            });
        }else{
            final MainActivity activity = (MainActivity) getActivity();
            activity.setSupportActionBar(toolbar);
            activity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    /*activity.getSupportFragmentManager().beginTransaction().
                            replace(R.id.container, new NewsGroupFragment()).commit();*/
                    startActivity(new Intent(context, MainActivity.class));
                }
            });
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.icon:
                showPopuWindow();
                break;
            case R.id.btn_exit:
                //清空账户信息
                SharedPreferenceUtil.saveAccount(context,null,null);
                if (getActivity()!=null){
//                    SharedPreferenceUtil.getAccount(context);
//                    loadUserInfo();
                    startActivity(new Intent(context,MainActivity.class));
                }
                break;
            case R.id.btn_takephoto:
                GetPictureUtil.openCamera(getActivity());
                //保存缩略图
//                Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//                if (takePictureIntent.resolveActivity(getActivity().getPackageManager()) != null) {
//                    startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
//                }
                //保存大图片
                //dispatchTakePictureIntent();
                //把拍到的照片存到手机画廊app中
                //galleryAddPic();
                //scaleImg(icon,mCurrentPhotoPath);
                isShowing = false;
                window.dismiss();
                break;
            case R.id.btn_phonephoto:
                GetPictureUtil.openGallery(UserInfoFragment.this);
               /* Bitmap bitmap = BitmapFactory.decodeFile(GetPictureUtil.getFilePathString(getActivity()));
                icon.setImageBitmap(bitmap);*/
                //Intent intent = new Intent(Intent.ACTION_PICK,
                //android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                //UserInfoFragment.this.startActivityForResult(intent,REQUEST_PICK);
                //Bitmap bitmap = BitmapFactory.decodeFile(GetPictureUtil.getFilePathString(getActivity()));
                //icon.setImageBitmap(bitmap);
                isShowing = false;
                window.dismiss();
                break;
            case R.id.btn_cancle:
                isShowing = false;
                window.dismiss();
                break;
        }

    }

    private void showPopuWindow() {
        if (isShowing) {
            //如果泡泡窗口显示
            return;
        }
        window = new PopupWindow(context);
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.use_icon, null);
        window.setContentView(view);
        window.setWidth(CommonUtil.getDisplayWidth(context));
        window.setHeight(600);
        //Toast.makeText(context, DensityUtil.dip2px(context,200)+"", Toast.LENGTH_SHORT).show();
        window.showAsDropDown(
                exit,//显示参照对象
                0,//x轴偏移量
                0 - DensityUtil.dip2px(context, 200)//y轴偏移量
        );
        isShowing = true;
        //Toast.makeText(context, DensityUtil.dip2px(context,200)+"", Toast.LENGTH_SHORT).show();
        view.findViewById(R.id.btn_takephoto).setOnClickListener(this);
        view.findViewById(R.id.btn_phonephoto).setOnClickListener(this);
        view.findViewById(R.id.btn_cancle).setOnClickListener(this);
    }

    private void loadUserInfo() {
        Map<String, String> p = new HashMap<>();
        // user_home?ver=版本号&imei=手机标识符&token =用户令牌
        p.put("ver", CommonUtil.getVersionCode(context) + "");
        p.put("imei", SystemUtils.getIMEI(context));
        p.put("token", SharedPreferenceUtil.getToken(context));
        String urlPath = UrlParameterUtil.parameter(Const.URL_USER_INFO, p);
        loadUserTask = new LoadUserTask();
        loadUserTask.execute(urlPath);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (loadUserTask!=null&&loadUserTask.isCancelled()) {
            loadUserTask.cancel(true);
        }
    }

    class LoadUserTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {
            UserManager m = new UserManager();
            try {
                return m.getUserInfo(params[0]);
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
            BaseEntity baseEntity = parseUserInfo(s);
            if (baseEntity == null) {
                return;
            } else if (baseEntity.getStatus().equals("0")) {
                //成功，把数据设置到view中
                setDataToView(baseEntity);
            } else {
                //失败
                Toast.makeText(context, "用户数据获取失败", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void setDataToView(BaseEntity baseEntity) {
        User user = (User) baseEntity.getData();
        //SharedPreferenceUtil.saveUserName(context,user.getUid());
        //SharedPreferenceUtil.saveHeader(context,user.getPortrait());
        useName.setText(user.getUid());
        String portrait = user.getPortrait();
        comment_count.setText(user.getComnum()+"");
        Glide.with(this).load(portrait)
                .centerCrop().into(icon);
        //integral.setText(user.getIntegration());
        //comment_count.setText(user.getComnum());
        List<LoginLog> loginlog = user.getLoginlog();
        LoginLogAdapter adapter = new LoginLogAdapter(context);
        adapter.appendData(loginlog,true);
        list.setAdapter(adapter);
    }

    private BaseEntity parseUserInfo(String s) {
        Gson g = new Gson();
        Type t = new TypeToken<BaseEntity<User>>() {
        }.getType();
        return g.fromJson(s, t);
    }

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getActivity().getPackageManager()) != null) {
            // Create the File where the photo should go
            //创建图片存放位置
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                // Error occurred while creating the File
                Toast.makeText(context, "创建图片文件失败", Toast.LENGTH_SHORT).show();
            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(context,
                        "com.example.android.fileprovider",
                        photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);
            }
        }
    }

    /**
     * 保存拍好的照片到画廊
     */
    private void galleryAddPic() {
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        File f = new File(mCurrentPhotoPath);
        Uri contentUri = Uri.fromFile(f);
        mediaScanIntent.setData(contentUri);
        context.sendBroadcast(mediaScanIntent);
    }

    /**
     * 缩放拍完的大图，并设置到imageview控件中
     *
     * @param mImageView        要放的图片控件
     * @param mCurrentPhotoPath 图片存放路径
     */
    private void scaleImg(ImageView mImageView, String mCurrentPhotoPath) {
        // Get the dimensions of the View
        int targetW = mImageView.getWidth();
        int targetH = mImageView.getHeight();

        // Get the dimensions of the bitmap
        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        bmOptions.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(mCurrentPhotoPath, bmOptions);
        int photoW = bmOptions.outWidth;
        int photoH = bmOptions.outHeight;

        // Determine how much to scale down the image
        int scaleFactor = Math.min(photoW / targetW, photoH / targetH);

        // Decode the image file into a Bitmap sized to fill the View
        bmOptions.inJustDecodeBounds = false;
        bmOptions.inSampleSize = scaleFactor;
        bmOptions.inPurgeable = true;

        Bitmap bitmap = BitmapFactory.decodeFile(mCurrentPhotoPath, bmOptions);
        mImageView.setImageBitmap(bitmap);
    }

    /**
     * 创建存放图片文件
     */
    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath = "file:" + image.getAbsolutePath();
        return image;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data==null) {
            return;
        }
        GetPictureUtil.onActivityResult(requestCode, data, this);
        if (resultCode == Activity.RESULT_OK) {
            String pathString = GetPictureUtil.getFilePathString(getActivity());
//            Glide.with(context)
//                    .load(pathString)//可以加载本地，也可以下载网络
//                    .centerCrop()//对bitmap像素缩放
//                    .placeholder(R.drawable.head)//默认图片
//                    .crossFade()//动画效果
//                    .into(icon);//把下载的图片放到imageview中
            //上传图片到服务器 user_image?token=用户令牌& portrait =头像
            File file = new File(pathString);
            new UploadFileTask(file).execute(Const.URL_USER_IMAGE);
        }
    }

    class UploadFileTask extends AsyncTask<String, Void, String> {

        private File file;

        public UploadFileTask(File file) {
            this.file = file;
        }

        @Override
        protected String doInBackground(String... params) {
            try {
                return OkHttpUtil.postFile(params[0], file, SharedPreferenceUtil.getToken(context));
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if (s!=null&&s.contains("OK")){
                loadUserInfo();
                Toast.makeText(context, "头像上传成功", Toast.LENGTH_SHORT).show();
            }else{
                Toast.makeText(context, "头像上传失败", Toast.LENGTH_SHORT).show();

            }

            /*BaseEntity baseEntity = parseUserInfo(s);
            if (baseEntity == null) {
                return;
            } else if (baseEntity.getStatus().equals("0")) {
                //成功，把数据设置到view中
                setDataToView(baseEntity);
            } else {
                //失败
                Toast.makeText(context, "用户数据获取失败", Toast.LENGTH_SHORT).show();
            }*/
        }
    }
}
