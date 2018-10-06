# Migrating From Guava

Many of the utilities provided by Alloy are intended to take the place of similar functionality in the Guava library. This guide will assist in making necessary changes to convert from Guava to Alloy

## Charsets

- Guava: com.google.common.base.Charsets

### Removed APIs

Charsets has not been implemented, as all values it presented are now provided within Java by java.nio.charset.StandardCharsets

## Comparators

- Guava: com.google.common.collect.Comparators
- Alloy: org.starchartlabs.alloy.core.Comparators

### Unchanged APIs

- Comparators.isInOrder(Iterable, Comparator)
- Comparators.isInStrictOrder(Iterable, Comparator)

### Updated APIs

- Comparators.emptiesFirst(Iterable, Comparator)
  - The behavior of null values has been changed - Guava treats empty Optionals and null as the same, Alloy does not
- Comparators.emptiesLast(Iterable, Comparator)
  - The behavior of null values has been changed - Guava treats empty Optionals and null as the same, Alloy does not

### Removed APIs

- Comparators.least(int, Comparator)
  - As of Java 8, this can be achieved via `Stream.sorted(Comparator).limit(int)`
- Comparators.greatest(int, Comparator)
  - As of Java 8, this can be achieved via `Stream.sorted(Comparator.reversed()).limit(int)`
  
### Unimplemented APIs

Guava Comparators also provides `Comparators.lexicographical`, however there does not appear to be a general use case for these. Issues/Pull requests accompanied by supporting arguments on why these are not overly-specialized are welcome
 

## Functions

- Guava: com.google.common.base.Functions

### Removed APIs

Functions has been replaced by Java's lambda functionality, and has not been implemented

- Functions.compose(Function<B, C>, Function<A, ? extends B>)
  - Replaced by java.util.function.Function.andThen(Function<? super R, ? extends V>) or java.util.function.Function.andThen(Function<? super V, ? extends T>)
- Functions.constant(E)
  - Replaced by lambda `input -> constant`
- Functions.forMap(Map<K, ? extends V>, V)
  - Replaced by lambda `key -> Optional.ofNullable(map.get(key)).orElse(default)`
- Functions.forMap(Map<K, V>)
  - Replaced by lambda `map::get`
- Functions.forPredicate(Predicate<T>)
  - Replaced by lambda `predicate::apply`
- Functions.forSupplier(Supplier<T>)
  - Replaced by lambda `input -> supplier.get()`
- Functions.identity()
  - Replaced by lambda `input -> input`
- Functions.toStringFunction()
  - Replaced by `Object::toString`

## Joiner

- Guava: com.google.common.base.Joiner

### Removed APIs

Joiner has not been implemented, as the same functionality exists via streams, filtering, and collectors within Java 8. An example of the Java methodology when joining values, skipping nulls, would be:

```
String joined = values.stream()
                   .filter(Objects::nonNull)
                   .collect(Collectors.joining(","));
```

## ListMultimap

- Guava: com.google.common.collect.ListMultimap
- Alloy: org.starchartlabs.alloy.core.collections.ListMultimap

Alloy's ListMultimap extends from Multimap in the same way Guava's does - see notes on Multimap for API changes in relation to Guava's Multimap

## MoreObjects

- Guava: com.google.common.base.MoreObjects
- Alloy: org.starchartlabs.alloy.core.MoreObjects

### Unchanged APIS

- MoreObjects.toStringHelper(Object)
- MoreObjects.toStringHelper(Class)
- MoreObjects.toStringHelper(String)

### Removed APIs

- MoreObjects.firstNonNull(T, T)
  - As of Java 8, this can be achieved via `Optional.orElse()`, or `Stream.of(a, b).filter(Objects::nonNull).findFirst()`

### Notes on ToStringBuilder

Alloy's version of ToStringBuilder does not currently overload `add` or `addValue` for primitives - this seemed to be present to avoid auto-boxing, but seemed overzealous. An issue has been filed to more formally evaluate the performance gained from such overloading to determine if it is worthwhile
  
## Multimap

- Guava: com.google.common.collect.Multimap
- Alloy: org.starchartlabs.alloy.core.collections.Multimap

### Unchanged APIs

- Multimap.isEmpty()
- Multimap.put(K, V)
- Multimap.putAll(K, Iterable<V>)
- Multimap.putAll(Multimap<K, V>)
- Multimap.replaceAll(K, Iterable<V>)
- Multimap.clear()
- Multimap.get(K)
- Multimap.keySet()
- Multimap.asMap()
- Multimap.forEach(BiConsumer<K,V>)

### Updated APIs

- Multimap.containsKey()
  - The containsKey function has been updated to take an instance of the generic type designated for keys instead of an Object instance
- Multimap.containsValue()
  - The containsValue function has been updated to take an instance of the generic type designated for values instead of an Object instance
