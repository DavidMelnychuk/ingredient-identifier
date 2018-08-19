package app.jade.ingredient_identifier;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.ml.vision.FirebaseVision;
import com.google.firebase.ml.vision.cloud.label.FirebaseVisionCloudLabel;
import com.google.firebase.ml.vision.cloud.label.FirebaseVisionCloudLabelDetector;
import com.google.firebase.ml.vision.common.FirebaseVisionImage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class IdentifyPhotoActivity extends AppCompatActivity {

    private static final String IMAGE_URI = "imageUri";
    private Uri imageUri;
    private TextView imageLabel;
    private FirebaseVisionImage image;
    private List<FirebaseVisionCloudLabel> mFirebaseVisionCloudLabels = new ArrayList<>();
    private List<String> mFirebaseVisionCloudLabelsStrings = new ArrayList<>();
    private ListView mListView;

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
        mListView =  findViewById(R.id.lv_list_of_labels);
        imageLabel = findViewById(R.id.tv_image_label);


        FirebaseVisionCloudLabelDetector detector = FirebaseVision.getInstance().getVisionCloudLabelDetector();

        Task<List<FirebaseVisionCloudLabel>> result = detector.detectInImage(image)
                .addOnSuccessListener(new OnSuccessListener<List<FirebaseVisionCloudLabel>>() {
                    @Override
                    public void onSuccess(List<FirebaseVisionCloudLabel> firebaseVisionLabels) {


                        for (FirebaseVisionCloudLabel label : firebaseVisionLabels) {
                            String text = label.getLabel();
                            imageLabel.setText(text);
                            String entityId = label.getEntityId();
                            float confidence = label.getConfidence();
                            mFirebaseVisionCloudLabelsStrings.add(text + "---" + entityId + "---" + confidence);
                        }

                        ArrayAdapter<String> adapter = new ArrayAdapter<String>(IdentifyPhotoActivity.this,
                                android.R.layout.simple_list_item_1, //layout to use
                                mFirebaseVisionCloudLabelsStrings);//Item to be display
                        mListView.setAdapter(adapter);

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                    }
                });

    }


    public static Intent makeLaunchIntent(Context context, Uri imageUri) {
        Intent intent = new Intent(context, IdentifyPhotoActivity.class);
        intent.putExtra(IMAGE_URI, imageUri.toString());
        return intent;
    }
}
