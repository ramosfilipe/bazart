package com.boleiros.bazart.camera;

import android.app.Dialog;
import android.app.DialogFragment;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.telephony.PhoneNumberFormattingTextWatcher;
import android.text.InputFilter;
import android.text.util.Rfc822Tokenizer;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.ex.chips.BaseRecipientAdapter;
import com.boleiros.bazart.Bazart;
import com.boleiros.bazart.R;
import com.boleiros.bazart.feed.Feed;
import com.boleiros.bazart.hashtags.CustomRecipients;
import com.boleiros.bazart.modelo.Produto;
import com.boleiros.bazart.util.ActivityStore;
import com.boleiros.bazart.util.NumericRangeFilter;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.location.LocationClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.parse.ParseFile;
import com.parse.ParseGeoPoint;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.io.IOException;
import java.util.List;
import java.util.Locale;
import java.util.Random;

public class InfoFragment extends Fragment implements LocationListener,
        GooglePlayServicesClient.ConnectionCallbacks,
        GooglePlayServicesClient.OnConnectionFailedListener {
    private static final InputFilter[] FILTERS = new InputFilter[]{new NumericRangeFilter(0.00,
            999999.99)};
    /*
      * Define a request code to send to Google Play services This code is returned in
      * Activity.onActivityResult
      */
    private final static int CONNECTION_FAILURE_RESOLUTION_REQUEST = 9000;
    /*
     * Constants for location update parameters
     */
    // Milliseconds per second
    private static final int MILLISECONDS_PER_SECOND = 1000;
    // The update interval
    private static final int UPDATE_INTERVAL_IN_SECONDS = 5;
    // Update interval in milliseconds
    private static final long UPDATE_INTERVAL_IN_MILLISECONDS = MILLISECONDS_PER_SECOND
            * UPDATE_INTERVAL_IN_SECONDS;
    // A fast interval ceiling
    private static final int FAST_CEILING_IN_SECONDS = 1;
    // A fast ceiling of update intervals, used when the app is visible
    private static final long FAST_INTERVAL_CEILING_IN_MILLISECONDS = MILLISECONDS_PER_SECOND
            * FAST_CEILING_IN_SECONDS;
    private Location lastLocation = null;
    private Location currentLocation = null;
    private LocationRequest locationRequest;
    private LocationClient locationClient;
    private TextView localizacao;

    public InfoFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        locationRequest = LocationRequest.create();
        locationRequest.setInterval(UPDATE_INTERVAL_IN_MILLISECONDS);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setFastestInterval(FAST_INTERVAL_CEILING_IN_MILLISECONDS);
        // Create a new location client, using the enclosing class to handle callbacks.
        locationClient = new LocationClient(this.getActivity(), this, this);
        locationClient.connect();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_info, container, false);
        ImageView preview = (ImageView) v.findViewById(R.id.previewAntesDeEnviar);
        final EditText telefone = (EditText) v.findViewById(R.id.editTextPhoneNumber);
        telefone.addTextChangedListener(new PhoneNumberFormattingTextWatcher());
        final EditText preco = (EditText) v.findViewById(R.id.editTextPreco);
        localizacao = (TextView) v.findViewById(R.id.textViewLocalizacao);

        final CustomRecipients hashtags = (CustomRecipients) v.findViewById(R.id.editTextHashtags);

        hashtags.setTokenizer(new Rfc822Tokenizer());
        BaseRecipientAdapter a = new BaseRecipientAdapter(BaseRecipientAdapter.QUERY_TYPE_PHONE,
                getActivity()) {
        };

        hashtags.setAdapter(new BaseRecipientAdapter(BaseRecipientAdapter.QUERY_TYPE_PHONE,
                getActivity()) {
        });
        preco.setFilters(FILTERS);
        // preco.setOnFocusChangeListener(ON_FOCUS);
        loadBitmap(ActivityStore.getInstance(this.getActivity()).getImage(), preview);
        ImageButton botaoVoltar = (ImageButton) v.findViewById(R.id.imageButtonBack);
        botaoVoltar.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                com.commonsware.cwac.camera.CameraFragment current = new CameraFragment();
                getFragmentManager().beginTransaction()
                        .replace(R.id.container, current).commit();
            }
        });


        final ImageButton botaoEnvia = (ImageButton) v.findViewById(R.id.imageButtonUpload);
        botaoEnvia.setVisibility(View.VISIBLE);
        botaoEnvia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (preco.getText().length() < 1) {
                    Toast.makeText(getActivity(), "Insira o preço do produto",
                            Toast.LENGTH_SHORT).show();
                } else if (telefone.getText().length() < 1) {
                    Toast.makeText(getActivity(), "Insira o número para contato",
                            Toast.LENGTH_SHORT).show();
                } else if (hashtags.getSize() == 0) {
                    Toast.makeText(getActivity(), "Insira alguma hashtag",
                            Toast.LENGTH_SHORT).show();
                } else if (hashtags.getSize() > 3) {
                    Toast.makeText(getActivity(), "Insira no máximo 3 hashtags",
                            Toast.LENGTH_SHORT).show();
                } else {

                    Location myLoc = (currentLocation == null) ? lastLocation : currentLocation;
                    if (myLoc == null) {
                        Toast.makeText(getActivity(),
                                "Por favor verifique se o GPS está ativado!",
                                Toast.LENGTH_LONG).show();
                        return;
                    }
                    final ParseGeoPoint myPoint = geoPointFromLocation(myLoc);

                    botaoEnvia.setVisibility(View.GONE);
                    int max = ActivityStore.getInstance(getActivity()).getFrases().size();
                    Random r = new Random();
                    int numeroSorteado = r.nextInt(max);
                    final ProgressDialog pDialog = ProgressDialog.show(getActivity(), null,
                            ActivityStore.getInstance(getActivity()).getFrases().get(numeroSorteado));
                    ParseFile photoFile = new ParseFile("fotoProduto.jpg",
                            ActivityStore.getInstance(getActivity()).
                                    getImage()
                    );
                    EditText preco1 = (EditText) getActivity().findViewById(R.id.editTextPreco);
                    EditText telefone1 = (EditText) getActivity().findViewById(R.id
                            .editTextPhoneNumber);
                    String precoStr = preco.getText().toString();
                    precoStr = "R$ " + precoStr;
                    Produto produto = new Produto();
                    produto.setLocation(myPoint);
                    produto.setVendido(false);
                    produto.setAuthor(ParseUser.getCurrentUser());
                    produto.setPhotoFile(photoFile);
                    produto.setPhoneNumber(telefone1.getText().toString());
                    produto.setPrice(precoStr);
                    produto.setArrayHashtags(hashtags.getArrayOfText());
                    produto.initLikeArray();
                    produto.setCity(localizacao.getText().toString());
                    produto.saveInBackground(new SaveCallback() {
                        @Override
                        public void done(com.parse.ParseException e) {
                            if (e != null) {
                                pDialog.dismiss();
                                Toast.makeText(getActivity(),
                                        "Ops... Tente Enviar novamente",
                                        Toast.LENGTH_LONG).show();
                                botaoEnvia.setVisibility(View.VISIBLE);

                            } else {
                                pDialog.dismiss();
                                Toast.makeText(getActivity(), "Produto anunciado!",
                                        Toast.LENGTH_LONG).show();
                                locationClient.disconnect();
                                Intent intent = new Intent(getActivity(), Feed.class);
                                startActivity(intent);
                            }
                        }


                    });
                }

            }
        });
        return v;
    }

    private void startPeriodicUpdates() {
        locationClient.requestLocationUpdates(locationRequest, this);
    }

    private void stopPeriodicUpdates() {
        locationClient.removeLocationUpdates(this);
    }

    private Location getLocation() {
        if (servicesConnected()) {
            return locationClient.getLastLocation();
        } else {
            return null;
        }
    }

    private boolean servicesConnected() {
        int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this.getActivity());

        if (ConnectionResult.SUCCESS == resultCode) {

            return true;
        } else {
            Dialog dialog = GooglePlayServicesUtil.getErrorDialog(resultCode, this.getActivity(),
                    0);
            if (dialog != null) {
                ErrorDialogFragment errorFragment = new ErrorDialogFragment();
                errorFragment.setDialog(dialog);
                errorFragment.show(getFragmentManager(), Bazart.APPTAG);
            }
            return false;
        }
    }

    /**
     * Called when the location has changed.
     * <p/>
     * <p> There are no restrictions on the use of the supplied Location object.
     *
     * @param location The new location, as a Location object.
     */
    @Override
    public void onLocationChanged(Location location) {
        currentLocation = location;
        if (lastLocation != null
                && geoPointFromLocation(location)
                .distanceInKilometersTo(geoPointFromLocation(lastLocation)) < 0.01) {
            return;
        }
        lastLocation = location;
    }

    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    /**
     * Called when the provider is enabled by the user.
     *
     * @param provider the name of the location provider associated with this
     *                 update.
     */
    public void onProviderEnabled(String provider) {

    }

    /**
     * Called when the provider is disabled by the user. If requestLocationUpdates
     * is called on an already disabled provider, this method is called
     * immediately.
     *
     * @param provider the name of the location provider associated with this
     *                 update.
     */

    public void onProviderDisabled(String provider) {

    }

    /*
      * Helper method to get the Parse GEO point representation of a location
      */
    private ParseGeoPoint geoPointFromLocation(Location loc) {
        return new ParseGeoPoint(loc.getLatitude(), loc.getLongitude());
    }

    @Override
    public void onConnected(Bundle bundle) {
        currentLocation = getLocation();
        startPeriodicUpdates();
        new GetAddressTask(this.getActivity(), localizacao).execute(currentLocation);

    }

    @Override
    public void onDisconnected() {

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        if (connectionResult.hasResolution()) {
            try {
                connectionResult.startResolutionForResult(this.getActivity(),
                        CONNECTION_FAILURE_RESOLUTION_REQUEST);
            } catch (IntentSender.SendIntentException e) {
                showErrorDialog(connectionResult.getErrorCode());
            }
        } else {
            showErrorDialog(connectionResult.getErrorCode());
        }
    }

    void loadBitmap(byte[] foto, ImageView imageView) {
        final BitmapWorker task = new BitmapWorker(imageView);
        task.execute(foto);
    }

    /*
     * Show a dialog returned by Google Play services for the connection error code
     */
    private void showErrorDialog(int errorCode) {
        // Get the error dialog from Google Play services
        Dialog errorDialog =
                GooglePlayServicesUtil.getErrorDialog(errorCode, this.getActivity(),
                        CONNECTION_FAILURE_RESOLUTION_REQUEST);

        // If Google Play services can provide an error dialog
        if (errorDialog != null) {

            // Create a new DialogFragment in which to show the error dialog
            ErrorDialogFragment errorFragment = new ErrorDialogFragment();

            // Set the dialog in the DialogFragment
            errorFragment.setDialog(errorDialog);

            // Show the error dialog in the DialogFragment
            errorFragment.show(this.getFragmentManager(), Bazart.APPTAG);
        }
    }

    public interface OnFragmentInteractionListener {
    }

    /*
     * Define a DialogFragment to display the error dialog generated in showErrorDialog.
     */
    public static class ErrorDialogFragment extends DialogFragment {
        // Global field to contain the error dialog
        private Dialog mDialog;

        /**
         * Default constructor. Sets the dialog field to null
         */
        public ErrorDialogFragment() {
            super();
            mDialog = null;
        }

        /*
         * Set the dialog to display
         *
         * @param dialog An error dialog
         */
        public void setDialog(Dialog dialog) {
            mDialog = dialog;
        }

        /*
         * This method must return a Dialog to the DialogFragment.
         */
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            return mDialog;
        }
    }

    class GetAddressTask extends AsyncTask<Location, Void, String> {
        Context mContext;
        TextView label;

        public GetAddressTask(Context context, TextView tx) {
            super();
            label = tx;
            mContext = context;
        }

        /**
         * Get a Geocoder instance, get the latitude and longitude
         * look up the address, and return it
         *
         * @return A string containing the address of the current
         * location, or an empty string if no address can be found,
         * or an error message
         * @params params One or more Location objects
         */
        @Override
        protected String doInBackground(Location... params) {
            Geocoder geocoder =
                    new Geocoder(mContext, Locale.getDefault());
            // Get the current location from the input parameter list
            Location loc = params[0];
            // Create a list to contain the result address
            List<android.location.Address> addresses = null;
            try {
                /*
                 * Return 1 address.
                 */
                if (loc != null) {
                    addresses = geocoder.getFromLocation(loc.getLatitude(),
                            loc.getLongitude(), 1);
                }
            } catch (IOException e1) {
                Log.e("LocationSampleActivity",
                        "IO Exception in getFromLocation()");
                e1.printStackTrace();
                return ("IO Exception trying to get address");
            } catch (IllegalArgumentException e2) {
                // Error message to post in the log
                String errorString = "Illegal arguments " +
                        Double.toString(loc.getLatitude()) +
                        " , " +
                        Double.toString(loc.getLongitude()) +
                        " passed to address service";
                Log.e("LocationSampleActivity", errorString);
                e2.printStackTrace();
                return errorString;
            }
            // If the reverse geocode returned an address
            if (addresses != null && addresses.size() > 0) {
                // Get the first address
                Address address = addresses.get(0);
                /*
                 * Format the first line of address (if available),
                 * city, and country name.
                 */
                String addressText = String.format(
                        "%s",

                        // Locality is usually a city
                        address.getAddressLine(1)
                        // The country of the address
                );
                // Return the text
                return addressText;
            } else {
                return "Endereço não encontrado";
            }
        }

        @Override
        protected void onPostExecute(String address) {
            label.setText(address);

            // Set the address in the UI
        }

    }


    class BitmapWorker extends AsyncTask<byte[], Void, Bitmap> {
        private ImageView imageViewReference;

        public BitmapWorker(ImageView imageView) {
            imageViewReference = imageView;
        }

        // Decode image in background.
        @Override
        protected Bitmap doInBackground(byte[]... params) {
            return BitmapFactory.decodeByteArray(params[0], 0, params[0].length);
        }

        // Once complete, see if ImageView is still around and set bitmap.
        @Override
        protected void onPostExecute(Bitmap bitmap) {
            if (imageViewReference != null && bitmap != null) {
                imageViewReference.setImageBitmap(bitmap);
            }
        }
    }
}



