package com.example.spotquiz;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.spotquiz.pojo.QuizLocation;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.model.PlaceLikelihood;
import com.google.android.libraries.places.api.net.FindCurrentPlaceRequest;
import com.google.android.libraries.places.api.net.FindCurrentPlaceResponse;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.android.libraries.places.widget.AutocompleteSupportFragment;
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.List;

public class MapsActivity extends AppCompatActivity  implements OnMapReadyCallback {

    private GoogleMap mMap;

    private static final String TAG = "MapsActivity";
    ListView lstPlaces;
    private PlacesClient mPlacesClient;
    private FusedLocationProviderClient mFusedLocationProviderClient;

    // The geographical location where the device is currently located. That is, the last-known
    // location retrieved by the Fused Location Provider.
    private Location mLastKnownLocation;

    // A default location and default zoom to use when location permission is
    // not granted.
    private final LatLng mDefaultLocation = new LatLng(44.640874, -63.578470);
    private static final int DEFAULT_ZOOM = 15;
    private static final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1;
    private boolean mLocationPermissionGranted;

    // Used for selecting the current place.
    private static final int M_MAX_ENTRIES = 5;
    private String[] mLikelyPlaceNames;
    private String[] mLikelyPlaceAddresses;
    private String[] mLikelyPlaceAttributions;
    private LatLng[] mLikelyPlaceLatLngs;
    private Marker marker;

    private Button selectLocation;
    private QuizLocation quizLocation;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        quizLocation = new QuizLocation();

