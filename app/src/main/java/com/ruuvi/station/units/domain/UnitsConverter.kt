package com.ruuvi.station.units.domain

import android.content.Context
import com.ruuvi.station.R
import com.ruuvi.station.app.preferences.PreferencesRepository
import com.ruuvi.station.units.model.HumidityUnit
import com.ruuvi.station.units.model.PressureUnit
import com.ruuvi.station.units.model.TemperatureUnit
import com.ruuvi.station.util.Utils

class UnitsConverter (
        private val context: Context,
        private val preferences: PreferencesRepository
) {

    // Temperature

    fun getTemperatureUnit(): TemperatureUnit = preferences.getTemperatureUnit()

    fun getAllTemperatureUnits(): Array<TemperatureUnit> = TemperatureUnit.values()

    fun getTemperatureUnitString(): String = context.getString(getTemperatureUnit().unit)

    fun getTemperatureValue(temperatureCelsius: Double): Double {
        return when (getTemperatureUnit()) {
            TemperatureUnit.CELSIUS -> temperatureCelsius
            TemperatureUnit.KELVIN-> TemperatureConverter.celsiusToKelvin(temperatureCelsius)
            TemperatureUnit.FAHRENHEIT -> TemperatureConverter.celsiusToFahrenheit(temperatureCelsius)
        }
    }

    fun getTemperatureString(temperature: Double?): String =
        if (temperature == null) {
            NO_VALUE_AVAILABLE
        } else {
            context.getString(R.string.temperature_reading, getTemperatureValue(temperature), getTemperatureUnitString())
        }

    fun getTemperatureStringWithoutUnit(temperature: Double?): String =
        if (temperature == null) {
            NO_VALUE_AVAILABLE
        } else {
            context.getString(R.string.temperature_reading, getTemperatureValue(temperature), "")
        }

    // Pressure

    fun getPressureUnit(): PressureUnit = preferences.getPressureUnit()

    fun getAllPressureUnits(): Array<PressureUnit> = PressureUnit.values()

    fun getPressureUnitString(): String = context.getString(getPressureUnit().unit)

    fun getPressureValue(pressurePascal: Double): Double {
        return when (getPressureUnit()) {
            PressureUnit.PA -> pressurePascal
            PressureUnit.HPA-> PressureConverter.pascalToHectopascal(pressurePascal)
            PressureUnit.MMHG -> PressureConverter.pascalToMmMercury(pressurePascal)
            PressureUnit.INHG -> PressureConverter.pascalToInchMercury(pressurePascal)
        }
    }

    fun getPressureString(pressure: Double?): String {
        return if (pressure == null) {
            NO_VALUE_AVAILABLE
        } else {
            if (getPressureUnit() == PressureUnit.PA) {
                context.getString(R.string.pressure_reading_pa, getPressureValue(pressure), getPressureUnitString())
            } else {
                context.getString(R.string.pressure_reading, getPressureValue(pressure), getPressureUnitString())
            }
        }
    }

    // Humidity

    fun getHumidityUnit(): HumidityUnit = preferences.getHumidityUnit()

    fun getAllHumidityUnits(): Array<HumidityUnit> = HumidityUnit.values()

    fun getHumidityUnitString(): String = context.getString(getHumidityUnit().unit)

    fun getHumidityValue(humidity: Double, temperature: Double): Double {
        val converter = HumidityConverter(temperature, humidity/100)

        return when (getHumidityUnit()) {
            HumidityUnit.PERCENT -> Utils.round(humidity, 2)
            HumidityUnit.GM3-> Utils.round(converter.ah, 2)
            HumidityUnit.DEW -> {
                when (getTemperatureUnit()) {
                    TemperatureUnit.CELSIUS -> Utils.round(converter.Td ?: 0.0, 2)
                    TemperatureUnit.KELVIN-> Utils.round(converter.TdK ?: 0.0, 2)
                    TemperatureUnit.FAHRENHEIT -> Utils.round(converter.TdF ?: 0.0, 2)
                }
            }
        } ?: 0.0
    }

    fun getHumidityString(humidity: Double?, temperature: Double?): String {
        return if (humidity == null || temperature == null) {
            NO_VALUE_AVAILABLE
        } else {
            if (getHumidityUnit() == HumidityUnit.DEW) {
                context.getString(R.string.humidity_reading, getHumidityValue(humidity, temperature), getTemperatureUnitString())
            } else {
                context.getString(R.string.humidity_reading, getHumidityValue(humidity, temperature), getHumidityUnitString())
            }
        }
    }

    fun getSignalString(rssi: Int): String =
        if (rssi != 0) {
            context.getString(R.string.signal_reading, rssi, context.getString(R.string.signal_unit))
        } else {
            context.getString(R.string.signal_reading_zero, context.getString(R.string.signal_unit))
        }

    companion object {
        const val NO_VALUE_AVAILABLE = "-"
    }
}