package app.jade.ingredient_identifier.ui;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.ml.vision.FirebaseVision;
import com.google.firebase.ml.vision.cloud.label.FirebaseVisionCloudLabel;
import com.google.firebase.ml.vision.cloud.label.FirebaseVisionCloudLabelDetector;
import com.google.firebase.ml.vision.common.FirebaseVisionImage;

import java.io.File;
import java.io.IOException;
import java.util.List;

import app.jade.ingredient_identifier.R;

public class IdentifyPhotoActivity extends AppCompatActivity {

    private static final String IMAGE_URI = "imageUri";
    private Uri imageUri;
    private TextView imageLabelTextView;
    private FirebaseVisionImage image;
    private ImageView capturedImage;
    private Button viewRecipesBtn;
    private ProgressBar mProgressBar;
    private String imageLabel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_identify_photo);
        imageUri = Uri.parse(getIntent().getStringExtra(IMAGE_URI));
        try {
            image = FirebaseVisionImage.fromFilePath(this, imageUri);
        } catch (IOException e) {
            e.printStackTrace();
        }
        imageLabelTextView = findViewById(R.id.tv_image_label);
        capturedImage = findViewById(R.id.iv_image);
        viewRecipesBtn = findViewById(R.id.btn_recipes);
        mProgressBar = findViewById(R.id.progressBar);


        FirebaseVisionCloudLabelDetector detector = FirebaseVision.getInstance().getVisionCloudLabelDetector();

        Task<List<FirebaseVisionCloudLabel>> result = detector.detectInImage(image)
                .addOnSuccessListener(new OnSuccessListener<List<FirebaseVisionCloudLabel>>() {
                    @Override
                    public void onSuccess(List<FirebaseVisionCloudLabel> firebaseVisionLabels) {

                        FirebaseVisionCloudLabel labelWithHighestConfidence = firebaseVisionLabels.get(0);

                        imageLabel = labelWithHighestConfidence.getLabel();

                        imageLabelTextView.setText(imageLabel + ", Confidence: " + (100 * labelWithHighestConfidence.getConfidence()) + "%");

                        setUpImageView();

                        updateUI();

                        setUpViewRecipesButton();


                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                    }
                });

    }


    private void setUpImageView() {
        File imgFile = new File(imageUri.getPath());
        if (imgFile.exists()) {
            capturedImage.setImageURI(imageUri);
        }
    }

    private void updateUI() {
        mProgressBar.setVisibility(View.GONE);
        imageLabelTextView.setVisibility(View.VISIBLE);
        capturedImage.setVisibility(View.VISIBLE);
        viewRecipesBtn.setVisibility(View.VISIBLE);
    }

    private void setUpViewRecipesButton() {
        viewRecipesBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = GetRecipesActivity.makeLaunchIntent(IdentifyPhotoActivity.this, imageLabel);
                startActivity(intent);
            }
        });
    }


    public static Intent makeLaunchIntent(Context context, Uri imageUri) {
        Intent intent = new Intent(context, IdentifyPhotoActivity.class);
        intent.putExtra(IMAGE_URI, imageUri.toString());
        return intent;
    }
}
