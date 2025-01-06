//
//  BuildConfig.swift
//  iosApp
//
//  Created by DenisLatushko on 06/01/2025.
//  Copyright © 2025 orgName. All rights reserved.
//

import Foundation

class BuildConfig {
    static func isDebug() -> Bool {
        var flag = false
        #if DEBUG
            flag = true
        #endif
        return flag
    }
}
