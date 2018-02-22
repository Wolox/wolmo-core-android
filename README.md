[![Build Status](https://www.bitrise.io/app/effc7a938ebd8bc5/status.svg?token=5aAalVW4BGoIUIy-xgHv3A&branch=master)](https://www.bitrise.io/app/effc7a938ebd8bc5)
[![Release](https://jitpack.io/v/Wolox/wolmo-core-android.svg)](https://jitpack.io/#Wolox/wolmo-core-android)
[![Coverage Status](https://coveralls.io/repos/github/Wolox/wolmo-core-android/badge.svg?branch=master)](https://coveralls.io/github/Wolox/wolmo-core-android?branch=master)

<p align="center">
  <img height="140px" width="400px" src="https://cloud.githubusercontent.com/assets/4109119/25450281/cac5979e-2a94-11e7-9176-8e323df5dab8.png"/>
</p>

# WOLMO

WOLMO is a an opinionated framework designed to reduce the time taken to develop mobile applications by cutting off boilerplate code and offering MVP-ready fragments and activities while also providing utilities and helper classes for everyday tasks.

#### Design principles
1. **Opinionated:** WOLMO is an opinionated framework. That means that we aim to define one "best way" of handling everyday tasks, including using certain third-party libraries, approaches and architectural decisions that we believe will make app development faster and easier.
2. **Modular:** The framework delegates functionalities to modules designed specifically to fulfil their role.
3. **Mashable:** WOLMO is an open source community-driven project intended for real life projects. Every contribution will be taken into account.

### Modules

WOLMO is made up of several modules, each one supporting a different role in the production of modern apps. The following are the current modules available for the WOLMO framework.

* **CORE:** This module provides standalone base classes and utilities of the framework. As a general rule, this module will be required in any WOLMO-based project. CORE is also a generally required dependency of other modules.
* **NETWORKING:** This module provides offline capabilities and abstracts the network layer of the rest of the project.
* **AUTH:** Provides utilities for accounts, login and signup features.
* **MAPS:** Google Maps helpers classes and views.

## Installation

Import the module as a library in your project using Gradle:

**root build.gradle**
```groovy
allprojects {
  repositories {
    ...
    maven { url 'https://jitpack.io' }
  }
}
```
**your app module build.gradle**
```groovy
dependencies {
        compile 'com.github.Wolox:wolmo-core-android:master-SNAPSHOT'
}
```
Note: The above line will download the latest version of the module, if you want to run a specific version replace `master-SNAPSHOT` with `2.0.0` or any other version. Available versions can be found here: [Github releases](https://github.com/Wolox/wolmo-core-android/releases)

## How to use

WOLMO v2 uses Dagger as dependency injection framework. It provides default modules and dependencies for all the utils in WOLMO and the ones a basic project could need.
You need to understand how Dagger Works to be able to use WOLMO the right way. In this examples we assume you know how `dagger-android` works. For more info see the [original documentation](https://google.github.io/dagger/android.html).

To use WOLMO in your project you need to create at least one dagger `Component` to provide the dependencies for your project.
WOLMO provides some `modules` that you need to add to your `Component` for it to work. These modules provide all the dependencies WOLMO classes need to work so the only thing you need to do is extend the base classes.

In the package `ar.com.wolox.wolmo.core.di` you can find all the provided modules and scopes defined in WOLMO.

The next example includes the modules: `DefaultModule`, `ContextModule` from WOLMO Core. `AndroidSupportInjectionModule` is needed by Dagger and `AppModule.class` is provided by the application.
We are binding `sharedPrefName` used by `ContextModule.provideSharedPreferences(...)` so it's easier to customize the name.

```java
@ApplicationScope
@Component( modules = { AndroidSupportInjectionModule.class, DefaultModule.class,
                   ContextModule.class, AppModule.class})
public interface AppComponent extends AndroidInjector<BootstrapApplication> {

    @Component.Builder
    abstract class Builder extends AndroidInjector.Builder<BootstrapApplication> {

        @BindsInstance
        public abstract Builder application(Application application);

        @BindsInstance
        public abstract Builder sharedPreferencesName(String sharedPrefName);

    }
}
```

Then you can use it in your app like this:

```java
public class BootstrapApplication extends WolmoApplication {

    @Override
    public void onInit() {
	...
    }

    @Override
    protected AndroidInjector<BootstrapApplication> applicationInjector() {
        return DaggerAppComponent.builder()
            .sharedPreferencesName(Configuration.SHARED_PREFERENCES_NAME).application(this)
            .create(this);
    }
}
```

After adding your fragments and activities to your `AppModule` you can start using WOLMO. (See Dagger documentation for more info).

```java
@Module
public abstract class AppModule {

    @ContributesAndroidInjector
    abstract ExampleActivity exampleActivity();

    @ContributesAndroidInjector
    abstract ExampleFragment exampleFragment();
}
```

The only thing you need to do to inject dependencies in a `Fragment` is to extend `WolmoFragment`, add an entry to your `AppModule` and you are ready to go.
*Note: WOLMO cannot provide other dependencies that are not defined in the modules provided, if you need extra dependencies for your Fragment or Presenter you need to create a Module and provide it in the same way as Dagger requires.*

WOLMO also provides some scopes for you to use in your application, you can use:

* ActivityScope
* FragmentScope

when you need to define a dependency only needed in those scopes or create your own.

**ApplicationScope** is reserved for *singletons* or another dependencies with only one instance.

## Features

CORE is the base module of the framework and provides all of the must have classes and utilities. These modules define the opinionated character of the framework and may be used by other modules to fulfill their role.

> Every module may depend on CORE, but not the other way around.

These features can be found in CORE:

* MVP-ready activities and fragments
* Files helpers
* Camera and image helpers

## <a name="topic-contributing"></a> Contributing

1. Fork it
2. Create your feature branch (`git checkout -b my-new-feature`)
3. Commit your changes (`git commit -am 'Add some feature'`)
4. Push your branch (`git push origin my-new-feature`)
5. Create a new Pull Request

## <a name="topic-about"></a> About

This project is maintained by [Juan Ignacio Molina](https://github.com/juanignaciomolina)
and it was written by [Wolox](http://www.wolox.com.ar).

![Wolox](https://raw.githubusercontent.com/Wolox/press-kit/master/logos/logo_banner.png)

## <a name="topic-license"></a> License

**WOLMO CORE** is available under the MIT [license](https://raw.githubusercontent.com/Wolox/wolmo-core-android/master/LICENSE.md).

    Copyright (c) Wolox S.A

    Permission is hereby granted, free of charge, to any person obtaining a copy
    of this software and associated documentation files (the "Software"), to deal
    in the Software without restriction, including without limitation the rights
    to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
    copies of the Software, and to permit persons to whom the Software is
    furnished to do so, subject to the following conditions:

    The above copyright notice and this permission notice shall be included in
    all copies or substantial portions of the Software.

    THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
    IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
    FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
    AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
    LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
    OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
    THE SOFTWARE.
