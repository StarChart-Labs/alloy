# Alloy

[![Travis CI](https://img.shields.io/travis/StarChart-Labs/alloy.svg?branch=master)](https://travis-ci.org/StarChart-Labs/alloy)

General Java utilities and quality of life tools

Alloy is intended to help streamline use of common Java patterns, and reduce boilerplate code

## This Looks a Lot Like Guava...

Alloy was heavily inspired by the Google Guava libraries, and replicates some of the core functionality of that utility. While the team working on Guava made an excellent library we've used over and over, enough minor annoyances have been encountered over time that we chose to make the effort to iterate on the established pattern.

There are a couple issues with using Guava in certain environments that led to the creation of Alloy

- Guava contains a large amount of utility that is now built into the Java language since Java 8. As a large amount of its code was based upon the Guava-specific contracts, it is very difficult for Guava to safely remove these now-duplicate elements
- Partially because of the above, Guava is a sizable library, not suitable for specifically size-sensitive applications. Aside from this, there are organization routes that could be taken to further reduce size by a new library
- Guava uses a somewhat non-standard approach to API designations - Beta tags indicate functionality should be used with caution, and Deprecated tags are not used often, and Deprecated APIs are rarely removed, even across major versions. While there are advantages to this approach, it leads to a build-up of "dead" APIs, especially those assimilated into Java as of Java 8

Alloy intends, in part, to create a stripped-down post-Java-8 version of these utilities which uses a more traditional approach to deprecation and removal. To address some of the above, Alloy intends to

- Deprecate and eventually remove replaced APIs, which will be more readily visible as issues to be addressed - most modern IDEs mark use of deprecated APIs as a warning automatically
- Split related sets of functionality into separate artifacts - this will allow consumers to more precisely choose what functionality they need, instead of pulling a large library in, or having to shade the library to reduce size
- Upgrade the minimum version of Java following the LTS path - as new LTW versions of Java become available, the minimum version will advance after a reasonable time

We may find that these annoyances are not work trying to create a new library - that's the nature of experimenting and trying things. In that case, we'll simply archive this project and continue to the next thing. Our hope is that these improvments will be significant enough to justify switching for us and others.
