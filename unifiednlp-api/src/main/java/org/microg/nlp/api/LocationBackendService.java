/*
 * Copyright 2013-2015 microG Project Team
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.microg.nlp.api;

import android.location.Location;
import android.os.IBinder;
import android.os.RemoteException;

public abstract class LocationBackendService extends AbstractBackendService {

    private final Backend backend = new Backend();
    private LocationCallback callback;
    private Location waiting;

    /**
     * Called, whenever an app requires a location update. This can be a single or a repeated request.
     * <p/>
     * You may return null if your backend has no newer location available then the last one.
     * Do not send the same {@link android.location.Location} twice, if it's not based on updated/refreshed data.
     * <p/>
     * You can completely ignore this method (means returning null) if you use {@link #report(android.location.Location)}.
     *
     * @return a new {@link android.location.Location} instance or null if not available.
     */
    protected Location update() {
        return null;
    }

    /**
     * Directly report a {@link android.location.Location} to the requesting apps. Use this if your updates are based
     * on environment changes (eg. cell id change).
     *
     * @param location the new {@link android.location.Location} instance to be send
     */
    public void report(Location location) {
        if (callback != null) {
            try {
                callback.report(location);
            } catch (android.os.DeadObjectException e) {
                waiting = location;
                callback = null;
            } catch (RemoteException e) {
                waiting = location;
            }
        } else {
            waiting = location;
        }
    }

    /**
     * @return true if we're an actively connected backend, false if not
     */
    public boolean isConnected() {
        return callback != null;
    }

    @Override
    protected IBinder getBackend() {
        return backend;
    }

    @Override
    public void disconnect() {
        if (callback != null) {
            onClose();
            callback = null;
        }
    }

    private class Backend extends LocationBackend.Stub {
        @Override
        public void open(LocationCallback callback) throws RemoteException {
            LocationBackendService.this.callback = callback;
            if (waiting != null) {
                callback.report(waiting);
                waiting = null;
            }
            onOpen();
        }

        @Override
        public Location update() throws RemoteException {
            return LocationBackendService.this.update();
        }

        @Override
        public void close() throws RemoteException {
            disconnect();
        }
    }
}
