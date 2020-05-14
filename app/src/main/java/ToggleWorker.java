import android.content.Context;
import android.provider.SyncStateContract;

import androidx.annotation.NonNull;
import androidx.work.Data;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.example.arrivalmessage.MainActivity;
import com.google.android.gms.common.internal.Constants;


public class ToggleWorker extends Worker {

    public ToggleWorker(
            @NonNull Context context,
            @NonNull WorkerParameters params) {
        super(context, params);
    }

    @Override
    public Result doWork() {


        if(MainActivity.isEnable==null||MainActivity.isEnable==false)
        {

        }

        return null;


    }
}
