# Migrating From Guava

Many of the utilities provided by Alloy are intended to take the place of similar functionality in the Guava library. This guide will assist in making necessary changes to convert from Guava to Alloy

## Suppliers

- Guava: com.google.common.base.Suppliers
- Alloy: org.starchartlabs.alloy.core.Suppliers

### Unchanged APIs

- Suppliers.memoize(Supplier)
- Suppliers.memoizeWithExpiration(Supplier, long, TimeUnit)
- Suppliers.synchronizedSupplier(Supplier)

### Updated APIs

- Guava Suppliers.compose(Function, Supplier)
  - In Alloy, this is presented as Suppliers.map(Supplier, Function) to better match the naming of similar operations in the Java language and reflect the chaining of elements that occurs

### Removed APIs

- Suppliers.ofInstance(T)
  - As of Java 8, lambdas allow easier providing of implementations. In place of providing Suppliers.ofInstance(T), instead use the lambda form `() -> T`
- Suppliers.supplierFunction()
  - As of Java 8, a supplier's `get` function can be referenced for use in a lambda. Use `Supplier::get` instead