import SwiftUI
import shared

struct ContentView: View {
	var body: some View {
        ZStack {
            ContentViewController()
        }
	}
}

private struct ContentViewController: UIViewControllerRepresentable {
    typealias UIViewControllerType = UIViewController

    func makeUIViewController(context: Context) -> UIViewControllerType {
        return AppViewControllerKt.AppViewController()
    }

    func updateUIViewController(_ uiViewController: UIViewControllerType, context: Context) {
        // no op
    }
}
