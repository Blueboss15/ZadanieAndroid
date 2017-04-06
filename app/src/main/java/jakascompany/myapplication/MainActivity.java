package jakascompany.myapplication;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Locale;

public class MainActivity extends AppCompatActivity{

    private static final String PREFERENCES_NAME = "myPreferences";
    private static final String PREFERENCES_TEXT_FIELD = "textField";
    private static final String PREFERENCES_TEXT_FIELD2 = "textField2";
    private static final String PREFERENCES_TEXT_FIELD3 = "textField3";
    private static final String PREFERENCES_TEXT_FIELD4 = "textField4";
    private static final String PREFERENCES_FLOAT_FIELD = "floatField";
    private static final String SWITCH_STATUS = "switch_status";
    private SharedPreferences preferences;
    private float check_BMI_status;
    private float weight;
    private float height;
    private EditText editText;
    private EditText editText2;
    private Switch mySwitch;
    private TextView textView;
    private TextView textView2;
    private TextView textView5;
    private String message;
    private String message2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        preferences = getSharedPreferences(PREFERENCES_NAME, Activity.MODE_PRIVATE);
        editText = (EditText) findViewById(R.id.editText01);
        editText2 = (EditText) findViewById(R.id.editText02);
        textView = (TextView) findViewById(R.id.textView02);
        textView2 = (TextView) findViewById(R.id.textView03);
        mySwitch = (Switch) findViewById(R.id.mySwitch);
        textView5 = (TextView) findViewById(R.id.textView05);
        mySwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView,
                                         boolean isChecked) {
                TextView textView1 = (TextView) findViewById(R.id.textView01);
                TextView textView4 = (TextView) findViewById(R.id.textView04);

                if (isChecked) {
                    textView1.setText(R.string.Text11);
                    textView4.setText(R.string.Text14);
                    mySwitch.setText(R.string.button_switch_changed);

                    try {
                        getValues();
                        weight = weight * 2.2046f;
                        height = height * 39.37f;
                        setValues();
                    } catch (NumberFormatException e) {
                    }
                } else {
                    textView1.setText(R.string.Text01);
                    textView4.setText(R.string.Text04);
                    mySwitch.setText(R.string.button_switch);
                    try {
                        getValues();
                        weight = weight / 2.2046f;
                        height = height / 39.37f;
                        setValues();
                    } catch (NumberFormatException e) {
                    }
                }

            }
        });

        editText.addTextChangedListener(new TextValidator(editText) {

            @Override
            public void validate(TextView textView, String text) {
                if (text.isEmpty()) {
                    editText.setError(getResources().getString(R.string.EmptyFields));
                }
            }


        });
        editText2.addTextChangedListener(new TextValidator(editText2) {
            @Override
            public void validate(TextView textView, String text) {
                if (text.isEmpty()) {
                    editText2.setError(getResources().getString(R.string.EmptyFields));
                }
            }
        });
        editText.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            public void afterTextChanged(Editable s) {
                textView.setText(getResources().getString(R.string.Empty));
                textView5.setText(getResources().getString(R.string.Empty));
            textView2.setText(getResources().getString(R.string.Empty));
            }
        });

        editText2.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            public void afterTextChanged(Editable s) {
                textView.setText("");
                textView5.setText("");
                textView2.setText(getResources().getString(R.string.Empty));
            }
        });

        restoreData();
        setBMIColor();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.save:
                if(checkDataCorrect()) {
                saveData();
                showToast(getResources().getString(R.string.DataSaved));}
                return true;

            case R.id.share:
                if(checkDataCorrect()) {
                    String share_massage = getResources().getString(R.string.StringBmi) + textView5.getText().toString();
                    Intent sendIntent = new Intent();
                    sendIntent.setAction(Intent.ACTION_SEND);
                    sendIntent.putExtra(Intent.EXTRA_TEXT, share_massage);
                    sendIntent.setType("text/plain");
                    startActivity(sendIntent);
                }
                return true;

            case R.id.credits:
                Intent intent = new Intent(this, DisplayMessageActivity.class);
                startActivity(intent);
                return true;

            default:
                return super.onOptionsItemSelected(item);

        }
    }
        @Override
        public void onSaveInstanceState(Bundle savedInstanceState) {
            super.onSaveInstanceState(savedInstanceState);

            savedInstanceState.putString("Result",textView.getText().toString());
            savedInstanceState.putString("Result2", textView5.getText().toString());
            savedInstanceState.putFloat("BMI_result", check_BMI_status);
            // etc.
        }

        @Override
        public void onRestoreInstanceState(Bundle savedInstanceState) {
            super.onRestoreInstanceState(savedInstanceState);

            message = savedInstanceState.getString("Result");
            message2 = savedInstanceState.getString("Result2");
            check_BMI_status = savedInstanceState.getFloat("BMI_result");
            textView.setText(message);
            textView5.setText(message2);
            setBMIColor();
        }

    public void countBMI_button(View view) {

        CountBmi BMI_Counter = new CountBmi();
        textView.setText(getResources().getString(R.string.Empty));
        textView5.setText(getResources().getString(R.string.Empty));

        try {
        getValues();
        textView2.setText(getResources().getString(R.string.Empty));
        }
        catch(NumberFormatException e){
            textView2.setText(R.string.EmptyFields);
            return;
        }

        if(mySwitch.isChecked()) {
            try {
                message = getResources().getString(R.string.showBMI);
                message2 = String.format(Locale.US,"%.2f",BMI_Counter.countBMI(weight / 2.2046f, (height / 39.37f)));
                check_BMI_status = Float.parseFloat(message2);
            } catch (IllegalArgumentException e) {
                DisplayError();
                message=getResources().getString(R.string.Empty);
                message2=getResources().getString(R.string.Empty);
            }
        }
       else {
       try {
           message = getResources().getString(R.string.showBMI);
           message2 = String.format(Locale.US,"%.2f",BMI_Counter.countBMI(weight, height));
           check_BMI_status = Float.parseFloat(message2);
        }  catch(IllegalArgumentException e) {
            DisplayError();
           message=getResources().getString(R.string.Empty);
           message2=getResources().getString(R.string.Empty);
        }
        }
        textView.setText(message);
        textView5.setText(message2);
        setBMIColor();



    }

    public void setBMIColor()
    {
        if(check_BMI_status<18.5f)
        {
            textView5.setTextColor(ContextCompat.getColor(this, R.color.Blue));
        }
        else
        if(check_BMI_status>=18.5f && check_BMI_status<24.9f)
        {
            textView5.setTextColor(ContextCompat.getColor(this, R.color.Green));
        }
        else
        if(check_BMI_status>=24.9f && check_BMI_status<30.0f)
        {
            textView5.setTextColor(ContextCompat.getColor(this, R.color.Orange));
        }
        else
        {
            textView5.setTextColor(ContextCompat.getColor(this, R.color.Red));
        }

    }

    public void getValues() throws NumberFormatException
    {
        String temp;

            message=textView.getText().toString();
            message2=textView5.getText().toString();
            temp = editText.getText().toString();
            weight = Float.parseFloat(temp);
            temp = editText2.getText().toString();
            height = Float.parseFloat(temp);
    }

    public void setValues()
    {
        String temp = String.format(Locale.US,"%.2f",weight);
            editText.setText(temp);
            temp = String.format(Locale.US,"%.2f",height);
            editText2.setText(temp);
            textView.setText(message);
            textView5.setText(message2);
    }
    private void showToast(String msg)
    {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

    public void DisplayError()
    {
        AlertDialog.Builder builder1 = new AlertDialog.Builder(this);

        builder1.setMessage(R.string.ErrorMessage);
        builder1.setPositiveButton(
                R.string.OK,
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        AlertDialog alert1 = builder1.create();
        alert1.show();
    }
    public boolean checkDataCorrect()
    {
        try {
            CountBmi BMI_Counter = new CountBmi();
            if(mySwitch.isChecked()) {
                BMI_Counter.countBMI(Float.parseFloat(editText.getText().toString())/ 2.2046f, Float.parseFloat(editText2.getText().toString())/ 39.37f);
            }
            else{
                BMI_Counter.countBMI(Float.parseFloat(editText.getText().toString()), Float.parseFloat(editText2.getText().toString()));}
        }catch(IllegalArgumentException e){DisplayError(); return false;}
        return true;
    }
    private void saveData() {

            String editTextData1 = editText.getText().toString();
            String editTextData2 = editText2.getText().toString();
            Boolean switch_status = mySwitch.isChecked();

            SharedPreferences.Editor preferencesEditor = preferences.edit();

            preferencesEditor.putBoolean(SWITCH_STATUS, switch_status);
            preferencesEditor.putString(PREFERENCES_TEXT_FIELD, editTextData1);
            preferencesEditor.putString(PREFERENCES_TEXT_FIELD2, editTextData2);

            String TextViewData1 = textView.getText().toString();
            preferencesEditor.putString(PREFERENCES_TEXT_FIELD3, TextViewData1);

            TextViewData1 = textView5.getText().toString();

            preferencesEditor.putString(PREFERENCES_TEXT_FIELD4, TextViewData1);
            preferencesEditor.putFloat(PREFERENCES_FLOAT_FIELD, check_BMI_status);

            preferencesEditor.apply();

    }
    private void restoreData() {

        String textFromPreferences = preferences.getString(PREFERENCES_TEXT_FIELD, getResources().getString(R.string.Empty));
        Boolean switch_status = preferences.getBoolean(SWITCH_STATUS, false);

        mySwitch.setChecked(switch_status);
        editText.setText(textFromPreferences);

        textFromPreferences = preferences.getString(PREFERENCES_TEXT_FIELD2, getResources().getString(R.string.Empty));
        editText2.setText(textFromPreferences);
        textFromPreferences = preferences.getString(PREFERENCES_TEXT_FIELD3, getResources().getString(R.string.Empty));
        textView.setText(textFromPreferences);
        textFromPreferences = preferences.getString(PREFERENCES_TEXT_FIELD4, getResources().getString(R.string.Empty));
        textView5.setText(textFromPreferences);
        check_BMI_status = preferences.getFloat(PREFERENCES_FLOAT_FIELD,0f);
    }

}
