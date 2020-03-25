import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class CarfaxWeatherForcast {

    private  String apiBase = "http://api.openweathermap.org/data/2.5/weather?q=";
    private  String apiForecast = "http://api.openweathermap.org/data/2.5/forecast/daily?q=";
    private  String units = "Metric";
    private  String lang = "en";
    private  String apiKey = "d0ba495471d24fff42487159824fe4a7";
    static String defaultLocation = "Munich,de";

    public static void main(String[] args) throws IOException {
        CarfaxWeatherForcast carfaxWeatherForcast = new CarfaxWeatherForcast();
        carfaxWeatherForcast.callWeatherApi(defaultLocation);
        carfaxWeatherForcast.callForcastApi(defaultLocation);
    }

    private void callWeatherApi(String location) throws JSONException, IOException {
        try{
            DefaultHttpClient httpClient = new DefaultHttpClient();
            String apiUrl = apiBase + location+ "&appid=" + apiKey + "&mode=json&units=" + units + "&lang=" + lang;
            HttpGet getRequest = new HttpGet(apiUrl);
            getRequest.addHeader("accept", "application/json");
            HttpResponse response = httpClient.execute(getRequest);

            if (response.getStatusLine().getStatusCode() != 200) {
                throw new RuntimeException("Failed : HTTP error code : "
                        + response.getStatusLine().getStatusCode());
            }
            ArrayList<String> weatherResult = getWeather(response);

            System.out.println("Today's Weather is: "+ weatherResult.get(0) );
            System.out.println("Minimum temperature is: "+ weatherResult.get(1) );
            System.out.println("Maximum temperature is: "+ weatherResult.get(2) );
            System.out.println("Wind Speed is: "+ weatherResult.get(3) );

            httpClient.getConnectionManager().shutdown();
        }
        catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static ArrayList<String> getWeather(HttpResponse response) throws JSONException, IOException {
        BufferedReader br = new BufferedReader(
                new InputStreamReader((response.getEntity().getContent())));
        String output;
        System.out.println("Output from Server .... \n");
        if ((output = br.readLine()) != null) {
            ArrayList<String> result = new ArrayList<String>();

            try {
                JSONObject obj = new JSONObject(output);
                result.add(obj.getJSONArray("weather").getJSONObject(0).get("description").toString());
                result.add(obj.getJSONObject("main").get("temp_min").toString());
                result.add(obj.getJSONObject("main").get("temp_max").toString());
                result.add(obj.getJSONObject("wind").get("speed").toString());
                return result;
            } catch (Exception e) {

                return null;
            }
        }
        return null;
    }

    private void callForcastApi(String location) throws JSONException, IOException {
        try{
            DefaultHttpClient httpClient = new DefaultHttpClient();
            String apiUrl = apiForecast + location+ "&appid=" + apiKey + "&mode=json&units=" + units + "&lang=" + lang;
            HttpGet getRequest = new HttpGet(apiUrl);
            getRequest.addHeader("accept", "application/json");
            HttpResponse response = httpClient.execute(getRequest);

            if (response.getStatusLine().getStatusCode() != 200) {
                throw new RuntimeException("Failed : HTTP error code : "
                        + response.getStatusLine().getStatusCode());
            }
            ArrayList<String> weatherResult = getForcast(response);

            System.out.println("Today's Weather is: "+ weatherResult.get(0) );
            System.out.println("Tomorow's Weather is: "+ weatherResult.get(1) );
            System.out.println("Weather in two days is: "+ weatherResult.get(2) );

            httpClient.getConnectionManager().shutdown();
        }
        catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public ArrayList<String> getForcast(HttpResponse response) throws ClientProtocolException, IOException, JSONException {
        BufferedReader br = new BufferedReader(new InputStreamReader((response.getEntity().getContent())));
        String output;
        if ((output = br.readLine()) != null) {
            ArrayList<String> result = new ArrayList<String>();
            try {
                JSONObject obj = new JSONObject(output);
                result.add(obj.getJSONArray("list").getJSONObject(0).getJSONArray("weather").getJSONObject(0).get("description").toString());
                result.add(obj.getJSONArray("list").getJSONObject(1).getJSONArray("weather").getJSONObject(0).get("description").toString());
                result.add(obj.getJSONArray("list").getJSONObject(2).getJSONArray("weather").getJSONObject(0).get("description").toString());

                return result;
            } catch (Exception e) {

                return null;
            }
        }
        return null;
    }

    private int[] getCurrentGeoLocation ()
    {
        return null;
    }
}

