# Contributing

We welcome any contributions! If you wish to contribute:

- Fork the `alloy` repository
- Clone your fork to your development machine
- Run `./gradlew clean build` to confirm you are starting from a working Setup
 - Please report any issues with this build step in the GitHub project's issues
- Create a branch for your work
- Setup your deveopment environment (see below)
- Make changes
- Run `./gradlew clean build` to test your changes locally
- Push your branch to your Fork
- Make a Pull Request against the `master` branch

## Development Environment Setup

Currently, Eclipse is the supported IDE for development of Alloy. It is recommended to create an isolated workspace for StarChart Labs projects. You should also import the standard StarChart Labs formatting and save settings from the [eclipse-configuration repository](https://github.com/StarChart-Labs/eclipse-configuration)

## General Standards

In general, pull requests should:
- Be small and focused on a single improvement/bug
- Include tests for changed behavior/new features
- Match the formatting of the existing code
- Have documentation for added methods/classes and appropriate in-line comments
- Have additions to the CHANGE_LOG.md file recording changed behavior

## Conduct

We expect both outside contributors and StarChart Labs members to treat each with respect and promote constructive discussion on how we can create better tools for developers. Please report any inappropraite interactions (with a link to the offending pull request, etc, if possible) to `contributing@starchartlabs.org`
