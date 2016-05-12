package org.martin.getfreaky.log;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.martin.getfreaky.MyPagerAdapter;
import org.martin.getfreaky.R;
import org.martin.getfreaky.dataObjects.BodyLog;
import org.martin.getfreaky.dataObjects.DayLog;
import org.martin.getfreaky.dataObjects.Measurements;

import io.realm.Realm;
import io.realm.RealmConfiguration;


public class BodyLogFragment extends Fragment {
    private Realm realm;

    private EditText weight;
    private EditText bodyfat;
    private EditText chest;
    private EditText waist;
    private EditText arms;
    private EditText shoulders;
    private EditText forearms;
    private EditText neck;
    private EditText hips;
    private EditText thighs;
    private EditText calves;
    private Button saveButton;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_body_log, null);

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        String daylogId = preferences.getString(MyPagerAdapter.SELECTED_DAYLOG_ID, "NO_KEY");

        RealmConfiguration config = new RealmConfiguration.Builder(getContext()).build();
        Realm.setDefaultConfiguration(config);
        realm = Realm.getDefaultInstance();
        DayLog dayLog = realm.where(DayLog.class).equalTo("dayLogId", daylogId).findFirst();

        weight = (EditText) v.findViewById(R.id.weight);
        bodyfat = (EditText) v.findViewById(R.id.bodyfat);
        chest = (EditText) v.findViewById(R.id.chest);
        waist = (EditText) v.findViewById(R.id.waist);
        arms = (EditText) v.findViewById(R.id.arms);
        shoulders = (EditText) v.findViewById(R.id.shoulders);
        forearms = (EditText) v.findViewById(R.id.forearms);
        neck = (EditText) v.findViewById(R.id.necks);
        hips = (EditText) v.findViewById(R.id.hips);
        thighs = (EditText) v.findViewById(R.id.thighs);
        calves = (EditText) v.findViewById(R.id.calves);

        final BodyLog bodyLog = dayLog.getBodylog();
        final Measurements measurements = bodyLog.getMeasurements();

        weight.setText(String.valueOf(bodyLog.getWeight()));
        bodyfat.setText(String.valueOf(bodyLog.getBodyFatPercentage()));

        chest.setText(String.valueOf(measurements.getChest()));
        waist.setText(String.valueOf(measurements.getWaist()));
        arms.setText(String.valueOf(measurements.getArms()));
        shoulders.setText(String.valueOf(measurements.getShoulders()));
        forearms.setText(String.valueOf(measurements.getForeArms()));
        neck.setText(String.valueOf(measurements.getNeck()));
        hips.setText(String.valueOf(measurements.getHips()));
        thighs.setText(String.valueOf(measurements.getThighs()));
        calves.setText(String.valueOf(measurements.getCalves()));

        saveButton = (Button) v.findViewById(R.id.btnSaveBodyLog);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                realm.beginTransaction();

                bodyLog.setWeight(Float.valueOf(weight.getText().toString()));
                bodyLog.setBodyFatPercentage(Integer.valueOf(bodyfat.getText().toString()));

                measurements.setChest(Integer.valueOf(chest.getText().toString()));
                measurements.setWaist(Integer.valueOf(waist.getText().toString()));
                measurements.setArms(Integer.valueOf(arms.getText().toString()));
                measurements.setShoulders(Integer.valueOf(shoulders.getText().toString()));
                measurements.setForeArms(Integer.valueOf(forearms.getText().toString()));
                measurements.setNeck(Integer.valueOf(neck.getText().toString()));
                measurements.setHips(Integer.valueOf(hips.getText().toString()));
                measurements.setThighs(Integer.valueOf(thighs.getText().toString()));
                measurements.setCalves(Integer.valueOf(calves.getText().toString()));

                realm.commitTransaction();
                Toast.makeText(v.getContext(), "Body log saved", Toast.LENGTH_LONG).show();
            }
        });

        return v;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        realm.close();
    }
}