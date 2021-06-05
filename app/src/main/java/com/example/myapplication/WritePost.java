package com.example.myapplication;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Address;
import android.location.Geocoder;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresPermission;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;

import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;


import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import com.example.myapplication.MainActivity;

public class WritePost extends AppCompatActivity {

    private final int GET_IMG_FROM_GALLERY = 200; //갤러리 사진 요청 request
    private final int GET_IMG_FROM_CAMERA = 400;
    private final int GPS_ENABLE_REQUEST_CODE=300;
    private ImageView imageview;
    private EditText editText;
    private Uri photoUri;
    private Uri selectedImageUri;
    String photoImgName;
    private String currentPhotoPathl;
    TextView locationTxt;
    String provider;
    double latitude=0; //위도
    double longitude=0;   //경도
    EditText contentText;
    EditText titleText;
    Location location;
    Bitmap bitmap;

    ActionBar ab;


    //GPSListener gpsListener;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_write_post);
        ab=getSupportActionBar();

        checkPermission();

        if(!checkLocationServiceStatus())
                showDialogForLocationServiceSetting();


        //본문 (content) 내용 string으로 바꿔오기
        contentText = (EditText) findViewById(R.id.content);
        titleText=(EditText) findViewById(R.id.title);


        //image view 위에 갤러리 접근 허가 요청 하기
        imageview = (ImageView) findViewById(R.id.getImage);//이미지뷰 연결
        imageview.setOnClickListener(new View.OnClickListener() {//이미지 뷰 클릭 -> 갤러리에서 url 가져오는 요청
            @Override
            public void onClick(View view) {
                //imgDialog();
                Intent gal=new Intent(Intent.ACTION_PICK);
                gal.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,"image/*");
                startActivityForResult(gal,GET_IMG_FROM_GALLERY);
            }
        });//image view 클릭시 동작 끝


        //버튼 누르면 현재 위치
        Button locationbutton = findViewById(R.id.locationbutton);
        final LocationManager lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        List<String> providers=lm.getProviders(true);

        locationTxt=findViewById(R.id.locationText);
        final Geocoder geocoder=new Geocoder(this);
        locationbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ActivityCompat.checkSelfPermission(WritePost.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(WritePost.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    checkPermission();
                }
                Log.v("check","enter onclick");
                //lm.requestLocationUpdates(LocationManager.GPS_PROVIDER,1000,100,gpsLocationListener);
                //Log.v("check","get location update");
                //location=lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                for(String provider:providers){
                    Location l=lm.getLastKnownLocation(provider);
                    if(l==null)
                        continue;
                    if(location==null||l.getAccuracy()<location.getAccuracy())
                        location=l;
                }
                Log.v("check","get last known location");
                String provider=location.getProvider();
                Log.v("check","get provider");
                latitude=location.getLatitude();//위도
                longitude=location.getLongitude();//경도

                String add=getCurrentAddress(latitude,longitude);

                //locationTxt.setText("위치정보: "+provider+"\n"+"위도: "+latitude+"\n"+"경도: "+longitude);
                locationTxt.setText(getCurrentAddress(latitude,longitude));
                //Toast.makeText(WritePost.this,"위도: "+latitude+", 경도: "+longitude,Toast.LENGTH_LONG).show();
                //lm.requestLocationUpdates(LocationManager.GPS_PROVIDER,5000,100,gpsLocationListener);//5초, 100미터
                //lm.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,5000,100,gpsLocationListener);

            }
        });


    }//oncreate 끝

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_upload,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch(item.getItemId()){
            case R.id.action_write:
                String contentString = contentText.getText().toString();
                String titleString=titleText.getText().toString();
                byte[] imageBitmap=null;
                if(bitmap==null) {
                    imageBitmap = null;
                    Log.v("check","bitmap is null");
                }
                if(bitmap!=null){
                    ByteArrayOutputStream byteArrayOutputStream=new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.JPEG,50,byteArrayOutputStream);
                    imageBitmap=byteArrayOutputStream.toByteArray();
                    String test=imageBitmap.toString();
                    Log.v("check","bit map: "+imageBitmap);
                }

                Log.v("check","enter submit, before open db");

                //MainActivity myDatabase= new MainActivity();

                DatabaseHelper databaseHelper = new DatabaseHelper(WritePost.this, "DB", null, 1);
                SQLiteDatabase db = databaseHelper.getWritableDatabase();

                Log.v("check","set database");
                String crt;
                crt = "CREATE TABLE IF NOT EXISTS " + "Post" +
                        "( PID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        "title TEXT, " +
                        "contents TEXT," +
                        "latitude REAL," +
                        "longtitude REAL," +
                        "image BLOB," +
                        "created_day DATETIME DEFAULT CURRENT_TIMESTAMP);";
                db.execSQL(crt);
                String sql="insert into Post (title,contents,latitude,longtitude,image) values (?,?,?,?,?)";
                Object[] params={titleString,contentString,latitude,longitude,imageBitmap};
                db.execSQL(sql,params);
                Log.v("check","exec success~");

                Toast.makeText(WritePost.this,"업로드",Toast.LENGTH_SHORT).show();
                finish();

                //finish();//db에 저장 후, 이전 액티비티로 돌아가기
                return true;

            case R.id.action_cancel:
                AlertDialog.Builder builder=new AlertDialog.Builder(WritePost.this);
                builder.setTitle("취소").setMessage("글 작성을 취소하시겠습니까? ");
                builder.setPositiveButton("네", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        //finishActivity(500);//게시판 (post) 액티비티에서 startactivity 로 write를 호출해줬으므로, 해당 액티비티 종료
                        finish();
                    }
                });
                builder.setNegativeButton("아니오", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        ;
                    }
                });
                AlertDialog alertDialog=builder.create();
                alertDialog.show();
                return true;
            default:
                return super.onOptionsItemSelected(item);

        }

    }

    @Override
    //사진 이미지 요청 들어오면 url 받아오는 동작
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==GET_IMG_FROM_GALLERY&&resultCode==RESULT_OK&&data!=null&&data.getData()!=null){

            selectedImageUri=data.getData();
            try{
                bitmap=MediaStore.Images.Media.getBitmap(getApplicationContext().getContentResolver(),selectedImageUri);
                Bitmap.createScaledBitmap(bitmap,150,150,true);
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
            //url db에 저장
            Log.v("check","gallery uri: "+selectedImageUri.toString());
            imageview.setImageURI(selectedImageUri);
        }

        if(requestCode==GPS_ENABLE_REQUEST_CODE&&resultCode==RESULT_OK){
            if(checkLocationServiceStatus()){
                Log.v("check","activity result, gps enabled: true");
                checkPermission();
                return;
            }

        }
    }

    private void showDialogForLocationServiceSetting(){
        AlertDialog.Builder builder=new AlertDialog.Builder(WritePost.this);
        builder.setTitle("위치 서비스 설정");
        builder.setMessage("앱을 사용하기 위해서는 위치 서비스가 필요합니다.\n"+"위치 설정을 수정하실래요?");
        builder.setCancelable(true);
        builder.setPositiveButton("설정", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Intent callGPSSetIntent=new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivityForResult(callGPSSetIntent,GPS_ENABLE_REQUEST_CODE);
            }
        });
        builder.setNegativeButton("취소", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.cancel();
            }
        });
        builder.create().show();
    }

    public boolean checkLocationServiceStatus(){
        LocationManager lm=(LocationManager) getSystemService(LOCATION_SERVICE);
        return lm.isProviderEnabled(LocationManager.GPS_PROVIDER)||(lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER));
    }





    private File createImageFile() throws IOException {
        File dir = new File(Environment.getExternalStorageDirectory() + "/./");
        if (!dir.exists()) { dir.mkdirs(); }
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        photoImgName = timeStamp + ".jpg";
        File storageDir = new File(Environment.getExternalStorageDirectory().getAbsoluteFile() + "/./" + photoImgName);
        currentPhotoPathl = storageDir.getAbsolutePath();
        Log.v("check","path creadted: "+currentPhotoPathl);
        return storageDir;
    }

    public String getCurrentAddress(double latitude,double longitude){
        Geocoder geocoder=new Geocoder(this, Locale.getDefault());
        List<Address> add;
        try{
            add=geocoder.getFromLocation(latitude,longitude,10);

        } catch (IOException ioException) {
            ioException.printStackTrace();
            //Toast.makeText(this,"지오코더 주소 변환 불가",Toast.LENGTH_LONG).show();
            return "지오코더 주소 변환 불가";
        }catch (IllegalArgumentException illegalArgumentException){
            //Toast.makeText(this,"잘못된 GPS 좌표",Toast.LENGTH_LONG).show();
            return "잘못된  GPS 좌표";
        }

        if(add==null||add.size()==0){
            return "주소 미발견";
        }
        Address address=add.get(0);
        String adr=address.getAddressLine(0).toString().replaceFirst("대한민국","");
        //return address.getAdminArea()+" "+address.getSubLocality()+" "+address.getThoroughfare().toString();  //지오코더 주소 자르기, 순서대로 도/행정구역/동 단위
        return adr;

        //return address.getAddressLine(0).toString()+"\n";
    }


    final LocationListener gpsLocationListener=new LocationListener() {
        @Override
        public void onLocationChanged(Location location) {
            String provider=location.getProvider(); //위치정보
            longitude=location.getLongitude(); //경도
            latitude=location.getLatitude(); //위도

            locationTxt.setText(getCurrentAddress(latitude,longitude));
            //Toast.makeText(WritePost.this,"위도: "+latitude+", 경도: "+longitude,Toast.LENGTH_LONG).show();
        }

        @Override
        public void onStatusChanged(String s, int i, Bundle bundle) {
        }

        @Override
        public void onProviderEnabled(String s) {
        }

        @Override
        public void onProviderDisabled(String s) {
        }
    };


    public void checkPermission(){
        String temp="";
        //read 권한 요청, permission 허가 안되어있으면 temp string에 권한 추가
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED){
            temp+=Manifest.permission.READ_EXTERNAL_STORAGE+" ";
        }
        //write권한 요청, permission 허가 안되어있으면 temp string에 권한 추가
        if(ContextCompat.checkSelfPermission(this,Manifest.permission.WRITE_EXTERNAL_STORAGE)!=PackageManager.PERMISSION_GRANTED){
            temp+=Manifest.permission.WRITE_EXTERNAL_STORAGE+" ";
        }
        /*
        if(ContextCompat.checkSelfPermission(this,Manifest.permission.CAMERA)!=PackageManager.PERMISSION_GRANTED) {
            temp += Manifest.permission.CAMERA + " ";
        }
        */

        if(ContextCompat.checkSelfPermission(this,Manifest.permission.ACCESS_FINE_LOCATION)!=PackageManager.PERMISSION_GRANTED){
            temp+=Manifest.permission.ACCESS_FINE_LOCATION+" ";
        }

        if(TextUtils.isEmpty(temp)==false){//temp에 내용이 있다면, 허가되지 않은 요청이 았다는 의미이므로
            ActivityCompat.requestPermissions(this,temp.trim().split(" "),1);//" "를 기준으로 권한요청
        }else{
            Toast.makeText(this,"어플리케이션 사용을 위해 갤러리, 위치 접근이 허가되어있습니다.",Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    //사용자의 권한에 대한 반응 처리 함수 (동의인지, 거부인지)
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1) {
            int length = permissions.length;
            for (int i = 0; i < length; i++) {
                if (grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                    //Log.i("Write Post permission", "권한 허용: " + permissions[i]);
                    Toast.makeText(this, "권한 사용 허가되었습니다.", Toast.LENGTH_SHORT).show();
                } else//사용자가 거부하면?
                {
                    Toast.makeText(this, "기능 사용을 위해 권한 동의가 필요합니다.", Toast.LENGTH_LONG).show();
                    //finish();
                }
            }
        }
    }






}

