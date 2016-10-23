package ga.washmose.mose.Util;

public class UGeoPoint {
        public double lat;
        public double lng;

        public UGeoPoint(double lat, double lng) {
            this.lat = lat;
            this.lng = lng;
        }

        public String toString() {
            return "lat: " + lat + ", lng: " + lng;
        }
    }