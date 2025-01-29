import SwiftUI
import shared

@main
struct iOSApp: App {

    init() {
        DIInitializer(isDebug: BuildConfig.isDebug()).start()
        MapInitializer.initMap()
    }

	var body: some Scene {
		WindowGroup {
			ContentView()
                .ignoresSafeArea(.all)
		}
	}
}
