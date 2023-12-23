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
                    if let urlComponents = URLComponents(url: url, resolvingAgainstBaseURL: false),
                       let queryItems = urlComponents.queryItems {
                        for queryItem in queryItems {
                            if queryItem.name == "code" {
                                if let codeValue = queryItem.value {
                                    // Use the codeValue here
                                    print("Code parameter value: \(codeValue)")
                                    // Call your method with the code parameter
                                    LoginUtilities.shared.parseUrlResult(urlResult: codeValue)
                                    // Assuming you want to break out of the loop after finding the first "code" parameter
                                    break
                                }
                            }
                        }
                    }
  
                    print(url.absoluteString)
                }
		}
	}

}
