import Foundation

/// A build info class
class BuildConfig {
    static func isDebug() -> Bool {
        var flag = false
        #if DEBUG
            flag = true
        #endif
        return flag
    }
}
