rootProject.name = "SimpleQiitaClient"

include(":app")

include(":common")
project(":common").projectDir = File("modules/common")

include(":ui")
project(":ui").projectDir = File("modules/ui")

include(":application")
project(":application").projectDir = File("modules/application")

include(":data")
project(":data").projectDir = File("modules/data")
