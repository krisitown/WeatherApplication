package bg.sofia.uni.fmi.mjt.weather.exceptions;

public class WeatherForecastClientException extends RuntimeException {
    public WeatherForecastClientException() {
        //its to create a default constructor
    }

    public WeatherForecastClientException(Throwable cause) {
        super(cause);
    }
}
