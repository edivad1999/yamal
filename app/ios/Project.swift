import ProjectDescription

let project = Project(
    name: "yamal",
    organizationName: "com.yamal",
    options: .options(
        automaticSchemesOptions: .disabled,
        disableBundleAccessors: true,
        disableSynthesizedResourceAccessors: true
    ),
    packages: [],
    settings: .settings(
        configurations: [
            .debug(name: "Debug"),
            .release(name: "Release")
        ]
    ),
    targets: [
        .target(
            name: "yamal",
            destinations: [.iPhone, .iPad],
            product: .app,
            bundleId: "com.yamal.app",
            deploymentTargets: .iOS("15.0"),
            infoPlist: .file(path: "iosApp/Info.plist"),
            sources: ["iosApp/**/*.swift"],
            resources: [
                "iosApp/Assets.xcassets",
                "iosApp/Preview Content/**"
            ],
            scripts: [
                .pre(
                    script: """
                    set -e
                    cd "$SRCROOT/../.."

                    echo "Building and embedding Kotlin Multiplatform Framework..."
                    echo "PLATFORM_NAME: $PLATFORM_NAME"
                    echo "ARCHS: $ARCHS"
                    echo "CONFIGURATION: $CONFIGURATION"
                    echo "TARGET_BUILD_DIR: $TARGET_BUILD_DIR"

                    # Use the official Gradle task that handles framework building, embedding, signing, and resource syncing
                    ./gradlew :app:shared:embedAndSignAppleFrameworkForXcode
                    """,
                    name: "Compile Kotlin Multiplatform Framework",
                    basedOnDependencyAnalysis: false
                )
            ],
            dependencies: [],
            settings: .settings(
                base: [
                    "ENABLE_USER_SCRIPT_SANDBOXING": "NO",
                    "ENABLE_PREVIEWS": "YES",
                    "SWIFT_VERSION": "5.9",
                    "LD_RUNPATH_SEARCH_PATHS": ["$(inherited)", "@executable_path/Frameworks"],
                    "VALIDATE_PRODUCT": "NO",
                    "FRAMEWORK_SEARCH_PATHS": [
                        "$(inherited)",
                        "$(SRCROOT)/../../app/shared/build/xcode-frameworks/$(CONFIGURATION)/$(SDK_NAME)"
                    ],
                    "OTHER_LDFLAGS": ["$(inherited)", "-framework", "ComposeApp"]
                ]
            )
        )
    ],
    schemes: [
        .scheme(
            name: "yamal",
            shared: true,
            buildAction: .buildAction(
                targets: ["yamal"]
            ),
            runAction: .runAction(
                configuration: "Debug",
                executable: "yamal"
            ),
            archiveAction: .archiveAction(
                configuration: "Release"
            ),
            profileAction: .profileAction(
                configuration: "Release",
                executable: "yamal"
            ),
            analyzeAction: .analyzeAction(
                configuration: "Debug"
            )
        )
    ]
)
