package com.example.demofilesystem;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.io.File;

public class MainActivity extends AppCompatActivity {
    //magic numbers bad, maak constante aan
    final int IMAGE_REQUEST = 2112;
    private ImageView profileIV;
    private File currentImage;
    private View.OnClickListener profileListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            dispatchTakePicture();
        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == IMAGE_REQUEST && resultCode == RESULT_OK) {
            Picasso.get()
                    .load(currentImage)
                    .placeholder(R.mipmap.ic_launcher_round)
                    .resize(200, 200)
                    .centerCrop()
                    .into(profileIV);
        }
    }

    private void dispatchTakePicture() {
        Intent pictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        //eerst kijken of er camera bestaat, is er een activity die kan omgaan met de genoemde action
        if(pictureIntent.resolveActivity(getPackageManager()) != null){
            currentImage = createImageFile();
            Uri imageUri = FileProvider.getUriForFile(getApplicationContext(), "com.example.demofilesystem.fileprovider", currentImage);
            pictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
            startActivityForResult(pictureIntent, IMAGE_REQUEST);
        }
    }
    private File createImageFile(){
        String fileName = "/"+System.currentTimeMillis()+"profilepicture.jpg";
        File storageDir = getFilesDir();
        return new File(storageDir + fileName);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        profileIV = findViewById(R.id.iv_profile_picture);
        profileIV.setOnClickListener(profileListener);
    }
}