        selectLocation = findViewById(R.id.btnSelectLocation);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        // Set up the action toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
       // Set up the views
        lstPlaces = (ListView) findViewById(R.id.listPlaces);
        // Initialize the Places client
        String apiKey = getString(R.string.google_maps_key);
        Places.initialize(getApplicationContext(), apiKey);
        mPlacesClient = Places.createClient(this);
        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        //Selecct location
        selectLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),QuizCreationActivity.class);
                intent.putExtra("location",quizLocation);
                startActivity(intent);

            }
        });


        //Autosuggestion
        AutocompleteSupportFragment autocompleteFragment = (AutocompleteSupportFragment)
                getSupportFragmentManager().findFragmentById(R.id.autocomplete_fragment);
        autocompleteFragment.setPlaceFields(Arrays.asList(Place.Field.ID, Place.Field.NAME,Place.Field.LAT_LNG));
        autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(Place place) {
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(place.getLatLng()
                        , DEFAULT_ZOOM));
                if(marker != null){
                    marker.remove();
                }
                marker = mMap.addMarker(new MarkerOptions()
                        .title(place.getName())
                        .position(place.getLatLng())
                        );

                quizLocation.setName(place.getName());
                quizLocation.setLatitude(place.getLatLng().latitude);
                quizLocation.setLongitude(place.getLatLng().longitude);

                Log.d(TAG, "Place: " + place.getName() + ", " + place.getId() + ", "+ place.getLatLng());
            }

            @Override
            public void onError(Status status) {
                Log.d(TAG, "An error occurred: " + status);
            }
        });
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
        LatLng sydney = new LatLng(-34, 151);
        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));

        // Enable the zoom controls for the map
        mMap.getUiSettings().setZoomControlsEnabled(true);

        // Prompt the user for permission.
        getLocationPermission();
    }

    private void getCurrentPlaceLikelihoods() {
        // Use fields to define the data types to return.
        List<Place.Field> placeFields = Arrays.asList(Place.Field.NAME, Place.Field.ADDRESS,
                Place.Field.LAT_LNG);

        // Get the likely places - that is, the businesses and other points of interest that
        // are the best match for the device's current location.
        @SuppressWarnings("MissingPermission") final FindCurrentPlaceRequest request =
                FindCurrentPlaceRequest.builder(placeFields).build();
        Task<FindCurrentPlaceResponse> placeResponse = mPlacesClient.findCurrentPlace(request);
        placeResponse.addOnCompleteListener(this,
                new OnCompleteListener<FindCurrentPlaceResponse>() {
                    @Override
                    public void onComplete(@NonNull Task<FindCurrentPlaceResponse> task) {
                        if (task.isSuccessful()) {
                            FindCurrentPlaceResponse response = task.getResult();
                            // Set the count, handling cases where less than 5 entries are returned.
                            int count;
                            if (response.getPlaceLikelihoods().size() < M_MAX_ENTRIES) {
                                count = response.getPlaceLikelihoods().size();
                            } else {
                                count = M_MAX_ENTRIES;
                            }

                            int i = 0;
                            mLikelyPlaceNames = new String[count];
                            mLikelyPlaceAddresses = new String[count];
                            mLikelyPlaceAttributions = new String[count];
                            mLikelyPlaceLatLngs = new LatLng[count];

                            for (PlaceLikelihood placeLikelihood : response.getPlaceLikelihoods()) {
                                Place currPlace = placeLikelihood.getPlace();
                                mLikelyPlaceNames[i] = currPlace.getName();
                                mLikelyPlaceAddresses[i] = currPlace.getAddress();
                                mLikelyPlaceAttributions[i] = (currPlace.getAttributions() == null) ?
                                        null : String.join(" ", currPlace.getAttributions());
                                mLikelyPlaceLatLngs[i] = currPlace.getLatLng();

                                String currLatLng = (mLikelyPlaceLatLngs[i] == null) ?
                                        "" : mLikelyPlaceLatLngs[i].toString();

                                Log.i(TAG, String.format("Place " + currPlace.getName()
                                        + " has likelihood: " + placeLikelihood.getLikelihood()
                                        + " at " + currLatLng));

                                i++;
                                if (i > (count - 1)) {
                                    break;
                                }
                            }

                            mLikelyPlaceNames[count-1] = "Biology building";
                            mLikelyPlaceAddresses[count-1] = "lol";
                            mLikelyPlaceAttributions[count-1] = null;
                            Double lat = 44.6363738;
                            Double lon = -63.5962449;
                            LatLng l = new LatLng(lat,lon);
                            mLikelyPlaceLatLngs[count-1] = l;
                            try {
                                getCurrentPlaceSuggestions();
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }


                            // COMMENTED OUT UNTIL WE DEFINE THE METHOD
                            // Populate the ListView
                             fillPlacesList();
                        } else {
                            Exception exception = task.getException();
                            if (exception instanceof ApiException) {
                                ApiException apiException = (ApiException) exception;
                                Log.e(TAG, "Place not found: " + apiException.getStatusCode());
                            }
                        }
                    }
                });
    }

    private void getCurrentPlaceSuggestions() throws JSONException {
        int count=0;
        mLikelyPlaceNames = new String[count];
        mLikelyPlaceAddresses = new String[count];
        mLikelyPlaceAttributions = new String[count];
        mLikelyPlaceLatLngs = new LatLng[count];
        String json =  getResources().getString(R.string.suggested_buildings);
        JSONObject jsonObj = new JSONObject(json);
        JSONArray build = jsonObj.getJSONArray("buildings");
        for(int i =0;i<build.length();i++){
            JSONObject b = build.getJSONObject(i);
           Log.e("jsonmap", b.getString("name"));
            /*mLikelyPlaceNames[i] = b.getString("name");
            mLikelyPlaceAddresses[i] =  b.getString("name");
            mLikelyPlaceAttributions[i] = null;
            Double lat =  Double.parseDouble(b.getString("lat"));
            Double lon =  Double.parseDouble(b.getString("lon"));
            LatLng l = new LatLng(lat,lon);
            mLikelyPlaceLatLngs[i] = l;*/
        }

        mLikelyPlaceNames[count] = "Tupper building";
        mLikelyPlaceAddresses[count] = "Tupper building";
        mLikelyPlaceAttributions[count] = null;
        Double lat = 44.639550;
        Double lon = -63.583717;
        LatLng l = new LatLng(lat,lon);
        mLikelyPlaceLatLngs[count] = l;
        count ++;

        mLikelyPlaceNames[count] = "Mona Campbell";
        mLikelyPlaceAddresses[count] = "Mona Campbell";
        mLikelyPlaceAttributions[count] = null;
        lat = 44.639550;
        lon = -63.583717;
        LatLng l = new LatLng(lat,lon);
        mLikelyPlaceLatLngs[count] = l;
        count ++;

        mLikelyPlaceNames[count] = "Tupper building";
        mLikelyPlaceAddresses[count] = "Tupper building";
        mLikelyPlaceAttributions[count] = null;
        lat = 44.639550;
        lon = -63.583717;
        LatLng l = new LatLng(lat,lon);
        mLikelyPlaceLatLngs[count] = l;
        count ++;

        mLikelyPlaceNames[count] = "Tupper building";
        mLikelyPlaceAddresses[count] = "Tupper building";
        mLikelyPlaceAttributions[count] = null;
        lat = 44.639550;
        lon = -63.583717;
        LatLng l = new LatLng(lat,lon);
        mLikelyPlaceLatLngs[count] = l;
        count ++;

        mLikelyPlaceNames[count] = "Tupper building";
        mLikelyPlaceAddresses[count] = "Tupper building";
        mLikelyPlaceAttributions[count] = null;
        lat = 44.639550;
        lon = -63.583717;
        LatLng l = new LatLng(lat,lon);
        mLikelyPlaceLatLngs[count] = l;



    }

    private void getDeviceLocation() {
        /*
         * Get the best and most recent location of the device, which may be null in rare
         * cases when a location is not available.
         */
        try {
            if (mLocationPermissionGranted) {
                Task<Location> locationResult = mFusedLocationProviderClient.getLastLocation();

                locationResult.addOnCompleteListener(this, new OnCompleteListener<Location>() {
                    @Override
                    public void onComplete(@NonNull Task<Location> task) {
                        if (task.isSuccessful()) {
                            // Set the map's camera position to the current location of the device.
                            mLastKnownLocation = task.getResult();
                            if(mLastKnownLocation!=null){
                            Log.d(TAG, "Latitude: " + mLastKnownLocation.getLatitude());
                            Log.d(TAG, "Longitude: " + mLastKnownLocation.getLongitude());

                                String api = "https://maps.googleapis.com/maps/api/geocode/json?latlng=";
                                api = api+mLastKnownLocation.getLatitude()+","+mLastKnownLocation.getLongitude()+"&key="+getResources().getString(R.string.google_maps_key);

                                RequestQueue queue = Volley.newRequestQueue(MapsActivity.this);

                                StringRequest stringRequest = new StringRequest(Request.Method.GET, api,
                                        new Response.Listener<String>() {
                                            @Override
                                            public void onResponse(String response) {
                                                Log.d("api results",response);
                                                System.out.println("api result"+response);
                                                JSONObject json = null;
                                                try {
                                                    json = new JSONObject(response);
                                                    JSONArray jsonArray = json.getJSONArray("results");
                                                    json = jsonArray.getJSONObject(0);
                                                    String address = json.get("formatted_address").toString();
                                                    System.out.println("address"+address);

                                                    marker =  mMap.addMarker(new MarkerOptions()
                                                            .title(address)
                                                            .position( new LatLng(mLastKnownLocation.getLatitude(),
                                                                    mLastKnownLocation.getLongitude()))
                                                            );
                                                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(
                                                            new LatLng(mLastKnownLocation.getLatitude(),
                                                                    mLastKnownLocation.getLongitude()), DEFAULT_ZOOM));
                                                    quizLocation.setName(address);
                                                    quizLocation.setLatitude(mLastKnownLocation.getLatitude());
                                                    quizLocation.setLongitude(mLastKnownLocation.getLongitude());
                                                } catch (JSONException e) {
                                                    e.printStackTrace();
                                                }


                                            }
                                        }, new Response.ErrorListener() {
                                    @Override
                                    public void onErrorResponse(VolleyError error) {
                                        Toast.makeText(MapsActivity.this,"Api Failed/Internet issue",Toast.LENGTH_LONG);
                                    }
                                });
                                queue.add(stringRequest);





                                // Position the map's camera at the location of the marker.
                             /*  */
                            }else{

                            }


                        } else {
                            Log.d(TAG, "Current location is null. Using defaults.");
                            Log.e(TAG, "Exception: %s", task.getException());
                            mMap.moveCamera(CameraUpdateFactory
                                    .newLatLngZoom(mDefaultLocation, DEFAULT_ZOOM));
                        }

                        getCurrentPlaceLikelihoods();
                    }
                });
            }
        } catch (SecurityException e)  {
            Log.e("Exception: %s", e.getMessage());
        }
    }

    private void pickCurrentPlace() {
        if (mMap == null) {
            return;
        }

        if (mLocationPermissionGranted) {
            getDeviceLocation();
        } else {
            // The user has not granted permission.
            Log.i(TAG, "The user did not grant location permission.");

            // Add a default marker, because the user hasn't selected a place.
            mMap.addMarker(new MarkerOptions()
                    .title(getString(R.string.default_info_title))
                    .position(mDefaultLocation)
                    .snippet(getString(R.string.default_info_snippet)));

            // Prompt the user for permission.
            getLocationPermission();
        }
    }

    private AdapterView.OnItemClickListener listClickedHandler = new AdapterView.OnItemClickListener() {
        public void onItemClick(AdapterView parent, View v, int position, long id) {
            // position will give us the index of which place was selected in the array
            LatLng markerLatLng = mLikelyPlaceLatLngs[position];
            String markerSnippet = mLikelyPlaceAddresses[position];
            if (mLikelyPlaceAttributions[position] != null) {
                markerSnippet = markerSnippet + "\n" + mLikelyPlaceAttributions[position];
            }

            // Add a marker for the selected place, with an info window
            // showing information about that place.
            if(marker != null){
                marker.remove();
            }

           marker =  mMap.addMarker(new MarkerOptions()
                    .title(mLikelyPlaceNames[position])
                    .position(markerLatLng)
                    .snippet(markerSnippet));



            // Position the map's camera at the location of the marker.
            mMap.moveCamera(CameraUpdateFactory.newLatLng(markerLatLng));
            quizLocation.setName(mLikelyPlaceNames[position]);
            quizLocation.setLatitude(markerLatLng.latitude);
            quizLocation.setLongitude(markerLatLng.longitude);
        }
    };

    private void fillPlacesList() {
        // Set up an ArrayAdapter to convert likely places into TextViews to populate the ListView
        ArrayAdapter<String> placesAdapter =
                new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, mLikelyPlaceNames);
        lstPlaces.setAdapter(placesAdapter);
        lstPlaces.setOnItemClickListener(listClickedHandler);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_geolocate:

                // COMMENTED OUT UNTIL WE DEFINE THE METHOD
                // Present the current place picker
                 pickCurrentPlace();
                return true;

            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);

        }
    }

    private void getLocationPermission() {
        /*
         * Request location permission, so that we can get the location of the
         * device. The result of the permission request is handled by a callback,
         * onRequestPermissionsResult.
         */
        mLocationPermissionGranted = false;
        if (ContextCompat.checkSelfPermission(this.getApplicationContext(),
                android.Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            mLocationPermissionGranted = true;
        } else {
            ActivityCompat.requestPermissions(this,
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                    PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
        }
    }

    /**
     * Handles the result of the request for location permissions.
     */
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String permissions[],
                                           @NonNull int[] grantResults) {
        mLocationPermissionGranted = false;
        switch (requestCode) {
            case PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    mLocationPermissionGranted = true;
                }
            }
        }
    }
}
