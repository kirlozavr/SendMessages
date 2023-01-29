package com.example.sendmessages.Sevice;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.ActivityResultRegistry;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;

import com.example.sendmessages.Common.DateFormat;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.time.ZonedDateTime;

/**
 * Класс отвечает за получения доступа к галерее,
 * сохранении картинки перед отправкой и отправка картинки в БД
 **/
public class ImageService {

    private static final String KEY = "key";
    private ActivityResultRegistry resultRegistry;
    private ActivityResultLauncher<String> resultLauncher;

    public ImageService(
            @NonNull ActivityResultRegistry resultRegistry,
            ImageView view,
            View layout
    ) {
        this.resultRegistry = resultRegistry;
        this.resultLauncher = this.resultRegistry
                .register(
                        KEY,
                        new ActivityResultContracts.GetContent(),
                        new ActivityResultCallback<Uri>() {
                            @Override
                            public void onActivityResult(Uri result) {
                                if (result != null) {
                                    view.setImageURI(result);
                                    layout.setVisibility(View.VISIBLE);
                                } else {

                                }
                            }
                        }
                );
    }

    /**
     * Метод отвечает за конвертацию изображения в массив байтов
     **/
    private byte[] convertImageToBytesArray(ImageView view) {
        Bitmap bitmap = ((BitmapDrawable) view.getDrawable()).getBitmap();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        return baos.toByteArray();
    }

    /**
     * Метод отвечает за запись изображения в БД
     **/
    public void setImageToDataBase(
            MessageService messageService,
            FrameLayout layout,
            ImageView view,
            ProgressBar progressBar
    ) {

        /** Формируется ссылка на изображение **/
        StorageReference storageReference = FirebaseStorage
                .getInstance()
                .getReference()
                .child("ImageMessage/" + DateFormat.getFormatToDataBase().format(ZonedDateTime.now()) + "_image");

        /** Запись изображения в БД **/
        UploadTask uploadTask = storageReference
                .putBytes(convertImageToBytesArray(view));

        /** Получение прогресса загрузки изображения в БД **/
        uploadTask
                .addOnProgressListener(
                        new OnProgressListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {
                                int progress = (int) ((100 * snapshot.getBytesTransferred()) / snapshot.getTotalByteCount());
                                view.setAlpha(0.4f);
                                progressBar.setVisibility(View.VISIBLE);
                                progressBar.setProgress(progress, false);
                            }
                        }).addOnSuccessListener(
                        new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                view.setAlpha(1.0f);
                                progressBar.setVisibility(View.INVISIBLE);
                                clearImageView(view, layout);
                            }
                        }
                );

        Task<Uri> task = uploadTask
                .continueWithTask(
                        new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                            @Override
                            public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                                if (!task.isSuccessful()) {
                                    throw task.getException();
                                }
                                /** Получение ссылки на изображение **/
                                return storageReference.getDownloadUrl();
                            }
                        }).addOnCompleteListener(
                        new OnCompleteListener<Uri>() {
                            @Override
                            public void onComplete(@NonNull Task<Uri> task) {
                                /** Запись ссылки на изображение в сущность сообщения **/
                                if (task.isSuccessful()) {
                                    messageService.setUriImage(task.getResult());
                                    messageService.addMessagesToDataBase();
                                }
                            }
                        });
    }

    /**
     * Метод отвечающий за удаление изображения из БД
     **/
    public void deleteImageFromDataBase(
            @NonNull Uri uriImage
    ) {

        StorageReference storageReference = FirebaseStorage
                .getInstance()
                .getReferenceFromUrl(uriImage.toString());

        storageReference
                .delete()
                .addOnSuccessListener(
                        new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                            }
                        }
                ).addOnFailureListener(
                        new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {

                            }
                        }
                );
    }

    /**
     * Метод отвечающий за очищение ImageView который хранит в себе изображение полученное из галереи
     **/
    public void clearImageView(
            @NonNull ImageView view,
            @NonNull View layout
    ) {
        view.setImageDrawable(null);
        view.clearColorFilter();
        layout.setVisibility(View.INVISIBLE);
    }

    /**
     * Метод указывающий Android, что необходимо показать только картинки
     **/
    public void launch() {
        resultLauncher.launch("image/*");
    }

}
