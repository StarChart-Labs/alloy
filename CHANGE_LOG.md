# Change Log
All notable changes to this project will be documented in this file.

The format is based on [Keep a Changelog](http://keepachangelog.com/)
and this project adheres to [Semantic Versioning](http://semver.org/).

## [Unreleased]
### Added
- Add org.starchartlabs.alloy.core.collections.MoreCollections for streamlined and reduced boilerplate in relation to Java collection constructs
- Add org.starchartlabs.alloy.core.collections.Multimap for streamlined and reduced boilerplate in relation to managing one-to-many data structures
- Add org.starchartlabs.alloy.core.collections.ListMultimap for streamlined and reduced boilerplate in relation to managing one-to-many data structures where the order of the "many" values matters
- Add org.starchartlabs.alloy.core.collections.SetMultimap for streamlined and reduced boilerplate in relation to managing one-to-many data structures where the "many" values are unique

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
