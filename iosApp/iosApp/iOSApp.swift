import SwiftUI
import ComposeApp
@main
struct iOSApp: App {
    
    init() {
        KoinInitializerKt.defaultKoinInitializer()

    }
	var body: some Scene {
		WindowGroup {
			ContentView()
                .onOpenURL { url in
                    print(url.absoluteString)
                    LoginUtilities.shared.parseUrlResult(urlResult: url.absoluteString)
                }
		}
	}

}
