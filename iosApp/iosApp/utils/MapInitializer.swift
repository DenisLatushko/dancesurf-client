import GoogleMaps

class MapInitializer {
    static func initMap() {
        GMSServices.provideAPIKey(SecretsLoader().mapApiKey())
    }
}
