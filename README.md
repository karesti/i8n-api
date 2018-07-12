[![Build Status](https://travis-ci.com/galderz/i8n-api.svg?branch=master)](https://travis-ci.com/galderz/i8n-api)

Some rules for consistency:

* API/impl versions, e.g. v1, v2...etc, should only used in artifact and package names, not in class names.
This does not apply to use case modules.

Rules for creating a new API version:

* Adding new methods does not require creating a new version of the interface.
Old clients are unaffected since they would not call this new method.
Problem would be for implementors of the old interface, adding a new method could be problematic.
However, with Java 8 and 'default' keyword, any method can be added without affecting implementors.

* Changing return type, number of parameters or type of parameters. 

All APIs names start with `Dummy` to make it clear that these are just dummy examples of potential APIs.
They are not intended as final API products, but just as examples. 

Dummy APIs explained:

* DummyMap v1 is a location-independent, key/value API with put returning previous value associated with key.
Most of the time, users want to store a value but don't care about previous value.
As implementors, we have to stick to the API, so we have to calculate previous value which might be used.
Calculating previous value might be costly, requiring an extra RPC or visit to the cache store.
This was an actual mistake we made in the original Infinispan API.

* DummyMap v2 is a location-independent, key/value API which fixes put to not return previous value.
We want the users to continue using `put()` since that's the most idiomatic way to simply store a value.
This is better than getting those users to use a different method that stores a value but does not return previous value.
Changing the return value is an incompatible API change, so instead let's move those users to a new API version.

* DummyMap v3 is an embedded-only API that offers a extra Java lambda-based operation.
It is designed as embedded-only is because there's no easy way to transform Java lambdas into functions in other languages.

* DummyAsyncMap v1 is a location-independent, async-only API.
Barring reactivestreams dependency (which is part of Java 9), it has no other dependencies.
This means that it offers an async API that works for both operations with single or multiple returns without external dependencies.
Higher level async-only APIs, such as Rx, would build on top of this API.
Those users interested in using async APIs are often interested in using them exclusive.
That is, they don't want to mix up up sync and async APIs.
This is why the API is standalone and independent from the sync APIs.
One example user here would be Vert.x.


# Design

* APIs are loaded using the service loader pattern which lookup high level APIs, e.g. `v1.ApiMap` or `ApiAsyncMap`.

* It assumes that given a high level API, e.g. `v1.ApiMap`, the user will only load one implementation at the time.
For example, for `v1.ApiMap`, it would load either the embedded or remote implementation, but not both at the same time.

* There's nothing stopping the user from loading two versions of the same API, e.g. `v1.ApiMap` and `v2.ApiMap`.
This might not be desirable.

* Loading different APIs should be allowed though. 
For example, a user should be able to load a map and query API at the same time.


# TODO

- [X] Async Map API - CompletionStage
- [X] Async Map API - Publisher
- [ ] Rx Map API 
- [ ] Query API
- [ ] Counter API
- [ ] Avoid loading multiple versions of same API
