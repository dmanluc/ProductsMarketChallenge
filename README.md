# Cabify Mobile Challenge

Besides providing exceptional transportation services, Cabify also runs a physical store which sells Products.

Our list of products looks like this:

``` 
Code         | Name                |  Price
-------------------------------------------------
VOUCHER      | Cabify Voucher      |   5.00€
TSHIRT       | Cabify T-Shirt      |  20.00€
MUG          | Cabify Coffee Mug   |   7.50€
```

Various departments have insisted on the following discounts:

 * The marketing department believes in 2-for-1 promotions (buy two of the same product, get one free), and would like to have a 2-for-1 special on `VOUCHER` items.

 * The CFO insists that the best way to increase sales is with discounts on bulk purchases (buying x or more of a product, the price of that product is reduced), and demands that if you buy 3 or more `TSHIRT` items, the price per unit should be 19.00€.

Cabify's checkout process allows for items to be scanned in any order, and should return the total amount to be paid.

Examples:

    Items: VOUCHER, TSHIRT, MUG
    Total: 32.50€

    Items: VOUCHER, TSHIRT, VOUCHER
    Total: 25.00€

    Items: TSHIRT, TSHIRT, TSHIRT, VOUCHER, TSHIRT
    Total: 81.00€

    Items: VOUCHER, TSHIRT, VOUCHER, VOUCHER, MUG, TSHIRT, TSHIRT
    Total: 74.50€


# To do
- Implement an app where a user can pick products from a list and checkout them to get the resulting price. No need to implement any real payment system, just a fake feedback about the payment has been completed.
- The discounts can change in the future, depending on the year season we apply different ones.
- There is no need for a user login screen.
- We would like to show users what discounts have been applied in their purchase. 
- You should get the list of products from [here](https://api.myjson.com/bins/4bwec).

## Bonus
- The app should work also offline, and all that this implies. 

**The code should:**
- Be written as production-ready code. You will write production code. We would like you to build it in the same way as if you were going to publish to the store.
- Be easy to grow and easy to add new functionality.
- Have notes attached, explaning the solution and why certain things are included and others are left out.
- Be written either in Swift or Kotlin, preferably in the latest stable version of the language.

---------------------------------------------------------------------

## Tech-stack

Min API level is set to [`21`](https://android-arsenal.com/api?level=21), so the presented approach is suitable for over
[85% of devices](https://developer.android.com/about/dashboards) running Android. This project takes advantage of latest
popular libraries and tools of the Android ecosystem

* Tech-stack
    * [Kotlin](https://kotlinlang.org/) + [Coroutines](https://kotlinlang.org/docs/reference/coroutines-overview.html) - perform background operations
    * [Koin](https://insert-koin.io/) - dependency injection
    * [Retrofit](https://square.github.io/retrofit/) - networking
    * [Jetpack](https://developer.android.com/jetpack)
        * [Navigation](https://developer.android.com/topic/libraries/architecture/navigation/) - deal with whole in-app navigation
        * [LiveData](https://developer.android.com/topic/libraries/architecture/livedata) - lets the components in your app, usually the UI, observe LiveData objects for changes.
        * [Lifecycle](https://developer.android.com/topic/libraries/architecture/lifecycle) - perform action when lifecycle state changes
        * [ViewModel](https://developer.android.com/topic/libraries/architecture/viewmodel) - store and manage UI-related data in a lifecycle conscious way
        * [Room](https://developer.android.com/topic/libraries/architecture/room) - create a cache of your app's data on a device that's running your app
  	* [Glide](https://bumptech.github.io/glide/) - image loading library
* Architecture
    * Clean Architecture
    * MVVM + Data Binding (presentation layer)
    * [Android Architecture components](https://developer.android.com/topic/libraries/architecture) ([ViewModel](https://developer.android.com/topic/libraries/architecture/viewmodel), [LiveData](https://developer.android.com/topic/libraries/architecture/livedata), [Navigation](https://developer.android.com/jetpack/androidx/releases/navigation))
* Tests
    * [Unit Tests](https://en.wikipedia.org/wiki/Unit_testing) ([JUnit](https://junit.org/junit4/))
    * [Mockk](https://mockk.io)
    * [Espresso (UI)](https://developer.android.com/training/testing/espresso)
