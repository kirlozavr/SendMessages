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

import com.example.sendmessages.General.DateFormat;
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

    private byte[] convertImageToBytesArray(ImageView view) {
        Bitmap bitmap = ((BitmapDrawable) view.getDrawable()).getBitmap();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        return baos.toByteArray();
    }

    public void setImageToDataBase(
            MessageService messageService,
            FrameLayout layout,
            ImageView view,
            ProgressBar progressBar
    ) {

        StorageReference storageReference = FirebaseStorage
                .getInstance()
                .getReference()
                .child("ImageMessage/" + DateFormat.getFormatToDataBase().format(ZonedDateTime.now()) + "_image");

        UploadTask uploadTask = storageReference
                .putBytes(convertImageToBytesArray(view));

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
                                return storageReference.getDownloadUrl();
                            }
                        }).addOnCompleteListener(
                        new OnCompleteListener<Uri>() {
                            @Override
                            public void onComplete(@NonNull Task<Uri> task) {
                                if (task.isSuccessful()) {
                                    messageService.setUriImage(task.getResult());
                                    messageService.addMessagesToDataBase();
                                }
                            }
                        });
    }

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

    public void clearImageView(
            @NonNull ImageView view,
            @NonNull View layout
    ) {
        view.setImageDrawable(null);
        view.clearColorFilter();
        layout.setVisibility(View.INVISIBLE);
    }

    public void launch() {
        resultLauncher.launch("image/*");
    }

}