- Multimap.containsEntry()
  - The containsEntry function has been updated to take instances of the generic types designated for keys and values instead of Object instances
- Multimap.remove()
  - The remove function has been updated to take instances of the generic types designated for keys and values instead of Object instances
- Multimap.removeAll()
  - The removeAll function has been updated to take an instance of the generic type designated for keys instead of an Object instance5
- Multimap.size()
  - In Alloy, this is presented as Multimap.flatSize(), to better matchMap naming conventions. Alloy Multimap.size() cooresponds to the sizing mechanics of Java Map.size() instead
- Multimap.keys()
  - In Alloy, this is presented as Multimap.flatKeys(), to conform to a consistent naming for entry-based calls
- Multimap.values()
  - In Alloy, this is presented as Multimap.flatValues(), to conform to a consistent naming for entry-based calls
- Multimap.entries()
  - In Alloy, this is presented as Multimap.flatEntries(), to conform to a consistent naming for entry-based calls
  
## Preconditions

- Guava: com.google.common.base.Preconditions
- Alloy: org.starchartlabs.alloy.core.Preconditions

### Unchanged APIs

- Preconditions.checkArgument(boolean)
- Preconditions.checkArgument(boolean, Object)

- Preconditions.checkState(boolean)
- Preconditions.checkState(boolean, Object)

### Updated APIs

- Guava Preconditions.checkArgument with message formatting
  - Instead of the large number of Guava methods for built-in message formatting, Alloy separates out the formatting concerns using Java's Supplier via Alloy Preconditions.checkArgument(boolean, Supplier<String>). Alloy also supplies Strings.format, which replicate's Guava's Preconditions formatting behavior and performance characteristics, and can be used via lambda as a supplier
  - Affected Guava Preconditions.checkArgument methods:
    - Preconditions.checkArgument(boolean, String, Object...)
    - Preconditions.checkArgument(boolean, String, char)
    - Preconditions.checkArgument(boolean, String, int)
    - Preconditions.checkArgument(boolean, String, long)
    - Preconditions.checkArgument(boolean, String, Object)
    - Preconditions.checkArgument(boolean, String, char, char)
    - Preconditions.checkArgument(boolean, String, char, int)
    - Preconditions.checkArgument(boolean, String, char, long)
    - Preconditions.checkArgument(boolean, String, char, Object)
    - Preconditions.checkArgument(boolean, String, int, char)
    - Preconditions.checkArgument(boolean, String, int, int)
    - Preconditions.checkArgument(boolean, String, int, long)
    - Preconditions.checkArgument(boolean, String, int, Object)
    - Preconditions.checkArgument(boolean, String, long, char)
    - Preconditions.checkArgument(boolean, String, long, int)
    - Preconditions.checkArgument(boolean, String, long, long)
    - Preconditions.checkArgument(boolean, String, long, Object)
    - Preconditions.checkArgument(boolean, String, Object, char)
    - Preconditions.checkArgument(boolean, String, Object, int)
    - Preconditions.checkArgument(boolean, String, Object, long)
    - Preconditions.checkArgument(boolean, String, Object, Object)
    - Preconditions.checkArgument(boolean, String, Object, Object, Object)
    - Preconditions.checkArgument(boolean, String, Object, Object, Object, Object)

- Guava Preconditions.checkState with message formatting
  - Instead of the large number of Guava methods for built-in message formatting, Alloy separates out the formatting concerns using Java's Supplier via Alloy Preconditions.checkState(boolean, Supplier<String>). Alloy also supplies Strings.format, which replicate's Guava's Preconditions formatting behavior and performance characteristics, and can be used via lambda as a supplier
  - Affected Guava Preconditions.checkState methods:
    - Preconditions.checkState(boolean, String, Object...)
    - Preconditions.checkState(boolean, String, char)
    - Preconditions.checkState(boolean, String, int)
    - Preconditions.checkState(boolean, String, long)
    - Preconditions.checkState(boolean, String, Object)
    - Preconditions.checkState(boolean, String, char, char)
    - Preconditions.checkState(boolean, String, char, int)
    - Preconditions.checkState(boolean, String, char, long)
    - Preconditions.checkState(boolean, String, char, Object)
    - Preconditions.checkState(boolean, String, int, char)
    - Preconditions.checkState(boolean, String, int, int)
    - Preconditions.checkState(boolean, String, int, long)
    - Preconditions.checkState(boolean, String, int, Object)
    - Preconditions.checkState(boolean, String, long, char)
    - Preconditions.checkState(boolean, String, long, int)
    - Preconditions.checkState(boolean, String, long, long)
    - Preconditions.checkState(boolean, String, long, Object)
    - Preconditions.checkState(boolean, String, Object, char)
    - Preconditions.checkState(boolean, String, Object, int)
    - Preconditions.checkState(boolean, String, Object, long)
    - Preconditions.checkState(boolean, String, Object, Object)
    - Preconditions.checkState(boolean, String, Object, Object, Object)
    - Preconditions.checkState(boolean, String, Object, Object, Object, Object)

