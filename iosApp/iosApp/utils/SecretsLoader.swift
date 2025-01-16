import UIKit

class SecretsLoader {
    
    private let secrets: NSDictionary?
    
    init() {
        let filePath = Bundle.main.path(forResource: "Secrets", ofType: "plist") ?? ""
        secrets = NSDictionary(contentsOfFile: filePath)
    }
    
    public func mapApiKey() -> String {
        return secrets == nil ? "" : secrets!["MAPS_API_KEY"] as? String ?? ""
    }
}
