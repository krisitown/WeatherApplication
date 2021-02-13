package bg.sofia.uni.fmi.mjt.weather.dto;

import java.util.Arrays;
import java.util.Objects;

public class WeatherForecast {
    private WeatherData main;
    private WeatherCondition[] weather;

    public WeatherForecast(WeatherCondition[] weather, WeatherData main) {
        this.main = main;
        this.weather = weather;
    }

    public WeatherData getMain() {
        return main;
    }

    public WeatherCondition[] getWeather() {
        return weather;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        WeatherForecast forecast = (WeatherForecast) o;
        return Objects.equals(main, forecast.main) &&
                Arrays.equals(weather, forecast.weather);
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(main);
        result = 31 * result + Arrays.hashCode(weather);
        return result;
    }
}
