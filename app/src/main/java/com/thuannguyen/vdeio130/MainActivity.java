package com.thuannguyen.vdeio130;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    Database database;
    ListView lstCv;
    ArrayList<CongViec> arrayCV;
    CongViecAdepter adepter;
    SharedPreferences sharedPreferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        sharedPreferences = getSharedPreferences("thongtin",MODE_PRIVATE);
        lstCv = (ListView) findViewById(R.id.listdto);
        arrayCV = new ArrayList<>();
        adepter = new CongViecAdepter(this, R.layout.dong_cong_viec, arrayCV);
        lstCv.setAdapter(adepter);
        //Tạo database ghi chú
        database = new Database(this, "ghichu.sqlite",null,1);
        //Tạo bảng
        database.QueryData("CREATE TABLE IF NOT EXISTS CongViec(Id INTEGER PRIMARY KEY AUTOINCREMENT, TenCV VARCHAR(200))");
        //insert data
        //database.QueryData("INSERT INTO CongViec VALUES(null, 'Viết ứng dụng ghi chú')");
        //select data
        GetDataCongViec();
    }

    public void  DialogXoaCV(String tencv, final int id){
        AlertDialog.Builder dialogXoa = new AlertDialog.Builder(this);
        dialogXoa.setMessage("Bạn có muốn xóa công việc" + tencv + " không");
        dialogXoa.setPositiveButton("Có", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                database.QueryData("DELETE FROM CongViec WHERE Id = '"+id+"' ");
                Toast.makeText(MainActivity.this, "Đã Xóa" +tencv, Toast.LENGTH_SHORT).show();
                GetDataCongViec();
            }
        });
        dialogXoa.setNegativeButton("Không", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        dialogXoa.show();
    }
    public void DialogSuaCV(String ten, final int id){
        Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_sua);
        EditText edttencv = (EditText) dialog.findViewById(R.id.edittencvedit);
        Button btnxacnhan = (Button) dialog.findViewById(R.id.buttonxacnhan);
        Button btnhuy1 = (Button) dialog.findViewById(R.id.buttonhuy1);
        edttencv.setText(ten);
        btnxacnhan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String tenMoi = edttencv.getText().toString().trim();
                database.QueryData("UPDATE CongViec SET TenCV = '"+tenMoi+"' WHERE Id ='"+id+"'");
                Toast.makeText(MainActivity.this, "Đã cập nhập", Toast.LENGTH_SHORT).show();
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("ten",tenMoi);
                dialog.dismiss();
                GetDataCongViec();
            }
        });
        btnhuy1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }
    private  void GetDataCongViec(){
        Cursor dataCongViec = database.GetData("SELECT * FROM CongViec");
        arrayCV.clear();
        while (dataCongViec.moveToNext()){
            String ten = dataCongViec.getString(1);
            int id = dataCongViec.getInt(0);
            arrayCV.add(new CongViec(id, ten));
        }
        adepter.notifyDataSetChanged();
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.add_congviec, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.menuAdd){
            DialogThem();
        }
        return super.onOptionsItemSelected(item);
    }
    private void DialogThem(){
        Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_them_cong_viec);
        EditText edtTen = dialog.findViewById(R.id.edittextTenCV);
        Button btnthem = (Button) dialog.findViewById(R.id.buttonthem);
        Button btnhuy = (Button) dialog.findViewById(R.id.buttonhuy);
        btnhuy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        btnthem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String tencv = edtTen.getText().toString();
                if (tencv.equals("")){
                    Toast.makeText(MainActivity.this, "Vui lòng nhập tên công việc!", Toast.LENGTH_SHORT).show();
                }else {
                    database.QueryData("INSERT INTO CongViec VALUES(null,'"+tencv+"')");
                    Toast.makeText(MainActivity.this,"Đã thêm", Toast.LENGTH_SHORT).show();
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("tencv",tencv);
                    dialog.dismiss();
                    GetDataCongViec();
                }
            }
        });
        dialog.show();
    }
}