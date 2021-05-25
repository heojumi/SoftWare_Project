package com.example.myapplication;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
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
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresPermission;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;


import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class WritePost extends AppCompatActivity {

    private final int GET_IMG_FROM_GALLERY = 200; //갤러리 사진 요청 request
    private final int GET_IMG_FROM_CAMERA = 400;
    private ImageView imageview;
    private EditText editText;
    private Uri photoUri;
    private Uri selectedImageUri;
    String photoImgName;
    private String currentPhotoPathl;
    TextView locationTxt;


    //GPSListener gpsListener;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_write_post);

            checkPermission();

        //본문 (content) 내용 string으로 바꿔오기
        editText = (EditText) findViewById(R.id.content);
        String mainText = editText.getText().toString();


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
        locationTxt=findViewById(R.id.locationText);
        final Geocoder geocoder=new Geocoder(this);
        locationbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ActivityCompat.checkSelfPermission(WritePost.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(WritePost.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    checkPermission();
                    return;
                }
                Location location = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                String provider=location.getProvider();
                double latitude=location.getLatitude();//위도
                double longitude=location.getLongitude();//경도

                String add=getCurrentAddress(latitude,longitude);

                //locationTxt.setText("위치정보: "+provider+"\n"+"위도: "+latitude+"\n"+"경도: "+longitude);
                locationTxt.setText(getCurrentAddress(latitude,longitude));
                //Toast.makeText(WritePost.this,"위도: "+latitude+", 경도: "+longitude,Toast.LENGTH_LONG).show();
                lm.requestLocationUpdates(LocationManager.GPS_PROVIDER,5000,100,gpsLocationListener);
                lm.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,5000,100,gpsLocationListener);

            }
        });

        //취소 버튼
        Button cancelButton=findViewById(R.id.goback);
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
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
            }
        });

        //완료 버튼:: db에 저장하기!
        Button submitButton=findViewById(R.id.submit);
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(WritePost.this,"업로드",Toast.LENGTH_SHORT).show();
                /*
                db에
                데이터 저장
                 */
                finish(); //db에 저장 후, 이전 액티비티로 돌아가기
            }
        });
    }//oncreate 끝


    @Override
    //사진 이미지 요청 들어오면 url 받아오는 동작
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==GET_IMG_FROM_GALLERY&&resultCode==RESULT_OK&&data!=null&&data.getData()!=null){

            selectedImageUri=data.getData();
            //url db에 저장
            Log.v("check","gallery uri: "+selectedImageUri.toString());
            imageview.setImageURI(selectedImageUri);
        }
        if(requestCode==GET_IMG_FROM_CAMERA){
            String a=photoUri.toString();
            Log.v("check","request start: "+a+"result code: "+resultCode);

            imageview.setImageURI(photoUri);
        }
    }


    private void imgDialog(){
        AlertDialog.Builder builder=new AlertDialog.Builder(WritePost.this);
        builder.setTitle("사진 업로드 방법 선택").setMessage("이미지를 업로드할 방법을 선택하세요: ");
        builder.setPositiveButton("갤러리", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Intent gallery=new Intent(Intent.ACTION_PICK);
                gallery.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,"image/*");
                startActivityForResult(gallery,GET_IMG_FROM_GALLERY);
            }
        });

        builder.setNegativeButton("카메라", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                String state=Environment.getExternalStorageState();
                if (Environment.MEDIA_MOUNTED.equals(state)) {
                    Log.v("check","state ok");

                    Intent cam = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    Log.v("check", "enter camera dialog");
                    //File f=new File(Environment.getExternalStorageDirectory(),"temp.jpg");
                    //cam.putExtra(MediaStore.EXTRA_OUTPUT,Uri.fromFile(F));
                    if (cam.resolveActivity(getPackageManager()) != null) {
                        File photo = null;
                        try {
                            photo = createImageFile();
                        }//새로운 file이 생성된 후, 해당 파일이 리턴된다.
                        catch (IOException ex) {
                            Toast.makeText(WritePost.this, "wrong createfile", Toast.LENGTH_LONG).show();

                        }
                        if (photo != null) {//제대로 photo 파일이 생성되었다면
                            Log.v("check", "photo not null");
                            try {
                                photoUri = FileProvider.getUriForFile(getApplicationContext(), "com.example.myapplication.fileProvider", photo);
                            } catch (IllegalArgumentException e) {
                                Log.v("check", "wrong uri, uri err");
                            }
                            //사진의 uri 가져오고
                            cam.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
                            String ss = photoUri.toString();

                            Log.v("check", "photo uri created" + ss);

                            if(Build.VERSION.SDK_INT>=24)
                                cam.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
                            else{
                                List<ResolveInfo> resolveInfoList=getPackageManager().queryIntentActivities(cam,PackageManager.MATCH_DEFAULT_ONLY);
                                for(ResolveInfo resinfo:resolveInfoList){
                                    String packageName = resinfo.activityInfo.packageName;
                                    grantUriPermission(packageName, photoUri, Intent.FLAG_GRANT_WRITE_URI_PERMISSION | Intent.FLAG_GRANT_READ_URI_PERMISSION);
                                }
                            }
                            startActivityForResult(cam, GET_IMG_FROM_CAMERA);
                        }
                    }
                }
            }
        });
        AlertDialog alertDialog=builder.create();
        alertDialog.show();
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
        return address.getAdminArea()+" "+address.getSubLocality()+" "+address.getThoroughfare().toString();  //지오코더 주소 자르기, 순서대로 도/행정구역/동 단위

        //return address.getAddressLine(0).toString()+"\n";
    }


    final LocationListener gpsLocationListener=new LocationListener() {
        @Override
        public void onLocationChanged(Location location) {
            String provider=location.getProvider(); //위치정보
            double longitude=location.getLongitude(); //경도
            double latitude=location.getLatitude(); //위도

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

        if(ContextCompat.checkSelfPermission(this,Manifest.permission.CAMERA)!=PackageManager.PERMISSION_GRANTED) {
            temp += Manifest.permission.CAMERA + " ";
        }

        if(ContextCompat.checkSelfPermission(this,Manifest.permission.ACCESS_FINE_LOCATION)!=PackageManager.PERMISSION_GRANTED){
            temp+=Manifest.permission.ACCESS_FINE_LOCATION+" ";
        }

        if(TextUtils.isEmpty(temp)==false){//temp에 내용이 있다면, 허가되지 않은 요청이 았다는 의미이므로
            ActivityCompat.requestPermissions(this,temp.trim().split(" "),1);//" "를 기준으로 권한요청
        }else{
            Toast.makeText(this,"어플리케이션 사용을 위해 카메라, 갤러리, 위치 접근이 허가되어있습니다.",Toast.LENGTH_SHORT).show();
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

