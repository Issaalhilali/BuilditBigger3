package com.example.builditbigger;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.jokepresenter.JokeActivity;


public class MainActivityFragment extends Fragment implements EndpointsAsyncTask.DataReceivedInterface {

    public static final String JOKE_TAG = "joke";
    private ProgressBar pbLoad;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_main, container, false);

        pbLoad = root.findViewById(R.id.pbLoad);

        root.findViewById(R.id.TellJoke).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pbLoad.setVisibility(View.VISIBLE);
                requestJoke();
            }
        });

        return root;
    }

    public void requestJoke() {
        new EndpointsAsyncTask(this).execute();
    }

    @Override
    public void onDataReceived(Exception error, String data) {
        pbLoad.setVisibility(View.GONE);
        if (error != null) {
            Toast.makeText(getActivity(), getResources().getText(R.string.retrieval_error), Toast.LENGTH_SHORT).show();
        } else {
            Intent intent = new Intent(getActivity(), JokeActivity.class);
            intent.putExtra(JOKE_TAG, data);
            startActivity(intent);
        }
    }
}
