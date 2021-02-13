package bg.sofia.uni.fmi.mjt.weather;

import static org.mockito.Mockito.when;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import bg.sofia.uni.fmi.mjt.weather.dto.WeatherCondition;
import bg.sofia.uni.fmi.mjt.weather.dto.WeatherData;
import bg.sofia.uni.fmi.mjt.weather.dto.WeatherForecast;
import bg.sofia.uni.fmi.mjt.weather.exceptions.LocationNotFoundException;
import bg.sofia.uni.fmi.mjt.weather.exceptions.WeatherForecastClientException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.io.IOException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

@RunWith(MockitoJUnitRunner.class)
public class WeatherForecastClientTest {

    @Mock
    private HttpClient mockedClient;

    @Mock
    private HttpResponse<String> mockedResponse;

    private WeatherForecastClient weatherForecastClient;

    @Before
    public void setUp() {
        this.weatherForecastClient = new WeatherForecastClient(mockedClient);
    }

    @Test
    public void testRequestIsMadeAndValidResponseIsReceived() throws IOException, InterruptedException {
        when(mockedClient.send(Mockito.any(HttpRequest.class),
                ArgumentMatchers.<HttpResponse.BodyHandler<String>>any()))
                .thenReturn(mockedResponse);

        when(mockedResponse.body())
                .thenReturn("{\"weather\": [{\"description\": \"clear\"}], \"main\": "
                        + "{\"temp\": 10.3, \"feels_like\": 7.52}}");

        when(mockedResponse.statusCode()).thenReturn(200);

        WeatherCondition weatherCondition = new WeatherCondition("clear");
        WeatherData weatherData = new WeatherData(10.3, 7.52);
        WeatherForecast expected = new WeatherForecast(new WeatherCondition[] {weatherCondition}, weatherData);

        WeatherForecast forecast = weatherForecastClient.getForecast("Sofia");

        assertEquals(expected.getMain().getTemp(), forecast.getMain().getTemp());
        assertEquals(expected.getWeather()[0].getDescription(), forecast.getWeather()[0].getDescription());
    }

    @Test
    public void testRequestIsMadeInvalidLocation() throws IOException, InterruptedException {
        when(mockedClient.send(Mockito.any(HttpRequest.class),
                ArgumentMatchers.<HttpResponse.BodyHandler<String>>any()))
                .thenReturn(mockedResponse);

        when(mockedResponse.statusCode()).thenReturn(404);

        try {
            this.weatherForecastClient.getForecast("Not a city");
            fail("A LocationNotFoundException was not thrown on return of a 404 status code.");
        } catch (LocationNotFoundException e) {
            //this is just to make sure the appropriate exception is thrown
        }
    }

    @Test
    public void testRequestIsMadeGeneralError() throws IOException, InterruptedException {
        when(mockedClient.send(Mockito.any(HttpRequest.class),
                ArgumentMatchers.<HttpResponse.BodyHandler<String>>any()))
                .thenReturn(mockedResponse);

        when(mockedResponse.statusCode()).thenReturn(500);

        try {
            this.weatherForecastClient.getForecast("Sofia");
            fail("A WeatherForcastClientException was not thrown when server returned error!");
        } catch (WeatherForecastClientException e) {
            //this is just to make sure the appropriate exception is thrown
        }
    }

    @Test
    public void checkingHashCodeAndEquals() {
        WeatherCondition conditionOne = new WeatherCondition("clear");
        WeatherData dataOne = new WeatherData(10.3, 7.52);
        WeatherForecast forcastOne =
                new WeatherForecast(new WeatherCondition[] {conditionOne}, dataOne);

        WeatherCondition conditionTwo = new WeatherCondition("clear");
        WeatherData dataTwo = new WeatherData(10.3, 7.52);
        WeatherForecast forcastTwo =
                new WeatherForecast(new WeatherCondition[] {conditionTwo}, dataTwo);

        assertEquals(conditionOne, conditionTwo);
        assertEquals(dataOne, dataTwo);
        assertEquals(forcastOne, forcastTwo);
    }

}
