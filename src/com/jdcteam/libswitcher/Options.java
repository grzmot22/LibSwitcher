package com.jdcteam.libswitcher;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.SwitchPreference;
import android.view.Menu;
import android.view.MenuItem;

import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;

/**
 * Created by AntaresOne on 23/09/2015.
 */
public class Options extends PreferenceActivity {

    private File mNewIr = new File("/system/lib/hw/consumerir.msm8960.new");
    private SwitchPreference mIrSwitch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preferences);
        mIrSwitch = (SwitchPreference) findPreference("pref_ir");
        if (checkIr()) {
            mIrSwitch.setChecked(true);
        }
        findPreference("pref_ir").setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                try {
                    Process su = Runtime.getRuntime().exec("su");
                    DataOutputStream dataOutputStream = new DataOutputStream(su.getOutputStream());
                    dataOutputStream.writeBytes("mount -o remount,rw /system\n");
                    if (checkIr()) {
                        dataOutputStream.writeBytes("mv /system/lib/hw/consumerir.msm8960.so /system/lib/hw/consumerir.msm8960.old\n");
                        dataOutputStream.writeBytes("mv /system/lib/hw/consumerir.msm8960.new /system/lib/hw/consumerir.msm8960.so\n");
                    } else {
                        dataOutputStream.writeBytes("mv /system/lib/hw/consumerir.msm8960.so /system/lib/hw/consumerir.msm8960.new\n");
                        dataOutputStream.writeBytes("mv /system/lib/hw/consumerir.msm8960.old /system/lib/hw/consumerir.msm8960.so\n");
                    }
                    dataOutputStream.writeBytes("chmod 0644 /system/lib/hw/consumerir.msm8960.so\n");
                    dataOutputStream.writeBytes("mount -o remount,ro /system && exit\n");
                    dataOutputStream.flush();
                    su.waitFor();
                    rebootDialog();
                } catch(IOException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                return true;
            }
        });
    }

    private boolean checkIr() {
        if (mNewIr.exists()) {
            return true;
        }
        return false;
    }

    private void rebootDialog() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder
                .setTitle(R.string.reboot_title)
                .setMessage(R.string.reboot_title_sum)
                .setCancelable(false)
                .setPositiveButton(R.string.positive_button, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        try {
                            Process su = Runtime.getRuntime().exec("su");
                            DataOutputStream dataOutputStream = new DataOutputStream(su.getOutputStream());
                            dataOutputStream.writeBytes("reboot system\n");
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }).setNegativeButton(R.string.negative_button, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.cancel();
            }
        });
        alertDialogBuilder.show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        switch (id) {
            case R.id.issue_report:
                String issueReport = this.getResources().getString(R.string.issue_report_url);
                Intent reportAnIssue = new Intent(Intent.ACTION_VIEW);
                reportAnIssue.setData(Uri.parse(issueReport));
                startActivity(reportAnIssue);
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}