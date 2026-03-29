package de.muench.berlinstations;

import android.graphics.*;
import android.graphics.drawable.BitmapDrawable;
import androidx.appcompat.app.AppCompatActivity;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Configuration.getInstance().setUserAgentValue(getPackageName());
        setContentView(R.layout.activity_map);

        mapView = findViewById(R.id.mapView);
        mapView.setMultiTouchControls(true);
        mapView.getController().setZoom(12.0);

        ArrayList<Station> stations = getIntent().getParcelableArrayListExtra("filteredStations");

        if (stations != null && !stations.isEmpty()) {

            mapView.getController().setCenter(
                    new GeoPoint(stations.get(0).getLatitude(), stations.get(0).getLongitude()));

            int index = 1;
            for (Station s : stations) {

                GeoPoint point = new GeoPoint(s.getLatitude(), s.getLongitude());
                Marker marker = new Marker(mapView);
                marker.setPosition(point);

                // Color by station type
                int color;
                switch (s.getType()) {
                    case BAHN: color = Color.BLUE; break;
                    case TRAM: color = Color.GREEN; break;
                    case BUS:  color = Color.RED; break;
                    default:   color = Color.GRAY; break;
                }

                // Shape condition: name ends with "(30min)"
                boolean isSquare = s.getName().trim().endsWith("(30min)");

                marker.setIcon(new BitmapDrawable(getResources(),
                        createNumberedMarkerWithLabel(index, s.getName(), color, isSquare)));

                marker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_CENTER);
                marker.setTitle(s.getName());
                mapView.getOverlays().add(marker);
                index++;
            }
        }

        Button saveButton = findViewById(R.id.saveButton);
        saveButton.setOnClickListener(v -> saveMapSnapshot());
    }

    private Bitmap createNumberedMarkerWithLabel(int number, String name, int color, boolean isDiamond) {
        int width = 200;
        int height = 200;
        int shapeSize = height / 3;
        int shapeCenterX = width / 2;
        int shapeCenterY = shapeSize / 2 + 10;

        Bitmap bmp = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bmp);
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setColor(color);

        if (isDiamond) {
            // --- Draw DIAMOND shape ---
            Path diamond = new Path();
            diamond.moveTo(shapeCenterX, shapeCenterY - shapeSize / 2f);        // top
            diamond.lineTo(shapeCenterX + shapeSize / 2f, shapeCenterY);        // right
            diamond.lineTo(shapeCenterX, shapeCenterY + shapeSize / 2f);        // bottom
            diamond.lineTo(shapeCenterX - shapeSize / 2f, shapeCenterY);        // left
            diamond.close();

            canvas.drawPath(diamond, paint);
        } else {
            // --- Draw CIRCLE (normal stations) ---
            canvas.drawCircle(shapeCenterX, shapeCenterY, shapeSize / 2f, paint);
        }

        // Draw number inside the shape
        paint.setColor(Color.WHITE);
        paint.setTextSize(35);
        paint.setTextAlign(Paint.Align.CENTER);

        Paint.FontMetrics fm = paint.getFontMetrics();
        float yNumber = shapeCenterY - (fm.ascent + fm.descent) / 2;
        canvas.drawText(String.valueOf(number), shapeCenterX, yNumber, paint);

        // Draw station name below
        paint.setColor(Color.BLACK);
        paint.setTextSize(28);
        float extraSpace = 20;
        drawMultilineText(canvas, paint, name, width / 2f, shapeSize * 2 + 10, width - 20, extraSpace);

        return bmp;
    }


    private void drawMultilineText(Canvas canvas, Paint paint, String text, float x,
                                   float startY, int maxWidth, float offset) {

        String[] words = text.split(" ");
        StringBuilder line = new StringBuilder();
        float lineHeight = -paint.ascent() + paint.descent();
        float y = startY + offset;

        for (String word : words) {
            String testLine = (line.length() == 0 ? word : line + " " + word);
            if (paint.measureText(testLine) > maxWidth) {
                canvas.drawText(line.toString(), x, y, paint);
                y += lineHeight;
                line = new StringBuilder(word);
            } else {
                line = new StringBuilder(testLine);
            }
        }
        canvas.drawText(line.toString(), x, y, paint);
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