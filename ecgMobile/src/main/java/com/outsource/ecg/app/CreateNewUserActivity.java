package com.outsource.ecg.app;

import com.outsource.ecg.R;
import com.outsource.ecg.defs.ECGUser;
import com.outsource.ecg.defs.ECGUserManager;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.Toast;

/**
 * This class mainly designed for guide user to create a new user to the current ECG system
 * The layout of "new_user" contains a DatePicker for inputting birthday,
 * an EditText for name, an radio group for selecting gender,
 * a button for committing input
 * @author faywong
 *
 */
public class CreateNewUserActivity extends Activity {

	DatePicker mDatePicker;
	String mGender = "male";
	EditText mNameInput;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.new_user);
		mNameInput = (EditText)findViewById(R.id.name);
		Button commitButton = (Button) findViewById(R.id.commit_input);
		commitButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				String birthday = String.valueOf(mDatePicker.getYear()) + "-"
						+ String.valueOf(mDatePicker.getMonth() + 1);
				String name = mNameInput.getText().toString();
				if (name.isEmpty()) {
					Toast.makeText(CreateNewUserActivity.this, getString(R.string.name_item_empty), Toast.LENGTH_SHORT).show();
					return;
				}
				try {
					ECGUserManager.Instance().addUser(
							new ECGUser(name, mGender, birthday, 0.0));
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					Toast.makeText(CreateNewUserActivity.this, getString(R.string.create_new_user_failed), Toast.LENGTH_LONG).show();
					setResult(Activity.RESULT_CANCELED); 
					finish();
				}
				setResult(Activity.RESULT_OK); 
				finish();
			}
		});
		mDatePicker = (DatePicker) findViewById(R.id.date_picker);
		mDatePicker.init(1987, 11, 24, null);
		RadioGroup rg = (RadioGroup) findViewById(R.id.gender_group);
		rg.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				// TODO Auto-generated method stub
				if (checkedId == R.id.male_radio) {
					CreateNewUserActivity.this.mGender = getString(R.string.male_label);
				}
				if (checkedId == R.id.female_radio) {
					CreateNewUserActivity.this.mGender = getString(R.string.female_label);
				}
			}

		});
	}

	@Override
	protected void onRestoreInstanceState(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onRestoreInstanceState(savedInstanceState);
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		// TODO Auto-generated method stub
		super.onSaveInstanceState(outState);
	}

}
