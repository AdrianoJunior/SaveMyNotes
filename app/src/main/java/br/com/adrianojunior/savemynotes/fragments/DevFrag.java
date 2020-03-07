package br.com.adrianojunior.savemynotes.fragments;


import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import br.com.adrianojunior.savemynotes.R;

public class DevFrag extends BaseFragment {

    private ImageButton telegramBtn;
    private ImageButton emailBtn;
    private ImageButton gitBtn;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_dev, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        telegramBtn = view.findViewById(R.id.imageTelegram);
        emailBtn = view.findViewById(R.id.imageMail);
        gitBtn = view.findViewById(R.id.imageGithub);

        telegramBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendTelegamMessage();
            }
        });

        emailBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendMail();
            }
        });

        gitBtn.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        openGithub();
                    }
                }
        );

    }

    private void sendMail() {

        Intent emailIntent = new Intent(Intent.ACTION_SEND);

        emailIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        emailIntent.setType("vnd.android.cursor.item/email");
        emailIntent.putExtra(android.content.Intent.EXTRA_EMAIL, new String[]{"developer.adrianscjr@gmail.com"});
        emailIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "");
        emailIntent.putExtra(android.content.Intent.EXTRA_TEXT, "\n\nENVIADO DO SAVE MY NOTES.");
        startActivity(Intent.createChooser(emailIntent, "Enviar e-mail utilizando..."));

    }

    private void sendTelegamMessage() {

        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://t.me/AndroidDev_Adriano")));

    }

    private void openGithub() {
        Intent siteIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.github.com/AdrianoJunior"));
        startActivity(siteIntent);
    }
}
