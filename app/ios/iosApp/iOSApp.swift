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
                    // Log the incoming URL
                    print("Received URL: \(url.absoluteString)")

                    // Parse the URL and extract the code parameter
                    if let urlComponents = URLComponents(url: url, resolvingAgainstBaseURL: false),
                       let queryItems = urlComponents.queryItems {
                        for queryItem in queryItems {
                            if queryItem.name == "code" {
                                if let codeValue = queryItem.value {
                                    print("OAuth code received: \(codeValue)")
                                    // Call the Kotlin/Native method with proper class name
                                    LoginUtilities.shared.parseUrlResult(urlResult: codeValue)
                                    break
                                }
                            }
                        }
                    }
                }
        }
    }
}
