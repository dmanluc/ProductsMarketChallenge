package com.dmanluc.cabifymarket.utils

import kotlinx.coroutines.CoroutineDispatcher

/**
 * @author   Daniel Manrique Lucas <dmanluc91@gmail.com>
 * @version  1
 *
 * App dispatchers to be used with coroutines (main for UI tasks and io for CPU/Netowrk based operations)
 *
 */
class AppDispatchers(val main: CoroutineDispatcher, val io: CoroutineDispatcher)