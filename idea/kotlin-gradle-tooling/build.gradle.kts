
description = "Kotlin Gradle Tooling support"

apply { plugin("kotlin") }

dependencies {
    compile(projectDist(":kotlin-stdlib"))
    compile(project(":compiler:cli-common"))
    compile(ideaSdkDeps("gradle-api",
                        "gradle-tooling-extension-api",
                        "gradle",
                        subdir = "plugins/gradle/lib"))
}

sourceSets {
    "main" { projectDefault() }
    "test" {}
}

runtimeJar()

ideaPlugin()
