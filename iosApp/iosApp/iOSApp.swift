import SwiftUI
import shared

@main
struct iOSApp: App {

    init(){
        DIInitializer.Companion()
            .doInitDi(isDebug: BuildConfig.isDebug())
    }

	var body: some Scene {
		WindowGroup {
			ContentView()
		}
	}
}
