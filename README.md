# Alloy

![GitHub Actions](https://github.com/StarChart-Labs/alloy/workflows/Java%20CI/badge.svg?branch=master) [![Code Coverage](https://img.shields.io/codecov/c/github/StarChart-Labs/alloy.svg)](https://codecov.io/github/StarChart-Labs/alloy) [![Black Duck Security Risk](https://copilot.blackducksoftware.com/github/repos/StarChart-Labs/alloy/branches/master/badge-risk.svg)](https://copilot.blackducksoftware.com/github/repos/StarChart-Labs/alloy/branches/master) [![Changelog validated by Chronicler](https://chronicler.starchartlabs.org/images/changelog-chronicler-success.png)](https://chronicler.starchartlabs.org/) [![License](https://img.shields.io/badge/License-MIT-blue.svg)](https://opensource.org/licenses/MIT)

* [Contributing](#contributing)
* [Legal](#legal)
* [Reporting Vulnerabilities](#reporting-vulnerabilities)
* [Projects](#projects)
* [This Looks a Lot Like Guava...](#this-looks-a-lot-like-guava)

General Java utilities and quality of life tools

Alloy is intended to help streamline use of common Java patterns, and reduce boilerplate code

For information on migrating between major versions of Alloy, see the [migration guide](./docs/MIGRATION.md)

## Contributing

Information for how to contribute to Alloy can be found in [the contribution guidelines](./docs/CONTRIBUTING.md)

## Legal

Alloy is distributed under the [MIT License](https://opensource.org/licenses/MIT). There are no requirements for using it in your own project (a line in a NOTICES file is appreciated but not necessary for use)

The requirement for a copy of the license being included in distributions is fulfilled by a copy of the [LICENSE](./LICENSE) file being included in constructed JAR archives

## Reporting Vulnerabilities

If you discover a security vulnerability, contact the development team by e-mail at `vulnerabilities@starchartlabs.org`

## Projects

### alloy-core

[![Maven Central](https://img.shields.io/maven-central/v/org.starchartlabs.alloy/alloy-core.svg)](https://mvnrepository.com/artifact/org.starchartlabs.alloy/alloy-core)

Alloy Core defines low-level utilities for basic Java use patterns. This primarily consists of utility classes such as `Suppliers`, `Throwables`, and other similar classes which streamline use of the associated Java objects.

## This Looks a Lot Like Guava...

Alloy was heavily inspired by the Google Guava libraries, and replicates some of the core functionality of that utility. While the team working on Guava made an excellent library we've used over and over, enough minor annoyances have been encountered over time that we chose to make the effort to iterate on the established pattern.

There are a couple issues with using Guava in certain environments that led to the creation of Alloy

- Guava contains a large amount of utility that is now built into the Java language since Java 8. As a large amount of its code was based upon the Guava-specific contracts, it is very difficult for Guava to safely remove these now-duplicate elements
- Partially because of the above, Guava is a sizable library, not suitable for specifically size-sensitive applications. Aside from this, there are organization routes that could be taken to further reduce size by a new library
- Guava uses a somewhat non-standard approach to API designations - Beta tags indicate functionality should be used with caution, and Deprecated tags are not used often, and Deprecated APIs are rarely removed, even across major versions. While there are advantages to this approach, it leads to a build-up of "dead" APIs, especially those assimilated into Java as of Java 8

Alloy intends, in part, to create a stripped-down post-Java-8 version of these utilities which uses a more traditional approach to deprecation and removal. To address some of the above, Alloy intends to

- Deprecate and eventually remove replaced APIs in a controlled manner, which will be more readily visible as issues to be addressed - most modern IDEs mark use of deprecated APIs as a warning automatically
- Split related sets of functionality into separate artifacts - this will allow consumers to more precisely choose what functionality they need, instead of pulling a large library in, or having to shade the library to reduce size

We welcome any feedback or suggestions from the community to make this as friendly as possible to use - the easier way to provide this is currently the issue tracker
