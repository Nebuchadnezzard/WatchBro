package com.watchbro.watchbro.services;

import android.app.IntentService;
import android.content.Intent;
import android.content.Context;

/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * <p>
 * TODO: Customize class - update intent actions, extra parameters and static
 * helper methods.
 */
public class StepsService extends IntentService {

    private static final String ACTION_FETCH_STEPS = "com.watchbro.watchbro.services.action.FETCH_STEPS";

    // TODO: Rename parameters

    public StepsService() {
        super("StepsService");
    }

    /**
     * Starts this service to perform action Foo with the given parameters. If
     * the service is already performing a task this action will be queued.
     *
     * @see IntentService
     */
    public static void startActionGetSteps(Context context) {
        Intent intent = new Intent(context, StepsService.class);
        intent.setAction(ACTION_FETCH_STEPS);
        context.startService(intent);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            final String action = intent.getAction();
            if (ACTION_FETCH_STEPS.equals(action)) {
                handleActionFetchSteps();
            }
        }
    }

    /**
     * Handle action Foo in the provided background thread with the provided
     * parameters.
     */
    private void handleActionFetchSteps() {
        // TODO: Handle action steps
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
