package bg.sofia.uni.fmi.mjt.weather;

import bg.sofia.uni.fmi.mjt.weather.dto.WeatherForecast;
import bg.sofia.uni.fmi.mjt.weather.exceptions.LocationNotFoundException;
import bg.sofia.uni.fmi.mjt.weather.exceptions.WeatherForecastClientException;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;

public class WeatherForecastClient {
    private final String apiUrl = "http://api.openweathermap.org/data/2.5/weather?q=%s&units=metric&lang=bg&appid=%s";
    private final String apiKey = "6d137c68cfe9f67de5a90a692c34afb5";

    private HttpClient weatherHttpClient;
    private Gson jsonParser;

    public WeatherForecastClient(HttpClient weatherHttpClient) {
        this.weatherHttpClient = weatherHttpClient;
        this.jsonParser = new GsonBuilder().create();
    }

    /**
     * Fetches the weather forecast for the specified city.
     *
     * @return the forecast
     * @throws LocationNotFoundException if the city is not found
     * @throws WeatherForecastClientException if information regarding the weather for
     * this location could not be retrieved
     */
    public WeatherForecast getForecast(String city) throws WeatherForecastClientException {
        try {
            HttpRequest forecastRequest = HttpRequest.newBuilder(URI.create(createEndpoint(city)))
                    .header("Content-Type", "application/json")
                    .GET()
                    .build();

            HttpResponse<String> response = this.weatherHttpClient
                    .send(forecastRequest, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 404) {
                throw new LocationNotFoundException();
            } else if (response.statusCode() != 200) {
                throw new WeatherForecastClientException();
            }

            return jsonParser.fromJson(response.body(), WeatherForecast.class);
        } catch (IOException | InterruptedException e) {
            throw new WeatherForecastClientException(e);
        }
    }

    private String createEndpoint(String city) {
        return String.format(this.apiUrl, URLEncoder.encode(city, StandardCharsets.UTF_8), apiKey);
    }

}
