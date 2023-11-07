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

import java.time.LocalDate;

public class MainActivity extends AppCompatActivity {
    TextView dateOfBirth;
    private AppDatabase appDatabase;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        dateOfBirth = findViewById(R.id.date_picker);

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
        dobControl.setText(dob.toString());
    }

    private  void viewDetails(){
        Intent intent = new Intent(this, DetailsActivity.class);
        startActivity(intent);
    }

    private boolean isValidEmail(String email) {
        String emailPattern = "[a-zA-Z0-9._-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,4}";
        return email.matches(emailPattern);
    }

    private void saveDetails() {
        EditText nameTxt = findViewById(R.id.nameText);
        TextView dateOfBirthTextView  = findViewById(R.id.date_picker);
        EditText emailTxt = findViewById(R.id.emailText);
        RadioGroup radioGroup = findViewById(R.id.radioGroup);
        int selectedRadioButtonId = radioGroup.getCheckedRadioButtonId();
        RadioButton selectedRadioButton = findViewById(selectedRadioButtonId);
        String imageTag;
        if (selectedRadioButton != null) {
            imageTag = (String) selectedRadioButton.getTag();
        } else {

            Toast.makeText(this, "Please complete all Field before save",
                    Toast.LENGTH_LONG
            ).show();
            return;
        }
        String packageName = getPackageName();
        int imageValue = getResources().getIdentifier(imageTag, "drawable", packageName);


        String name = nameTxt.getText().toString();
        String dob = dateOfBirthTextView.getText().toString();
        String email = emailTxt.getText().toString();

        if(name.trim().isEmpty() || dob.trim().isEmpty() || email.trim().isEmpty()){
            Toast.makeText(this, "Please complete all Field before save",
                    Toast.LENGTH_LONG
            ).show();
            return;
        }
        if (!isValidEmail(email)) {
            Toast.makeText(this, "Please enter a valid email address",
                    Toast.LENGTH_LONG
            ).show();
            return;
        }

        User user = new User();
        user.name = name;
        user.dob = dob;
        user.email = email;
        user.image = imageValue;

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