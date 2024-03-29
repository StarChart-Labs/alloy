# Change Log
All notable changes to this project will be documented in this file.

The format is based on [Keep a Changelog](http://keepachangelog.com/)
and this project adheres to [Semantic Versioning](http://semver.org/).

## [Unreleased]

## [1.0.3]
### Changed
- Updated `org.mockito:mockito-core` from `4.1.0` to `4.8.0`
- Updated `org.testng:testng` from `7.4.0` to `7.5`

## [1.0.2]
### Changed
- Updated Mockito from 3.9.0 to 4.1.0

## [1.0.1]
### Changed
- Updated Mockito to 3.9.0
- Updated TestNG to 7.4.0

## [1.0.0]
### Changed
- Updated TestNG and Mockito Libraries to next major revision
- (GH-32) Make `org.starchartlabs.alloy.core.MoreObjects` final

## [0.5.0]
### Changed
- Fixed modifier order issue in org.starchartlabs.alloy.core.Comparators
- Added org.starchartlabs.alloy.core.Preconditions.checkArgument and checkState that supports value assignment
- (GH-57) Added integrated support for Preconditions with formatted messages

## [0.4.1]
### Changed

- Updated external dependency versions to latest bugfix releases

## [0.4.0]
### Added
- Add org.starchartlabs.alloy.core.collections.MoreSpliterators for streamlined and reduced boilerplate in relation to Java spliterator constructs
- Addition of support for short-circuited spliterators via org.starchartlabs.alloy.core.collections.MoreSpliterators.shortCircuit
- Addition of support for paged-data-based spliterators via org.starchartlabs.alloy.core.collections.MoreSpliterators.ofPaged and org.starchartlabs.alloy.core.collections.PageProvider

## [0.3.0]
### Added
- Add org.starchartlabs.alloy.core.collections.MoreCollections for streamlined and reduced boilerplate in relation to Java collection constructs

## [0.2.0]
### Added
- Add org.starchartlabs.alloy.core.Comparators for streamlined and reduced boilerplate in relation to Java Comparator construct
- Prevent FindBugs JSR-305 implementation from being added as a transitive dependency to consuming projects
- Add org.starchartlabs.alloy.core.Throwables for streamlined and reduced boilerplate in relation to Java exception handling

## [0.1.1]
### Updated
- Add description to maven POM to fulfill Maven Central requirements

## [0.1.0]
### Added
- Add org.starchartlabs.alloy.core.Suppliers for streamlined and simplified use of Java Supplier construct
- Add org.starchartlabs.alloy.core.Preconditions for streamlined validation of caller arguments
- Add org.starchartlabs.alloy.core.Strings for streamlined and simplified use of Java Strings
- Add org.starchartlabs.alloy.core.MoreObjects for streamlined and simplified object operations such as toString method implementations
