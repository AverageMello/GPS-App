package com.example.oepnv_tracking

import java.util.UUID

class WebserverInterface {
    fun createUniqueToken() : String {
        return UUID.randomUUID().toString();
    }
}