# Release

1. Update the version number in `gradle.properties`
2. Commit the changes with the commit message `Release <version>`
3. Create a tag for the version `v<version>`
4. Push the commit and tag to GitHub
5. Wait until CI passes
6. Unless it is a minor release, create a GitHub release with the patch notes, and the binaries
7. Update the version to the next developmental version
8. Commit the changes with the commit message `Start development on <version>`
9. Push the changes
