rootProject.name = "SimpleQiitaClient"

include(":app")

include(":common")
project(":common").projectDir = File("modules/common")

include(":ui")
project(":ui").projectDir = File("modules/ui")
