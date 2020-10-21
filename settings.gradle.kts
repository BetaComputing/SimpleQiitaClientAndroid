rootProject.name = "SimpleQiitaClient"

include(":app")

include(":common")
project(":common").projectDir = File("modules/common")
