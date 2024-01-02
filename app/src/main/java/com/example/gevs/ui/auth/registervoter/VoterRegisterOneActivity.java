package com.example.gevs.ui.auth.registervoter;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Toast;

import com.example.gevs.R;
import com.example.gevs.databinding.ActivityVoterRegisterOneBinding;
import com.example.gevs.ui.auth.loginvoter.VoterLoginActivity;
import com.google.android.material.datepicker.CalendarConstraints;
import com.google.android.material.datepicker.DateValidatorPointBackward;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;

import java.text.SimpleDateFormat;
import java.util.Date;

public class VoterRegisterOneActivity extends AppCompatActivity {

    ActivityVoterRegisterOneBinding binding;
    Long dateOfBirth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_voter_register_one);

        String[] constituency = getResources().getStringArray(R.array.constituency);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(VoterRegisterOneActivity.this, R.layout.list_item, constituency);
        binding.registerConstituencyEditText.setAdapter(adapter);

        binding.registerLoginTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(VoterRegisterOneActivity.this, VoterLoginActivity.class));
                finish();
            }
        });

        binding.registerNextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getData();
            }
        });

        binding.registerDobInputText.setEndIconOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePicker(binding.registerDobEditText);
            }
        });

    }

    public void getData() {
        //Validate inputs
        String fullName = binding.registerFullnameEditText.getText().toString().trim();
        String dateOfBirth = binding.registerDobEditText.getText().toString();
        String constituency = binding.registerConstituencyEditText.getText().toString();

        if (!TextUtils.isEmpty(fullName) && !TextUtils.isEmpty(dateOfBirth) && !TextUtils.isEmpty(constituency)) {
            Intent i = new Intent(VoterRegisterOneActivity.this, VoterRegisterTwoActivity.class);
            i.putExtra("FullName", fullName);
            i.putExtra("DateOfBirth", this.dateOfBirth);
            i.putExtra("Constituency", constituency);
            startActivity(i);
        } else {
            Toast.makeText(VoterRegisterOneActivity.this, "Please fill in all details", Toast.LENGTH_SHORT).show();
        }

    }

    public void showDatePicker(AutoCompleteTextView view) {
        MaterialDatePicker.Builder<Long> datePicker = MaterialDatePicker.Builder.datePicker();
        CalendarConstraints.Builder constraintsBuilder = new CalendarConstraints.Builder().setValidator(DateValidatorPointBackward.now());
        datePicker.setTitleText("Select Date of Birth");
        datePicker.setSelection(MaterialDatePicker.todayInUtcMilliseconds());
        datePicker.setCalendarConstraints(constraintsBuilder.build());
        MaterialDatePicker<Long> materialDatePicker = datePicker.build();
        materialDatePicker.show(getSupportFragmentManager(), "DateOfBirth");

        materialDatePicker.addOnPositiveButtonClickListener(new MaterialPickerOnPositiveButtonClickListener<Long>() {
            @Override
            public void onPositiveButtonClick(Long selection) {
                dateOfBirth = selection;
                view.setText(longToDate(dateOfBirth));
            }
        });

    }

    public String longToDate(Long date) {
        Date d = new Date(date);
        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
        return format.format(d);
    }


}