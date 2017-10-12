package com.android.enforcedstreamsilencer;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.media.AudioManager;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.Toast;

public class EnforcedStreamSilencerActivity extends Activity
{
	private static final int ENFORCED_STREAM_ID = 7;

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);

		setContentView(R.layout.main);

	} // onCreate

	@Override
	protected void onResume()
	{
		super.onResume();

		CheckBox streamDisabledCheckBox = this.getCheckbox();
		if (null == streamDisabledCheckBox)
		{
			return;
		}

		AudioManager audioManager = this.getAudioManager();
		if (null == audioManager)
		{
			return;
		}

		int currentStreamVolume = audioManager.getStreamVolume(ENFORCED_STREAM_ID);
		if (currentStreamVolume == 0)
		{
			streamDisabledCheckBox.setChecked(true);
		}
		else
		{
			streamDisabledCheckBox.setChecked(false);
		}

	} // onResume

	public void onCheckboxCheckChanged(View view)
	{
		CheckBox streamDisabledCheckBox = this.getCheckbox();
		if (null == streamDisabledCheckBox)
		{
			return;
		}

		AudioManager audioManager = this.getAudioManager();
		if (null == audioManager)
		{
			return;
		}

		if (streamDisabledCheckBox.isChecked())
		{
			// set volume to 0
			audioManager.setStreamVolume(ENFORCED_STREAM_ID, 0, 0);

			// check that it is actually set to 0
			int currentVolume = audioManager.getStreamVolume(ENFORCED_STREAM_ID);
			if (0 == currentVolume)
			{
				Toast t = Toast.makeText(this, "Successfully set volume to 0.", Toast.LENGTH_SHORT);
				if (null != t)
				{
					t.show();
				}
			}
			else
			{
				// setting failed, so uncheck the box
				streamDisabledCheckBox.setChecked(false);
				Toast t = Toast.makeText(this, "Did not successfully set volume to 0, please try again!", Toast.LENGTH_SHORT);
				if (null != t)
				{
					t.show();
				}
			}

		}
		else
		{
			// set volume back to max
			int maxVolume = audioManager.getStreamMaxVolume(ENFORCED_STREAM_ID);
			audioManager.setStreamVolume(ENFORCED_STREAM_ID, maxVolume, 0);

			// check that it is actually set to max
			int currentVolume = audioManager.getStreamVolume(ENFORCED_STREAM_ID);
			if (maxVolume == currentVolume)
			{
				Toast t = Toast.makeText(this, "Successfully set volume to max.", Toast.LENGTH_SHORT);
				if (null != t)
				{
					t.show();
				}
			}
			else
			{
				// setting failed, so check the box
				streamDisabledCheckBox.setChecked(true);
				Toast t = Toast.makeText(this, "Did not successfully set volume to max, please try again!", Toast.LENGTH_SHORT);
				if (null != t)
				{
					t.show();
				}
			}

		} // else

	} // onCheckboxCheckChanged

	private CheckBox getCheckbox()
	{
		CheckBox checkBox = (CheckBox) findViewById(R.id.checkBox1);
		if (checkBox == null)
		{
			ShowAlertAndFinish("Could not get reference to CheckBox, this should never happen!");
			return null;
		}

		return checkBox;

	} // getCheckbox

	private AudioManager getAudioManager()
	{
		AudioManager audioManager = (AudioManager) this.getSystemService(Context.AUDIO_SERVICE);
		if (null == audioManager)
		{
			ShowAlertAndFinish("Could not get reference to Audio Manager, this should never happen!");
			return null;
		}

		return audioManager;

	} // getAudioManager

	private void ShowAlertAndFinish(String message)
	{
		Builder b = new AlertDialog.Builder(this);	
		b.setIcon(android.R.drawable.ic_dialog_alert);
		b.setTitle("Error");
		b.setMessage(message);
		b.setPositiveButton("Ok", new DialogInterface.OnClickListener()
		{
			@Override
			public void onClick(DialogInterface dialog, int which)
			{
				EnforcedStreamSilencerActivity.this.finish();
			}
		});

		b.show();

	} // ShowAlertAndFinish

} // class