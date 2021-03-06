package dynoapps.exchange_rates.provider;

/**
 * Created by erdemmac on 25/11/2016.
 */

public interface IPollingSource {

    void one_shot();

    void start();

    void stop();

    void cancel();

    interface SourceCallback<T> {
        void onResult(T value);

        void onError();
    }
}



