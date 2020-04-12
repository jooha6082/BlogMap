package com.juha.blogmap;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.widget.Toast;

public class checkPermissionsActivity extends AppCompatActivity {

    /*
    권한 체크 방법
    1. 사용자의 스마트폰이 마시멜로우(23) 이상 버전인지 이하 버전인지 체크
        -> 마시멜로우 이상 버전일때는 2번 부터 진행
        -> 마시멜로우 이하 버전일때는 7번 부터 진행
    2. 사용자가 해당 권한에 대해 거부를 했는지 안했는지 확인(현재 상태)
        -> 권한을 수락(허용)한 상태라면 MainActivity 실행(7번 진행)
        -> 권한을 수락하지 않았다면(거부되어있는 상태라면) 3번 부터 진행
    3. 사용자가 권한에 대해 거부한 이력이 있는지 확인
        -> 거부한 적이 있다면 최초 실행이 아님 -> 4번부터 진행
        -> 거부를 한 적이 없다면 최초 실행이므로 5번부터 진행
    4. 사용자가 최초 실행이 아니고 권한에 대해 거부했다면
        개발자는 사용자에게 이 권한이 왜 필요한지에 대해 자세한 설명을 제공한 후 5번 진행
    5. requestPermissions() 를 통해서 권한 체크 다이얼러그 실행
    6. onRequestPermissionsResult() 를 통해 권한 체크 여부 확인
        -> 권한 수락이 이루어지면 7번 진행
        -> 권한이 거부되면 앱 종료, 혹은 그 기능을 못쓰게 하고 앱은 실행
    7. 앱 실행
     */
    @Override
    protected void onResume() {
        super.onResume();

        //사용자의 OS 버전이 마시멜로우 이상인지 판별
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            //사용자의 단말에 부여된 권한 중 위치가져오기(ACCESS_FIND_LOCATION)의 권한
            //허가 여부를 가져온다.
            //checkSelfPermission(권한);
            //허가 -> PERMISSION_GRANTED
            //거부 -> PERMISSION_DENIED
            int permissionCheck = checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION);

            //현재 어플리케이션이 권한에 대해 거부되었는지 확인
            if (permissionCheck == PackageManager.PERMISSION_DENIED){
                //권한을 거부한 적이 있다면 true -> 최초 실행이 아님
                //권한을 거부한 적이 없다면 false -> 최초 실행임
                if (shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_FINE_LOCATION)){
                    requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1000);
                } else {
                    requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1000);
                }
            } else {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
                finish();
            }
        } else {
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(intent);
            finish();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_permissions);
    }

    //요청에 대한 응답을 처리하는 부분
    //int requestCode : 사용자 요청 코드(1000)
    //String[] permissions : 사용자가 요청한 권한들(개발자)
    //int[] grantedResult : 혀용권한에 대한 응답들(인덱스별로 매칭)
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == 1000){
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                if (ActivityCompat.checkSelfPermission(this,
                        Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_DENIED){

                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(intent);
                    finish();

                } else {
                    Toast.makeText(this, "권한을 거부하셨습니다.", Toast.LENGTH_SHORT).show();
                }

            } else {
                Toast.makeText(this, "권한을 거부하셨습니다.", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
