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

### Usage

Import the module as alibrary in your project using Gradle:

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
Note: The above line will download the latest version of the module, if you want to run an specific version replace `master-SNAPSHOT` with `1.0.0` or any other version. Avaiable versions can be found here: [Github releases](https://github.com/Wolox/wolmo-core-android/releases)

## Features

CORE is the base module of the framework and provides all of the must have classes and utilities. This modules defines the opinionated character of the framework and may be used by other modules to fulfill their role.

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
