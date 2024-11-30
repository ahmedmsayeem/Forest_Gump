package com.example.running.ui.home;

import android.annotation.SuppressLint;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.running.R;
import com.example.running.databinding.FragmentHomeBinding;

import java.text.DecimalFormat;
import java.text.NumberFormat;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;

     Button btn;
     EditText run;
     EditText walk;
     Boolean runningBool =true;
     CountDownTimer countDownTimer;
    TextView textView;
    Button reset ;
    int runCount=0;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {


        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();



        textView= binding.textHome;
        btn = binding.button2;
        walk= binding.restTime;
        run = binding.runTime;
        reset =binding.restart;


        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(requireContext(), "Timer Started", Toast.LENGTH_SHORT).show();
                String content = "U ran "+runCount+" times";
                textView.setText(content);
                int runTime = Integer.parseInt(run.getText().toString());
                int walkTime= Integer.parseInt(walk.getText().toString());
                startTimer(runTime* 1000L, walkTime* 1000L);
            }
        });

        reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                runCount = 0;

                // Clear EditText fields
                run.setText("");
                walk.setText("");

                // Reset TextView
                binding.textHome.setText("Ready to start");

                // Cancel any running timer
                if (countDownTimer != null) {
                    countDownTimer.cancel();
                }

                // Reset button text
                btn.setText("Start");
            }
        });
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }
        binding = null;
    }

    @SuppressLint("SetTextI18n")
    public void startTimer(long runningTime, long walkingTime) {
        playBeepSound("whistle");
        countDownTimer = new CountDownTimer(runningTime, 1000) {
            public void onTick(long millisUntilFinished) {

                NumberFormat f = new DecimalFormat("00");
                long hour = (millisUntilFinished / 3600000) % 24;
                long min = (millisUntilFinished / 60000) % 60;
                long sec = (millisUntilFinished / 1000) % 60;
                btn.setText(f.format(hour) + ":" + f.format(min) + ":" + f.format(sec));
            }

            public void onFinish() {
                runCount++;
                textView.setText("U ran "+runCount+" times");
                btn.setText("00:00:00");
                Toast.makeText(requireContext(), "Rest time", Toast.LENGTH_SHORT).show();
                startWalkingTimer(walkingTime);
            }
        }.start();
    }

    public void startWalkingTimer(long walkingTime) {
        playBeepSound("stop");
        countDownTimer = new CountDownTimer(walkingTime, 1000) {
            public void onTick(long millisUntilFinished) {
                NumberFormat f = new DecimalFormat("00");
                long hour = (millisUntilFinished / 3600000) % 24;
                long min = (millisUntilFinished / 60000) % 60;
                long sec = (millisUntilFinished / 1000) % 60;
                btn.setText(f.format(hour) + ":" + f.format(min) + ":" + f.format(sec));
            }

            public void onFinish() {
                btn.setText("00:00:00");
                Toast.makeText(requireContext(), "Start Running", Toast.LENGTH_SHORT).show();
                startTimer(Integer.parseInt(run.getText().toString())*1000L,
                        walkingTime);
            }
        }.start();
    }


    private void playBeepSound(String audio) {
        MediaPlayer mediaPlayer = new MediaPlayer();
        if (audio.equals("whistle")) {
            mediaPlayer = MediaPlayer.create(requireContext(), R.raw.whistle);

        } else if (audio.equals("stop")) {
            mediaPlayer = MediaPlayer.create(requireContext(), R.raw.stop);

        }
        mediaPlayer.start();

        // Optional: release resources after sound plays
        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                mp.release();
            }
        });
    }

}