### Removed APIs

- Guava Preconditions.checkNotNull
  - As of Java 8, the langauge libraries provide built-in null validation. Use `Objects.requireNonNull()' instead
  - Affected Guava Preconditions.checkNotNull methods:
    - Preconditions.checkNotNull(T)
    - Preconditions.checkNotNull(T, Object)
    - Preconditions.checkNotNull(T, String, Object...)
    - Preconditions.checkNotNull(T, String, char)
    - Preconditions.checkNotNull(T, String, int)
    - Preconditions.checkNotNull(T, String, long)
    - Preconditions.checkNotNull(T, String, Object)
    - Preconditions.checkNotNull(T, String, char, char)
    - Preconditions.checkNotNull(T, String, char, int)
    - Preconditions.checkNotNull(T, String, char, long)
    - Preconditions.checkNotNull(T, String, char, Object)
    - Preconditions.checkNotNull(T, String, int, char)
    - Preconditions.checkNotNull(T, String, int, int)
    - Preconditions.checkNotNull(T, String, int, long)
    - Preconditions.checkNotNull(T, String, int, Object)
    - Preconditions.checkNotNull(T, String, long, char)
    - Preconditions.checkNotNull(T, String, long, int)
    - Preconditions.checkNotNull(T, String, long, long)
    - Preconditions.checkNotNull(T, String, long, Object)
    - Preconditions.checkNotNull(T, String, Object, char)
    - Preconditions.checkNotNull(T, String, Object, int)
    - Preconditions.checkNotNull(T, String, Object, long)
    - Preconditions.checkNotNull(T, String, Object, Object)
    - Preconditions.checkNotNull(T, String, Object, Object, Object)
    - Preconditions.checkNotNull(T, String, Object, Object, Object, Object)

### Unimplemented APIs

Guava Preconditions also provides some element index checking, however there does not appear to be a general use case for these. Issues/Pull requests accompanied by supporting arguments on why these are not overly-specialized are welcome

- Affected Guava Preconditions.checkNotNull methods
  - Preconditions.checkElementIndex(int, int)
  - Preconditions.checkElementIndex(int, int, String)
  - Preconditions.checkPositionIndex(int, int)
  - Preconditions.checkPositionIndex(int, int, String)
  - Preconditions.checkPositionIndexes(int, int, int)
  
## SetMultimap

- Guava: com.google.common.collect.SetMultimap
- Alloy: org.starchartlabs.alloy.core.collections.SetMultimap

Alloy's SetMultimap extends from Multimap in the same way Guava's does - see notes on Multimap for API changes in relation to Guava's Multimap
  
## Strings

- Guava: com.google.common.base.Strings
- Alloy: org.starchartlabs.alloy.core.Strings

### Unchanged APIs

- Strings.nullToEmpty(String)
- Strings.emptyToNull(String)
- Strings.isNullOrEmpty(String)
- Strings.padStart(String, int, char)
- Strings.padEnd(String, int, char)
- Strings.repeat(String, int)
- Strings.commonPrefix(CharSequence, CharSequence)
- Strings.commonSuffix(CharSequence, CharSequence)

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
  
## Throwables

- Guava: com.google.common.base.Throwables
- Alloy: org.starchartlabs.alloy.core.Throwables

### Unchanged APIs

### Removed APIs

- Throwables.propagateIfInstanceOf(@Nullable Throwable throwable, Class<X> declaredType)
  - Replaced by Throwables.throwIfInstanceOf
- Throwables.propagateIfPossible(@Nullable Throwable throwable)
  - Replaced by Throwables.throwIfUnchecked
- Throwables.propagate(Throwable throwable)
  - Use `throw e` or `throw new RuntimeException(e)` directly, or use a combination of Throwables.throwIfUnchecked and `throw new RuntimeException(e)`

### Unimplemented APIs

Use cases are not strong enough currently to include these functions, but issues/pull requests accompanied by supporting arguments on why these should be included are welcome

- Throwables.propagateIfPossible(@Nullable Throwable throwable, Class<X> declaredType)
- Throwables.propagateIfPossible(@Nullable Throwable throwable, Class<X1> declaredType1, Class<X2> declaredType2) throws X1, X2
- Throwables.getCauseAs(Throwable throwable, Class<X> expectedCauseType)
- Throwables.lazyStackTrace(Throwable throwable)
- Throwables.lazyStackTraceIsLazy()
- Throwables.jlaStackTrace(final Throwable t)