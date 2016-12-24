package com.android.studentslist.activities;

import android.app.AlertDialog;
import android.bluetooth.BluetoothAdapter;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.studentslist.BroadcastToaster;
import com.android.studentslist.R;
import com.android.studentslist.api.GitService;
import com.android.studentslist.api.GitUser;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class GitActivity extends AppCompatActivity implements View.OnClickListener {
    final private String LOG_SPAWNER = getClass().getSimpleName();

    final BroadcastToaster broadcastToaster = new BroadcastToaster();
    static final int ACTION_REQUEST_GALLERY = 0;
    static final int ACTION_REQUEST_CAMERA = 1;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.git_activity);
        findViewById(R.id.image_git).setOnClickListener(this);
    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

        String path = getIntent().getData().getPath();
        Call<GitUser> userCall = new GitService().api.getUserInfo(path.substring(1, path.length()));
        userCall.enqueue(new Callback<GitUser>() {
            @Override
            public void onResponse(Call<GitUser> call, Response<GitUser> response) {
                GitUser user = response.body();
                Picasso.with(getBaseContext()).load(user.getImageUrl()).into((ImageView) findViewById(R.id.image_git));
                ((TextView) findViewById(R.id.text_git)).setText(user.getName());
                ((TextView) findViewById((R.id.login_git))).setText("R.id.".concat(user.getLogin()));
            }

            @Override
            public void onFailure(Call<GitUser> call, Throwable t) {
            }
        });
    }



    @Override
    protected void onResume() {
        super.onResume();
        this.registerReceiver(broadcastToaster, new IntentFilter(BluetoothAdapter.ACTION_STATE_CHANGED));
    }

    @Override
    protected void onPause() {
        super.onPause();
        this.unregisterReceiver(broadcastToaster);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.image_git: {
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("Choose Image Source");
                builder.setItems(new CharSequence[]{"Gallery", "Camera"},
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                switch (which) {
                                    case 0:
                                        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                                        intent.setType("image/*");
                                        Intent chooser = Intent.createChooser(intent, "Choose a Picture");
                                        startActivityForResult(chooser, ACTION_REQUEST_GALLERY);
                                        break;
                                    case 1:
                                        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                                        startActivityForResult(takePictureIntent, ACTION_REQUEST_CAMERA);
                                        break;
                                }
                            }
                        });
                builder.show();
                break;
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            ImageView imageView = (ImageView) findViewById(R.id.image_git);
            switch (requestCode) {
                case ACTION_REQUEST_GALLERY:
                    Picasso.with(getBaseContext()).load(data.getData()).into(imageView);
                    break;
                case ACTION_REQUEST_CAMERA:
                    imageView.setImageBitmap((Bitmap) data.getExtras().get("data"));
                    break;
            }
        }
    }

}