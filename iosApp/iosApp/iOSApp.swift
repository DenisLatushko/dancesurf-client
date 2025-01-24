import SwiftUI
import shared

@main
struct iOSApp: App {

    init() {
        MapInitializer.initMap()
    }

	var body: some Scene {
		WindowGroup {
			ContentView()
                .ignoresSafeArea(.all)
		}
	}
}
