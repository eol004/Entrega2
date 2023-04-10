package com.example.entrega2;
import android.content.Context;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.lifecycle.Observer;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkInfo;
import androidx.work.WorkManager;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

public class conexionBDWebService extends Worker {

    public conexionBDWebService(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
        OneTimeWorkRequest otwr = new OneTimeWorkRequest.Builder(ConexionServidorDAS.class).build();
        WorkManager.getInstance(this).getWorkInfoByIdLiveData(otwr.getId())
                .observe(this, new Observer<WorkInfo>() {
                    @Override
                    public void onChanged(WorkInfo workInfo) {
                        if(workInfo != null && workInfo.getState().isFinished()){
                            TextView textViewResult = findViewById(R.id.textoResultado);
                            textViewResult.setText(workInfo.getOutputData().getString("datos"));
                        }
                    }
                });
        WorkManager.getInstance(this).enqueue(otwr);
    }

    @NonNull
    @Override
    public Result doWork() {
        return null;
    }
}
