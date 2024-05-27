package br.com.fatex.backend_fatex.services;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class GeocodingService {

    @Value("${google.maps.api.key}")
    private String apiKey;

    public String getCoordinates(String address) {
        String url = "https://maps.googleapis.com/maps/api/geocode/json?address=" + address.replace(" ", "%20") + "&key=" + apiKey;
        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            HttpGet request = new HttpGet(url);
            HttpResponse response = httpClient.execute(request);
            HttpEntity entity = response.getEntity();

            if (entity != null) {
                String result = EntityUtils.toString(entity);
                JSONObject jsonObject = new JSONObject(result);

                if ("OK".equals(jsonObject.getString("status"))) {
                    JSONArray results = jsonObject.getJSONArray("results");
                    JSONObject geometry = results.getJSONObject(0).getJSONObject("geometry");
                    JSONObject location = geometry.getJSONObject("location");

                    double latitude = location.getDouble("lat");
                    double longitude = location.getDouble("lng");

                    return latitude + "," + longitude;
                } else {
                    return "Geocoding API error: " + jsonObject.getString("status");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            return "Error: " + e.getMessage();
        }
        return "Error retrieving coordinates.";
    }
}
