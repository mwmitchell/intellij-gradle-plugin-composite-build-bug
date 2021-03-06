# Test Project

This project shows a problem related to Gradle's composite build support, a Gradle custom plugin, and IntelliJ. StackOverflow post is [here](https://stackoverflow.com/questions/53975344/gradle-composite-build-with-custom-gradle-plugin-fails-in-intellij-could-not-f).

In summary, the project works entirely fine from the CLI:

```
./gradlew tasks
./gradlew -p project-1 tasks
./gradlew -p test-plugin tasks
```

Even when running gradle within each of the projects:

```
cd project-1
./gradlew tasks
```

```
cd test-plugin
./gradlew tasks
```

The problem occurs when opening the top-level `build.gradle` file in IntelliJ. The result is this error:

```
Parallel execution is an incubating feature.

> Configure project :project-1
resolving test-plugin dependency

FAILURE: Build failed with an exception.

* Where:
Build file '/Users/mattmitchell/Projects/intellij-gradle-plugin-composite-build-bug/project-1/library-a/build.gradle' line: 2

* What went wrong:
A problem occurred evaluating project ':project-1:library-a'.
> Could not find method api() for arguments [{group=com.google.guava, name=guava, version=23.0}] on object of type org.gradle.api.internal.artifacts.dsl.dependencies.DefaultDependencyHandler.

* Try:
Run with --stacktrace option to get the stack trace. Run with --info or --debug option to get more log output. Run with --scan to get full insights.

* Get more help at https://help.gradle.org

CONFIGURE FAILED in 0s
Could not find method api() for arguments [{group=com.google.guava, name=guava, version=23.0}] on object of type org.gradle.api.internal.artifacts.dsl.dependencies.DefaultDependencyHandler.
```

The `api` method comes from the `java-library` plugin, which is added [here](https://github.com/mwmitchell/intellij-gradle-plugin-composite-build-bug/blob/master/project-1/build.gradle#L10).

After publishing the test-plugin to maven local: `./gradlew -p test-plugin publishToMavenLocal`, IntelliJ will succesfully load the project. But when attempting to run `publishToMavenLocal` from IntelliJ, a new error is thrown: `Included build in ./intellij-composite-build-bug/test-plugin has the same root project name 'test-plugin' as the main build.`

If you [uncomment these lines](https://github.com/mwmitchell/intellij-gradle-plugin-composite-build-bug/blob/master/project-1/library-a/build.gradle#L1-L5), and IntelliJ will load the composite build project without error. Obviously not a work around, but may help in debugging the issue.

Another way to get IntelliJ to "pass", is to comment-out the plugin definition in the `plugins` block, as well as the `apply plugin: 'test-plugin', [here](https://github.com/mwmitchell/intellij-gradle-plugin-composite-build-bug/blob/master/project-1/build.gradle#L2) and [here](https://github.com/mwmitchell/intellij-gradle-plugin-composite-build-bug/blob/master/project-1/build.gradle#L6). Not a work around, but may be a valuable clue as to what's going on in terms of project loading.
