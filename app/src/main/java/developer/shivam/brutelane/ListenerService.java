package developer.shivam.brutelane;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

public class ListenerService extends Service {

    public ListenerService() {

    }

    @Override
    public IBinder onBind(Intent intent) {
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
