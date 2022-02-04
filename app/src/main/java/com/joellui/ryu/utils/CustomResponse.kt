package com.joellui.ryu.utils

import retrofit2.Response

class CustomResponse<T : Any?> {
    val response: Response<T>
    val type: Int

    constructor (r: Response<T>, t: Int) {
        response = r;
        type = t;
    }
}