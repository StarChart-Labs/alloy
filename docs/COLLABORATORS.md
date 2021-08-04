# Collaborator information

## Release Process

* Update the version number to remove the "-SNAPSHOT" designation. All version numbers should be a fully-qualified semantic version of form `<major>.<minor>.<micro>`
* Change the header "Unreleased" in CHANGE_LOG.md to the target release number, and create a new "Unreleased" header above it
* Run a full build via `./gradlew clean build`
  * If there are any errors, stash the changes to the version number and changelog until the issue can be corrected and merged to master as a separate commit/issue
* Commit the version number and CHANGE_LOG updates
* Tag the git repository with the fully-qualified semantic version number
* Push changes and tag to GitHub
* Run the "Publish" GitHub Actions workflow
* Verify artifacts are present on Maven central in the [Staging repositories](https://oss.sonatype.org/#stagingRepositories) (login required), "close" it, and then "release" it
* Change version number to `<released version> + 1 micro` + `-SNAPSHOT`
* Commit to git
* Push changes and tag to GitHub
* Create a release on GitHub including all binary and source jars
* Change the `next-release` milestone to the released version number, move any unresolved tickets/pull-requests to a new `next-release` milestone, and close the version'd milestone
