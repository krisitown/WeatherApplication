package bg.sofia.uni.fmi.mjt.weather.dto;

import java.util.Objects;

public class WeatherData {
    private Double temp;
    private Double feelsLike;

    public WeatherData(Double temp, Double feelsLike) {
        this.temp = temp;
        this.feelsLike = feelsLike;
    }

    public Double getTemp() {
        return temp;
    }

    public Double getFeelsLike() {
        return feelsLike;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        WeatherData that = (WeatherData) o;
        return Objects.equals(temp, that.temp) &&
                Objects.equals(feelsLike, that.feelsLike);
    }

    @Override
    public int hashCode() {
        return Objects.hash(temp, feelsLike);
    }
}
