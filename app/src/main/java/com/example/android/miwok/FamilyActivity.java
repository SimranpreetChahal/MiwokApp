package com.example.android.miwok;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;

import static android.media.AudioManager.AUDIOFOCUS_GAIN;
import static android.media.AudioManager.AUDIOFOCUS_LOSS_TRANSIENT;
import static android.media.AudioManager.AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK;

public class FamilyActivity extends AppCompatActivity {
    private MediaPlayer mMediaPlayer;
    private AudioManager mAudioManager;

    private MediaPlayer.OnCompletionListener mOnCompletionListener = new MediaPlayer.OnCompletionListener(){
        @Override
        public void onCompletion(MediaPlayer mp) {
            releaseMediaPlayer();
        }
    };

    AudioManager.OnAudioFocusChangeListener mAudioFocusChangeListener =
            new AudioManager.OnAudioFocusChangeListener() {
                public void onAudioFocusChange(int focusChange) {
                    if (focusChange == AUDIOFOCUS_LOSS_TRANSIENT || focusChange == AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK) {
                        //Pause playback and reset the player to start of the file.
                        mMediaPlayer.pause();
                        mMediaPlayer.seekTo(0);
                    } else if (focusChange == AUDIOFOCUS_GAIN) {
                        //regain focus and resume playing
                        mMediaPlayer.start();
                    } else if (focusChange == AudioManager.AUDIOFOCUS_LOSS) {
                        //lost the sudio and hence stop playback and cleanup resources
                        releaseMediaPlayer();
                    }
                }
            };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.word_list);
        mAudioManager = (AudioManager)getSystemService(Context.AUDIO_SERVICE);

        //create array of numbers
        final ArrayList<Word> words = new ArrayList<Word>();
        words.add(new Word("Daughter","tune",R.mipmap.family_daughter,R.raw.family_daughter));
        words.add(new Word("Father","әpә",R.mipmap.family_father,R.raw.family_father));
        words.add(new Word("Grandfather","paapa",R.mipmap.family_grandfather,R.raw.family_grandfather));
        words.add(new Word("Grandmother","ama",R.mipmap.family_grandmother,R.raw.family_grandmother));
        words.add(new Word("Mother","әṭa",R.mipmap.family_mother,R.raw.family_mother));
        words.add(new Word("Older Brother","taachi",R.mipmap.family_older_brother,R.raw.family_older_brother));
        words.add(new Word("Older sister","teṭe",R.mipmap.family_older_sister,R.raw.family_older_sister));
        words.add(new Word("son","angsi",R.mipmap.family_son,R.raw.family_son));
        words.add(new Word("Younger Brother","chalitti",R.mipmap.family_younger_brother,R.raw.family_younger_brother));
        words.add(new Word("Younger sister","kolliti",R.mipmap.family_younger_sister,R.raw.family_younger_sister));

        //populate numbersview with words
        WordAdaptor adaptor = new WordAdaptor(this,words,R.color.category_family);
        ListView listView = (ListView) findViewById(R.id.list);
        listView.setAdapter(adaptor);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Word currentWord = words.get(position);
                releaseMediaPlayer();

                //Request audiofocus for playback
                int result = mAudioManager.requestAudioFocus(mAudioFocusChangeListener,
                        // Use the music stream.
                        AudioManager.STREAM_MUSIC,
                        // Request permanent focus.
                        AudioManager.AUDIOFOCUS_GAIN_TRANSIENT);
                if(result == AudioManager.AUDIOFOCUS_REQUEST_GRANTED) {
                    mMediaPlayer = MediaPlayer.create(FamilyActivity.this,currentWord.getMediaFileResourceId());
                    mMediaPlayer.start();
                    mMediaPlayer.setOnCompletionListener(mOnCompletionListener);
                }
            }
        });

    }

    private void releaseMediaPlayer() {
        // If the media player is not null, then it may be currently playing a sound.
        if (mMediaPlayer != null) {
            // Regardless of the current state of the media player, release its resources
            // because we no longer need it.
            mMediaPlayer.release();

            // Set the media player back to null. For our code, we've decided that
            // setting the media player to null is an easy way to tell that the media player
            // is not configured to play an audio file at the moment.
            mMediaPlayer = null;
            //abandon audiofocus when playback completes
            mAudioManager.abandonAudioFocus(mAudioFocusChangeListener);

        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        releaseMediaPlayer();
    }

}
