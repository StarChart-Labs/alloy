# Collaborator information

## One-time Setup

* Set the `BINTRAY_USER` and `BINTRAY_API_KEY` environment variables with your credentials to allow uploading artifacts to BinTray

## Release Process

* Update the version number to remove the "-SNAPSHOT" designation. All version numbers should be a fully-qualified semantic version of form `<major>.<minor>.<micro>`
* Change the header "Unreleased" in CHANGE_LOG.md to the target release number, and create a new "Unreleased" header above it
* Run a full build via `./gradlew clean build`
  * If there are any errors, stash the changes to the version number and changelog until the issue can be corrected and merged to master as a separate commit/issue
* Commit the version number and CHANGE_LOG updates
* Tag the git repository with the fully-qualified semantic version number
* Upload artifacts to bintray via `./gradlew bintrayUpload -PremoteDeploy`
* Verify all artifacts were correctly uploaded - check that POM.xml scopes and version numbers are correct
* Change version number to `<released version> + 1 micro` + `-SNAPSHOT`
* Commit to git
* Push changes and tag to GitHub
* Publish artifacts on BinTray
* [Synchronize BinTray artifacts to Maven central](https://bintray.com/docs/usermanual/uploads/uploads_syncingwiththirdpartyplatforms.html)
* Verify artifacts are present on Maven central
* Create a release on GitHub including all binary and source jars
* Change the `next-release` milestone to the released version number, move any unresolved tickets/pull-requests to a new `next-release` milestone, and close the version'd milestone
