# Major Version Migrations

This details steps for migrating between major versions of Alloy, which may contain breaking changes

## 0.x to 1.x

### May be Completed Prior to Upgrade

- Remove any extensions of `org.starchartlabs.alloy.core.MoreObjects` - this class has been made final, as it was not intended to be extended (GH-32)