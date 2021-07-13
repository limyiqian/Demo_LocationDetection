package sg.edu.rp.c346.id19020125.demolocationdetection;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.PermissionChecker;

import android.Manifest;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

public class MainActivity extends AppCompatActivity {

    Button btnGLL, btnGLU, btnRLU;
    LocationRequest mLocationRequest;
    LocationCallback mLocationCallback;
    private FusedLocationProviderClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        client = LocationServices.getFusedLocationProviderClient(this);

        btnGLL = findViewById(R.id.btnGetLastLocation);
        btnGLU = findViewById(R.id.btnGetLocationUpdate);
        btnRLU = findViewById(R.id.btnRemoveLocationUpdate);

        btnGLL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkPermission();
                client.getLastLocation().addOnSuccessListener(MainActivity.this, new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        String msg;
                        if(location != null) {
                            msg = "Lat: " + location.getLatitude();
                            msg += "Lng: " + location.getLongitude();
                        }
                        else{
                            msg = "No last location found";
                        }
                        Toast.makeText(MainActivity.this,msg, Toast.LENGTH_SHORT).show();
                    }
                });

            }
        });

        btnGLU.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkPermission();
                mLocationRequest = LocationRequest.create();
                mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
                mLocationRequest.setInterval(10000);
                mLocationRequest.setFastestInterval(5000);
                mLocationRequest.setSmallestDisplacement(100);
                mLocationCallback = new LocationCallback() {
                    @Override
                    public void onLocationResult(LocationResult locationResult) {
                        if(locationResult != null){
                            Location data = locationResult.getLastLocation();
                            double lat = data.getLatitude();
                            double lng = data.getLongitude();
                            Toast.makeText(MainActivity.this,"Lat: "+lat+"Lng: "+lng, Toast.LENGTH_SHORT).show();
                        }
                    }
                };
                client.requestLocationUpdates(mLocationRequest, mLocationCallback, null);
            }
        });

        btnRLU.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                client.removeLocationUpdates(mLocationCallback);
            }
        });
    }
    private boolean checkPermission() {
        int permissionCheck_Coarse = ContextCompat.checkSelfPermission(
                MainActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION);
        int permissionCheck_Fine = ContextCompat.checkSelfPermission(
                MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION);

        if(permissionCheck_Coarse == PermissionChecker.PERMISSION_GRANTED || permissionCheck_Fine == PermissionChecker.PERMISSION_GRANTED) {
            return true;
        }
        else {
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 0);
            return false;
        }
    }
}

