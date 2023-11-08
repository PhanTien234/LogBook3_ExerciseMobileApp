package com.example.logbook3.Activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
import androidx.room.Room;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.logbook3.Database.AppDatabase;
import com.example.logbook3.Models.User;
import com.example.logbook3.R;

import java.lang.reflect.Field;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import pl.droidsonroids.gif.GifImageView;

public class MainActivity extends AppCompatActivity {
    TextView dateOfBirth;
    private GifImageView imageView;
    private List<Integer> imageResources;
    private int currentImageIndex = -1;
    private AppDatabase appDatabase;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        dateOfBirth = findViewById(R.id.date_picker);
        imageView = findViewById(R.id.imageView);
        imageResources = getImageResources();
        dateOfBirth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogFragment newFragment = new DatePickerFragment();
                newFragment.show(getSupportFragmentManager(), "datePicker");
            }
        });
        appDatabase = Room.databaseBuilder(getApplicationContext(), AppDatabase.class, "users")
                .allowMainThreadQueries()
                .build();
        Button saveDetailsButton = findViewById(R.id.saveDetailsButton);

        saveDetailsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveDetails();
            }
        });
        Button viewDetailsButton = findViewById(R.id.viewDetailsButton);
        viewDetailsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                viewDetails();
            }
        });
    }

    // Method to update the displayed image
    private void updateImage() {
        int imageResourceId = imageResources.get(currentImageIndex);
        imageView.setImageResource(imageResourceId);
    }
    // Button click handler for displaying the next image
    public void onNextButtonClick(View view) {
        if (currentImageIndex < imageResources.size() - 1) {
            currentImageIndex++;
        } else {
            currentImageIndex = 0;
        }
        updateImage();
    }
    // Button click handler for displaying the previous image
    public void onPreviousButtonClick(View view) {
        if (currentImageIndex > 0) {
            currentImageIndex--;
        } else {
            currentImageIndex = imageResources.size() - 1;
        }
        updateImage();
    }

    // Method to dynamically get the list of image resources from the drawable folder
    private List<Integer> getImageResources() {
        List<Integer> imageResources = new ArrayList<>();
        Field[] drawables = R.drawable.class.getFields();

        TypedValue typedValue = new TypedValue();
        for (Field field : drawables) {
            try {
                getResources().getValue(field.getInt(null), typedValue, true);
                if (typedValue.string.toString().endsWith(".jpg")
                        || typedValue.string.toString().endsWith(".png")
                        || typedValue.string.toString().endsWith(".gif")) {
                    int resourceId = field.getInt(null);
                    imageResources.add(resourceId);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return imageResources;
    }
    public static class DatePickerFragment extends DialogFragment implements
            DatePickerDialog.OnDateSetListener {
        @NonNull
        @Override
        public Dialog onCreateDialog(@Nullable Bundle savedInstanceState)
        {
            LocalDate d = LocalDate.now();
            int year = d.getYear();
            int month = d.getMonthValue();
            int day = d.getDayOfMonth();
            return new DatePickerDialog(getActivity(), this, year, --month, day);}
        @Override
        public void onDateSet(DatePicker datePicker, int year, int month, int day){
            LocalDate dob = LocalDate.of(year, ++month, day);
            ((MainActivity)getActivity()).updateDateOfBirth(dob);
        }
    }

    public void updateDateOfBirth(LocalDate dob){
        TextView dobControl = findViewById(R.id.date_picker);
        // Format the date in "dd/MM/yyyy" format
        String formattedDate = dob.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
        dobControl.setText(formattedDate);
    }

    private  void viewDetails(){
        Intent intent = new Intent(this, DetailsActivity.class);
        startActivity(intent);
    }


    private void saveDetails() {
        EditText nameTxt = findViewById(R.id.nameText);
        TextView dateOfBirthTextView  = findViewById(R.id.date_picker);
        EditText emailTxt = findViewById(R.id.emailText);
        EditText phoneInput = findViewById(R.id.phoneText);

        String name = nameTxt.getText().toString();
        String dob = dateOfBirthTextView.getText().toString();
        String email = emailTxt.getText().toString();
        String phoneNumber = phoneInput.getText().toString();

        if(name.trim().isEmpty() || dob.trim().isEmpty() || email.trim().isEmpty() || phoneNumber.trim().isEmpty()){
            Toast.makeText(this, "Please complete all Field before save",
                    Toast.LENGTH_LONG
            ).show();
            return;
        }
        // Validate email using regex pattern
        String emailPattern = "[a-zA-Z0-9._-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,4}";
        if (!email.matches(emailPattern)) {
            Toast.makeText(this, "Please enter a valid email address",
                    Toast.LENGTH_LONG
            ).show();
            return;
        }
        User user = new User();
        user.name = name;
        user.dob = dob;
        user.email = email;
        user.phoneNumber = phoneNumber;
        user.imageResourceId = imageResources.get(currentImageIndex); //Set the image resource ID

        appDatabase.userDao().insertUser(user);

        new AlertDialog.Builder(this)
                .setTitle("Success")
                .setMessage(
                        "New Contact is saved"
                )
                .setNeutralButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                    }
                })
                .show();
    }
}