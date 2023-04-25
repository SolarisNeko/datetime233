package com.neko233.datetime.settings;

/**
 * @author SolarisNeko
 * Date on 2023-04-23
 */
public class SystemClockSettingsTest {

    public void test() {
        SystemClockSettings.modifyOsDateTime(System.currentTimeMillis() + 5000);
    }

}