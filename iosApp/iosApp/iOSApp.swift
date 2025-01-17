import SwiftUI
import shared
import GoogleMaps

@main
struct iOSApp: App {

    init(){
        MapInitializer.initMap()
    }

	var body: some Scene {
		WindowGroup {
			ContentView()
                .ignoresSafeArea()
		}
	}
}
