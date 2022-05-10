package com.sweteam5.ladybugadmin;

import android.location.Location;

public class BusLocator {
    private int currentIndex = 0;

    private static StationDataManager stationDataManager = null;

    public BusLocator(int startIndex) {
        currentIndex = startIndex;
    }

    public BusLocator(int startIndex, StationDataManager pStationDataManager) {
        currentIndex = startIndex;
        if(stationDataManager == null)
            stationDataManager = pStationDataManager;
    }

    private boolean isNearStation(Location currentLocation) {
        Station station = stationDataManager.stations[(currentIndex + 1) / 2];

        Location stationLocation = new Location(station.getName());
        stationLocation.setLatitude(station.getLatitude());
        stationLocation.setLongitude(station.getLongitude());
        double distance = currentLocation.distanceTo(stationLocation);

        if(distance < 5) {
            return true;
        }
        else {
            return false;
        }
    }

    public void setCurrentIndex(Location currentLocation) {
        boolean isNear = isNearStation(currentLocation);
        // currentIndex가 홀수이면 정류장 사이에 있다는 뜻
        if(isNear && currentIndex % 2 == 1) {
            currentIndex++;
        }
        else if(!isNear && currentIndex % 2 == 0){
            currentIndex++;
        }

        if(currentIndex >= stationDataManager.stations.length) {
            currentIndex = 0;
        }
    }

    public int getCurrentIndex() {
        return currentIndex;
    }
}
