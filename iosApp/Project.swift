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
                    cd "$SRCROOT/.."

                    echo "Building Kotlin Multiplatform Framework..."
                    echo "PLATFORM_NAME: $PLATFORM_NAME"
                    echo "ARCHS: $ARCHS"
                    echo "CONFIGURATION: $CONFIGURATION"

                    # Determine the architecture
                    if [ "$PLATFORM_NAME" = "iphonesimulator" ]; then
                        if [ "$ARCHS" = "arm64" ] || [ "$NATIVE_ARCH" = "arm64" ]; then
                            ARCH_NAME="iosSimulatorArm64"
                        else
                            ARCH_NAME="iosX64"
                        fi
                    else
                        ARCH_NAME="iosArm64"
                    fi

                    echo "Target architecture: $ARCH_NAME"

                    # Build the framework
                    if [ "$CONFIGURATION" = "Debug" ]; then
                        ./gradlew :composeApp:linkDebugFramework$ARCH_NAME
                        FRAMEWORK_PATH="composeApp/build/bin/$ARCH_NAME/debugFramework/ComposeApp.framework"
                    else
                        ./gradlew :composeApp:linkReleaseFramework$ARCH_NAME
                        FRAMEWORK_PATH="composeApp/build/bin/$ARCH_NAME/releaseFramework/ComposeApp.framework"
                    fi

                    # Ensure framework was built
                    if [ ! -d "$FRAMEWORK_PATH" ]; then
                        echo "error: Framework not found at $FRAMEWORK_PATH"
                        exit 1
                    fi

                    # Copy framework to a consistent location for Xcode to find
                    DEST_PATH="$BUILT_PRODUCTS_DIR/ComposeApp.framework"
                    rm -rf "$DEST_PATH"
                    cp -R "$FRAMEWORK_PATH" "$DEST_PATH"

                    echo "Framework copied to $DEST_PATH"
                    """,
                    name: "Compile Kotlin Multiplatform Framework",
                    basedOnDependencyAnalysis: false
                ),
                .post(
                    script: """
                    # Embed the framework in the app bundle
                    FRAMEWORK_PATH="$BUILT_PRODUCTS_DIR/ComposeApp.framework"
                    APP_PATH="$BUILT_PRODUCTS_DIR/$FULL_PRODUCT_NAME"
                    APP_FRAMEWORKS_PATH="$APP_PATH/Frameworks"

                    echo "Embedding framework into app bundle..."
                    echo "Framework source: $FRAMEWORK_PATH"
                    echo "App destination: $APP_FRAMEWORKS_PATH"

                    if [ -d "$FRAMEWORK_PATH" ]; then
                        # Create Frameworks directory in app bundle
                        mkdir -p "$APP_FRAMEWORKS_PATH"

                        # Remove old framework if exists
                        rm -rf "$APP_FRAMEWORKS_PATH/ComposeApp.framework"

                        # Copy framework to app bundle
                        cp -R "$FRAMEWORK_PATH" "$APP_FRAMEWORKS_PATH/"

                        echo "Framework embedded at: $APP_FRAMEWORKS_PATH/ComposeApp.framework"

                        # Update framework's install name
                        install_name_tool -id "@rpath/ComposeApp.framework/ComposeApp" "$APP_FRAMEWORKS_PATH/ComposeApp.framework/ComposeApp"

                        # Code sign the framework
                        if [ -n "$EXPANDED_CODE_SIGN_IDENTITY" ] && [ "$CODE_SIGNING_ALLOWED" != "NO" ]; then
                            codesign --force --deep --sign "$EXPANDED_CODE_SIGN_IDENTITY" "$APP_FRAMEWORKS_PATH/ComposeApp.framework"
                        fi
                    else
                        echo "error: ComposeApp.framework not found at $FRAMEWORK_PATH"
                        exit 1
                    fi
                    """,
                    name: "Embed Kotlin Framework",
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
                    "FRAMEWORK_SEARCH_PATHS": ["$(inherited)", "$(BUILT_PRODUCTS_DIR)"],
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