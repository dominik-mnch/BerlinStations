package de.muench.berlinstations;

import android.graphics.drawable.BitmapDrawable;
import androidx.appcompat.app.AppCompatActivity;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.os.Environment;
import android.widget.Button;
import android.widget.Toast;

import de.muench.berlinstations.model.Station;
import org.osmdroid.config.Configuration;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Marker;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;

public class MapActivity extends AppCompatActivity {

    private MapView mapView;
    private ArrayList<Station> stations;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Configuration.getInstance().setUserAgentValue(getPackageName());
        setContentView(R.layout.activity_map);

        mapView = findViewById(R.id.mapView);
        mapView.setMultiTouchControls(true);
        mapView.getController().setZoom(12.0);

        // Receive filtered stations
        stations = getIntent().getParcelableArrayListExtra("filteredStations");

        if (stations != null && !stations.isEmpty()) {
            // Center on first station
            mapView.getController().setCenter(
                    new GeoPoint(stations.get(0).getLatitude(), stations.get(0).getLongitude()));

            int index = 1;
            for (Station s : stations) {
                GeoPoint point = new GeoPoint(s.getLatitude(), s.getLongitude());
                Marker marker = new Marker(mapView);
                marker.setPosition(point);

                // Set numbered marker with label
                marker.setIcon(new BitmapDrawable(getResources(),
                        createNumberedMarkerWithLabel(index, s.getName())));
                marker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_CENTER);
                marker.setTitle(s.getName());

                mapView.getOverlays().add(marker);
                index++;
            }
        }

        Button saveButton = findViewById(R.id.saveButton);
        saveButton.setOnClickListener(v -> saveMapSnapshot());
    }

    // Create a bigger marker bitmap with number and label
    private Bitmap createNumberedMarkerWithLabel(int number, String name) {
        int width = 200;  // enough for station name
        int height = 200; // enough for circle + 2 lines of text
        int circleDiameter = height / 3;   // one-third of bitmap height
        int circleRadius = circleDiameter / 2;
        Bitmap bmp = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bmp);
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);

        // Circle drawing
        canvas.drawCircle(width/2f, circleRadius + 10, circleRadius, paint);

        // Number inside circle
        paint.setColor(Color.WHITE);
        paint.setTextSize(35);
        paint.setTextAlign(Paint.Align.CENTER);
        Paint.FontMetrics fm = paint.getFontMetrics();
        float yNumber = circleRadius + 10 - (fm.ascent + fm.descent)/2;
        canvas.drawText(String.valueOf(number), width/2f, yNumber, paint);

        // Draw multiline station name with extra space below circle
        paint.setColor(Color.BLACK);
        paint.setTextSize(28);
        float extraSpace = 20; // distance between circle and first line of text
        drawMultilineText(canvas, paint, name, width/2f, circleRadius*2 + 10, width - 20, extraSpace);
        return bmp;
    }

    private void drawMultilineText(Canvas canvas, Paint paint, String text, float x, float startY, int maxWidth, float offsetBelowCircle) {
        String[] words = text.split(" ");
        StringBuilder line = new StringBuilder();
        float lineHeight = -paint.ascent() + paint.descent();
        float yOffset = startY + offsetBelowCircle; // add space below circle

        for (String word : words) {
            String testLine = line.length() == 0 ? word : line + " " + word;
            if (paint.measureText(testLine) > maxWidth) {
                canvas.drawText(line.toString(), x, yOffset, paint);
                yOffset += lineHeight;
                line = new StringBuilder(word);
            } else {
                line = new StringBuilder(testLine);
            }
        }
        canvas.drawText(line.toString(), x, yOffset, paint);
    }


    private void saveMapSnapshot() {
        try {
            Bitmap bmp = Bitmap.createBitmap(mapView.getWidth(), mapView.getHeight(), Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(bmp);
            mapView.draw(canvas);

            File publicDir = new File(Environment.getExternalStoragePublicDirectory(
                    Environment.DIRECTORY_PICTURES), "StationMaps");
            if (!publicDir.exists()) publicDir.mkdirs();
            File file = new File(publicDir, "stations_map.png");
            FileOutputStream out = new FileOutputStream(file);
            bmp.compress(Bitmap.CompressFormat.PNG, 100, out);
            out.flush();
            out.close();

            Toast.makeText(this, "Map saved: " + file.getAbsolutePath(), Toast.LENGTH_LONG).show();
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "Failed to save map", Toast.LENGTH_SHORT).show();
        }
    }


}

